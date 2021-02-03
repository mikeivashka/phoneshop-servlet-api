package com.es.phoneshop.model.product;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;

public class PriceHistoryEvent {
    private BigDecimal newPrice;
    private LocalDate time;

    public PriceHistoryEvent(BigDecimal newPrice, LocalDate time) {
        this.newPrice = newPrice;
        this.time = time;
    }

    public BigDecimal getNewPrice() {
        return newPrice;
    }

    public void setNewPrice(BigDecimal newPrice) {
        this.newPrice = newPrice;
    }

    public LocalDate getTime() {
        return time;
    }

    public void setTime(LocalDate time) {
        this.time = time;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PriceHistoryEvent that = (PriceHistoryEvent) o;

        if (!Objects.equals(newPrice, that.newPrice)) return false;
        return Objects.equals(time, that.time);
    }

    @Override
    public int hashCode() {
        int result = newPrice != null ? newPrice.hashCode() : 0;
        result = 31 * result + (time != null ? time.hashCode() : 0);
        return result;
    }
}
