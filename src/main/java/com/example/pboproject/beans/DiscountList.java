package com.example.pboproject.beans;

public class DiscountList {
    private int discId;
    private double discPercent;

    public double getDiscPercent() {
        return discPercent;
    }

    public void setDiscPercent(double discPercent) {
        this.discPercent = discPercent;
    }

    private String dateStart;
    private String validUntilDate;
    private int requiredPaymentMethod;
    private String requiredProduct;
    private String requiredType;

    public String getRequiredType() {
        return requiredType;
    }

    public void setRequiredType(String requiredType) {
        this.requiredType = requiredType;
    }

    public void setDiscId(int discId) {
        this.discId = discId;
    }

    public void setDateStart(String dateStart) {
        this.dateStart = dateStart;
    }

    public void setRequiredPaymentMethod(int requiredPaymentMethod) {
        this.requiredPaymentMethod = requiredPaymentMethod;
    }

    public void setRequiredProduct(String requiredProduct) {
        this.requiredProduct = requiredProduct;
    }

    public int getDiscId() {
        return discId;
    }

    public String getDateStart() {
        return dateStart;
    }

    public String getValidUntilDate() {
        return validUntilDate;
    }

    public int getRequiredPaymentMethod() {
        return requiredPaymentMethod;
    }

    public String getRequiredProduct() {
        return requiredProduct;
    }

    public void setValidUntilDate(String validUntilDate) {
        this.validUntilDate = validUntilDate;
    }



}
