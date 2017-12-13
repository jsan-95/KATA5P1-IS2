import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.Scanner;


public class Kata5P1{
    
    public static void main(String args[]) throws ClassNotFoundException, SQLException, IOException{
        Class.forName("oracle.jdbc.driver.OracleDriver");
        //createTableDB();
        consultaDb();
        
    }

    private static void consultaDb() throws SQLException {
        String url = "jdbc:oracle:thin:@127.0.0.1:1521:orcl";
        String usr = "system";
        String pass = "orcl";
        try (Connection con = DriverManager.getConnection(url,usr,pass)) {
            PreparedStatement ps= con.prepareStatement("select * from HISTORICO_CAMBIOS where DIVISA_DESDE = 'GBP'");
            ps.execute();
            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                System.out.println(rs.getString(1)+" "+rs.getString(2)+" "+rs.getFloat(3)+" "+rs.getTimestamp(6,Calendar.getInstance()));
            }
            countRegisters(con);
            countFromTo(con);
            getRegistersFromTo(con);
            getExchangeRateFromCadToUsd(con);
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
        BufferedReader reader = new BufferedReader(new FileReader(new File("/src/emails.txt")));
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

    private static void countRegisters(Connection con) throws SQLException {
        PreparedStatement ps= con.prepareStatement("select count(*) from HISTORICO_CAMBIOS ");
        ps.execute();
        ResultSet rs = ps.executeQuery();
        while(rs.next()){
            System.out.println("Número de registros = "+ rs.getInt(1));
        }
    }

    private static void countFromTo(Connection con) throws SQLException {
        PreparedStatement ps= con.prepareStatement("select COUNT(*) from HISTORICO_CAMBIOS where DIVISA_DESDE = 'GBP' or DIVISA_A = 'USD'");
        ps.execute();
        ResultSet rs = ps.executeQuery();
        while(rs.next()){
            System.out.println("Número de registros = "+ rs.getInt(1));
        }
    }

    private static void getRegistersFromTo(Connection con) throws SQLException {
        PreparedStatement ps= con.prepareStatement("select * from HISTORICO_CAMBIOS where DIVISA_DESDE = 'GBP' and DIVISA_A = 'USD'");
        ps.execute();
        ResultSet rs = ps.executeQuery();
        while(rs.next()){
            System.out.println(rs.getString(1)+" "+rs.getString(2)+" "+rs.getFloat(3)+" "+rs.getTimestamp(6,Calendar.getInstance()));
        }
    }

    private static void getExchangeRateFromCadToUsd(Connection con) throws SQLException {
        PreparedStatement ps= con.prepareStatement("select cambio from HISTORICO_CAMBIOS where rownum = 1 and DIVISA_DESDE = 'CAD' and DIVISA_A = 'USD'");
        ps.execute();
        ResultSet rs = ps.executeQuery();
        while(rs.next()){
            System.out.println("Introduzca cantidad: ");
            Scanner scanner = new Scanner(System.in);
            double amount = Double.parseDouble(scanner.next());
            System.out.println("El cambio de "+ amount+" CAD a USD es: "+rs.getFloat(1)*amount);
        }
    }
}
