package com.sge.mobile.domain.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;
import com.sge.mobile.domain.core.Entity;

import java.util.Collection;
import java.util.List;

/**
 * Created by Daniel on 30/03/14.
 */
@DatabaseTable(tableName = "rubros")
public class Rubro extends Entity {
    public static final String RUBRO_ID = "rubro_id";
    public static final String DESCRIPCION = "descripcion";
    public static final String ESTADO = "estado";

    @DatabaseField(id = true, columnName = RUBRO_ID)
    private int rubroId;
    @DatabaseField(canBeNull = true, columnName = DESCRIPCION)
    private String descripcion;
    @DatabaseField(canBeNull = true, columnName = ESTADO)
    private int estado;
    @ForeignCollectionField(eager = false, orderColumnName = Producto.DESCRIPCION, orderAscending = true)
    private Collection<Producto> productos;

    public Rubro() {
        super();
    }

    @Override
    public int getId() {
        return this.rubroId;
    }

    @Override
    public void setId(int id) {
        this.rubroId = id;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public int getEstado() {
        return estado;
    }

    public void setEstado(int estado) {
        this.estado = estado;
    }

    public Collection<Producto> getProductos() {
        return productos;
    }

    public void setProductos(Collection<Producto> productos) {
        this.productos = productos;
    }

    @Override
    public List<String> validate() {
        return this.validationResults;
    }
}
