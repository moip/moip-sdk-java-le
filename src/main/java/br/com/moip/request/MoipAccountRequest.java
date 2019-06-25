package br.com.moip.request;

public class MoipAccountRequest {

    private String id;
    private TaxDocumentRequest taxDocument;

    public MoipAccountRequest(String moipAccount) {
        this.id = moipAccount;
    }

    public MoipAccountRequest(TaxDocumentRequest taxDocument) {
        this.taxDocument = taxDocument;
    }

    public String getId() {
        return id;
    }

    public TaxDocumentRequest getTaxDocument() {
        return taxDocument;
    }
}
