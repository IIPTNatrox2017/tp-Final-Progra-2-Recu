package Models;

import Enums.UsuarioRol;

import java.util.Objects;

public abstract class Usuario extends Persona
{
    private final Integer idUsuario;
    private String usuario;
    private String contrasenia;
    
    public Usuario(Integer idPersona, String dni, String nombre, String origen, String domicilio, Integer idUsuario,
                   String usuario, String contrasenia
    )
    {
        super(idPersona, dni, nombre, origen, domicilio);
        
        this.idUsuario = idUsuario;
        this.usuario = usuario;
        this.contrasenia = contrasenia;
    }
    
    public Integer getIdUsuario()
    {
        return this.idUsuario;
    }
    
    public String getUsuario()
    {
        return this.usuario;
    }
    
    public void setUsuario(String usuario)
    {
        this.usuario = usuario;
    }
    
    public String getContrasenia()
    {
        return this.contrasenia;
    }
    
    public void setContrasenia(String contrasenia)
    {
        this.contrasenia = contrasenia;
    }
    
    public abstract UsuarioRol getRol();
    
    @Override
    public boolean equals(Object o)
    {
        if (!(o instanceof Usuario usuario))
        {
            return false;
        }
        if (!super.equals(o))
        {
            return false;
        }
        return Objects.equals(this.getIdUsuario(), usuario.getIdUsuario());
    }
    
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), this.getIdUsuario());
    }
    
    @Override
    public String toString()
    {
        return "Usuario{" +
                "idUsuario=" + this.getIdUsuario() +
                ", usuario='" + this.getUsuario() + '\'' +
                ", contrasenia='" + this.getContrasenia() + '\'' +
                ", rol='" + this.getRol().getDescripcion() + '\'' +
                '}';
    }
}
