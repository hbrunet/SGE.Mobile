package com.sge.mobile.domain.core;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.PreparedQuery;

import java.util.List;

/**
 * Created by Daniel on 10/07/13.
 */
public interface Repository<TEntity extends Entity> {
    Dao<TEntity, Integer> getEntityDao();

    TEntity get(int id);

    List<TEntity> getAll();

    void store(TEntity item);

    void remove(TEntity item);

    List<TEntity> getFiltered(PreparedQuery<TEntity> preparedQuery);
}
