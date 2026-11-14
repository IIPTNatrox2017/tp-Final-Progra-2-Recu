import Database.DatabaseConnection;

import java.sql.Connection;
import java.sql.SQLException;

void main()
{
    try (Connection connection = DatabaseConnection.getConnection())
    {
        System.out.println("Connected to the database");
    }
    catch (SQLException e)
    {
        e.printStackTrace();
    }
}
