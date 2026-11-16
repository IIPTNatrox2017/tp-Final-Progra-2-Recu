package Interfaces;
import Exceptions.RepositoryWriteException;

import java.sql.SQLException;
import java.util.Set;

public interface ICrudRepository<T>
{
    T insert(T Element) throws RepositoryWriteException;

    Set<T> selectAll();
    
    void update(T Element) throws RepositoryWriteException;
    
    void delete(T Element) throws RepositoryWriteException;
}
