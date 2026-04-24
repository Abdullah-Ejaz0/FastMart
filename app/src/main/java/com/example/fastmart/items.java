package com.example.fastmart;

public class items {
    public String name, model, newPrice, originalPrice, description, sDesc, type;
    int image, quantity;
    boolean favourite, dotd;

    items(String name, String model, String newPrice, String originalPrice, String description, String sDesc, int img, String type, boolean dotd) {
        this.name = name; this.newPrice = newPrice; this.image = img; this.quantity = 1; this.sDesc = sDesc;this.dotd = dotd;
        this.model = model; this.description = description; this.favourite = false;this.type = type;this.originalPrice = originalPrice;
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

}
