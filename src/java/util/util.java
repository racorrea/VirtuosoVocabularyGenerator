
package util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author roddycorrea
 */
public class util {
    private static String URL = "jdbc:virtuoso://localhost:1111/";
    private static String JDBC = "virtuoso.jdbc4.Driver";
    private static String USER = "dba";
    private static String PASS = "dba";
    
    public Connection getConnect(){
        Connection conn=null;
            try {
                Class.forName(JDBC);
                conn = DriverManager.getConnection(URL, USER, PASS);
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(util.class.getName()).log(Level.SEVERE, null, ex);
            } catch (SQLException ex) {
                Logger.getLogger(util.class.getName()).log(Level.SEVERE, null, ex);
            }
        return conn;
    }
    
}
