package Controllers;

import Exceptions.RepositoryWriteException;
import Models.Ocupacion;
import Repositories.OcupacionRepository;

public class OcupacionesController extends GenericController<Ocupacion>
{
    public OcupacionesController() throws RuntimeException
    {
        this.add(new OcupacionRepository().selectAll());
    }
    
    @Override
    public Boolean exists(Integer id)
    {
        return this.elementos.stream().anyMatch(o -> o.getIdOcupacion().equals(id));
    }
    
    @Override
    public Ocupacion get(Integer id)
    {
        return this.elementos.stream().filter(o -> o.getIdOcupacion().equals(id)).findFirst().orElse(null);
    }
    
    @Override
    public Boolean delete(Integer id)
    {
        Ocupacion ocupacion = this.get(id);
        
        if(ocupacion == null)
        {
            return false;
        }
        
        new OcupacionRepository().delete(ocupacion);
        
        this.elementos.remove(ocupacion);
        
        return true;
    }
    
    @Override
    protected Ocupacion addRepository(Ocupacion element) throws RepositoryWriteException
    {
        return new OcupacionRepository().insert(element);
    }
    
    @Override
    protected void updateRepository(Ocupacion element) throws RepositoryWriteException
    {
        new OcupacionRepository().update(element);
    }
}
