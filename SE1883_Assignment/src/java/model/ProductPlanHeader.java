/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import java.util.ArrayList;

/**
 *
 * @author admin
 */
public class ProductPlanHeader {
    private int id;
    private ProductPlan plan;
    private Product product;
    private int quantity;
    private float estimatedeffort;
    private ArrayList<ProductPlanDetail> details = new ArrayList<>();

    public ArrayList<ProductPlanDetail> getDetails() {
        return details;
    }

    public void setDetails(ArrayList<ProductPlanDetail> details) {
        this.details = details;
    }
    

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public ProductPlan getPlan() {
        return plan;
    }

    public void setPlan(ProductPlan plan) {
        this.plan = plan;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public float getEstimatedeffort() {
        return estimatedeffort;
    }

    public void setEstimatedeffort(float estimatedeffort) {
        this.estimatedeffort = estimatedeffort;
    }
    
            
}
