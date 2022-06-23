package com.juancho.escuela.dao;

import com.juancho.escuela.modelo.Matricula;

import java.util.List;

public interface MatriculaDAO extends DAO<Matricula, Long> {
    List<Matricula> obtenerPorAlumno(Long alumno) throws DAOException;
}
