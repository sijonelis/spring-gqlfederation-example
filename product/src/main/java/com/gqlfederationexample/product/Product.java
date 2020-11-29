package com.gqlfederationexample.product;

public class Product {
    private final String upc;
    private final String name;
    private final Integer price;
    private final Integer weight;

    public Product(String upc, String name, Integer price, Integer weight) {
        this.upc = upc;
        this.name = name;
        this.price = price;
        this.weight = weight;
    }

    public String getUpc() {
        return upc;
    }

    public String getName() {
        return name;
    }

    public Integer getPrice() {
        return price;
    }

    public Integer getWeight() {
        return weight;
    }
}
