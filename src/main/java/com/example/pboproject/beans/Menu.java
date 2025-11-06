package com.example.pboproject.beans;

public class Menu {
    private String productId;
    private String productType;
    private String productName;

    public Menu(String productId, String productType, String productName) {
        this.productId = productId;
        this.productType = productType;
        this.productName = productName;
    }

    public Menu() {

    }

    public String getProductType() {
        return productType;
    }

    public void setProductType(String productType) {
        this.productType = productType;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }
}
