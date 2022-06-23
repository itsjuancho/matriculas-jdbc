package com.juancho.jdbc;

import com.juancho.escuela.dao.DAO;
import com.juancho.escuela.dao.DAOException;
import com.juancho.escuela.dao.FactoryDAOException;
import com.juancho.escuela.dao.MatriculaDAO;
import com.juancho.escuela.dao.impl.FactoryDAO;
import com.juancho.escuela.modelo.Alumno;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class Main {
    static Connection conexion = null;
    private static Logger LoggerFactory;
    private static final Logger LOG = LoggerFactory.getLogger(String.valueOf(Main.class));

    public static void conectar() throws SQLException {
        String jdbc = "jdbc:mysql://localhost:3306/escuela";
        conexion = DriverManager.getConnection(jdbc, "root", "");
        System.out.println("Conectándome a MySQL...");
        conexion.setAutoCommit(false);
    }

    public static void consulta(String apellidosParam) throws SQLException {
        String query = "SELECT id_alumno, nombre, apellidos FROM alumno WHERE apellidos = ?";
//        Statement stmt = conexion.createStatement();
        PreparedStatement stmt = conexion.prepareStatement(query);
        stmt.setString(1, apellidosParam);
//        ResultSet set = stmt.executeQuery(query);
        ResultSet set = stmt.executeQuery();
        while (set.next()) {
            Integer idAlumno = set.getInt("id_alumno");
            String nombre = set.getString("nombre");
            String apellidos = set.getString("apellidos");
            System.out.println("Alumno #" + idAlumno + " | Nombre: " + nombre + " - Apellido: " + apellidos);
        }
        set.close();
        stmt.close();
    }

    public static void transaccion() throws SQLException {
        final String PROFESOR = "INSERT INTO profesor (nombre, apellidos) VALUES (?, ?)";
        final String ASIGNATURA = "INSERT INTO asignatura (nombre, profesor) VALUES (?, ?);";
        PreparedStatement profesor = null, asignatura = null;
        try {
            profesor = conexion.prepareStatement(PROFESOR, Statement.RETURN_GENERATED_KEYS);
            profesor.setString(1, "Nicolás");
            profesor.setString(2, "López");
            profesor.executeUpdate();
            Integer idUltimoProfesor = 0;
            ResultSet profesorInfo = profesor.getGeneratedKeys();
            if (profesorInfo.next()) {
                idUltimoProfesor = profesorInfo.getInt(1);
            }

            asignatura = conexion.prepareStatement(ASIGNATURA);
            asignatura.setString(1, "Programación Orientada a Objetos");
            asignatura.setInt(2, idUltimoProfesor);
            asignatura.executeUpdate();

            conexion.commit();
            System.out.println("Query transaccional ejecutada!");
        } catch (SQLException e) {
            conexion.rollback();
            e.printStackTrace();
        } finally {
            if (profesor != null) {
                profesor.close();
            }
            if (asignatura != null) {
                asignatura.close();
            }
        }
    }

    public static void cerrar() throws SQLException {
        if (conexion != null) {
            conexion.close();
            System.out.println("Cerrando conexión a MySQL");
        }
    }

    public static void main(String[] args) {
//        try {
//            conectar();
////            consulta("Pérez");
//            transaccion();
//            cerrar();
//        } catch (SQLException ex) {
//            System.out.println("Error mientras se usa JDBC " + ex.getMessage());
//        }
//        MatriculaDAO entity = (MatriculaDAO) FactoryDAO.getInstance().obtenerDAO("MatriculaDAO");
//        try {
//            entity.obtenerTodos();
//        } catch (DAOException e) {
//            e.printStackTrace();
//        }
//        List<Alumno> alumnos = null;
//
//        try {
//            alumnos = entity.obtenerTodos();
//        } catch (DAOException e) {
//            e.printStackTrace();
//        }
//
//        for (Alumno alumno : alumnos) {
//            System.out.println(alumno.getApellidos());
//        }
    }
}
