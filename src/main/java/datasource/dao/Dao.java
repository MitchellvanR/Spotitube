package datasource.dao;

import datasource.util.DatabaseProperties;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public class Dao {

    public PreparedStatement prepareStatement(String sqlString) throws SQLException {

        return DatabaseProperties.getInstance().getConnection().prepareStatement(sqlString);
    }

    public void disconnect() {
        DatabaseProperties.getInstance().disconnect();
    }
}
