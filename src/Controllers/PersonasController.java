package Controllers;

import Exceptions.RepositoryWriteException;
import Models.Persona;
import Repositories.PersonaRepository;

public class PersonasController extends GenericController<Persona>
{
    public PersonasController() throws RuntimeException
    {
        this.add(new PersonaRepository().selectAll());
    }
    
    @Override
    public Boolean exists(Integer id)
    {
        return this.elementos.stream().anyMatch(p -> p.getIdPersona().equals(id));
    }
    
    public Boolean exists(String dni)
    {
        return this.elementos.stream().anyMatch(p -> p.getDni().equals(dni));
    }
    
    @Override
    public Persona get(Integer id)
    {
        return this.elementos.stream().filter(p -> p.getIdPersona().equals(id)).findFirst().orElse(null);
    }
    
    public Persona get(String dni)
    {
        return this.elementos.stream().filter(p -> p.getDni().equals(dni)).findFirst().orElse(null);
    }
    
    @Override
    public Boolean delete(Integer id)
    {
        Persona persona = this.get(id);
        
        if(persona == null)
        {
            return false;
        }
        
        new PersonaRepository().delete(persona);
        
        this.elementos.remove(persona);
        
        return true;
    }
    
    @Override
    protected void addRepository(Persona element) throws RepositoryWriteException
    {
        new PersonaRepository().insert(element);
    }
    
    @Override
    protected void updateRepository(Persona element) throws RepositoryWriteException
    {
        new PersonaRepository().update(element);
    }
}
