package com.sge.mobile.infrastructure.data.repositories;

import android.util.Log;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.stmt.query.OrderBy;
import com.sge.mobile.domain.model.Accesorio;
import com.sge.mobile.domain.model.ProductRepository;
import com.sge.mobile.domain.model.Producto;
import com.sge.mobile.domain.model.Rubro;
import com.sge.mobile.infrastructure.data.SGEDBHelper;
import com.sge.mobile.infrastructure.data.core.RepositoryImpl;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by Daniel on 03/04/14.
 */
public class ProductRepositoryImpl extends RepositoryImpl<Producto> implements ProductRepository {
    private Dao<Producto, Integer> productDao;
    private Dao<Accesorio, Integer> accessorieDao;

    public ProductRepositoryImpl(final SGEDBHelper sgeDBHelper) {
        super(sgeDBHelper);
        try {
            this.productDao = sgeDBHelper.getProductoDao();
            this.accessorieDao = sgeDBHelper.getAccesorioDao();
        } catch (SQLException e) {
            Log.e(TAG, e.getMessage());
        }
    }

    @Override
    public List<Producto> getAll() {
        try {
            QueryBuilder<Producto, Integer> qb = this.getEntityDao().queryBuilder();
            qb.where().eq(Producto.VISIBLE, true)
                    .and().eq(Producto.ESTADO, 1)
                    .and().eq(Producto.ES_ACCESORIO, false);
            qb.orderBy(Producto.DESCRIPCION, true);
            return qb.query();
        } catch (SQLException e) {
            Log.e(TAG, e.getMessage());
            return null;
        }
    }

    @Override
    public Producto GetByDescription(String description) {
        try {
            QueryBuilder<Producto, Integer> qb = this.getEntityDao().queryBuilder();
            qb.where().eq(Producto.DESCRIPCION, description);
            return qb.queryForFirst();
        } catch (SQLException e) {
            Log.e(TAG, e.getMessage());
            return null;
        }
    }

    @Override
    public void storeAccessorie(Accesorio accessorie) {
        try {
            this.accessorieDao.createOrUpdate(accessorie);
        } catch (SQLException e) {
            Log.e(TAG, e.getMessage());
        }
    }

    @Override
    public Dao<Producto, Integer> getEntityDao() {
        return this.productDao;
    }

    @Override
    public void removeAll() {
        this.removeAll(Accesorio.class);
        this.removeAll(Producto.class);
    }

    @Override
    public List<Producto> getFiltered(String searchText) {
        try {
            QueryBuilder<Producto, Integer> qb = this.getEntityDao().queryBuilder();
            qb.where().eq(Producto.VISIBLE, true)
                    .and().eq(Producto.ESTADO, 1)
                    .and().eq(Producto.ES_ACCESORIO, false)
                    .and().like(Producto.DESCRIPCION, "%" + searchText + "%");
            qb.orderBy(Producto.DESCRIPCION, true);
            return qb.query();
        } catch (SQLException e) {
            Log.e(TAG, e.getMessage());
            return null;
        }
    }
}
