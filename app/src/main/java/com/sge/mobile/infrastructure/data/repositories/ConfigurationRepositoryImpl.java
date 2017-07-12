package com.sge.mobile.infrastructure.data.repositories;

import android.util.Log;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.QueryBuilder;
import com.sge.mobile.domain.model.Configuracion;
import com.sge.mobile.domain.model.ConfigurationRepository;
import com.sge.mobile.infrastructure.data.SGEDBHelper;
import com.sge.mobile.infrastructure.data.core.RepositoryImpl;

import java.sql.SQLException;

/**
 * Created by Daniel on 10/04/14.
 */
public class ConfigurationRepositoryImpl extends RepositoryImpl<Configuracion> implements ConfigurationRepository {
    private Dao<Configuracion, Integer> configurationDao;

    public ConfigurationRepositoryImpl(final SGEDBHelper sgeDBHelper) {
        super(sgeDBHelper);
        try {
            this.configurationDao = sgeDBHelper.getConfiguracionDao();
        } catch (SQLException e) {
            Log.e(TAG, e.getMessage());
        }
    }

    @Override
    public Configuracion getFirst() {
        try {
            QueryBuilder<Configuracion, Integer> qb = this.getEntityDao().queryBuilder();
            return qb.queryForFirst();
        } catch (SQLException e) {
            Log.e(TAG, e.getMessage());
            return null;
        }
    }

    @Override
    public Dao<Configuracion, Integer> getEntityDao() {
        return this.configurationDao;
    }
}
