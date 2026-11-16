
package Repositories;

import Database.DatabaseConnection;
import Enums.IdConst;
import Exceptions.RepositoryWriteException;
import Models.Pasajero;
import Models.Persona;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;

public class PasajeroRepository extends GenericRepository<Pasajero> {

    @Override
    protected Pasajero create(ResultSet rs) throws SQLException
    {
        Integer idPersona = rs.getInt("id_persona");
        String dni = rs.getString("dni");
        String nombre = rs.getString("nombre");
        String origen = rs.getString("origen");
        String domicilio = rs.getString("domicilio");
        Integer idPasajero = rs.getInt("id_pasajero");

        Set<Integer> reservas = new HashSet<>();
        Set<Integer> ocupaciones = new HashSet<>();

        return new Pasajero(
                idPersona,
                dni,
                nombre,
                origen,
                domicilio,
                idPasajero,
                reservas,
                ocupaciones
        );
    }

    @Override
    public Pasajero insert(Pasajero element) throws RepositoryWriteException {
        String query = "INSERT INTO pasajero (id_persona) VALUES (?)";

        try {
            int idPersona = element.getIdPersona();

            if (idPersona == IdConst.SIN_ID.getValue()) {
                Persona persona = new Persona(
                        IdConst.SIN_ID.getValue(),
                        element.getDni(),
                        element.getNombre(),
                        element.getOrigen(),
                        element.getDomicilio()
                ) {};

                idPersona = new PersonaRepository()
                        .insert(persona)
                        .getIdPersona();
            }

            int idPasajero = this.write(query, idPersona);

            return new Pasajero(
                    idPersona,
                    element.getDni(),
                    element.getNombre(),
                    element.getOrigen(),
                    element.getDomicilio(),
                    idPasajero,
                    element.getReservas(),
                    element.getOcupaciones()
            );

        } catch (RepositoryWriteException | SQLException e) {
            throw new RepositoryWriteException(
                    "Error al insertar pasajero: " + e.getMessage()
            );
        }
    }

    @Override
    public Set<Pasajero> selectAll() {
        Set<Pasajero> pasajeros = new HashSet<>();

        String query = """
                SELECT pa.id_pasajero, p.id_persona, p.dni,
                       p.nombre, p.origen, p.domicilio
                FROM pasajero pa
                JOIN persona p ON p.id_persona = pa.id_persona
                """;

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query);
             ResultSet rs = statement.executeQuery()) {

            while (rs.next()) {
                pasajeros.add(create(rs));
            }

        } catch (SQLException e) {
            throw new RuntimeException(
                    "Error al consultar los pasajeros: " + e.getMessage()
            );
        }

        return pasajeros;
    }

    @Override
    public void update(Pasajero element) throws RepositoryWriteException
    {
        try {
            Persona persona = new Persona(
                    element.getIdPersona(),
                    element.getDni(),
                    element.getNombre(),
                    element.getOrigen(),
                    element.getDomicilio()
            ) {};

            new PersonaRepository().update(persona);

        } catch (RepositoryWriteException e) {
            throw new RepositoryWriteException(
                    "Error al actualizar pasajero: " + e.getMessage()
            );
        }
    }

    @Override
    public void delete(Pasajero element) throws RepositoryWriteException {
        String query = "DELETE FROM pasajero WHERE id_pasajero = ?";

        try {
            this.write(query, element.getIdPasajero());

            Persona persona = new Persona(
                    element.getIdPersona(),
                    element.getDni(),
                    element.getNombre(),
                    element.getOrigen(),
                    element.getDomicilio()
            ) {};

            new PersonaRepository().delete(persona);

        } catch (RepositoryWriteException | SQLException e) {
            throw new RepositoryWriteException(
                    "Error al eliminar pasajero: " + e.getMessage()
            );
        }
    }
}
