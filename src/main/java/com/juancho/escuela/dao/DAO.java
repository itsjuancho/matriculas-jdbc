package com.juancho.escuela.dao;

import java.util.List;

public interface DAO<T, K> {
    void crear(T t) throws DAOException;
    void actualizar(T t) throws DAOException;
    void eliminar(T t) throws DAOException;
    List<T> obtenerTodos() throws DAOException;
    T obtener(K k) throws DAOException;
}
