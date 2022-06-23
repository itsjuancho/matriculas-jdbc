package com.juancho.escuela.dao.impl;

import com.juancho.escuela.dao.DAOException;
import com.juancho.escuela.dao.MatriculaDAO;
import com.juancho.escuela.modelo.Matricula;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MatriculaDAOImpl implements MatriculaDAO {
    final String CREATE = "INSERT INTO matricula (alumno, asignatura, fecha) VALUES (?, ?, ?)";
    final String UPDATE = "UPDATE matricula SET alumno = ?, asignatura = ?, fecha = ?, nota = ? WHERE id_matricula = ?";
    final String DELETE = "DELETE FROM matricula WHERE id_matricula = ?";
    final String SELECT_ALL = "SELECT id_matricula, alumno, asignatura, fecha, nota FROM matricula";
    final String SELECT = SELECT_ALL + " WHERE id_matricula = ?";
    final String SELECT_BY_ALUMNO = SELECT_ALL + " WHERE alumno = ?";
    final String SELECT_BY_FECHA = SELECT_ALL + " WHERE fecha = ?";
    final String SELECT_BY_ASIGNATURA = SELECT_ALL + " WHERE asignatura = ?";
    private Connection conn;

    public MatriculaDAOImpl(Connection conn) {
        this.conn = conn;
    }

    @Override
    public void crear(Matricula matricula) throws DAOException {
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            stmt = this.conn.prepareStatement(CREATE, Statement.RETURN_GENERATED_KEYS);
            stmt.setLong(1, matricula.getAlumnoId());
            stmt.setLong(2, matricula.getAsignaturaId());
            stmt.setInt(3, matricula.getYear());
            if (stmt.executeUpdate() == 0) {
                throw new DAOException("No se pudo crear la matricula");
            }
            rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                matricula.setId(rs.getLong(1));
            }
        } catch (SQLException e) {
            throw new DAOException("Hubo un error en la consulta SQL", e);
        } finally {
            if (stmt != null) {
                try {
                    stmt.close();
                } catch (SQLException e) {
                    throw new DAOException("Hubo un error al cerrar la consulta preparada", e);
                }
            }
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException e) {
                    throw new DAOException("Hubo un error al cerrar el set de resultados", e);
                }
            }
        }
    }

    @Override
    public void actualizar(Matricula matricula) throws DAOException {
        PreparedStatement stmt = null;
        try {
            stmt = this.conn.prepareStatement(UPDATE);
            stmt.setLong(1, matricula.getAlumnoId());
            stmt.setLong(2, matricula.getAsignaturaId());
            stmt.setInt(3, matricula.getYear());
            stmt.setInt(4, matricula.getNota());
            stmt.setLong(5, matricula.getId());
            if (stmt.executeUpdate() == 0) {
                throw new DAOException("No se pudo atualizar la matricula con ID " + matricula.getId());
            }
        } catch (SQLException e) {
            throw new DAOException("Ocurrió un error en la consulta SQL", e);
        } finally {
            if (stmt != null) {
                try {
                    stmt.close();
                } catch (SQLException e) {
                    throw new DAOException("Hubo un error al cerrar la consulta preparada", e);
                }
            }
        }
    }

    @Override
    public void eliminar(Matricula matricula) throws DAOException {
        PreparedStatement stmt = null;
        try {
            stmt = this.conn.prepareStatement(DELETE);
            stmt.setLong(1, matricula.getId());
            if (stmt.executeUpdate() == 0) {
                throw new DAOException("No se pudo eliminar la matricula con ID " + matricula.getId());
            }
        } catch (SQLException e) {
            throw new DAOException("Error en la consulta SQL para eliminar ", e);
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
    public List<Matricula> obtenerTodos() throws DAOException {
        PreparedStatement stmt = null;
        ResultSet rs = null;
        List<Matricula> matriculas = new ArrayList<>();
        try {
            stmt = this.conn.prepareStatement(SELECT_ALL);
            rs = stmt.executeQuery();
            while (rs.next()) {
                matriculas.add(this.convertir(rs));
            }
        } catch (SQLException e) {
            throw new DAOException("Error en la consulta SQL", e);
        } finally {
            if (stmt != null) {
                try {
                    stmt.close();
                } catch (SQLException e) {
                    throw new DAOException("Hubo un error al cerrar la consulta preparada", e);
                }
            }
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException e) {
                    throw new DAOException("Hubo un error al cerrar el set de resultados", e);
                }
            }
        }
        return matriculas;
    }

    @Override
    public Matricula obtener(Long id) throws DAOException {
        PreparedStatement stmt = null;
        ResultSet rs = null;
        Matricula matricula = null;
        try {
            stmt = this.conn.prepareStatement(SELECT);
            stmt.setLong(1, id);
            rs = stmt.executeQuery();
            if (rs.next()) {
                matricula = this.convertir(rs);
            } else {
                throw new DAOException("No se encontró ninguna matricula con la ID " + id);
            }
        } catch (SQLException e) {
            throw new DAOException("Error en la consulta SQL", e);
        } finally {
            if (stmt != null) {
                try {
                    stmt.close();
                } catch (SQLException e) {
                    throw new DAOException("Hubo un error al cerrar la consulta preparada", e);
                }
            }
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException e) {
                    throw new DAOException("Hubo un error al cerrar el set de resultados", e);
                }
            }
        }
        return matricula;
    }

    private Matricula convertir(ResultSet rs) throws SQLException {
        Matricula matricula = new Matricula(
                rs.getLong("alumno"),
                rs.getLong("asignatura"),
                rs.getInt("fecha")
        );
        matricula.setId(rs.getLong("id_matricula"));
        matricula.setNota(rs.getInt("nota"));
        return matricula;
    }

    @Override
    public List<Matricula> obtenerPorAlumno(Long alumno) throws DAOException {
        System.out.println("Hola soy la matricula!");
        return null;
    }

    public static void main(String[] args) {
//        Matricula matricula = new Matricula(1L, 6L, 2020);
        Matricula matricula = null;
        MatriculaDAO entity = (MatriculaDAO) FactoryDAO.getInstance().obtenerDAO("MatriculaDAO");
        try {
            matricula = entity.obtener(2L);
            System.out.println("Antes de actualizar" + matricula);
            matricula.setNota(9);
            entity.actualizar(matricula);
        } catch (DAOException e) {
            e.printStackTrace();
        }
        System.out.println("Después de actualizar" + matricula);
    }
}
