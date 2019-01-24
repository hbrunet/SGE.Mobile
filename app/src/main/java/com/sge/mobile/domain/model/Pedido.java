package com.sge.mobile.domain.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Daniel on 30/03/14.
 */
public class Pedido {
    private String observacion;
    private List<LineaPedido> lineasPedido;
    private Mesa mesa;

    public Pedido() {
        this.lineasPedido = new ArrayList<>();
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

    public Mesa getMesa() {
        return mesa;
    }

    public void setMesa(Mesa mesa) {
        this.mesa = mesa;
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
