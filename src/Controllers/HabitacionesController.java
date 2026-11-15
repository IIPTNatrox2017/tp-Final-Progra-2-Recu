package Controllers;

import Models.Habitacion;

public class HabitacionesController extends GenericController<Habitacion>
{
    @Override
    public Boolean exists(Integer id)
    {
        return this.elementos.stream().anyMatch(h -> h.getIdHabitacion().equals(id));
    }
    
    @Override
    public Habitacion get(Integer id)
    {
        return this.elementos.stream().filter(h -> h.getIdHabitacion().equals(id)).findFirst().orElse(null);
    }
    
    @Override
    public Boolean delete(Integer id)
    {
        return this.elementos.removeIf(h -> h.getIdHabitacion().equals(id));
    }
}
