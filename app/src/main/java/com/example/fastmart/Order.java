package com.example.fastmart;
import java.util.ArrayList;
public class Order {
    public String orderId, date, totalCost, customerName;
    public ArrayList<items> orderedItems;
    public Order() {}
    public Order(String orderId, String date, String totalCost, String customerName, ArrayList<items> orderedItems) {
        this.orderId = orderId;
        this.date = date;
        this.totalCost = totalCost;
        this.customerName = customerName;
        this.orderedItems = orderedItems;
    }
    public String getOrderId() { return orderId; }
    public String getDate() { return date; }
    public String getTotalCost() { return totalCost; }
    public String getCustomerName() { return customerName; }
    public ArrayList<items> getOrderedItems() { return orderedItems; }
}
