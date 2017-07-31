package com.sge.mobile.domain.model;

import org.ksoap2.serialization.KvmSerializable;
import org.ksoap2.serialization.PropertyInfo;

import java.util.Hashtable;

/**
 * Created by DEVNET on 26/07/2017.
 */

public class ResumenMesaDetalle implements KvmSerializable {
    public int pedidoId;
    public String descripcion;
    public String cantidad;
    public String accesorios;
    public String fecha;
    public boolean anulado;
    public boolean modificado;

    public ResumenMesaDetalle(){
        anulado = false;
        modificado = false;
    }

    public int getPedidoId() {
        return pedidoId;
    }

    public void setPedidoId(int pedidoId) {
        this.pedidoId = pedidoId;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getCantidad() {
        return cantidad;
    }

    public void setCantidad(String cantidad) {
        this.cantidad = cantidad;
    }

    public String getAccesorios() {
        return accesorios;
    }

    public void setAccesorios(String accesorios) {
        this.accesorios = accesorios;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public boolean isAnulado() {
        return anulado;
    }

    public void setAnulado(boolean anulado) {
        this.anulado = anulado;
    }

    public boolean isModificado() {
        return modificado;
    }

    public void setModificado(boolean modificado) {
        this.modificado = modificado;
    }

    @Override
    public Object getProperty(int i) {
        switch (i){
            case 0:
                return pedidoId;
            case 1:
                return descripcion;
            case 2:
                return cantidad;
            case 3:
                return accesorios;
            case 4:
                return fecha;
            case 5:
                return anulado;
            case 6:
                return modificado;
            default:
                return null;
        }
    }

    @Override
    public int getPropertyCount() {
        return 7;
    }

    @Override
    public void setProperty(int i, Object o) {
        switch (i){
            case 0:
                pedidoId = (int)o;
                break;
            case 1:
                descripcion = o.toString();
                break;
            case 2:
                cantidad = o.toString();
                break;
            case 3:
                accesorios = o.toString();
                break;
            case 4:
                fecha = o.toString();
                break;
            case 5:
                anulado = (boolean)o;
                break;
            case 6:
                modificado = (boolean)o;
                break;
            default:
                break;
        }
    }

    @Override
    public void getPropertyInfo(int i, Hashtable hashtable, PropertyInfo propertyInfo) {
        switch (i){
            case 0:
                propertyInfo.type = PropertyInfo.INTEGER_CLASS;
                propertyInfo.name = "padidoId";
                break;
            case 1:
                propertyInfo.type = PropertyInfo.STRING_CLASS;
                propertyInfo.name = "descripcion";
                break;
            case 2:
                propertyInfo.type = PropertyInfo.STRING_CLASS;
                propertyInfo.name = "cantidad";
                break;
            case 3:
                propertyInfo.type = PropertyInfo.STRING_CLASS;
                propertyInfo.name = "accesorios";
                break;
            case 4:
                propertyInfo.type = PropertyInfo.STRING_CLASS;
                propertyInfo.name = "fecha";
                break;
            case 5:
                propertyInfo.type = PropertyInfo.BOOLEAN_CLASS;
                propertyInfo.name = "anulado";
                break;
            case 6:
                propertyInfo.type = PropertyInfo.BOOLEAN_CLASS;
                propertyInfo.name = "modificado";
                break;
            default:
                break;
        }
    }
}
