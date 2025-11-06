package com.example.pboproject.beans;

public class TransactionProductDetail {
    private int productDetailId;
    private int productQuantity;
    private int totalPrice;
    private int totalDisc;
    private String specialRequest;

    public TransactionProductDetail(int productDetailId, int productQuantity, int totalPrice, int totalDisc, String specialRequest) {
        this.productDetailId = productDetailId;
        this.productQuantity = productQuantity;
        this.totalPrice = totalPrice;
        this.totalDisc = totalDisc;
        this.specialRequest = specialRequest;
    }
}
