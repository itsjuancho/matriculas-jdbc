package com.juancho.escuela.modelo;

import java.util.Objects;

public class Asignatura {
    private Long id = null;
    private String nombre;
    private Long idProfesor;

    public Asignatura(String nombre, Long idProfesor) {
        this.nombre = nombre;
        this.idProfesor = idProfesor;
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

    public Long getIdProfesor() {
        return idProfesor;
    }

    public void setIdProfesor(Long idProfesor) {
        this.idProfesor = idProfesor;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Asignatura that = (Asignatura) o;
        return Objects.equals(id, that.id) && Objects.equals(nombre, that.nombre) && Objects.equals(idProfesor, that.idProfesor);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, nombre, idProfesor);
    }

    @Override
    public String toString() {
        return "Asignatura{" +
                "id=" + id +
                ", nombre='" + nombre + '\'' +
                ", idProfesor=" + idProfesor +
                '}';
    }
}
