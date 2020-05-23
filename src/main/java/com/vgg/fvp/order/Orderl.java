package com.vgg.fvp.order;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.vgg.fvp.common.data.AbstractEntity;
import com.vgg.fvp.customer.Customer;
import com.vgg.fvp.vendor.Menu.Menu;
import com.vgg.fvp.vendor.Vendor;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Orderl extends AbstractEntity {
    private String description;
    private BigDecimal amountDue;
    private BigDecimal amountPaid;
    private BigDecimal amountOutstanding;
    private String orderStatus;
    private String paymentStatus;

    @JsonIgnore
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name="orders_menus",joinColumns = @JoinColumn(name = "order_id"),
            inverseJoinColumns = @JoinColumn(name="menu_id"))
    private List<Menu> itemsOrdered;

    @ManyToOne
    private Customer customer;
    @ManyToOne
    private Vendor vendor;

    public Orderl() {
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getAmountDue() {
        return amountDue;
    }

    public void setAmountDue(BigDecimal amountDue) {
        this.amountDue = amountDue;
    }

    public BigDecimal getAmountPaid() {
        return amountPaid;
    }

    public void setAmountPaid(BigDecimal amountPaid) {
        this.amountPaid = amountPaid;
    }

    public BigDecimal getAmountOutstanding() {
        return amountOutstanding;
    }

    public void setAmountOutstanding(BigDecimal amountOutstanding) {
        this.amountOutstanding = amountOutstanding;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public String getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(String paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public List<Menu> getItemsOrdered() {
        return itemsOrdered;
    }

    public void setItemsOrdered(List<Menu> itemsOrdered) {
        this.itemsOrdered = itemsOrdered;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public Vendor getVendor() {
        return vendor;
    }

    public void setVendor(Vendor vendor) {
        this.vendor = vendor;
    }

    public void addItem(Menu menu){
        if(itemsOrdered == null){
            itemsOrdered = new ArrayList<>();
        }
        itemsOrdered.add(menu);
    }

    @Override
    public String toString() {
        return "Orderl{" +
                "description='" + description + '\'' +
                ", amountDue=" + amountDue +
                ", amountPaid=" + amountPaid +
                ", amountOutstanding=" + amountOutstanding +
                ", orderStatus='" + orderStatus + '\'' +
                ", itemsOrdered=" + itemsOrdered +
                ", customers=" + customer +
                ", vendor=" + vendor +
                '}';
    }
}
