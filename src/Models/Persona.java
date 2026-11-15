package Models;

import java.util.Objects;

public abstract class Persona
{
    private Integer idPersona;
    private String dni;
    private String nombre;
    private String origen;
    private String domicilio;
    
    public Persona(Integer idPersona, String dni, String nombre, String origen, String domicilio)
    {
        this.idPersona = idPersona;
        this.dni = dni;
        this.nombre = nombre;
        this.origen = origen;
        this.domicilio = domicilio;
    }
    
    public Integer getIdPersona()
    {
        return this.idPersona;
    }
    
    public void setIdPersona(Integer idPersona)
    {
        this.idPersona = idPersona;
    }
    
    public String getDni()
    {
        return this.dni;
    }
    
    public void setDni(String dni)
    {
        this.dni = dni;
    }
    
    public String getNombre()
    {
        return this.nombre;
    }
    
    public void setNombre(String nombre)
    {
        this.nombre = nombre;
    }
    
    public String getOrigen()
    {
        return this.origen;
    }
    
    public void setOrigen(String origen)
    {
        this.origen = origen;
    }
    
    public String getDomicilio()
    {
        return this.domicilio;
    }
    
    public void setDomicilio(String domicilio)
    {
        this.domicilio = domicilio;
    }
    
    @Override
    public boolean equals(Object o)
    {
        if (!(o instanceof Persona persona))
        {
            return false;
        }
        return Objects.equals(this.getIdPersona(), persona.getIdPersona());
    }
    
    @Override
    public int hashCode()
    {
        return Objects.hashCode(this.getIdPersona());
    }
    
    @Override
    public String toString()
    {
        return "Persona{" +
                "idPersona=" + this.getIdPersona() +
                ", dni='" + this.getDni() + '\'' +
                ", nombre='" + this.getNombre() + '\'' +
                ", origen='" + this.getOrigen() + '\'' +
                ", domicilio='" + this.getDomicilio() + '\'' +
                '}';
    }
}
