package com.juancho.escuela.modelo;

import java.util.Objects;

public class Matricula {
    private Long id = null;
    private Long alumnoId;
    private Long asignaturaId;
    private Integer year;
    private Integer nota = null;

    public Matricula(Long alumno, Long asignatura, Integer year) {
        this.alumnoId = alumno;
        this.asignaturaId = asignatura;
        this.year = year;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getAlumnoId() {
        return alumnoId;
    }

    public void setAlumnoId(Long alumnoId) {
        this.alumnoId = alumnoId;
    }

    public Long getAsignaturaId() {
        return asignaturaId;
    }

    public void setAsignaturaId(Long asignaturaId) {
        this.asignaturaId = asignaturaId;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public Integer getNota() {
        return nota;
    }

    public void setNota(Integer nota) {
        this.nota = nota;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Matricula matricula = (Matricula) o;
        return Objects.equals(id, matricula.id) && Objects.equals(alumnoId, matricula.alumnoId) && Objects.equals(asignaturaId, matricula.asignaturaId) && Objects.equals(year, matricula.year) && Objects.equals(nota, matricula.nota);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, alumnoId, asignaturaId, year, nota);
    }

    @Override
    public String toString() {
        return "Matricula{" +
                "id=" + id +
                ", alumno=" + alumnoId +
                ", asignatura=" + asignaturaId +
                ", year=" + year +
                ", nota=" + nota +
                '}';
    }
}
