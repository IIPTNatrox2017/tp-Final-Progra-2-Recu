package Enums;

public enum UsuarioRol
{
    ADMINISTRATIVO("Administrativo"),
    RECEPCIONISTA("Recepcionista");
    
    private String descripcion;
    
    private UsuarioRol(String descripcion)
    {
        this.descripcion = descripcion;
    }
    
    public String getDescripcion()
    {
        return this.descripcion;
    }
}
