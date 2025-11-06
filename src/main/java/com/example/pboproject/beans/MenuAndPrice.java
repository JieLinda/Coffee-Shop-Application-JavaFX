package com.example.pboproject.beans;

public class MenuAndPrice {
    private String productId;
    private String productType;
    private String productName;
    private String sizePriceId;
    private Sizes size;
    private double price;

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
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

    public String getSizePriceId() {
        return sizePriceId;
    }

    public void setSizePriceId(String sizePrizeId) {
        this.sizePriceId = sizePrizeId;
    }

    public Sizes getSize() {
        return size;
    }

    public void setSize(Sizes size) {
        this.size = size;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
}
