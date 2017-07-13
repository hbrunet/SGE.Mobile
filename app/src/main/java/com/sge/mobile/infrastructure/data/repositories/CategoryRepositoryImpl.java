package com.sge.mobile.infrastructure.data.repositories;

import android.util.Log;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.QueryBuilder;
import com.sge.mobile.domain.model.CategoryRepository;
import com.sge.mobile.domain.model.Producto;
import com.sge.mobile.domain.model.Rubro;
import com.sge.mobile.infrastructure.data.SGEDBHelper;
import com.sge.mobile.infrastructure.data.core.RepositoryImpl;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by Daniel on 03/04/14.
 */
public class CategoryRepositoryImpl extends RepositoryImpl<Rubro> implements CategoryRepository {
    private Dao<Rubro, Integer> categoryDao;
    private Dao<Producto, Integer> productDao;

    public CategoryRepositoryImpl(final SGEDBHelper sgeDBHelper) {
        super(sgeDBHelper);
        try {
            this.categoryDao = sgeDBHelper.getRubroDao();
            this.productDao = sgeDBHelper.getProductoDao();
        } catch (SQLException e) {
            Log.e(TAG, e.getMessage());
        }
    }

    @Override
    public List<Rubro> getAll() {
        try {
            QueryBuilder<Rubro, Integer> qb = this.getEntityDao().queryBuilder();
            qb.where().eq(Producto.ESTADO, 1);
            qb.orderBy(Rubro.DESCRIPCION, true);
            return qb.query();
        } catch (SQLException e) {
            Log.e(TAG, e.getMessage());
            return null;
        }
    }

    @Override
    public Rubro getByDescription(String description) {
        try {
            QueryBuilder<Rubro, Integer> qb = this.getEntityDao().queryBuilder();
            qb.where().eq(Rubro.DESCRIPCION, description);
            return qb.queryForFirst();
        } catch (SQLException e) {
            Log.e(TAG, e.getMessage());
            return null;
        }
    }

    @Override
    public List<Rubro> getActives() {
        try {
            QueryBuilder<Producto, Integer> productQb = this.productDao.queryBuilder();
            productQb.where().eq(Producto.VISIBLE, true)
                    .and().eq(Producto.ESTADO, 1)
                    .and().eq(Producto.ES_ACCESORIO, false);
            productQb.selectColumns(Producto.RUBRO_ID).distinct();

            QueryBuilder<Rubro, Integer> categoryQb = this.categoryDao.queryBuilder();
            categoryQb.where().in(Rubro.RUBRO_ID, productQb);
            categoryQb.orderBy(Rubro.DESCRIPCION, true);

            return categoryQb.query();

        } catch (SQLException e) {
            Log.e(TAG, e.getMessage());
            return null;
        }
    }

    @Override
    public Dao<Rubro, Integer> getEntityDao() {
        return categoryDao;
    }

    @Override
    public void removeAll() {
        this.removeAll(Rubro.class);
    }
}
