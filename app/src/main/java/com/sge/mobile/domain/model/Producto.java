package com.sge.mobile.domain.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;
import com.sge.mobile.domain.core.Entity;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by Daniel on 30/03/14.
 */
@DatabaseTable(tableName = "productos")
public class Producto extends Entity {
    public static final String PRODUCTO_ID = "producto_id";
    public static final String DESCRIPCION = "descripcion";
    public static final String RUBRO_ID = "rubro_id";
    public static final String ESTADO = "estado";
    public static final String PRECIO = "precio";
    public static final String VISIBLE = "visible";
    public static final String ES_ACCESORIO = "es_accesorio";

    @DatabaseField(id = true, columnName = PRODUCTO_ID)
    private int productoId;
    @DatabaseField(canBeNull = true, columnName = DESCRIPCION)
    private String descripcion;
    @DatabaseField(canBeNull = true, columnName = ESTADO)
    private int estado;
    @DatabaseField(canBeNull = true, columnName = PRECIO)
    private float precio;
    @DatabaseField(canBeNull = true, columnName = VISIBLE)
    private boolean visible;
    @DatabaseField(canBeNull = true, columnName = ES_ACCESORIO)
    private boolean accesorio;
    @DatabaseField(foreign = true, foreignAutoRefresh = true, columnName = RUBRO_ID, canBeNull = false)
    private Rubro rubro;
    @ForeignCollectionField(eager = false, columnName = Accesorio.PRODUCTO_PADRE_ID,
            orderColumnName = Accesorio.ACCESORIO_ID, orderAscending = true)
    private Collection<Accesorio> accesorios;

    public Producto() {
        super();
    }

    @Override
    public int getId() {
        return this.productoId;
    }

    @Override
    public void setId(int id) {
        this.productoId = id;
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

    public float getPrecio() {
        return precio;
    }

    public void setPrecio(float precio) {
        this.precio = precio;
    }

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    public Rubro getRubro() {
        return rubro;
    }

    public void setRubro(Rubro rubro) {
        this.rubro = rubro;
    }

    public boolean isAccesorio() {
        return accesorio;
    }

    public void setAccesorio(boolean accesorio) {
        this.accesorio = accesorio;
    }

    public Collection<Accesorio> getAccesorios() {
        return accesorios;
    }

    public void setAccesorios(Collection<Accesorio> accesorios) {
        this.accesorios = accesorios;
    }

    public String getDescripcionAccesoriosPorDefecto() {
        String descripcionAccesorios = "";
        List<Accesorio> defaultAccessories = new ArrayList<Accesorio>();
        for (Accesorio item : this.getAccesorios()) {
            if (item.isPorDefecto())
                defaultAccessories.add(item);
        }
        if (defaultAccessories.size() > 0) {
            descripcionAccesorios = "C/";
            for (Accesorio item : defaultAccessories) {
                descripcionAccesorios += item.getProducto().getDescripcion() + ", ";
            }
        }
        return descripcionAccesorios != "" ?
                descripcionAccesorios.substring(0, descripcionAccesorios.length() - 2) : "";
    }

    public List<Integer> getIdsAccesoriosPorDefecto() {
        List<Integer> defaultAccessories = new ArrayList<Integer>();
        for (Accesorio item : this.getAccesorios()) {
            if (item.isPorDefecto())
                defaultAccessories.add(item.getProducto().getId());
        }
        return defaultAccessories;
    }

    @Override
    public List<String> validate() {
        return this.validationResults;
    }
}
