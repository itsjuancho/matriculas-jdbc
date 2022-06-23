package com.juancho.escuela.dao.impl;

import com.juancho.escuela.dao.AlumnoDAO;
import com.juancho.escuela.dao.DAOException;
import com.juancho.escuela.modelo.Alumno;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AlumnoDAOImpl implements AlumnoDAO {
    final String INSERT = "INSERT INTO alumno (nombre, apellidos, fecha_nacimiento) VALUES (?, ?, ?)";
    final String UPDATE = "UPDATE alumno SET nombre = ?, apellidos = ?, fecha_nacimiento = ? WHERE id_alumno = ?";
    final String DELETE = "DELETE FROM alumno WHERE id_alumno = ?";
    final String SELECT_ALL = "SELECT id_alumno, nombre, apellidos, fecha_nacimiento FROM alumno";
    final String SELECT = "SELECT id_alumno, nombre, apellidos, fecha_nacimiento FROM alumno WHERE id_alumno = ?";

    private Connection conn;

    public AlumnoDAOImpl (Connection conn) {
        this.conn = conn;
    }

    @Override
    public void crear(Alumno alumno) throws DAOException {
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            stmt = this.conn.prepareStatement(INSERT, Statement.RETURN_GENERATED_KEYS);
            stmt.setString(1, alumno.getNombre());
            stmt.setString(2, alumno.getApellidos());
            stmt.setDate(3, new Date(alumno.getFechaNacimiento().getTime()));
            if (stmt.executeUpdate() == 0) {
                throw new DAOException("Hubo una falla a la hora de guardar el nuevo Alumno.");
            }
            rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                alumno.setId(rs.getLong(1));
            }
        } catch (SQLException ex) {
             throw new DAOException("Error en SQL", ex);
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException ex) {
                    throw new DAOException("Ha ocurrido un error al cerrar el ResultSet", ex);
                }
            }
            if (stmt != null) {
                try {
                    stmt.close();
                } catch (SQLException ex) {
                    throw new DAOException("Error a la hora de cerrar la conexión SQL", ex);
                }
            }
        }
    }

    @Override
    public void actualizar(Alumno alumno) throws DAOException {
        PreparedStatement stmt = null;
        try {
            stmt = this.conn.prepareStatement(UPDATE);
            stmt.setString(1, alumno.getNombre());
            stmt.setString(2, alumno.getApellidos());
            stmt.setDate(3, new Date(alumno.getFechaNacimiento().getTime()));
            stmt.setLong(4, alumno.getId());
            if (stmt.executeUpdate() == 0) {
                throw new DAOException("Hubo una falla a la hora de actualizar el Alumno con ID " + alumno.getId());
            }
        } catch (SQLException ex) {
            throw new DAOException("Error en SQL", ex);
        } finally {
            if (stmt != null) {
                try {
                    stmt.close();
                } catch (SQLException ex) {
                    throw new DAOException("Error a la hora de cerrar la conexión SQL", ex);
                }
            }
        }
    }

    @Override
    public void eliminar(Alumno alumno) throws DAOException {
        PreparedStatement stmt = null;
        try {
            stmt = this.conn.prepareStatement(DELETE);
            stmt.setLong(1, alumno.getId());
            if (stmt.executeUpdate() == 0) {
                throw new DAOException("Hubo una falla a la hora de eliminar el Alumno con ID " + alumno.getId());
            }
        } catch (SQLException ex) {
            throw new DAOException("Error en SQL", ex);
        } finally {
            if (stmt != null) {
                try {
                    stmt.close();
                } catch (SQLException ex) {
                    throw new DAOException("Error a la hora de cerrar la conexión SQL", ex);
                }
            }
        }
    }

    @Override
    public List<Alumno> obtenerTodos() throws DAOException {
        PreparedStatement stmt = null;
        ResultSet rs = null;
        List<Alumno> alumnos = new ArrayList<>();
        try {
            stmt = this.conn.prepareStatement(SELECT_ALL);
            rs = stmt.executeQuery();
            while (rs.next()) {
                alumnos.add(convertir(rs));
            }
        } catch (SQLException ex) {
            throw new DAOException("Error al obtener todos los alumnos", ex);
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException ex) {
                    throw new DAOException("Ha ocurrido un error al cerrar el ResultSet", ex);
                }
            }
            if (stmt != null) {
                try {
                    stmt.close();
                } catch (SQLException ex) {
                    throw new DAOException("Ha ocurrido un error al cerrar la PreparedStatment", ex);
                }
            }
        }
        return alumnos;
    }

    @Override
    public Alumno obtener(Long id) throws DAOException {
        PreparedStatement stmt = null;
        ResultSet rs = null;
        Alumno alumno = null;
        try {
            stmt = this.conn.prepareStatement(SELECT);
            stmt.setLong(1, id);
            rs = stmt.executeQuery();
            if (rs.next()) {
                alumno = convertir(rs);
            } else {
                throw new DAOException("No se ha encontrado el Alumno con la ID " + id);
            }
        } catch (SQLException ex) {
            throw new DAOException("Error al obtener el alumno con ID " + id, ex);
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException ex) {
                    throw new DAOException("Ha ocurrido un error al cerrar el ResultSet", ex);
                }
            }
            if (stmt != null) {
                try {
                    stmt.close();
                } catch (SQLException ex) {
                    throw new DAOException("Ha ocurrido un error al cerrar la PreparedStatment", ex);
                }
            }
        }
        return alumno;
    }

    public static void main(String[] args) throws SQLException, DAOException {
        Connection conn = null;

        try {
            String jdbc = "jdbc:mysql://localhost:3306/escuela";
            conn = DriverManager.getConnection(jdbc, "root", "");
            AlumnoDAO dao = new AlumnoDAOImpl(conn);
            Alumno nuevo = new Alumno("Sara", "Palacios", new java.util.Date(2004, 7, 7));
            dao.crear(nuevo);
            System.out.println("El nuevo alumno tiene por tiene la ID " + nuevo.getId());
//            List<Alumno> alumnos = dao.obtenerTodos();
//            for (Alumno alumno : alumnos) {
//                System.out.println(alumno);
//            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (DAOException e) {
            e.printStackTrace();
        } finally {
            if (conn != null) {
                conn.close();
            }
        }
    }

    private Alumno convertir(ResultSet rs) throws SQLException {
        Alumno alumno = new Alumno(
                rs.getString("nombre"),
                rs.getString("apellidos"),
                rs.getDate("fecha_nacimiento")
        );
        alumno.setId(rs.getLong("id_alumno"));
        return alumno;
    }
}
