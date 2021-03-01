package com.es.phoneshop.model.order;

import com.es.phoneshop.model.generic.Dao;

import java.util.Optional;
import java.util.UUID;

public interface OrderDao extends Dao<Order, Long> {
    Optional<Order> getOrderBySecureId(UUID secureId);
}
