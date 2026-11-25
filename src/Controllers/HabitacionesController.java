package Controllers;

import Exceptions.RepositoryWriteException;
import Models.Habitacion;
import Repositories.HabitacionRepository;
import java.util.ArrayList;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class HabitacionesController extends GenericController<Habitacion>
{
    public HabitacionesController() throws RuntimeException
    {
        this.add(new HabitacionRepository().selectAll());
    }
    
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
        Habitacion habitacion = this.get(id);
        
        if(habitacion == null)
        {
            return false;
        }
        
        new HabitacionRepository().delete(habitacion);
        
        this.elementos.remove(habitacion);
        
        return true;
    }
    
    @Override
    protected Habitacion addRepository(Habitacion element) throws RepositoryWriteException
    {
        return new HabitacionRepository().insert(element);
    }
    
    @Override
    protected void updateRepository(Habitacion element) throws RepositoryWriteException
    {
        new HabitacionRepository().update(element);
    }

    public ArrayList<Habitacion> buscarPorCapacidad(int capacidad)
    {
        ArrayList<Habitacion> habitaciones = new ArrayList<>();

        for (Habitacion h : this.getAll())
        {
            if (h.getCapacidad() >= capacidad)
            {
                habitaciones.add(h);
            }
        }

        return habitaciones;
    }

    public ArrayList<Habitacion> buscarPorPrecio(double minimo, double maximo)
    {
        ArrayList<Habitacion> habitaciones = new ArrayList<>();

        for (Habitacion h : this.getAll())
        {
            if (h.getPrecio() >= minimo && h.getPrecio() <= maximo)
            {
                habitaciones.add(h);
            }
        }

        return habitaciones;
    }

    public ArrayList<Habitacion> ordenarPorPrecioAscendente()
    {
        ArrayList<Habitacion> habitaciones = new ArrayList<>(this.getAll());

        Collections.sort(habitaciones, new Comparator<Habitacion>()
        {
            @Override
            public int compare(Habitacion h1, Habitacion h2)
            {
                return Double.compare(h1.getPrecio(), h2.getPrecio());
            }
        });

        return habitaciones;
    }

    public ArrayList<Habitacion> ordenarPorPrecioDescendente()
    {
        ArrayList<Habitacion> habitaciones = new ArrayList<>(this.getAll());

        Collections.sort(habitaciones, new Comparator<Habitacion>()
        {
            @Override
            public int compare(Habitacion h1, Habitacion h2)
            {
                return Double.compare(h2.getPrecio(), h1.getPrecio());
            }
        });

        return habitaciones;
    }
}
