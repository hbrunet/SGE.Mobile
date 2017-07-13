package com.sge.mobile.infrastructure.data.core;

import android.util.Log;

import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.table.TableUtils;
import com.sge.mobile.domain.core.Entity;
import com.sge.mobile.domain.core.Repository;
import com.sge.mobile.infrastructure.data.SGEDBHelper;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by Daniel on 08/07/13.
 */
public abstract class RepositoryImpl<TEntity extends Entity> implements Repository<TEntity> {
    public final static String TAG = "SGELog";

    private SGEDBHelper sgeDBHelper;

    public RepositoryImpl(final SGEDBHelper sgeDBHelper) {
        this.sgeDBHelper = sgeDBHelper;
    }

    @Override
    public TEntity get(int id) {
        try {
            return this.getEntityDao().queryForId(id);
        } catch (SQLException e) {
            Log.e(TAG, e.getMessage());
            return null;
        }
    }

    @Override
    public List<TEntity> getAll() {
        try {
            return this.getEntityDao().queryForAll();
        } catch (SQLException e) {
            Log.e(TAG, e.getMessage());
            return null;
        }
    }

    @Override
    public void store(TEntity item) {
        try {
            this.getEntityDao().createOrUpdate(item);
        } catch (SQLException e) {
            Log.e(TAG, e.getMessage());
        }
    }

    @Override
    public void remove(TEntity item) {
        try {
            this.getEntityDao().delete(item);
        } catch (SQLException e) {
            Log.e(TAG, e.getMessage());
        }
    }

    @Override
    public void removeAll(Class<?> dataClass) {
        try {
            TableUtils.clearTable(this.sgeDBHelper.getConnectionSource(), dataClass);
        } catch (SQLException e) {
            Log.e(TAG, e.getMessage());
        }
    }

    @Override
    public List<TEntity> getFiltered(PreparedQuery<TEntity> preparedQuery) {
        try {
            return this.getEntityDao().query(preparedQuery);
        } catch (SQLException e) {
            Log.e(TAG, e.getMessage());
            return null;
        }
    }
}

