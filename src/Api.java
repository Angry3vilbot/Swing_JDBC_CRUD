import javax.swing.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

public class Api extends JFrame {
    private final String DATABASE_URL = "jdbc:postgresql://localhost:5432/postgres";
    private final String password = System.getenv("PASSWORD");
    private Connection connection = null;
    private PreparedStatement pstat = null;

    void createItem(String username, int age, double balance) throws SQLException {
        SQLException error;
        String errorText = null;
        SQLException errorException = null;

        try {
            //Establish a connection
            connection = DriverManager.getConnection(DATABASE_URL, "postgres", password);
            //Create a prepared statement for inserting data
            pstat = connection.prepareStatement("INSERT INTO \"user\" (username, age, balance) VALUES (?,?,?)");
            pstat.setString(1, username);
            pstat.setInt(2, age);
            pstat.setDouble(3, balance);
            //Insert data into the table
            pstat.executeUpdate();

        } catch (SQLException e) {
            errorText = "Error 1: Failed to upload";
            errorException = e;
        } finally {
            try {
                pstat.close();
                connection.close();
            } catch (SQLException e) {
                errorText = "Error 2: Failed to close the connection";
                errorException = e;
            }
        }

        if(errorText != null) {
            error = new SQLException(errorText, errorException.getCause());
            throw error;
        }
    }

    String[][] readItems() throws SQLException {
        String[][] result = new String[3][100];

        //Establish a connection
        connection = DriverManager.getConnection(DATABASE_URL, "postgres", password);
        //Create a prepared statement for querying data in the table
        pstat = connection.prepareStatement("SELECT username, age, balance FROM \"user\"");
        ResultSet resultSet = pstat.executeQuery();
        //Process the results of the query
        ResultSetMetaData metaData = resultSet.getMetaData();
        int j = 0;
        while(resultSet.next()) {
            for(int i = 1; i <= metaData.getColumnCount(); i++) {
                //[Column][row]
                result[i - 1][j] = resultSet.getString(i);
            }
            j++;
        }
        pstat.close();
        connection.close();
        return result;
    }

    void updateItem(String queryName,String username, int age, double balance) throws SQLException{
        //Establish a connection
        connection = DriverManager.getConnection(DATABASE_URL, "postgres", password);
        //Create a prepared statement for updating data in the table
        pstat = connection.prepareStatement("UPDATE \"user\" SET username=?, age=?, balance=? WHERE username=?");
        pstat.setString(1, username);
        pstat.setInt(2, age);
        pstat.setDouble(3, balance);
        pstat.setString(4, queryName);
        //Update the table
        pstat.executeUpdate();
    }

    void deleteItem(String username) throws SQLException{
        //Establish a connection
        connection = DriverManager.getConnection(DATABASE_URL, "postgres", password);
        //Create a prepared statement for updating data in the table
        pstat = connection.prepareStatement("DELETE FROM \"user\" WHERE username=?");
        pstat.setString(1, username);
        //Update the table
        pstat.executeUpdate();
        pstat.close();
        connection.close();
    }
}
