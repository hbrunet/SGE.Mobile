package com.sge.mobile.infrastructure.data.repositories;

import android.util.Log;

import com.j256.ormlite.dao.Dao;
import com.sge.mobile.domain.model.Accesorio;
import com.sge.mobile.domain.model.AccessoryRepository;
import com.sge.mobile.infrastructure.data.SGEDBHelper;
import com.sge.mobile.infrastructure.data.core.RepositoryImpl;

import java.sql.SQLException;

/**
 * Created by hbrunet on 14/07/2017.
 */

public class AccessoryRepositoryImpl extends RepositoryImpl<Accesorio> implements AccessoryRepository {

    private Dao<Accesorio, Integer> accessoryDao;

    public AccessoryRepositoryImpl(final SGEDBHelper sgeDBHelper) {
        super(sgeDBHelper);
        try {
            this.accessoryDao = sgeDBHelper.getAccesorioDao();
        } catch (SQLException e) {
            Log.e(TAG, e.getMessage());
        }
    }

    @Override
    public Dao<Accesorio, Integer> getEntityDao() {
        return this.accessoryDao;
    }
}
