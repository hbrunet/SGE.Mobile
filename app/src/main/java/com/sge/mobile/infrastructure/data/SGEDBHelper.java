package com.sge.mobile.infrastructure.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import com.sge.mobile.domain.model.Accesorio;
import com.sge.mobile.domain.model.Configuracion;
import com.sge.mobile.domain.model.Producto;
import com.sge.mobile.domain.model.Rubro;

import java.sql.SQLException;

/**
 * Created by Daniel on 10/07/13.
 */
public class SGEDBHelper extends OrmLiteSqliteOpenHelper {

    private static final String DATABASE_NAME = "sge_mobile.db";
    private static final int DATABASE_VERSION = 4;

    private Dao<Producto, Integer> productoDao;
    private Dao<Rubro, Integer> rubroDao;
    private Dao<Accesorio, Integer> accesorioDao;
    private Dao<Configuracion, Integer> configuracionDao;

    public SGEDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase, ConnectionSource connectionSource) {
        try {
            TableUtils.dropTable(connectionSource, Rubro.class, true);
            TableUtils.dropTable(connectionSource, Producto.class, true);
            TableUtils.dropTable(connectionSource, Accesorio.class, true);
            TableUtils.dropTable(connectionSource, Configuracion.class, true);

            TableUtils.createTable(connectionSource, Rubro.class);
            TableUtils.createTable(connectionSource, Producto.class);
            TableUtils.createTable(connectionSource, Accesorio.class);
            TableUtils.createTable(connectionSource, Configuracion.class);

            Configuracion configuracion = new Configuracion();
            configuracion.setUrlServicioWebSge("http://financiera.dyndns.biz:58084/OrderService");
            configuracion.setClaveAdministrador("1234");

            this.configuracionDao.createOrUpdate(configuracion);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, ConnectionSource connectionSource, int i, int i2) {
        onCreate(sqLiteDatabase, connectionSource);
    }

    @Override
    public void close() {
        super.close();
        this.productoDao = null;
        this.rubroDao = null;
    }

    public Dao<Producto, Integer> getProductoDao() throws SQLException {
        if (productoDao == null) {
            productoDao = this.getDao(Producto.class);
        }
        return productoDao;
    }

    public Dao<Rubro, Integer> getRubroDao() throws SQLException {
        if (rubroDao == null) {
            rubroDao = this.getDao(Rubro.class);
        }
        return rubroDao;
    }

    public Dao<Accesorio, Integer> getAccesorioDao() throws SQLException {
        if (accesorioDao == null) {
            accesorioDao = this.getDao(Accesorio.class);
        }
        return accesorioDao;
    }

    public Dao<Configuracion, Integer> getConfiguracionDao() throws SQLException {
        if (configuracionDao == null) {
            configuracionDao = this.getDao(Configuracion.class);
        }
        return configuracionDao;
    }
}
