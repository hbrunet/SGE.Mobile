package com.sge.mobile.domain.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Daniel on 30/03/14.
 */
public class Pedido {
    private int nroMesa;
    private String observacion;
    private List<LineaPedido> lineasPedido;

    public Pedido() {
        this.lineasPedido = new ArrayList<LineaPedido>();
    }

    public int getNroMesa() {
        return nroMesa;
    }

    public void setNroMesa(int nroMesa) {
        this.nroMesa = nroMesa;
    }

    public String getObservacion() {
        return observacion;
    }

    public void setObservacion(String observacion) {
        this.observacion = observacion;
    }

    public List<LineaPedido> getLineasPedido() {
        return lineasPedido;
    }

    public void setLineasPedido(List<LineaPedido> lineasPedido) {
        this.lineasPedido = lineasPedido;
    }

    public float getPrecioTotal() {
        float total = 0;
        for (LineaPedido lineaPedido : this.getLineasPedido()) {
            total = total + lineaPedido.getPrecio();
        }
        return total;
    }

    @Override
    public String toString() {
        String result = "";
        for (LineaPedido orderLine : this.getLineasPedido()) {
            result += orderLine.toString() + ";";
        }
        result = result.substring(0, result.length() - 1);
        return result;
    }
}
