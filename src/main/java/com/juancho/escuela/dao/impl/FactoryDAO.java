package com.juancho.escuela.dao.impl;

import com.juancho.escuela.dao.DAO;
import com.juancho.escuela.dao.FactoryDAOException;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class FactoryDAO {
    private static FactoryDAO instance;
    private static final String ALUMNO_DAO = "AlumnoDAO";
    private static final String ASIGNATURA_DAO = "AsignaturaDAO";
    private static final String PROFESOR_DAO = "ProfesorDAO";
    private static final String MATRICULA_DAO = "MatriculaDAO";

    private Connection conn;

    private FactoryDAO() {
        this.getConnectionMySQL();
    }

    public static FactoryDAO getInstance () {
        if (instance == null) {
            instance = new FactoryDAO();
        }
        return instance;
    }

    private void getConnectionMySQL() {
        try {
            String jdbc = "jdbc:mysql://localhost:3306/escuela";
            this.conn = DriverManager.getConnection(jdbc, "root", "");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private DAO construirDAO(String clase) throws FactoryDAOException {
        switch (clase) {
            case ALUMNO_DAO:
                return new AlumnoDAOImpl(this.conn);
            case PROFESOR_DAO:
                return new ProfesorDAOImpl(this.conn);
            case ASIGNATURA_DAO:
                return new AsignaturaDAOImpl(this.conn);
            case MATRICULA_DAO:
                return new MatriculaDAOImpl(this.conn);
        }
        throw new FactoryDAOException("La clase de implementación con nombre " + clase + " no existe.");
    }

    public DAO obtenerDAO(String clase) {
        DAO entidad = null;
        try {
            entidad = this.construirDAO(clase);
        } catch (FactoryDAOException e) {
            e.printStackTrace();
        }
        return entidad;
    }
}
