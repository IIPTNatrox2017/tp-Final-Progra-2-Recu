package Repositories;

import Exceptions.RepositoryWriteException;
import Models.Persona;

import Database.DatabaseConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;

public class PersonaRepository extends GenericRepository<Persona>
{
    @Override
    protected Persona create(ResultSet rs) throws SQLException
    {
        return new Persona(
                rs.getInt("id_persona"),
                rs.getString("dni"),
                rs.getString("nombre"),
                rs.getString("origen"),
                rs.getString("domicilio")
        ) {};
    }

    @Override
    public Persona insert(Persona element) throws RepositoryWriteException
    {
        String query = "INSERT INTO persona (dni, nombre, origen, domicilio) VALUES (?, ?, ?, ?)";

        try
        {
            int idPersona = this.write(query,
                    element.getDni(),
                    element.getNombre(),
                    element.getOrigen(),
                    element.getDomicilio());

            return new Persona(idPersona, element.getDni(), element.getNombre(),
                    element.getOrigen(), element.getDomicilio()) {};
        }
        catch (RepositoryWriteException | SQLException e)
        {
            throw new RepositoryWriteException("Error al insertar persona: " + e.getMessage());
        }
    }

    @Override
    public Set<Persona> selectAll()
    {
        Set<Persona> personas = new HashSet<>();
        
        String query = "SELECT id_persona, dni, nombre, origen, domicilio FROM persona";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query);
             ResultSet rs = statement.executeQuery())
        {
            while (rs.next())
            {
                personas.add(create(rs));
            }
        }
        catch (SQLException e)
        {
            throw new RuntimeException("Error al consultar las personas: " + e.getMessage());
        }

        return personas;
    }

    @Override
    public void update(Persona element) throws RepositoryWriteException
    {
        String query = "UPDATE persona SET dni = ?, nombre = ?, origen = ?, domicilio = ? " +
                "WHERE id_persona = ?";

        try
        {
            this.write(query,
                    element.getDni(),
                    element.getNombre(),
                    element.getOrigen(),
                    element.getDomicilio(),
                    element.getIdPersona());
        }
        catch (RepositoryWriteException | SQLException e)
        {
            throw new RepositoryWriteException("Error al actualizar persona: " + e.getMessage());
        }
    }

    @Override
    public void delete(Persona element) throws RepositoryWriteException
    {
        String query = "DELETE FROM persona WHERE id_persona = ?";

        try
        {
            this.write(query, element.getIdPersona());
        }
        catch (RepositoryWriteException | SQLException e)
        {
            throw new RepositoryWriteException("Error al eliminar persona: " + e.getMessage());
        }
    }
}
