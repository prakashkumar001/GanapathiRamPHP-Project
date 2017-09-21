package com.ganapathyram.theatre.model;

/**
 * Created by Prakash on 9/21/2017.
 */

public class ProductAvailable {
    public boolean isProductAvailable;

    public ProductAvailable(boolean isProductAvailable, int indexofProduct) {
        this.isProductAvailable = isProductAvailable;
        this.indexofProduct = indexofProduct;
    }

    public int indexofProduct;

}
