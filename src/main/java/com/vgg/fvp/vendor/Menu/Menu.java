package com.vgg.fvp.vendor.Menu;

import com.vgg.fvp.common.data.AbstractEntity;
import com.vgg.fvp.vendor.Vendor;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import java.math.BigDecimal;

@Entity
public class Menu extends AbstractEntity {

    private String name;
    private String description;
    private BigDecimal price;
    private Long quantity;
    private boolean isRecurring = false;
    private int frequencyOfReocurrence;
    @ManyToOne
    private Vendor vendor;

    public Menu() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Long getQuantity() {
        return quantity;
    }

    public void setQuantity(Long quantity) {
        this.quantity = quantity;
    }

    public boolean isRecurring() {
        return isRecurring;
    }

    public void setRecurring(boolean recurring) {
        isRecurring = recurring;
    }

    public int getFrequencyOfReocurrence() {
        return frequencyOfReocurrence;
    }

    public void setFrequencyOfReocurrence(int frequencyOfReocurrence) {
        this.frequencyOfReocurrence = frequencyOfReocurrence;
    }

    public Vendor getVendor() {
        return vendor;
    }

    public void setVendor(Vendor vendor) {
        this.vendor = vendor;
    }

    @Override
    public String toString() {
        return "Menu{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", price=" + price +
                ", quantity=" + quantity +
                ", isRecuring=" + isRecurring +
                ", frequencyOfReocurrence=" + frequencyOfReocurrence +
                ", vendor=" + vendor +
                '}';
    }
}
