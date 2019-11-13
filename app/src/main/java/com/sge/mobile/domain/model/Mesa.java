package com.sge.mobile.domain.model;

public class Mesa {
    private int id;
    private String descripcion;
    private int sectorId;
    private String sectorDescripcion;

    public Mesa(){}

    public  Mesa(int id, String descripcion, int sectorId, String sectorDescripcion){
        this.id = id;
        this.descripcion = descripcion;
        this.sectorId = sectorId;
        this.sectorDescripcion = sectorDescripcion;
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

    public int getSectorId() {
        return sectorId;
    }

    public void setSectorId(int sectorId) {
        this.sectorId = sectorId;
    }

    public String getSectorDescripcion() {
        return sectorDescripcion;
    }

    public void setSectorDescripcion(String sectorDescripcion) {
        this.sectorDescripcion = sectorDescripcion;
    }

    @Override
    public String toString() {
        return this.descripcion;
    }
}
