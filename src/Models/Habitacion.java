package Models;

import Enums.EstadoHabitacion;

import java.util.Objects;

public class Habitacion
{
    private final Integer idHabitacion;
    private String habitacion;
    private Integer capacidad;
    private Double precio;
    private EstadoHabitacion estadoHabitacion;
    
    public Habitacion(Integer idHabitacion, String habitacion, Integer capacidad, Double precio,
                      EstadoHabitacion estadoHabitacion
    )
    {
        this.idHabitacion = idHabitacion;
        this.habitacion = habitacion;
        this.capacidad = capacidad;
        this.precio = precio;
        this.estadoHabitacion = estadoHabitacion;
    }
    
    public Integer getIdHabitacion()
    {
        return this.idHabitacion;
    }
    
    public String getHabitacion()
    {
        return this.habitacion;
    }
    
    public void setHabitacion(String habitacion)
    {
        this.habitacion = habitacion;
    }
    
    public Integer getCapacidad()
    {
        return this.capacidad;
    }
    
    public void setCapacidad(Integer capacidad)
    {
        this.capacidad = capacidad;
    }
    
    public Double getPrecio()
    {
        return this.precio;
    }
    
    public void setPrecio(Double precio)
    {
        this.precio = precio;
    }
    
    public EstadoHabitacion getEstadoHabitacion()
    {
        return this.estadoHabitacion;
    }
    
    public void setEstadoHabitacion(EstadoHabitacion estadoHabitacion)
    {
        this.estadoHabitacion = estadoHabitacion;
    }
    
    @Override
    public boolean equals(Object o)
    {
        if (!(o instanceof Habitacion that))
        {
            return false;
        }
        return Objects.equals(this.getIdHabitacion(), that.getIdHabitacion());
    }
    
    @Override
    public int hashCode()
    {
        return Objects.hashCode(this.getIdHabitacion());
    }
    
    @Override
    public String toString()
    {
        return "Habitacion{" +
                "idHabitacion=" + this.getIdHabitacion() +
                ", habitacion='" + this.getHabitacion() + '\'' +
                ", capacidad=" + this.getCapacidad() +
                ", precio=" + this.getPrecio() +
                ", estadoHabitacion=" + this.getEstadoHabitacion().getDescripcion() +
                '}';
    }
}
