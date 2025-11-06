package com.example.pboproject.beans;

public class Transaction {
    private String number; //END
    private String custname;
    private int totalitem;
    private double totalTransaction; //beserta diskonnya dan potongan dari point jika member
    private double totalPrice; //totalPrice dari order //TIDAK DIMASUKAN KE DATA BASE
    private double totalDisc; // total jumlah potongan //altertable
    private int point_disc; //akumulasi point member yg digunakan sebagai potongan harga pada transaksi tersebut
    private int pointgain; //untuk member
    private int payment_method;  //payment_id
    private double payment;
    private double change;

    private String cashier; //altered
    private int member_id;

    public void reset() {
        this.number = null;
        this.custname = null;
        this.totalitem = 0;
        this.totalTransaction = 0.0;
        this.totalPrice = 0.0;
        this.totalDisc = 0.0;
        this.point_disc = 0;
        this.pointgain = 0;
        this.payment_method = 0;
        this.payment = 0.0;
        this.change = 0.0;
        this.cashier = null;
        this.member_id = -1;
    }
    public Transaction() {
        this.payment = 0.0;
        this.change = 0.0;
        this.totalDisc = 0.0;
        this.point_disc = 0;
        this.pointgain = 0;
        this.member_id =-1;
    }
    public Transaction(String number, String date, String time, String custname, int totalitem, double totalTransaction, double payment, double change, int pointgain, String cashier) {
        this.number = number;
        this.custname = custname;
        this.totalitem = totalitem;
        this.totalTransaction = totalTransaction;
        this.payment = 0;
        this.change = 0;
        this.pointgain = 0;
        this.cashier = cashier;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public double getTotalDisc() {
        return totalDisc;
    }

    public void setTotalDisc(double totalDisc) {
        this.totalDisc = totalDisc;
    }

    public int getPoint_disc() {
        return point_disc;
    }

    public void setPoint_disc(int point_disc) {
        this.point_disc = point_disc;
    }



    public int getPayment_method() {
        return payment_method;
    }

    public void setPayment_method(int payment_method) {
        this.payment_method = payment_method;
    }

    public int getMember_id() {
        return member_id;
    }

    public void setMember_id(int member_id) {
        this.member_id = member_id;
    }

    public String getCashier() {
        return cashier;
    }

    public void setCashier(String cashier) {
        this.cashier = cashier;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getCustname() {
        return custname;
    }

    public void setCustname(String custname) {
        this.custname = custname;
    }

    public int getTotalitem() {
        return totalitem;
    }

    public void setTotalitem(int totalitem) {
        this.totalitem = totalitem;
    }

    public double getTotalTransaction() {
        return totalTransaction;
    }

    public void setTotalTransaction(double totalTransaction) {
        this.totalTransaction = totalTransaction;
    }

    public double getPayment() {
        return payment;
    }

    public void setPayment(double payment) {
        this.payment = payment;
    }

    public double getChange() {
        return change;
    }

    public void setChange(double change) {
        this.change = change;
    }

    public int getPointgain() {
        return pointgain;
    }

    public void setPointgain(int pointgain) {
        this.pointgain = pointgain;
    }

}
