package com.jde.portal.dto;

public class SalesOrderLineDto {
    private String Item_Number;
    private Double Quantity_Ordered;
    private Double Unit_Price;
    private String Branch__Plant;
    private String UoM;

    // Getters and Setters
    public String getItem_Number() { return Item_Number; }
    public void setItem_Number(String Item_Number) { this.Item_Number = Item_Number; }
    public Double getQuantity_Ordered() { return Quantity_Ordered; }
    public void setQuantity_Ordered(Double Quantity_Ordered) { this.Quantity_Ordered = Quantity_Ordered; }
    public Double getUnit_Price() { return Unit_Price; }
    public void setUnit_Price(Double Unit_Price) { this.Unit_Price = Unit_Price; }
    public String getBranch__Plant() { return Branch__Plant; }
    public void setBranch__Plant(String Branch__Plant) { this.Branch__Plant = Branch__Plant; }
    public String getUoM() { return UoM; }
    public void setUoM(String UoM) { this.UoM = UoM; }
} 