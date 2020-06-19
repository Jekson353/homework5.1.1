package com.samoylenko.homework222;

import android.graphics.drawable.Drawable;

public class Product {

    private String tittle;
    private String subtitle;
    private Drawable image;

    public Product(String tittle, String subtitle, Drawable image) {
        this.tittle = tittle;
        this.subtitle = subtitle;
        this.image = image;
    }

    public Product() {
    }

    public String getTittle() {
        return tittle;
    }

    public void setTittle(String tittle) {
        this.tittle = tittle;
    }

    public String getSubtitle() {
        return subtitle;
    }

    public void setSubtitle(String subtitle) {
        this.subtitle = subtitle;
    }

    public Drawable getImage() {
        return image;
    }

    public void setImage(Drawable image) {
        this.image = image;
    }

}
