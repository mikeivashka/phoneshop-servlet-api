package com.es.phoneshop.model.generic;

import java.util.Optional;

public interface Dao<T extends Id<K>, K> {
    Optional<T> findById(K id);

    void save(T order);

    void delete(K id);
}
