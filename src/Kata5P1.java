
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


public class Kata5P1{
    
    
    public static void main(String args[]) throws ClassNotFoundException, SQLException{
        Class.forName("org.sqlite.JDBC");
        try (Connection con = DriverManager.getConnection("jdbc:sqlite:/Users/Yisus95/NetBeansProjects/Bases de datos/ejemplo KATA5.db")) {
            PreparedStatement ps= con.prepareStatement("select * from people;");
            ps.execute();
            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                System.out.println(rs.getInt("id")+" "+rs.getString("NAME")+" "+rs.getString("APELLIDOS")+" "+rs.getString("DEPARTAMENTO"));
            }
        }
        
    }
}
