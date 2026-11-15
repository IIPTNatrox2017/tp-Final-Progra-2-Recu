package Controllers;

import Models.Ocupacion;

public class OcupacionesController extends GenericController<Ocupacion>
{
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
        return this.elementos.removeIf(o -> o.getIdOcupacion().equals(id));
    }
}
