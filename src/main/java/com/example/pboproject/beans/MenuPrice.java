package com.example.pboproject.beans;

public class MenuPrice {
    private String sizePrizeId;
    private Sizes size;
    private double price;

    public MenuPrice(String sizePrizeId, Sizes size, double price) {
        this.sizePrizeId = sizePrizeId;
        this.size = size;
        this.price = price;
    }

    public MenuPrice() {

    }

    public String getSizePrizeId() {
        return sizePrizeId;
    }

    public Sizes getSize() {
        return size;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public void setSizePrizeId(String sizePrizeId) {
        this.sizePrizeId = sizePrizeId;
    }

    public void setSize(Sizes size) {
        this.size = size;
    }
}
