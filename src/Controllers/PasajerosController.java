package Controllers;

import Models.Pasajero;

public class PasajerosController extends GenericController<Pasajero>
{
    @Override
    public Boolean exists(Integer id)
    {
        return this.elementos.stream().anyMatch(p -> p.getIdPasajero().equals(id));
    }
    
    @Override
    public Pasajero get(Integer id)
    {
        return this.elementos.stream().filter(p -> p.getIdPasajero().equals(id)).findFirst().orElse(null);
    }
    
    @Override
    public Boolean delete(Integer id)
    {
        return this.elementos.removeIf(p -> p.getIdPasajero().equals(id));
    }
}
