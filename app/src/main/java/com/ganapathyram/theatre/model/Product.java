package com.ganapathyram.theatre.model;

/**
 * Created by Prakash on 9/19/2017.
 */

public class Product {
    public String productid;
    public String productname;
    public String productprice;
    public int quantity;
    public int productimage;
    public String totalprice;
    public boolean isSelected;

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public String getTotalprice() {
        return totalprice;
    }

    public void setTotalprice(String totalprice) {
        this.totalprice = totalprice;
    }

    public int getProductimage() {
        return productimage;
    }

    public void setProductimage(int productimage) {
        this.productimage = productimage;
    }

    public String getProductid() {
        return productid;
    }

    public void setProductid(String productid) {
        this.productid = productid;
    }

    public String getProductname() {
        return productname;
    }

    public void setProductname(String productname) {
        this.productname = productname;
    }

    public String getProductprice() {
        return productprice;
    }

    public void setProductprice(String productprice) {
        this.productprice = productprice;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public Product(String productid, String productname, String productprice, int productimage, int quantity,String totalprice,boolean isSelected) {
        this.productid = productid;
        this.productname = productname;
        this.productprice = productprice;
        this.productimage = productimage;
        this.quantity=quantity;
        this.totalprice=totalprice;
        this.isSelected=isSelected;
    }
}

