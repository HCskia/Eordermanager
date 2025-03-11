package com.hcskia.Eordermanager.pojo;
import jakarta.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Table(name = "orderlist")
@Entity
public class OrderList {
//    public static class CompositeIdId implements Serializable {
//
//
//    }
//
//    @EmbeddedId
//    private MaiData.CompositeIdId compositeId;
    @Id
    @Column(name = "orderid")
    private String orderId;
    public String getOrderId(){return this.orderId;}
    public void setOrderId(String orderId){this.orderId = orderId;}

    @Column(name = "platform")
    private String Platform;
    public String getPlatform(){return this.Platform;}
    public void setPlatform(String Platform){this.Platform = Platform;}

    @Column(name = "buyer")
    private String Buyer;
    public String getBuyer(){return this.Buyer;}
    public void setBuyer(String Buyer){this.Buyer = Buyer;}

    @Column(name = "orderdate")
    private Date OrderDate;
    public Date getOrderDate(){return this.OrderDate;}
    public void setOrderDate(Date OrderDate){this.OrderDate = OrderDate;}

    @Column(name = "orderdateconvert")
    private String OrderDateConvert;
    public String getOrderDateConvert(){return this.OrderDateConvert;}
    public void setOrderDateConvert(String OrderDateConvert){this.OrderDateConvert = OrderDateConvert;}

    @Column(name = "orderinfo")
    private String OrderInfo;
    public String getOrderInfo(){return this.OrderInfo;}
    public void setOrderInfo(String OrderInfo){this.OrderInfo = OrderInfo;}

    @Column(name = "orderprice")
    private double OrderPrice;
    public double getOrderPrice(){return this.OrderPrice;}
    public void setOrderPrice(double OrderPrice){this.OrderPrice = OrderPrice;}

    @Column(name = "ordermount")
    private int OrderMount;
    public int getOrderMount(){return this.OrderMount;}
    public void setOrderMount(int OrderMount){this.OrderMount = OrderMount;}

    @Column(name = "productid")
    private String ProductID;
    public String getProductID(){return this.ProductID;}
    public void setProductID(String ProductID){this.ProductID = ProductID;}

    @Column(name = "buyeraddress")
    private String BuyerAddress;
    public String getBuyerAddress(){return this.BuyerAddress;}
    public void setBuyerAddress(String BuyerAddress){this.BuyerAddress = BuyerAddress;}

    @Column(name = "userid")
    private String userId;
    public String getuserId(){return this.userId;}
    public void setuserId(String userId){this.userId = userId;}
}
