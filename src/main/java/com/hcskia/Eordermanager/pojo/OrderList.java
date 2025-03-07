package com.hcskia.Eordermanager.pojo;
import jakarta.persistence.*;
import java.io.Serializable;
import java.util.Date;


@Entity
public class OrderList {
    public static class CompositeIdId implements Serializable {
        @Column(name = "OrderId")
        private String OrderId;
        public String getOrderId(){return this.OrderId;}
        public void setOrderId(String OrderId){this.OrderId = OrderId;}

        @Column(name = "Platform")
        private int Platform;
        public int getPlatform(){return this.Platform;}
        public void setPlatform(int Platform){this.Platform = Platform;}

    }

    @EmbeddedId
    private MaiData.CompositeIdId compositeId;

    @Column(name = "Buyer")
    private String Buyer;
    public String getBuyer(){return this.Buyer;}
    public void setBuyer(String Buyer){this.Buyer = Buyer;}

    @Column(name = "OrderDate")
    private Date OrderDate;
    public Date getOrderDate(){return this.OrderDate;}
    public void setOrderDate(Date OrderDate){this.OrderDate = OrderDate;}

    @Column(name = "OrderInfo")
    private String OrderInfo;
    public String getOrderInfo(){return this.OrderInfo;}
    public void setOrderInfo(String OrderInfo){this.OrderInfo = OrderInfo;}

    @Column(name = "OrderPrice")
    private double OrderPrice;
    public double getOrderPrice(){return this.OrderPrice;}
    public void setOrderPrice(double OrderPrice){this.OrderPrice = OrderPrice;}

    @Column(name = "OrderMount")
    private int OrderMount;
    public int getOrderMount(){return this.OrderMount;}
    public void setOrderMount(int OrderMount){this.OrderMount = OrderMount;}

    @Column(name = "ProductID")
    private String ProductID;
    public String getProductID(){return this.ProductID;}
    public void setProductID(String ProductID){this.ProductID = ProductID;}

    @Column(name = "BuyerAddress")
    private String BuyerAddress;
    public String getBuyerAddress(){return this.BuyerAddress;}
    public void setBuyerAddress(String BuyerAddress){this.BuyerAddress = BuyerAddress;}
}
