package com.ganapathyram.theatre.database;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;

/**
 * Created by Prakash on 11/1/2017.
 */

@Entity
public class LocalSalesError {
    @Id

    public Long productId;
    public String productUid;
    public String productName;
    public String categoryUid;
    public String price;
    public String description;
    public String taxPercent;
    public String active;
    public int quantity;
    public String productimage;
    public String taxAmount;
    public String totalprice;
    public boolean isSelected;
    @Generated(hash = 2121792102)
    public LocalSalesError(Long productId, String productUid, String productName,
            String categoryUid, String price, String description, String taxPercent,
            String active, int quantity, String productimage, String taxAmount,
            String totalprice, boolean isSelected) {
        this.productId = productId;
        this.productUid = productUid;
        this.productName = productName;
        this.categoryUid = categoryUid;
        this.price = price;
        this.description = description;
        this.taxPercent = taxPercent;
        this.active = active;
        this.quantity = quantity;
        this.productimage = productimage;
        this.taxAmount = taxAmount;
        this.totalprice = totalprice;
        this.isSelected = isSelected;
    }
    @Generated(hash = 882844260)
    public LocalSalesError() {
    }
    public Long getProductId() {
        return this.productId;
    }
    public void setProductId(Long productId) {
        this.productId = productId;
    }
    public String getProductUid() {
        return this.productUid;
    }
    public void setProductUid(String productUid) {
        this.productUid = productUid;
    }
    public String getProductName() {
        return this.productName;
    }
    public void setProductName(String productName) {
        this.productName = productName;
    }
    public String getCategoryUid() {
        return this.categoryUid;
    }
    public void setCategoryUid(String categoryUid) {
        this.categoryUid = categoryUid;
    }
    public String getPrice() {
        return this.price;
    }
    public void setPrice(String price) {
        this.price = price;
    }
    public String getDescription() {
        return this.description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public String getTaxPercent() {
        return this.taxPercent;
    }
    public void setTaxPercent(String taxPercent) {
        this.taxPercent = taxPercent;
    }
    public String getActive() {
        return this.active;
    }
    public void setActive(String active) {
        this.active = active;
    }
    public int getQuantity() {
        return this.quantity;
    }
    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
    public String getProductimage() {
        return this.productimage;
    }
    public void setProductimage(String productimage) {
        this.productimage = productimage;
    }
    public String getTaxAmount() {
        return this.taxAmount;
    }
    public void setTaxAmount(String taxAmount) {
        this.taxAmount = taxAmount;
    }
    public String getTotalprice() {
        return this.totalprice;
    }
    public void setTotalprice(String totalprice) {
        this.totalprice = totalprice;
    }
    public boolean getIsSelected() {
        return this.isSelected;
    }
    public void setIsSelected(boolean isSelected) {
        this.isSelected = isSelected;
    }


}
