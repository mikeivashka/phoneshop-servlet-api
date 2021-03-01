package com.es.phoneshop.model.order.impl;

import com.es.phoneshop.model.generic.AbstractListDao;
import com.es.phoneshop.model.order.Order;
import com.es.phoneshop.model.order.OrderDao;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class ArrayListOrderDao extends AbstractListDao<Order, Long> implements OrderDao {
    private final AtomicLong nextId = new AtomicLong();

    private ArrayListOrderDao() {
        super();
    }

    public static ArrayListOrderDao getInstance() {
        return LazySingletonHolder.HOLDER_INSTANCE;
    }

    @Override
    protected List<Order> getContainer() {
        return new ArrayList<>();
    }

    @Override
    public Optional<Order> getOrderBySecureId(UUID secureId) {
        if (secureId == null) {
            throw new NullPointerException();
        }
        readWriteLock.readLock().lock();
        Optional<Order> order = items.stream()
                .filter(i -> secureId.equals(i.getSecureId()))
                .findAny();
        readWriteLock.readLock().unlock();
        return order;
    }

    @Override
    protected Long getNextId() {
        return nextId.getAndIncrement();
    }

    private static class LazySingletonHolder {
        public static final ArrayListOrderDao HOLDER_INSTANCE = new ArrayListOrderDao();
    }
}
