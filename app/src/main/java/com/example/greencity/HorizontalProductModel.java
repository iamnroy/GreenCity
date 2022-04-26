package com.example.greencity;

public class HorizontalProductModel {
    private int productImage;
    private String productTitle;
    private String productDesc;
    private String productPrice;

    public HorizontalProductModel(int productImage, String productTitle, String productDesc, String productPrice) {
        this.productImage = productImage;
        this.productTitle = productTitle;
        this.productDesc = productDesc;
        this.productPrice = productPrice;
    }

    public int getProductImage() {
        return productImage;
    }

    public void setProductImage(int productImage) {
        this.productImage = productImage;
    }

    public String getProductTitle() {
        return productTitle;
    }

    public void setProductTitle(String productTitle) {
        this.productTitle = productTitle;
    }

    public String getProductDesc() {
        return productDesc;
    }

    public void setProductDesc(String productDesc) {
        this.productDesc = productDesc;
    }

    public String getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(String productPrice) {
        this.productPrice = productPrice;
    }
}
