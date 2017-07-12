package com.sge.mobile.domain.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import com.sge.mobile.domain.core.Entity;

import java.util.List;

/**
 * Created by Daniel on 02/04/14.
 */
@DatabaseTable(tableName = "accesorios")
public class Accesorio extends Entity {
    public static final String ACCESORIO_ID = "accesorio_id";
    public static final String PRODUCTO_ID = "producto_id";
    public static final String PRODUCTO_PADRE_ID = "producto_padre_id";
    public static final String ES_POR_DEFECTO = "es_por_defecto";

    @DatabaseField(id = true, columnName = ACCESORIO_ID)
    private int accesorioId;
    //@DatabaseField(canBeNull = true, columnName = PRODUCTO_ID)
    //private int productoId;
    //@DatabaseField(canBeNull = true, columnName = PRODUCTO_PADRE_ID)
    //private int productoPadreId;
    @DatabaseField(canBeNull = true, columnName = ES_POR_DEFECTO)
    private boolean porDefecto;
    @DatabaseField(foreign = true, foreignAutoRefresh = true, columnName = PRODUCTO_ID, canBeNull = true)
    private Producto producto;
    @DatabaseField(foreign = true, foreignAutoRefresh = true, columnName = PRODUCTO_PADRE_ID, canBeNull = true)
    private Producto productoPadre;

    public Accesorio() {
        super();
    }

    @Override
    public int getId() {
        return this.accesorioId;
    }

    @Override
    public void setId(int id) {
        this.accesorioId = id;
    }

    /*public int getProductoId() {
        return productoId;
    }

    public void setProductoId(int productoId) {
        this.productoId = productoId;
    }

    public int getProductoPadreId() {
        return productoPadreId;
    }

    public void setProductoPadreId(int productoPadreId) {
        this.productoPadreId = productoPadreId;
    }*/

    public boolean isPorDefecto() {
        return porDefecto;
    }

    public void setPorDefecto(boolean porDefecto) {
        this.porDefecto = porDefecto;
    }

    public Producto getProducto() {
        return producto;
    }

    public void setProducto(Producto producto) {
        this.producto = producto;
    }

    public Producto getProductoPadre() {
        return productoPadre;
    }

    public void setProductoPadre(Producto productoPadre) {
        this.productoPadre = productoPadre;
    }

    @Override
    public List<String> validate() {
        return this.validationResults;
    }
}
