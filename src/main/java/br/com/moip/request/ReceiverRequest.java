package br.com.moip.request;

public class ReceiverRequest {
    private ReceiverTypeRequest type;
    private MoipAccountRequest moipAccount;
    private AmountRequest amount;
    private boolean feePayor;

    public ReceiverRequest primary(final String moipAccount, final AmountRequest amountRequest) {
        defaultAttributes(ReceiverTypeRequest.PRIMARY, moipAccount, amountRequest, true);

        return this;
    }

    public ReceiverRequest primary(final String moipAccount, final AmountRequest amountRequest, boolean feePayor) {
        defaultAttributes(ReceiverTypeRequest.PRIMARY, moipAccount, amountRequest, feePayor);

        return this;
    }

    public ReceiverRequest primary(final TaxDocumentRequest document, final AmountRequest amountRequest, boolean feePayor) {
        defaultAttributes(ReceiverTypeRequest.PRIMARY, document, amountRequest, feePayor);

        return this;
    }

    public ReceiverRequest secondary(final String moipAccount, final AmountRequest amountRequest) {
        defaultAttributes(ReceiverTypeRequest.SECONDARY, moipAccount, amountRequest, false);

        return this;
    }

    public ReceiverRequest secondary(final String moipAccount, final AmountRequest amountRequest, boolean feePayor) {
        defaultAttributes(ReceiverTypeRequest.SECONDARY, moipAccount, amountRequest, feePayor);

        return this;
    }

    public ReceiverRequest secondary(final TaxDocumentRequest document, final AmountRequest amountRequest, boolean feePayor) {
        defaultAttributes(ReceiverTypeRequest.SECONDARY, document, amountRequest, feePayor);

        return this;
    }


    private void defaultAttributes(ReceiverTypeRequest receiverTypeRequest, String moipAccount, AmountRequest amountRequest, boolean feePayor) {
        this.type = receiverTypeRequest;
        this.moipAccount = new MoipAccountRequest(moipAccount);
        this.amount = amountRequest;
        this.feePayor = feePayor;
    }

    private void defaultAttributes(ReceiverTypeRequest receiverTypeRequest, TaxDocumentRequest document, AmountRequest amountRequest, boolean feePayor) {
        this.type = receiverTypeRequest;
        this.amount = amountRequest;
        this.feePayor = feePayor;
        this.moipAccount = new MoipAccountRequest(document);
    }

    public ReceiverTypeRequest getType() {
        return type;
    }

    public MoipAccountRequest getMoipAccount() {
        return moipAccount;
    }

    public AmountRequest getAmount() {
        return amount;
    }

    public boolean getFeePayor() {
        return feePayor;
    }
}
