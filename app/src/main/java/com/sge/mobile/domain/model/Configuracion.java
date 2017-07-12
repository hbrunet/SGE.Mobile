package com.sge.mobile.domain.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import com.sge.mobile.domain.core.Entity;

import java.util.List;

/**
 * Created by Daniel on 10/04/14.
 */
@DatabaseTable(tableName = "configuraciones")
public class Configuracion extends Entity {
    public static final String CONFIGURACION_ID = "configuracion_id";
    public static final String URL_SERVICIO_WEB_SGE = "url_servicio_web_sge";
    public static final String CLAVE_ADMINISTRADOR = "clave_administrador";

    @DatabaseField(generatedId = true, columnName = CONFIGURACION_ID)
    private int configuracionId;
    @DatabaseField(canBeNull = true, columnName = URL_SERVICIO_WEB_SGE)
    private String urlServicioWebSge;
    @DatabaseField(canBeNull = true, columnName = CLAVE_ADMINISTRADOR)
    private String claveAdministrador;

    public Configuracion() {
        super();
    }

    @Override
    public int getId() {
        return configuracionId;
    }

    @Override
    public void setId(int id) {
        this.configuracionId = id;
    }

    public String getUrlServicioWebSge() {
        return urlServicioWebSge;
    }

    public void setUrlServicioWebSge(String urlServicioWebSge) {
        this.urlServicioWebSge = urlServicioWebSge;
    }

    public String getClaveAdministrador() {
        return claveAdministrador;
    }

    public void setClaveAdministrador(String claveAdministrador) {
        this.claveAdministrador = claveAdministrador;
    }

    @Override
    public List<String> validate() {
        return this.validationResults;
    }
}
