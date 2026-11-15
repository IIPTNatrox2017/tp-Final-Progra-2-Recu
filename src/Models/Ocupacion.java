package Models;

import java.time.LocalDate;
import java.util.Objects;
import java.util.Set;

public class Ocupacion
{
    private Integer idOcupacion;
    private Habitacion habitacion;
    private final Set<Pasajero> pasajeros;
    private LocalDate checkIn;
    private LocalDate checkOut;
    
    public Ocupacion(Integer idOcupacion, Habitacion habitacion, Set<Pasajero> pasajeros, LocalDate checkIn,
                     LocalDate checkOut
    )
    {
        this.idOcupacion = idOcupacion;
        this.habitacion = habitacion;
        this.pasajeros = pasajeros;
        this.checkIn = checkIn;
        this.checkOut = checkOut;
    }
    
    public Integer getIdOcupacion()
    {
        return this.idOcupacion;
    }
    
    public void setIdOcupacion(Integer idOcupacion)
    {
        this.idOcupacion = idOcupacion;
    }
    
    public Habitacion getHabitacion()
    {
        return this.habitacion;
    }
    
    public void setHabitacion(Habitacion habitacion)
    {
        this.habitacion = habitacion;
    }
    
    public Set<Pasajero> getPasajeros()
    {
        return Set.copyOf(this.pasajeros);
    }
    
    public Boolean addPasajero(Pasajero pasajero)
    {
        if(this.pasajeros.contains(pasajero))
        {
            return false;
        }
        
        this.pasajeros.add(pasajero);
        
        return true;
    }
    
    public Pasajero getPasajero(Integer idPasajero)
    {
        return this.pasajeros.stream().filter(p -> p.getIdPasajero().equals(idPasajero)).findFirst().orElse(null);
    }
    
    public Boolean removePasajero(Integer idPasajero)
    {
        return this.pasajeros.removeIf(p -> p.getIdPasajero().equals(idPasajero));
    }
    
    public LocalDate getCheckIn()
    {
        return this.checkIn;
    }
    
    public void setCheckIn(LocalDate checkIn)
    {
        this.checkIn = checkIn;
    }
    
    public LocalDate getCheckOut()
    {
        return this.checkOut;
    }
    
    public void setCheckOut(LocalDate checkOut)
    {
        this.checkOut = checkOut;
    }
    
    public String getHabitacionString()
    {
        return this.habitacion.getHabitacion();
    }
    
    public String getPasajerosNombre()
    {
        StringBuilder sb = new StringBuilder();
        
        for(Pasajero pasajero : this.pasajeros)
        {
            sb.append(pasajero.getNombre())
                    .append(", ");
        }
        
        sb.setLength(sb.length() - 2);
        
        return sb.toString();
    }
    
    @Override
    public boolean equals(Object o)
    {
        if (!(o instanceof Ocupacion ocupacion))
        {
            return false;
        }
        return Objects.equals(this.getIdOcupacion(), ocupacion.getIdOcupacion());
    }
    
    @Override
    public int hashCode()
    {
        return Objects.hashCode(this.getIdOcupacion());
    }
    
    @Override
    public String toString()
    {
        return "Ocupacion{" +
                "idOcupacion=" + this.getIdOcupacion() +
                ", habitacion='" + this.getHabitacionString() + '\'' +
                ", pasajeros='" + this.getPasajerosNombre() + '\'' +
                ", checkIn=" + this.getCheckIn() +
                ", checkOut=" + this.getCheckOut() +
                '}';
    }
}
