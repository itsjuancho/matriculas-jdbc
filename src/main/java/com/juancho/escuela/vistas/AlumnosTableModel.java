package com.juancho.escuela.vistas;

import com.juancho.escuela.dao.AlumnoDAO;
import com.juancho.escuela.dao.DAOException;
import com.juancho.escuela.modelo.Alumno;

import javax.swing.table.AbstractTableModel;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.List;

public class AlumnosTableModel extends AbstractTableModel {
    private AlumnoDAO dao;
    private List<Alumno> alumnos;

    public AlumnosTableModel(AlumnoDAO dao) {
        this.dao = dao;
        alumnos = new ArrayList<>();
    }

    public void updateModel() throws DAOException {
        this.alumnos = dao.obtenerTodos();
    }

    @Override
    public int getRowCount() {
        return alumnos.size();
    }

    @Override
    public int getColumnCount() {
        return 4;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Alumno alumnoSolicitado = alumnos.get(rowIndex);
        switch (columnIndex) {
            case 0:
                return alumnoSolicitado.getId();
            case 1:
                return alumnoSolicitado.getApellidos();
            case 2:
                return alumnoSolicitado.getNombre();
            case 3:
                DateFormat df = DateFormat.getDateInstance();
                return df.format(alumnoSolicitado.getFechaNacimiento());
            default:
                return "[NO]";
        }
    }

    @Override
    public String getColumnName(int column) {
        switch (column) {
            case 0:
                return "ID";
            case 1:
                return "Apellidos";
            case 2:
                return "Nombre";
            case 3:
                return "Fecha de Nacimiento";
            default:
                return "[NO]";
        }
    }
}
