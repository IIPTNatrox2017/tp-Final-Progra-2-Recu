package Interfaces;

import java.sql.SQLException;

public interface ICrudRepository<T>
{
    void insert(T Element);
    
    T select(int id);
    
    void update(T Element);
    
    void delete(T Element);
}
