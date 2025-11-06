package com.example.pboproject.beans;

public class Discount {
    private int id;
    private Double disc;
    private String requiredType;
    private String requiredProduct;
    private String pmethod;
    private String bank;
    int payment_id;

    public Discount(int id, Double disc, String requiredType, String requiredProduct, String pmethod, String bank) {
        this.id = id;
        this.disc = disc;
        this.requiredType = requiredType;
        this.requiredProduct = requiredProduct;
        this.pmethod = pmethod;
        this.bank = bank;
    }

    public Discount() {
    }

    public int getPayment_id() {
        return payment_id;
    }

    public void setPayment_id(int payment_id) {
        this.payment_id = payment_id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Double getDisc() {
        return disc;
    }

    public void setDisc(Double disc) {
        this.disc = disc;
    }

    public String getRequiredType() {
        return requiredType;
    }

    public void setRequiredType(String requiredType) {
        this.requiredType = requiredType;
    }

    public String getRequiredProduct() {
        return requiredProduct;
    }

    public void setRequiredProduct(String requiredProduct) {
        this.requiredProduct = requiredProduct;
    }

    public String getPmethod() {
        return pmethod;
    }

    public void setPmethod(String pmethod) {
        this.pmethod = pmethod;
    }

    public String getBank() {
        return bank;
    }

    public void setBank(String bank) {
        this.bank = bank;
    }
}