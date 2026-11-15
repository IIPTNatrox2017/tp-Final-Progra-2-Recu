package Enums;

public enum EstadoHabitacion
{
    DISPONIBLE("Disponible"),
    OCUPADA("Ocupada"),
    EN_LIMPIEZA("En limpieza"),
    EN_REPARACION("En reparacion"),
    EN_DESINFECCION("En desinfeccion"),
    RESERVADA("Reservada");
    
    private final String descripcion;
    
    EstadoHabitacion(String descripcion)
    {
        this.descripcion = descripcion;
    }
    
    public String getDescripcion()
    {
        return this.descripcion;
    }
}
