package com.jde.portal.dto;
import java.util.List;

public class SalesOrderDto {
    private String Order_Number;
    private String Order_Key_Company;
    private String Order_Type;
    private String Currency_Code;
    private String Business_Unit;
    private String Long_Address_Number_ShipTo;
    private String Long_Address_Number_SoldTo;
    private String Order_Date;
    private String Requested;
    private List<SalesOrderLineDto> orderLines;

    // Getters and Setters
    public String getOrder_Number() { return Order_Number; }
    public void setOrder_Number(String Order_Number) { this.Order_Number = Order_Number; }
    public String getOrder_Key_Company() { return Order_Key_Company; }
    public void setOrder_Key_Company(String Order_Key_Company) { this.Order_Key_Company = Order_Key_Company; }
    public String getOrder_Type() { return Order_Type; }
    public void setOrder_Type(String Order_Type) { this.Order_Type = Order_Type; }
    public String getCurrency_Code() { return Currency_Code; }
    public void setCurrency_Code(String Currency_Code) { this.Currency_Code = Currency_Code; }
    public String getBusiness_Unit() { return Business_Unit; }
    public void setBusiness_Unit(String Business_Unit) { this.Business_Unit = Business_Unit; }
    public String getLong_Address_Number_ShipTo() { return Long_Address_Number_ShipTo; }
    public void setLong_Address_Number_ShipTo(String Long_Address_Number_ShipTo) { this.Long_Address_Number_ShipTo = Long_Address_Number_ShipTo; }
    public String getLong_Address_Number_SoldTo() { return Long_Address_Number_SoldTo; }
    public void setLong_Address_Number_SoldTo(String Long_Address_Number_SoldTo) { this.Long_Address_Number_SoldTo = Long_Address_Number_SoldTo; }
    public String getOrder_Date() { return Order_Date; }
    public void setOrder_Date(String Order_Date) { this.Order_Date = Order_Date; }
    public String getRequested() { return Requested; }
    public void setRequested(String Requested) { this.Requested = Requested; }
    public List<SalesOrderLineDto> getOrderLines() { return orderLines; }
    public void setOrderLines(List<SalesOrderLineDto> orderLines) { this.orderLines = orderLines; }
}