package Models;

import java.time.LocalDate;
import java.util.Objects;
import java.util.Set;

public class Reserva
{
    private Integer idReserva;
    private final Set<Habitacion> habitaciones;
    private LocalDate fechaReserva;
    private LocalDate fechaCheckIn;
    private LocalDate fechaCheckOut;
    
    public Reserva(Integer idReserva, Set<Habitacion> habitaciones, LocalDate fechaReserva, LocalDate fechaCheckIn,
                   LocalDate fechaCheckOut
    )
    {
        this.idReserva = idReserva;
        this.habitaciones = habitaciones;
        this.fechaReserva = fechaReserva;
        this.fechaCheckIn = fechaCheckIn;
        this.fechaCheckOut = fechaCheckOut;
    }
    
    public Integer getIdReserva()
    {
        return this.idReserva;
    }
    
    public void setIdReserva(Integer idReserva)
    {
        this.idReserva = idReserva;
    }
    
    public Set<Habitacion> getHabitaciones()
    {
        return Set.copyOf(this.habitaciones);
    }
    
    public Boolean addHabitacion(Habitacion habitacion)
    {
        if(this.habitaciones.contains(habitacion))
        {
            return false;
        }
        
        this.habitaciones.add(habitacion);
        
        return true;
    }
    
    public Habitacion getHabitacion(Integer idHabitacion)
    {
        return this.habitaciones.stream().filter(h -> h.getIdHabitacion().equals(idHabitacion)).findFirst().orElse(null);
    }
    
    public Boolean removeHabitacion(Integer idHabitacion)
    {
        return this.habitaciones.removeIf(h -> h.getIdHabitacion().equals(idHabitacion));
    }
    
    public LocalDate getFechaReserva()
    {
        return this.fechaReserva;
    }
    
    public void setFechaReserva(LocalDate fechaReserva)
    {
        this.fechaReserva = fechaReserva;
    }
    
    public LocalDate getFechaCheckIn()
    {
        return this.fechaCheckIn;
    }
    
    public void setFechaCheckIn(LocalDate fechaCheckIn)
    {
        this.fechaCheckIn = fechaCheckIn;
    }
    
    public LocalDate getFechaCheckOut()
    {
        return this.fechaCheckOut;
    }
    
    public void setFechaCheckOut(LocalDate fechaCheckOut)
    {
        this.fechaCheckOut = fechaCheckOut;
    }
    
    public Integer getCantidadDiasReserva()
    {
        return (int) (this.fechaCheckOut.toEpochDay() - this.fechaCheckIn.toEpochDay());
    }
    
    public String getHabitacionesString()
    {
        StringBuilder sb = new StringBuilder();
        
        for(Habitacion habitacion : this.habitaciones)
        {
            sb.append(habitacion.getHabitacion())
                    .append(", ");
        }
        
        sb.setLength(sb.length() - 2);
        
        return sb.toString();
    }
    
    @Override
    public boolean equals(Object o)
    {
        if (!(o instanceof Reserva reserva))
        {
            return false;
        }
        return Objects.equals(this.getIdReserva(), reserva.getIdReserva());
    }
    
    @Override
    public int hashCode()
    {
        return Objects.hashCode(this.getIdReserva());
    }
    
    @Override
    public String toString()
    {
        return "Reserva{" +
                "idReserva=" + this.getIdReserva() +
                ", habitaciones='" + this.getHabitacionesString() + '\'' +
                ", fechaReserva=" + this.getFechaReserva() +
                ", fechaCheckIn=" + this.getFechaCheckIn() +
                ", fechaCheckOut=" + this.getFechaCheckOut() +
                '}';
    }
}
