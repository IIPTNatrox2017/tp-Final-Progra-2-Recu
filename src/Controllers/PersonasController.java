package Controllers;

import Models.Persona;

public class PersonasController extends GenericController<Persona>
{
    @Override
    public Boolean exists(Integer id)
    {
        return this.elementos.stream().anyMatch(p -> p.getIdPersona().equals(id));
    }
    
    @Override
    public Persona get(Integer id)
    {
        return this.elementos.stream().filter(p -> p.getIdPersona().equals(id)).findFirst().orElse(null);
    }
    
    @Override
    public Boolean delete(Integer id)
    {
        return this.elementos.removeIf(p -> p.getIdPersona().equals(id));
    }
}
