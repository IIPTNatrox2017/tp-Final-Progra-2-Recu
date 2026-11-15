package Models;

import Enums.UsuarioRol;

public class Recepcionista extends Usuario
{
    public Recepcionista(Integer idPersona, String dni, String nombre, String origen, String domicilio,
                         Integer idUsuario,
                         String usuario, String contrasenia
    )
    {
        super(idPersona, dni, nombre, origen, domicilio, idUsuario, usuario, contrasenia);
    }
    
    @Override
    public UsuarioRol getRol()
    {
        return UsuarioRol.RECEPCIONISTA;
    }
}
