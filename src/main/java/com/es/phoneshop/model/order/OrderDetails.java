package com.es.phoneshop.model.order;

import com.es.phoneshop.enumeration.PaymentMethod;

import java.math.BigDecimal;
import java.time.LocalDate;

public class OrderDetails {
    private LocalDate deliveryDate;
    private BigDecimal deliveryCost;
    private PaymentMethod paymentMethod;

    public LocalDate getDeliveryDate() {
        return deliveryDate;
    }

    public void setDeliveryDate(LocalDate deliveryDate) {
        this.deliveryDate = deliveryDate;
    }

    public BigDecimal getDeliveryCost() {
        return deliveryCost;
    }

    public void setDeliveryCost(BigDecimal deliveryCost) {
        this.deliveryCost = deliveryCost;
    }

    public PaymentMethod getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(PaymentMethod paymentMethod) {
        this.paymentMethod = paymentMethod;
    }
}
