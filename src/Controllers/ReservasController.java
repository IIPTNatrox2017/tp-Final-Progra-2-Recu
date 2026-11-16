package Controllers;

import Exceptions.RepositoryWriteException;
import Models.Reserva;
import Models.Pasajero;
import Repositories.ReservaRepository;
import java.time.LocalDate;
import java.util.Set;

public class ReservasController extends GenericController<Reserva>
{
    public ReservasController() throws RuntimeException
    {
        this.add(new ReservaRepository().selectAll());
    }
    
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
        Reserva reserva = this.get(id);
        
        if(reserva == null)
        {
            return false;
        }
        
        new ReservaRepository().delete(reserva);
        
        this.elementos.remove(reserva);
        
        return true;
    }
    
    @Override
    protected Reserva addRepository(Reserva element) throws RepositoryWriteException
    {
        return new ReservaRepository().insert(element);
    }
    
    @Override
    protected void updateRepository(Reserva element) throws RepositoryWriteException
    {
        new ReservaRepository().update(element);
    }

    public Boolean addConPasajero(Reserva reserva, Pasajero pasajero)
            throws RepositoryWriteException
    {
        Reserva reservaCreada = this.addRepository(reserva);
        
        if (!this.elementos.add(reservaCreada))
        {
            return false;
        }

        try
        {
            new ReservaRepository().insertRelacionPasajero(
                    reservaCreada.getIdReserva(), pasajero.getIdPasajero()
            );
            
            pasajero.addReserva(reservaCreada.getIdReserva());
            
            return true;
        }
        catch (RepositoryWriteException e)
        {
            this.delete(reservaCreada.getIdReserva());
            
            throw e;
        }
    }

    public Set<Integer> obtenerPasajeros(Integer idReserva)
    {
        return new ReservaRepository().obtenerPasajeros(idReserva);
    }

    public Set<Reserva> obtenerReservasDelDia(LocalDate fecha)
    {
        return new ReservaRepository().obtenerReservasDelDia(fecha);
    }
}
