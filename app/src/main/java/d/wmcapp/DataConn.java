package d.wmcapp;

import android.annotation.SuppressLint;
import android.os.StrictMode;
import android.util.Log;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


/**
 * Created by Josh on 23/03/2016.
 */
public class DataConn {
    // Declare Variables
    String server = "SQL2014.studentwebserver.co.uk";
    //driver for accessing MS SQL Server and pulling back into resultsets
    String driver = "net.sourceforge.jtds.jdbc.Driver";
    String db = "db_1525770_WMCapp";
    String user = "user_db_1525770_WMCapp";
    String password = "WMCapp123";

    @SuppressLint("NewAPI")
    //inner class to build connection string
    public Connection CONN() {
        //utilsing strictmode to keep network operations off main thread
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        //set conn as null
        Connection conn = null;
        String ConnURL = null;

        //try/catch connection
        try {
            Class.forName(driver);
            ConnURL = "jdbc:jtds:sqlserver://" + server + ";" +
                    "databaseName=" + db + ";user=" + user +
                    ";password=" + password + ";";

            conn = DriverManager.getConnection(ConnURL);
        //output error types
        } catch (SQLException e) {
            Log.e("ERRO", e.getMessage());
        } catch (ClassNotFoundException e) {
            Log.e("ERRO", e.getMessage());
        } catch (Exception e) {
            Log.e("ERRO", e.getMessage());
        }
        return conn;
    }
}

