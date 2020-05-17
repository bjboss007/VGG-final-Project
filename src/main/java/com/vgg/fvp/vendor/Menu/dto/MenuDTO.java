package com.vgg.fvp.vendor.Menu.dto;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

public class MenuDTO {

    private Long id;
    @NotNull
    private String name;
    @NotNull
    private String description;
    @NotNull
    private BigDecimal price;
    @NotNull
    private Long quantity;
    @NotNull
    private boolean isRecurring = false;
    @NotNull
    private int frequencyOfReocurrence;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    @Override
    public String toString() {
        return "MenuDTO{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", price=" + price +
                ", quantity=" + quantity +
                ", isRecurring=" + isRecurring +
                ", frequencyOfReocurrence=" + frequencyOfReocurrence +
                '}';
    }
}
