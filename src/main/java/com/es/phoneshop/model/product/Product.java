package com.es.phoneshop.model.product;

import java.math.BigDecimal;
import java.util.Currency;
import java.util.List;
import java.util.Objects;

public class Product {
    private Long id;
    private String code;
    private String description;
    private Currency currency;
    private int stock;
    private String imageUrl;
    private BigDecimal price;
    private List<PriceHistoryEvent> priceHistory;

    public Product(String code, String description, BigDecimal price, Currency currency, int stock, String imageUrl, List<PriceHistoryEvent> priceHistory) {
        this.code = code;
        this.description = description;
        this.currency = currency;
        this.stock = stock;
        this.imageUrl = imageUrl;
        this.priceHistory = priceHistory;
        this.price = price;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Currency getCurrency() {
        return currency;
    }

    public void setCurrency(Currency currency) {
        this.currency = currency;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public List<PriceHistoryEvent> getPriceHistory() {
        return priceHistory;
    }

    public void setPriceHistory(List<PriceHistoryEvent> priceHistory) {
        this.priceHistory = priceHistory;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Product product = (Product) o;

        if (stock != product.stock) return false;
        if (!Objects.equals(id, product.id)) return false;
        if (!Objects.equals(code, product.code)) return false;
        if (!Objects.equals(description, product.description)) return false;
        if (!Objects.equals(currency, product.currency)) return false;
        if (!Objects.equals(imageUrl, product.imageUrl)) return false;
        if (!Objects.equals(price, product.price)) return false;
        return Objects.equals(priceHistory, product.priceHistory);
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (code != null ? code.hashCode() : 0);
        result = 31 * result + (description != null ? description.hashCode() : 0);
        result = 31 * result + (currency != null ? currency.hashCode() : 0);
        result = 31 * result + stock;
        result = 31 * result + (imageUrl != null ? imageUrl.hashCode() : 0);
        result = 31 * result + (price != null ? price.hashCode() : 0);
        result = 31 * result + (priceHistory != null ? priceHistory.hashCode() : 0);
        return result;
    }
}