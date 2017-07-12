package com.sge.mobile.domain.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Daniel on 05/04/14.
 */
public class LineaPedido {
    private int productoId;
    private List<Integer> accesorios;
    private String descripcionProducto;
    private String descripcionAccesorios;
    private float precio;

    public LineaPedido() {
        this.accesorios = new ArrayList<Integer>();
    }

    public int getProductoId() {
        return productoId;
    }

    public void setProductoId(int productoId) {
        this.productoId = productoId;
    }

    public List<Integer> getAccesorios() {
        return accesorios;
    }

    public void setAccesorios(List<Integer> accesorios) {
        this.accesorios = accesorios;
    }

    public String getDescripcionProducto() {
        return descripcionProducto;
    }

    public void setDescripcionProducto(String descripcionProducto) {
        this.descripcionProducto = descripcionProducto;
    }

    public String getDescripcionAccesorios() {
        return descripcionAccesorios;
    }

    public void setDescripcionAccesorios(String descripcionAccesorios) {
        this.descripcionAccesorios = descripcionAccesorios;
    }

    public float getPrecio() {
        return precio;
    }

    public void setPrecio(float precio) {
        this.precio = precio;
    }

    @Override
    public String toString() {
        String result = "";
        result += this.productoId;
        for (Integer accessorieId : this.getAccesorios()) {
            result += "," + accessorieId;
        }
        return result;
    }
}
