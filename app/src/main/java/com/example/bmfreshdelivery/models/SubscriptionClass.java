package com.example.bmfreshdelivery.models;

import java.text.DecimalFormat;

public class SubscriptionClass {
    private String customerName;
    private String planSelected;
    double planTypePrice;
    double milkPrice;
    double lemonadePrice;
    double deliveryTypePrice;

    // Formatting the total cost to two decimal places with a dollar sign
    DecimalFormat decimalFormat = new DecimalFormat("CAD 0.00");

    // Constructor to initialize all the fields
    public SubscriptionClass(String customerName, String planSelected, double planTypePrice, double milkPrice, double lemonadePrice, double deliveryTypePrice){
        this.customerName = customerName;
        this.planSelected = planSelected;
        this.planTypePrice = planTypePrice;
        this.milkPrice = milkPrice;
        this.lemonadePrice = lemonadePrice;
        this.deliveryTypePrice = deliveryTypePrice;
    }

    // Method to get a formatted string representing the subscription details
    public String getSubscriptionClass(){
        double additionalCost = milkPrice + lemonadePrice; //Calculated additional cost
        double totalCost = 0;
        totalCost = additionalCost + planTypePrice + deliveryTypePrice; // Calculate total cost

        // Return a formatted string with subscription details
        return " Name: " + customerName + "\n" +
                " Plan: " + planSelected + "\n" +
                " Total Cost: " + decimalFormat.format(totalCost);
    }
}
