import java.sql.Connection;
import java.sql.DriverManager;

public class SqlConnection{

        static String connect = "jdbc:postgresql://localhost:5432/cc";
        static String roleUser = "pi";
        static String rolePass = "server";
        static Connection conn;

    public static Connection getConnection(){
        
        try {
            Class.forName("org.postgresql.Driver");
            try {
                conn = DriverManager.getConnection(connect, roleUser, rolePass);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return conn;
    }
}
