package com.samoylenko.homework222;

import android.graphics.drawable.Drawable;

class Product {

    private String tittle;
    private String subtitle;
    private Drawable image;

    Product(String tittle, String subtitle, Drawable image) {
        this.tittle = tittle;
        this.subtitle = subtitle;
        this.image = image;
    }

    String getTittle() {
        return tittle;
    }

    String getSubtitle() {
        return subtitle;
    }

    Drawable getImage() {
        return image;
    }
}
