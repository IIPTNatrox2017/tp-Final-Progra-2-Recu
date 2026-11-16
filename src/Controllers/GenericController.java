package Controllers;

import Exceptions.RepositoryWriteException;
import Interfaces.ICrudController;

import java.util.HashSet;
import java.util.Set;

public abstract class GenericController<T> implements ICrudController<T>
{
    protected final Set<T> elementos = new HashSet<>();
    
    protected abstract void addRepository(T element) throws RepositoryWriteException;
    
    protected abstract void updateRepository(T element) throws RepositoryWriteException;
    
    @Override
    public Boolean add(T elemento) throws RepositoryWriteException
    {
        if (this.elementos.contains(elemento))
        {
            return false;
        }
        
        this.elementos.add(elemento);
        
        this.addRepository(elemento);
        
        return true;
    }
    
    @Override
    public Boolean add(Set<T> elementos)
    {
        if (this.elementos.containsAll(elementos))
        {
            return false;
        }
        
        this.elementos.addAll(elementos);
        
        return true;
    }
    
    @Override
    public Set<T> getAll()
    {
        return Set.copyOf(this.elementos);
    }
    
    @Override
    public Boolean update(T elemento) throws RepositoryWriteException
    {
        if (this.elementos.contains(elemento) == false)
        {
            return false;
        }
        
        this.updateRepository(elemento);
        
        return true;
    }
    
    @Override
    public Boolean isEmpty()
    {
        return this.elementos.isEmpty();
    }
}
