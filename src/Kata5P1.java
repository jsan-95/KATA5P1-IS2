
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


public class Kata5P1{
    
    
    public static void main(String args[]) throws ClassNotFoundException, SQLException, IOException{
        Class.forName("org.sqlite.JDBC");
        createTableDB();
        consultaDb();
        getAllEmails();
        
    }

    private static void consultaDb() throws SQLException {
        try (Connection con = DriverManager.getConnection("jdbc:sqlite:/Users/Yisus95/NetBeansProjects/Bases de datos/ejemplo KATA5.db")) {
            PreparedStatement ps= con.prepareStatement("select * from people;");
            ps.execute();
            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                System.out.println(rs.getInt("id")+" "+rs.getString("NAME")+" "+rs.getString("APELLIDOS")+" "+rs.getString("DEPARTAMENTO"));
            }
        }
    }

    private static void createTableDB() throws SQLException {
        try (Connection con = DriverManager.getConnection("jdbc:sqlite:/Users/Yisus95/NetBeansProjects/Bases de datos/ejemplo KATA5.db")) {
            PreparedStatement ps= con.prepareStatement("create table if not exists EMAIL (id integer PRIMARY KEY AUTOINCREMENT,"
                                                                        + "mail text NOT NULL);");
            ps.execute();

        }
    }

    private static void getAllEmails() throws FileNotFoundException, IOException, SQLException {
        BufferedReader reader = new BufferedReader(new FileReader(new File("src/emails.txt")));
        String mail = "";
        while((mail = reader.readLine()) != null){
            if(mail.contains("@")){
                insertIntoEmails(mail);
            }
        }
        
    }

    private static void insertIntoEmails(String mail) throws SQLException {
        try (Connection con = DriverManager.getConnection("jdbc:sqlite:/Users/Yisus95/NetBeansProjects/Bases de datos/ejemplo KATA5.db")) {
            PreparedStatement ps= con.prepareStatement("insert into EMAIL (mail) values(?)");
            ps.setString(1, mail);
            ps.execute();
        }
    }




}
