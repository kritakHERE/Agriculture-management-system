package com.agrinepal.dao;

import com.agrinepal.model.Identifiable;
import com.agrinepal.util.FileUtil;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public abstract class FileBasedDAO<T extends Identifiable & Serializable> implements GenericDAO<T> {
    private final String path;

    protected FileBasedDAO(String path) {
        this.path = path;
    }

    @Override
    public List<T> findAll() {
        return new ArrayList<>(FileUtil.readList(path));
    }

    @Override
    public Optional<T> findById(String id) {
        return findAll().stream().filter(e -> e.getId().equals(id)).findFirst();
    }

    @Override
    public void save(T entity) {
        List<T> all = findAll();
        all.add(entity);
        FileUtil.writeList(path, all);
    }

    @Override
    public void update(T entity) {
        List<T> all = findAll();
        for (int i = 0; i < all.size(); i++) {
            if (all.get(i).getId().equals(entity.getId())) {
                all.set(i, entity);
                FileUtil.writeList(path, all);
                return;
            }
        }
        throw new IllegalArgumentException("Entity not found: " + entity.getId());
    }

    @Override
    public void delete(String id) {
        List<T> all = findAll();
        all.removeIf(e -> e.getId().equals(id));
        FileUtil.writeList(path, all);
    }
}
