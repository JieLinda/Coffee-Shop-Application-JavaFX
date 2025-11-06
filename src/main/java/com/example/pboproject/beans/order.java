package com.example.pboproject.beans;

public class order {
    //Product detial id SERIAL
    private String priceid; //o
    private String productName; //o
    private String type; //o
    private Sizes size; //o
    private double price; //o
    private String request; //o
    private int qty; //o
    private double totaldisc ; //P
    private String transactionNum; //P
    private double totalprice; //o
    private int dicount_id; //P


    public order() {
        this.totaldisc = 0;
        this.request = "-";
        this.dicount_id = -1;
    }
    public order(String priceid, String productName, String type, Sizes size, double price, String request, int qty) {
        this.priceid = priceid;
        this.productName = productName;
        this.type = type;
        this.size = size;
        this.price = price;
        this.request = request;
        this.qty = qty;
        this.setTotaldisc(0);
    }


    public int getDicount_id() {
        return dicount_id;
    }

    public void setDicount_id(int dicount_id) {
        this.dicount_id = dicount_id;
    }

    public double getTotaldisc() {
        return totaldisc;
    }

    public void setTotaldisc(double totaldisc) {
        this.totaldisc = totaldisc;
    }

    public String getTransactionNum() {
        return transactionNum;
    }

    public void setTransactionNum(String transactionNum) {
        this.transactionNum = transactionNum;
    }

    public double getTotalprice() {
        return totalprice;
    }

    public void setTotalprice(double totalprice) {
        this.totalprice = totalprice;
    }


    public String getPriceid() {
        return priceid;
    }

    public void setPriceid(String priceid) {
        this.priceid = priceid;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
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

    public String getRequest() {
        return request;
    }

    public void setRequest(String request) {
        this.request = request;
    }

    public int getQty() {
        return qty;
    }

    public void setQty(int qty) {
        this.qty = qty;
    }
    @Override
    public String toString() {
        return "SkinCareProduct{" +
                "id=" + priceid +
                ", name='" + productName + '\'' +
                ", description='" + request + '\'' +
                ", price=" + price +
                '}';
    }
}
