package com.example.fastmart;

import java.io.Serializable;

public class items implements Serializable {
    public String name, model, newPrice, originalPrice, description, sDesc, type, imageUrl;
    int image, quantity;
    boolean favourite, dotd;

    public items() {} // Required for Firebase

    public items(String name, String model, String newPrice, String originalPrice, String description, String sDesc, int img, String type, boolean dotd) {
        this.name = name; 
        this.newPrice = newPrice; 
        this.image = img; 
        this.quantity = 1; 
        this.sDesc = sDesc;
        this.dotd = dotd;
        this.model = model; 
        this.description = description; 
        this.favourite = false;
        this.type = type;
        this.originalPrice = originalPrice;
        this.imageUrl = "";
    }

    public boolean isDotd() {
        return dotd;
    }

    public String getName() {
        return name;
    }

    public String getModel() {
        return model;
    }

    public String getNewPrice() {
        return newPrice;
    }

    public String getOriginalPrice(){
        return originalPrice;
    }

    public String getDescription() {
        return description;
    }

    public int getImage() {
        return image;
    }

    public int getQuantity() {
        return quantity;
    }

    public boolean isFavourite() {
        return favourite;
    }

    public String getsDesc() {
        return sDesc;
    }

    public String getType() {
        return type;
    }

    public void setFavourite(boolean favourite) {
        this.favourite = favourite;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

}
