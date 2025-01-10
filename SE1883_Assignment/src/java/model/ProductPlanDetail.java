/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import java.sql.Date;
import java.util.ArrayList;

/**
 *
 * @author admin
 */
public class ProductPlanDetail {
    private int id;
    private ProductPlanHeader header;
    private Shift shift;
    private Date date;
    private int quantity;
    private ArrayList<WorkAssignment> works = new ArrayList<>();

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public ProductPlanHeader getHeader() {
        return header;
    }

    public void setHeader(ProductPlanHeader header) {
        this.header = header;
    }

    public Shift getShift() {
        return shift;
    }

    public void setShift(Shift shift) {
        this.shift = shift;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public ArrayList<WorkAssignment> getWorks() {
        return works;
    }

    public void setWorks(ArrayList<WorkAssignment> works) {
        this.works = works;
    }
    
    
}
