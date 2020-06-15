package Modelo;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * @author Enrique Baena Arrabal
 */
public class Conector {

    private Connection conexion;
    private String strSQL;
    private ResultSet cursor;

    //Fichero properties
    public Conector(File archivo) throws IOException, ClassNotFoundException, SQLException, FileNotFoundException {
        String classforname="com.mysql.jdbc.Driver";
        String servidor="jdbc:mysql://localhost:3306/";
        String usuario="USU";
        String password="USU";
        String bd="EQUIPOQUINIELA";

        Class.forName(classforname);

        //Nos conectamos a la base de datos.
        conexion = DriverManager.getConnection(servidor + bd, usuario, password);

    }

    public void setSQL(String consulta) {
        this.strSQL = consulta;
    }

    public boolean ejecutarConsultaActualizable() {
        try {
            Statement statement = (Statement) conexion.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            statement.executeUpdate(strSQL);
            return true;
        } catch (SQLException ex) {
            ex.printStackTrace();
            return false;
        }
    }

    public boolean ejecutarConsulta() {
        try {
            Statement statement = (Statement) conexion.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            cursor = statement.executeQuery(strSQL);
            return true;
        } catch (SQLException ex) {
            ex.printStackTrace();
            return false;
        }
    }
    
    public ResultSet getCursor() {
        return this.cursor;
    }

    public void cerrarConexion() {
        try {
            if (conexion != null) {
                conexion.close();
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public void cerrarCursor() {
        if (cursor != null) {
            try {
                cursor.close();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }

    public boolean isValid(int i) throws SQLException {
        return conexion.isValid(i);
    }
}
