package Models;

import java.util.Objects;
import java.util.Set;

public class Pasajero extends Persona
{
    private final Integer idPasajero;
    private final Set<Integer> reservas;
    private final Set<Integer> ocupaciones;
    
    public Pasajero(Integer idPersona, String dni, String nombre, String origen, String domicilio, Integer idPasajero,
                    Set<Integer> reservas, Set<Integer> ocupaciones
    )
    {
        super(idPersona, dni, nombre, origen, domicilio);
        
        this.idPasajero = idPasajero;
        this.reservas = reservas;
        this.ocupaciones = ocupaciones;
    }
    
    public Integer getIdPasajero()
    {
        return this.idPasajero;
    }
    
    public Set<Integer> getReservas()
    {
        return Set.copyOf(this.reservas);
    }
    
    public Boolean addReserva(Integer idReserva)
    {
        if(this.reservas.contains(idReserva))
        {
            return false;
        }
        
        this.reservas.add(idReserva);
        
        return true;
    }
    
    public Boolean existsReserva(Integer idReserva)
    {
        return this.reservas.contains(idReserva);
    }
    
    public Boolean removeReserva(Integer idReserva)
    {
        return this.reservas.remove(idReserva);
    }
    
    public Set<Integer> getOcupaciones()
    {
        return Set.copyOf(this.ocupaciones);
    }
    
    public Boolean addOcupaciones(Integer idOcupacion)
    {
        if(this.ocupaciones.contains(idOcupacion))
        {
            return false;
        }
        
        this.ocupaciones.add(idOcupacion);
        
        return true;
    }
    
    public Boolean existsOcupacion(Integer idOcupacion)
    {
        return this.ocupaciones.contains(idOcupacion);
    }
    
    public Boolean removeOcupacion(Integer idOcupacion)
    {
        return this.ocupaciones.remove(idOcupacion);
    }
    
    @Override
    public boolean equals(Object o)
    {
        if (!(o instanceof Pasajero pasajero))
        {
            return false;
        }
        if (!super.equals(o))
        {
            return false;
        }
        return Objects.equals(this.getIdPasajero(), pasajero.getIdPasajero());
    }
    
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), this.getIdPasajero());
    }
    
    @Override
    public String toString()
    {
        return "Pasajero{" +
                "idPasajero=" + this.getIdPasajero() +
                ", dni='" + this.getDni() + '\'' +
                ", nombre='" + this.getNombre() + '\'' +
                ", origen='" + this.getOrigen() + '\'' +
                ", domicilio='" + this.getDomicilio() + '\'' +
                '}';
    }
}
