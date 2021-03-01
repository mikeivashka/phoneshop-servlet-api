package com.es.phoneshop.model.generic;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public abstract class AbstractListDao<T extends Id<K>, K> implements Dao<T, K> {
    protected final List<T> items;
    protected final ReadWriteLock readWriteLock = new ReentrantReadWriteLock();

    protected AbstractListDao() {
        items = getContainer();
    }

    @Override
    public Optional<T> findById(K id) {
        if (id == null) {
            throw new NullPointerException();
        }
        readWriteLock.readLock().lock();
        Optional<T> item = items.stream()
                .filter(i -> id.equals(i.getId()))
                .findAny();
        readWriteLock.readLock().unlock();
        return item;
    }

    public void save(T item) {
        if (item.getId() != null && this.findById(item.getId()).isPresent()) {
            readWriteLock.writeLock().lock();
            this.items.removeIf(o -> o.getId().equals(item.getId()));
            readWriteLock.writeLock().unlock();
        } else {
            item.setId(getNextId());
        }
        readWriteLock.writeLock().lock();
        items.add(item);
        readWriteLock.writeLock().unlock();
    }

    public void delete(K id) {
        readWriteLock.writeLock().lock();
        items.removeIf(i -> i.getId().equals(id));
        readWriteLock.writeLock().unlock();
    }

    protected abstract List<T> getContainer();

    protected abstract K getNextId();
}
