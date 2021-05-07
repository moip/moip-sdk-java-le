package br.com.moip.resource;

import br.com.moip.resource.links.CustomerLinks;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Customer {

    private String ownId;
    private String fullname;
    private String email;
    private TaxDocument taxDocument;
    private Phone phone;
    private Date birthDate;
    private List<Address> addresses = new ArrayList<>();
    private ShippingAddress shippingAddress;
    private String id;
    private FundingInstrument fundingInstrument;
    private MoipAccount moipAccount;
    private CustomerLinks _links;
    private List<FundingInstrument> fundingInstruments;

    public String getId() {
        return id;
    }

    public String getOwnId() {
        return ownId;
    }

    public String getFullname() {
        return fullname;
    }

    public Date getBirthDate() {
        return birthDate;
    }

    public String getEmail() {
        return email;
    }

    public Customer setEmail(final String email) {
        this.email = email;

        return this;
    }

    public TaxDocument getTaxDocument() {
        return taxDocument;
    }

    public Phone getPhone() {
        return phone;
    }

    public List<Address> getAddresses() { return addresses; }

    public ShippingAddress getShippingAddress() {
        return shippingAddress;
    }

    public FundingInstrument getFundingInstrument() {
        return fundingInstrument;
    }

    public String getMoipAccountId() { 
        return moipAccount != null ? moipAccount.getId() : null;
    }

    public CustomerLinks getLinks() { return _links; }

    public List<FundingInstrument> getFundingInstruments() {
        return fundingInstruments;
    }

    @Override
    public String toString() {
        return "Customer{" +
                "id='" + id + '\'' +
                ", ownId='" + ownId + '\'' +
                ", fullname='" + fullname + '\'' +
                ", email='" + email + '\'' +
                ", taxDocument=" + taxDocument +
                ", phone=" + phone +
                ", birthDate=" + birthDate +
                ", addresses=[" + addresses + ']' +
                ", shippingAddress=" + shippingAddress +
                ", fundingInstrument=" + fundingInstrument +
                ", moipAccount={" + moipAccount +
                ", _links=" + _links +
                ", fundingInstruments=[" + fundingInstruments + ']' +
                '}';
    }

}
