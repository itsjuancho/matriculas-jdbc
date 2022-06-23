package com.juancho.escuela.modelo;

import java.util.Objects;

public class Profesor {
    private Long id = null;
    private String nombre;
    private String apellidos;

    public Profesor(String nombre, String apellidos) {
        this.nombre = nombre;
        this.apellidos = apellidos;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellidos() {
        return apellidos;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Profesor profesor = (Profesor) o;
        return Objects.equals(id, profesor.id) && Objects.equals(nombre, profesor.nombre) && Objects.equals(apellidos, profesor.apellidos);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, nombre, apellidos);
    }
}
