package br.com.moip;

import br.com.moip.authentication.Authentication;
import br.com.moip.exception.MoipException;
import br.com.moip.exception.UnauthorizedException;
import br.com.moip.exception.UnexpectedException;
import br.com.moip.exception.ValidationException;
import br.com.moip.resource.Errors;
import br.com.moip.ssl.SSLSupport;
import br.com.moip.util.GsonFactory;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import javax.net.ssl.HttpsURLConnection;
import java.io.DataOutputStream;
import java.io.OutputStreamWriter;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import org.apache.http.entity.ContentType;

import static br.com.moip.util.DataHelper.jsonToUrlEncodedString;

public class Client {

    private static final Logger LOGGER = LoggerFactory.getLogger(Client.class);
    public static final String PRODUCTION = "https://api.moip.com.br";
    public static final String SANDBOX = "https://sandbox.moip.com.br";
    public static final String CONNECT_PRODUCTION = "https://connect.moip.com.br";
    public static final String CONNECT_SANDBOX = "https://connect-sandbox.moip.com.br";
    private static String USER_AGENT;

    static {
        try {
            InputStream inputStream = Client.class.getResourceAsStream("/moipJavaSDK.properties");
            Properties properties = new Properties();
            properties.load(inputStream);

            USER_AGENT = properties.getProperty("userAgent");
        } catch (Exception e) {
            USER_AGENT = "MoipJavaSDK/UnknownVersion (+https://github.com/moip/moip-sdk-java/)";
        }
    }


    private final String endpoint;
    private final Authentication authentication;
    private final Gson gson;
    private int connectTimeout;
    private int readTimeout;

    public Client(final String endpoint, final Authentication authentication) {
        this.endpoint = endpoint;
        this.authentication = authentication;
        this.gson = GsonFactory.gson();
    }
    
    public Client(final String endpoint, final Authentication authentication, int connectTimeout, int readTimeout) {
        this(endpoint, authentication);
        this.connectTimeout = connectTimeout;
        this.readTimeout = readTimeout;
    }

    public <T> T post(final String path, final Object object, final Class<T> type) {
        return doRequest("POST", path, object, type, ContentType.APPLICATION_JSON);
    }

    public <T> T post(final String path, final Object object, final Class<T> type, ContentType contentType) {
        return doRequest("POST", path, object, type, contentType);
    }

    public <T> T post(final String path, final Class<T> type) {
        return doRequest("POST", path, null, type, ContentType.APPLICATION_JSON);
    }

    public <T> T put(final String path, final Object object, final Class<T> type) {
        return doRequest("PUT", path, object, type, ContentType.APPLICATION_JSON);
    }

    public <T> T get(String path, Class<T> type) {
        return doRequest("GET", path, null, type, ContentType.APPLICATION_JSON);
    }

    public <T> T delete(String path, Class<T> type) {
        return doRequest("DELETE", path, null, type, ContentType.APPLICATION_JSON);
    }

    private <T> T doRequest(final String method, final String path, final Object object, final Class<T> type, final ContentType contentType) {
        try {
            URL url = new URL(endpoint + path);

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestProperty("User-Agent", USER_AGENT);
            conn.setRequestProperty("Content-type", contentType.getMimeType());
    		conn.setConnectTimeout(connectTimeout);
    		conn.setReadTimeout(readTimeout);
            conn.setRequestMethod(method);

            // Disable TLS 1.0
            if (conn instanceof HttpsURLConnection) {
                ((HttpsURLConnection) conn).setSSLSocketFactory(new SSLSupport());
            }

            if (authentication != null) {
                authentication.authenticate(conn);
            }

            LOGGER.debug("---> {} {}", method, conn.getURL().toString());
            logHeaders(conn.getRequestProperties().entrySet());

            if (object != null) {
                conn.setDoOutput(true);
                String body = getBody(object, contentType);

                LOGGER.debug("");
                LOGGER.debug("{}", body);

                DataOutputStream wr = new DataOutputStream(conn.getOutputStream());
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(wr, "UTF-8"));
                writer.write(body);
                writer.close();
                wr.flush();
                wr.close();
            }

            LOGGER.debug("---> END HTTP");

            int responseCode = conn.getResponseCode();

            LOGGER.debug("<--- {} {}", responseCode, conn.getResponseMessage());
            logHeaders(conn.getHeaderFields().entrySet());

            StringBuilder responseBody = new StringBuilder();
            if (responseCode >= 200 && responseCode < 299) {
                responseBody = readBody(conn.getInputStream());
            }

            if (responseCode == 401) {
                throw new UnauthorizedException();
            }

            if (responseCode >= 400 && responseCode < 499) {

                responseBody = readBody(conn.getErrorStream());
                LOGGER.debug("API ERROR {}", responseBody.toString());

                Errors errors = new Errors();
                try {
                    errors = gson.fromJson(responseBody.toString(), Errors.class);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                throw new ValidationException(responseCode, conn.getResponseMessage(), errors);
            }

            if (responseCode >= 500) {
                throw new UnexpectedException();
            }

            LOGGER.debug("");
            LOGGER.debug("{}", responseBody.toString());
            LOGGER.debug("<-- END HTTP ({}-byte body)", conn.getContentLength());

            return gson.fromJson(responseBody.toString(), type);
        } catch (IOException | KeyManagementException | NoSuchAlgorithmException e) {
            throw new MoipException("Error occurred connecting to Moip API: " + e.getMessage(), e);
        }
    }

	private void logHeaders(Set<Map.Entry<String, List<String>>> entries) {
        for (Map.Entry<String, List<String>> header : entries) {
            if (header.getKey() != null) {
                LOGGER.debug("{}: {}", header.getKey(), header.getValue());
            }
        }
    }

    private StringBuilder readBody(final InputStream inputStream) throws IOException {
        StringBuilder body = new StringBuilder();
        BufferedReader in = new BufferedReader(new InputStreamReader(inputStream));

        String inputLine;
        while ((inputLine = in.readLine()) != null) {
            body.append(inputLine);
        }
        in.close();

        return body;
    }

    private String getBody(Object object, ContentType contentType) {
        if (contentType == ContentType.APPLICATION_FORM_URLENCODED) {
            return jsonToUrlEncodedString((JsonObject) new JsonParser().parse(gson.toJson(object)));
        }

        return gson.toJson(object);
    }

    public Authentication getAuthentication() {
        return authentication;
    }

    public String getEndpoint() {
        return endpoint;
    }

}