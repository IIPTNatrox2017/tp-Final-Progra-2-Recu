package Interfaces;

import java.util.Set;

public interface ICrudController<T>
{
    Boolean add(T elemento);
    
    Boolean add(Set<T> elementos);
    
    Boolean exists(Integer id);
    
    T get(Integer id);
    
    Set<T> getAll();
    
    Boolean update(T elemento);
    
    Boolean delete(Integer id);
    
    Boolean isEmpty();
}
