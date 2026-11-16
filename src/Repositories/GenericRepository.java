package Repositories;

import Database.DatabaseConnection;
import Exceptions.RepositoryWriteException;
import Interfaces.ICrudRepository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public abstract class GenericRepository<T> implements ICrudRepository<T>
{
    protected abstract T create(ResultSet rs) throws SQLException;
    
    protected int write(String query, Object... params) throws SQLException, RepositoryWriteException
    {
        Connection connection = DatabaseConnection.getConnection();
        PreparedStatement ps = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
        
        for (int i = 0; i < params.length; i++)
        {
            ps.setObject(i + 1, params[i]);
        }
        
        if (ps.executeUpdate() == 0)
        {
            throw new RepositoryWriteException("No se modificaron filas");
        }
        
        ResultSet rs = ps.getGeneratedKeys();
        
        if (rs.next())
        {
            return rs.getInt(1);
        }
        
        return 0;
    }
}
