package com.vgg.fvp.order;

import java.math.BigDecimal;

public class PaymentDTO {

    private BigDecimal amount;

    public PaymentDTO() {
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }
}
