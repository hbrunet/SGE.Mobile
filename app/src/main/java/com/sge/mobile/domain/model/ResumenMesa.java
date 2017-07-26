package com.sge.mobile.domain.model;

import com.sge.mobile.domain.core.Entity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by DEVNET on 26/07/2017.
 */

public class ResumenMesa {
    private int cantidadPedidos;
    private List<ResumenMesaDetalle> detalle;
    private String error;
    private boolean valida;

    public ResumenMesa(){
        detalle = new ArrayList<ResumenMesaDetalle>();
        valida = false;
    }

    public int getCantidadPedidos() {
        return cantidadPedidos;
    }

    public void setCantidadPedidos(int cantidadPedidos) {
        this.cantidadPedidos = cantidadPedidos;
    }

    public List<ResumenMesaDetalle> getDetalle() {
        return detalle;
    }

    public void setDetalle(List<ResumenMesaDetalle> detalle) {
        this.detalle = detalle;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public boolean isValida() {
        return valida;
    }

    public void setValida(boolean valida) {
        this.valida = valida;
    }
}
