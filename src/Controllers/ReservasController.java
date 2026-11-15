package Controllers;

import Models.Reserva;

public class ReservasController extends GenericController<Reserva>
{
    @Override
    public Boolean exists(Integer id)
    {
        return this.elementos.stream().anyMatch(p -> p.getIdReserva().equals(id));
    }
    
    @Override
    public Reserva get(Integer id)
    {
        return this.elementos.stream().filter(p -> p.getIdReserva().equals(id)).findFirst().orElse(null);
    }
    
    @Override
    public Boolean delete(Integer id)
    {
        return this.elementos.removeIf(p -> p.getIdReserva().equals(id));
    }
}
