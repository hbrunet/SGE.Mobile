package com.sge.mobile.domain.model;

import java.util.ArrayList;
import java.util.List;

public class Sector {
    private int id;
    private String descripcion;
    private List<Mesa> mesas;

    public Sector() {
        this.mesas = new ArrayList<>();
    }

    public Sector(Sector s, List<Mesa> tables){
        this.id = s.id;
        this.descripcion = s.descripcion;
        this.setMesas(tables);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public List<Mesa> getMesas() {
        return mesas;
    }

    public void setMesas(List<Mesa> mesas) {
        this.mesas = mesas;
    }
}
