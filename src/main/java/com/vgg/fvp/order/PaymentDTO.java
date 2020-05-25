package com.vgg.fvp.order;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

public class PaymentDTO {

    @NotNull
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
