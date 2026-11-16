package Repositories;

import Enums.IdConst;
import Enums.UsuarioRol;
import Exceptions.RepositoryWriteException;
import Models.Administrativo;
import Models.Persona;
import Models.Recepcionista;
import Models.Usuario;

import Database.DatabaseConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;

public class UsuarioRepository extends GenericRepository<Usuario>
{
    @Override
    protected Usuario create(ResultSet rs) throws SQLException
    {
        Integer idPersona = rs.getInt("id_persona");
        String dni = rs.getString("dni");
        String nombre = rs.getString("nombre");
        String origen = rs.getString("origen");
        String domicilio = rs.getString("domicilio");
        Integer idUsuario = rs.getInt("id_usuario");
        String usuario = rs.getString("usuario");
        String contrasenia = rs.getString("contrasenia");
        UsuarioRol rol = UsuarioRol.valueOf(rs.getString("rol"));

        if(rol == UsuarioRol.ADMINISTRATIVO)
        {
            return new Administrativo(idPersona, dni, nombre, origen, domicilio, idUsuario, usuario, contrasenia);
        }

        return new Recepcionista(idPersona, dni, nombre, origen, domicilio, idUsuario, usuario, contrasenia);
    }
    
    @Override
    public Usuario insert(Usuario element) throws RepositoryWriteException
    {
        String usuarioInsert = "INSERT INTO usuario " +
                "(id_persona, usuario, contrasenia, rol) VALUES (?, ?, ?, ?)";

        try
        {
            int idPersona = element.getIdPersona();
            
            if (idPersona == IdConst.SIN_ID.getValue())
            {
                Persona persona = new Persona(IdConst.SIN_ID.getValue(),
                        element.getDni(),
                        element.getNombre(),
                        element.getOrigen(),
                        element.getDomicilio()) {};
                
                idPersona = new PersonaRepository().insert(persona).getIdPersona();
            }

            int idUsuario = write(usuarioInsert,
                    idPersona,
                    element.getUsuario(),
                    element.getContrasenia(),
                    element.getRol().name());

            if (element.getRol() == UsuarioRol.ADMINISTRATIVO)
            {
                return new Administrativo(idPersona, element.getDni(), element.getNombre(),
                        element.getOrigen(), element.getDomicilio(), idUsuario,
                        element.getUsuario(), element.getContrasenia());
            }

            return new Recepcionista(idPersona, element.getDni(), element.getNombre(),
                    element.getOrigen(), element.getDomicilio(), idUsuario,
                    element.getUsuario(), element.getContrasenia());
        }
        catch (RepositoryWriteException | SQLException e)
        {
            throw new RepositoryWriteException("Error al insertar usuario: " + e.getMessage());
        }
    }
    
    @Override
    public Set<Usuario> selectAll() throws RuntimeException
    {
        Set<Usuario> usuarios = new HashSet<>();
        
        String query = """
                SELECT u.id_usuario, u.id_persona, p.dni, p.nombre, p.origen,
                    p.domicilio, u.usuario, u.contrasenia, u.rol
                FROM usuario u
                JOIN persona p ON p.id_persona = u.id_persona
                """;

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query);
             ResultSet rs = statement.executeQuery())
        {
            while (rs.next())
            {
                usuarios.add(create(rs));
            }
        }
        catch (SQLException e)
        {
            throw new RuntimeException("Error al consultar los usuarios: " + e.getMessage());
        }

        return usuarios;
    }
    
    @Override
    public void update(Usuario element) throws RepositoryWriteException
    {
        String usuarioUpdate = "UPDATE usuario SET usuario = ?, contrasenia = ?, rol = ? " +
                "WHERE id_usuario = ?";

        try
        {
            Persona persona = new Persona(element.getIdPersona(),
                    element.getDni(),
                    element.getNombre(),
                    element.getOrigen(),
                    element.getDomicilio()) {};
            
            new PersonaRepository().update(persona);
            
            this.write(usuarioUpdate,
                    element.getUsuario(),
                    element.getContrasenia(),
                    element.getRol().name(),
                    element.getIdUsuario());
        }
        catch (RepositoryWriteException | SQLException e)
        {
            throw new RepositoryWriteException("Error al actualizar usuario: " + e.getMessage());
        }
    }
    
    @Override
    public void delete(Usuario element) throws RepositoryWriteException
    {
        String query = "DELETE FROM usuario WHERE id_usuario = ?";

        try
        {
            this.write(query, element.getIdUsuario());
        }
        catch (RepositoryWriteException | SQLException e)
        {
            throw new RepositoryWriteException("Error al eliminar usuario: " + e.getMessage());
        }
    }
}
