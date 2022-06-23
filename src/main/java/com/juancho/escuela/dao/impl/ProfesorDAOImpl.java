package com.juancho.escuela.dao.impl;

import com.juancho.escuela.dao.DAOException;
import com.juancho.escuela.dao.ProfesorDAO;
import com.juancho.escuela.modelo.Profesor;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProfesorDAOImpl implements ProfesorDAO {
    final String INSERT = "INSERT INTO profesor (nombre, apellidos) VALUES (?, ?)";
    final String UPDATE = "UPDATE profesor SET nombre = ?, apellidos = ? WHERE id_profesor = ?";
    final String DELETE = "DELETE FROM profesor WHERE id_profesor = ?";
    final String SELECT_ALL = "SELECT id_profesor, nombre, apellidos FROM profesor";
    final String SELECT = "SELECT id_profesor, nombre, apellidos FROM profesor WHERE id_profesor = ?";

    private Connection conn;

    public ProfesorDAOImpl(Connection conn) {
        this.conn = conn;
    }

    @Override
    public void crear(Profesor profesor) throws DAOException {
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            stmt = this.conn.prepareStatement(INSERT, Statement.RETURN_GENERATED_KEYS);
            stmt.setString(1, profesor.getNombre());
            stmt.setString(2, profesor.getApellidos());
            if (stmt.executeUpdate() == 0) {
                throw new DAOException("Hubo una falla a la hora de guardar el nuevo Alumno.");
            }
            rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                profesor.setId(rs.getLong(1));
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
    public void actualizar(Profesor profesor) throws DAOException {
        PreparedStatement stmt = null;
        try {
            stmt = this.conn.prepareStatement(UPDATE);
            stmt.setString(1, profesor.getNombre());
            stmt.setString(2, profesor.getApellidos());
            stmt.setLong(4, profesor.getId());
            if (stmt.executeUpdate() == 0) {
                throw new DAOException("Hubo una falla a la hora de actualizar el Alumno con ID " + profesor.getId());
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
    public void eliminar(Profesor profesor) throws DAOException {
        PreparedStatement stmt = null;
        try {
            stmt = this.conn.prepareStatement(DELETE);
            stmt.setLong(1, profesor.getId());
            if (stmt.executeUpdate() == 0) {
                throw new DAOException("Hubo una falla a la hora de eliminar el Alumno con ID " + profesor.getId());
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
    public List<Profesor> obtenerTodos() throws DAOException {
        PreparedStatement stmt = null;
        ResultSet rs = null;
        List<Profesor> profesores = new ArrayList<>();
        try {
            stmt = this.conn.prepareStatement(SELECT_ALL);
            rs = stmt.executeQuery();
            while (rs.next()) {
                profesores.add(this.convertir(rs));
            }
        } catch (SQLException e) {
            throw new DAOException("Error en la consulta SQL", e);
        } finally {
            if (stmt != null) {
                try {
                    stmt.close();
                } catch (SQLException ex) {
                    throw new DAOException("Hubo un error a la hora de cerrar la conexión...", ex);
                }
            }
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException ex) {
                    throw new DAOException("Error al cerrar la consulta preparada", ex);
                }
            }
        }
        return profesores;
    }

    @Override
    public Profesor obtener(Long id) throws DAOException {
        PreparedStatement stmt = null;
        ResultSet rs = null;
        Profesor profesor = null;
        try {
            stmt = this.conn.prepareStatement(SELECT);
            stmt.setLong(1, id);
            rs = stmt.executeQuery();
            if (rs.next()) {
                profesor = this.convertir(rs);
            } else {
                throw new DAOException("No se encontró e usuario con el ID " + id);
            }
        } catch (SQLException e) {
            throw new DAOException("Hubo un error en la consulta", e);
        } finally {
            if (stmt != null) {
                try {
                    stmt.close();
                } catch (SQLException e) {
                    throw new DAOException("Ocurrió un error al cerrar la consulta preparada", e);
                }
            }
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException e) {
                    throw new DAOException("Ocurrió un error al cerrar el set de resultados", e);
                }
            }
        }
        return profesor;
    }

    private Profesor convertir(ResultSet rs) throws SQLException {
        Profesor profesor = new Profesor(
                rs.getString("nombre"),
                rs.getString("apellidos")
        );
        profesor.setId(rs.getLong("id_profesor"));
        return profesor;
    }
}
