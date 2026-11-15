package Models;

import Enums.UsuarioRol;

public class Administrativo extends Usuario
{
    public Administrativo(Integer idPersona, String dni, String nombre, String origen, String domicilio,
                          Integer idUsuario, String usuario, String contrasenia
    )
    {
        super(idPersona, dni, nombre, origen, domicilio, idUsuario, usuario, contrasenia);
    }
    
    @Override
    public UsuarioRol getRol()
    {
        return UsuarioRol.ADMINISTRATIVO;
    }
}
