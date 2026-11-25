package Controllers;

import Exceptions.RepositoryWriteException;
import Models.Pasajero;
import Repositories.PasajeroRepository;
import Models.Reserva;
import Models.Ocupacion;
import java.util.ArrayList;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class PasajerosController extends GenericController<Pasajero>
{
    private final PersonasController personas;

    public PasajerosController(PersonasController personas) throws RuntimeException
    {
        this.personas = personas;
        this.add(new PasajeroRepository().selectAll());
    }
    
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
        Pasajero pasajero = this.get(id);
        
        if(pasajero == null)
        {
            return false;
        }
        
        new PasajeroRepository().delete(pasajero);
        
        this.elementos.remove(pasajero);
        
        return true;
    }
    
    @Override
    protected Pasajero addRepository(Pasajero element) throws RepositoryWriteException
    {
        if (this.personas.exists(element.getDni()))
        {
            throw new RepositoryWriteException("Ya existe una persona con el DNI ingresado");
        }

        return new PasajeroRepository().insert(element);
    }
    
    @Override
    protected void updateRepository(Pasajero element) throws RepositoryWriteException
    {
        new PasajeroRepository().update(element);
    }

    public ArrayList<Reserva> obtenerHistorialReservas(Pasajero pasajero, ReservasController reservas)
    {
        ArrayList<Reserva> historial = new ArrayList<>();

        for (Reserva reserva : reservas.getAll())
        {
            if (reservas.obtenerPasajeros(reserva.getIdReserva())
                    .contains(pasajero.getIdPasajero()))
            {
                historial.add(reserva);
            }
        }

        return historial;
    }

    public ArrayList<Ocupacion> obtenerHistorialOcupaciones(Pasajero pasajero, OcupacionesController ocupaciones)
    {
        ArrayList<Ocupacion> historial = new ArrayList<>();

        for (Ocupacion ocupacion : ocupaciones.getAll())
        {
            if (ocupacion.getPasajero(pasajero.getIdPasajero()) != null)
            {
                historial.add(ocupacion);
            }
        }

        return historial;
    }

    public long calcularTotalNoches(ArrayList<Ocupacion> historial)
    {
        long total = 0;

        for (Ocupacion ocupacion : historial)
        {
            total += ChronoUnit.DAYS.between(ocupacion.getCheckIn(), ocupacion.getCheckOut()
            );
        }

        return total;
    }
}

