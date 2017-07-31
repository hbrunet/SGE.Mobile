package com.sge.mobile.domain.model;

import com.sge.mobile.domain.core.Entity;

import org.ksoap2.serialization.KvmSerializable;
import org.ksoap2.serialization.PropertyInfo;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

/**
 * Created by DEVNET on 26/07/2017.
 */

public class ResumenMesa implements KvmSerializable {
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

    @Override
    public Object getProperty(int i) {
        switch (i){
            case 0:
                return cantidadPedidos;
            case 1:
                return detalle;
            case 2:
                return error;
            case 3:
                return valida;
            default:
                return null;
        }
    }

    @Override
    public int getPropertyCount() {
        return 4;
    }

    @Override
    public void setProperty(int i, Object o) {
        switch(i)
        {
            case 0:
                cantidadPedidos = (int)o;
                break;
            case 1:
                detalle = (ArrayList<ResumenMesaDetalle>) o;
                break;
            case 2:
                error = o.toString();
                break;
            case 3:
                valida = (boolean)o;
                break;
            default:
                break;
        }
    }

    @Override
    public void getPropertyInfo(int i, Hashtable hashtable, PropertyInfo propertyInfo) {
        switch(i)
        {
            case 0:
                propertyInfo.type = PropertyInfo.INTEGER_CLASS;
                propertyInfo.name = "cantidadPedidos";
                break;
            case 1:
                propertyInfo.type = PropertyInfo.OBJECT_TYPE;
                propertyInfo.name = "detalle";
                break;
            case 2:
                propertyInfo.type = PropertyInfo.STRING_CLASS;
                propertyInfo.name = "error";
                break;
            case 3:
                propertyInfo.type = PropertyInfo.BOOLEAN_CLASS;
                propertyInfo.name = "valida";
                break;
            default:
                break;
        }
    }
}
