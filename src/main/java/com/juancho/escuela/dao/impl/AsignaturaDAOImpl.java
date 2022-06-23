package com.juancho.escuela.dao.impl;

import com.juancho.escuela.dao.AsignaturaDAO;
import com.juancho.escuela.dao.DAO;
import com.juancho.escuela.dao.DAOException;
import com.juancho.escuela.modelo.Asignatura;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AsignaturaDAOImpl implements AsignaturaDAO {
    final String CREATE = "INSERT INTO asignatura (nombre, profesor) VALUES (?, ?)";
    final String UPDATE = "UPDATE asignatura SET nombre = ?, profesor = ? WHERE id_asignatura = ?";
    final String DELETE = "DELETE FROM asignatura WHERE id_asignatura = ?";
    final String SELECT_ALL = "SELECT id_asignatura, nombre, profesor FROM asignatura";
    final String SELECT = "SELECT id_asignatura, nombre, profesor FROM asignatura WHERE id_asignatura = ?";
    private Connection conn;

    public AsignaturaDAOImpl(Connection conn) {
        this.conn = conn;
    }

    @Override
    public void crear(Asignatura asignatura) throws DAOException {
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            stmt = this.conn.prepareStatement(CREATE, Statement.RETURN_GENERATED_KEYS);
            stmt.setString(1, asignatura.getNombre());
            stmt.setLong(2, asignatura.getIdProfesor());
            if (stmt.executeUpdate() == 0) {
                throw new DAOException("No se pudo crear la asignatura con nombre " + asignatura.getNombre());
            }
            rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                asignatura.setId(rs.getLong(1));
            }
        } catch (SQLException e) {
            throw new DAOException("Hubo un error al crear la Asignatura", e);
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
    public void actualizar(Asignatura asignatura) throws DAOException {
        PreparedStatement stmt = null;
        try {
            stmt = this.conn.prepareStatement(UPDATE);
            stmt.setString(1, asignatura.getNombre());
            stmt.setLong(2, asignatura.getIdProfesor());
            stmt.setLong(3, asignatura.getId());
            if (stmt.executeUpdate() == 0) {
                throw new DAOException("No se pudo actualizar la asignatura con ID " + asignatura.getId());
            }
        } catch (SQLException e) {
            throw new DAOException("No se pudo ejecutar la consulta de actualización de Asignatura", e);
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
    public void eliminar(Asignatura asignatura) throws DAOException {
        PreparedStatement stmt = null;
        try {
            stmt = this.conn.prepareStatement(DELETE);
            stmt.setLong(1, asignatura.getId());
            if (stmt.executeUpdate() == 0) {
                throw new DAOException("No se pudo eliminar la asignatura con ID " + asignatura.getId());
            }
        } catch (SQLException e) {
            throw new DAOException("Hubo un error en la consulta SQL", e);
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
    public List<Asignatura> obtenerTodos() throws DAOException {
        PreparedStatement stmt = null;
        ResultSet rs = null;
        List<Asignatura> asignaturas = new ArrayList<>();
        try {
            stmt = this.conn.prepareStatement(SELECT_ALL);
            rs = stmt.executeQuery();
            while (rs.next()) {
                asignaturas.add(this.convertir(rs));
            }
        } catch (SQLException e) {
            throw new DAOException("Hubo un error en la consulta SQL", e);
        } finally {
            if (stmt != null) {
                try {
                    stmt.close();
                } catch (SQLException ex) {
                    throw new DAOException("Error a la hora de cerrar la conexión SQL", ex);
                }
            }
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException ex) {
                    throw new DAOException("Error a la hora de cerrar el set de resultados", ex);
                }
            }
        }
        return asignaturas;
    }

    @Override
    public Asignatura obtener(Long id) throws DAOException {
        PreparedStatement stmt = null;
        ResultSet rs = null;
        Asignatura asignatura = null;
        try {
            stmt = this.conn.prepareStatement(SELECT);
            stmt.setLong(1, id);
            rs = stmt.executeQuery();
            if (rs.next()) {
                asignatura = this.convertir(rs);
            } else {
                throw new DAOException("No se encontró la asignatura con ID " + id);
            }
        } catch (SQLException e) {
            throw new DAOException("Error en la consulta SQL", e);
        } finally {
            if (stmt != null) {
                try {
                    stmt.close();
                } catch (SQLException ex) {
                    throw new DAOException("Error a la hora de cerrar la conexión SQL", ex);
                }
            }
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException ex) {
                    throw new DAOException("Error a la hora de cerrar el set de resultados", ex);
                }
            }
        }
        return asignatura;
    }

    private Asignatura convertir(ResultSet rs) throws SQLException {
        Asignatura asignatura = new Asignatura(
                rs.getString("nombre"),
                rs.getLong("profesor")
        );
        asignatura.setId(rs.getLong("id_asignatura"));
        return asignatura;
    }
}
