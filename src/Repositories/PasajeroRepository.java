
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

        Set<Integer> reservas = obtenerReservas(idPasajero);
        Set<Integer> ocupaciones = obtenerOcupaciones(idPasajero);

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
    public Set<Pasajero> selectAll() throws RuntimeException{
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
        } catch (RepositoryWriteException | SQLException e) {
            throw new RepositoryWriteException(
                    "Error al eliminar pasajero: " + e.getMessage()
            );
        }
    }


    private Set<Integer> obtenerReservas(Integer idPasajero)
            throws SQLException {

        Set<Integer> reservas = new HashSet<>();

        String query = """
            SELECT id_reserva
            FROM pasajero_reserva
            WHERE id_pasajero = ?
            """;

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setInt(1, idPasajero);

            try (ResultSet rs = statement.executeQuery()) {

                while (rs.next()) {
                    reservas.add(rs.getInt("id_reserva"));
                }
            }
        }

        return reservas;
    }


    private Set<Integer> obtenerOcupaciones(Integer idPasajero)
            throws SQLException {

        Set<Integer> ocupaciones = new HashSet<>();

        String query = """
            SELECT id_ocupacion
            FROM ocupacion_pasajero
            WHERE id_pasajero = ?
            """;

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setInt(1, idPasajero);

            try (ResultSet rs = statement.executeQuery()) {

                while (rs.next()) {
                    ocupaciones.add(rs.getInt("id_ocupacion"));
                }
            }
        }

        return ocupaciones;
    }


    public Set<Integer> obtenerReservasAntiguas(Integer idPasajero) {

        Set<Integer> reservas = new HashSet<>();

        String query = """
            SELECT r.id_reserva
            FROM reserva r
            JOIN pasajero_reserva pr
                ON r.id_reserva = pr.id_reserva
            WHERE pr.id_pasajero = ?
              AND r.fecha_check_out <= ?
            """;

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setInt(1, idPasajero);
            statement.setDate(2, java.sql.Date.valueOf(java.time.LocalDate.now()));

            try (ResultSet rs = statement.executeQuery()) {

                while (rs.next()) {
                    reservas.add(rs.getInt("id_reserva"));
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException(
                    "Error al consultar reservas antiguas: " + e.getMessage(), e
            );
        }

        return reservas;
    }


    public Set<Integer> obtenerReservasActuales(Integer idPasajero) {

        Set<Integer> reservas = new HashSet<>();

        String query = """
            SELECT r.id_reserva
            FROM reserva r
            JOIN pasajero_reserva pr
                ON r.id_reserva = pr.id_reserva
            WHERE pr.id_pasajero = ?
              AND r.fecha_check_in <= ?
              AND r.fecha_check_out > ?
            """;

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            java.sql.Date hoy = java.sql.Date.valueOf(java.time.LocalDate.now());

            statement.setInt(1, idPasajero);
            statement.setDate(2, hoy);
            statement.setDate(3, hoy);

            try (ResultSet rs = statement.executeQuery()) {

                while (rs.next()) {
                    reservas.add(rs.getInt("id_reserva"));
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException(
                    "Error al consultar reservas actuales: " + e.getMessage(), e
            );
        }

        return reservas;
    }


    public Set<Integer> obtenerOcupacionesAntiguas(Integer idPasajero) {

        Set<Integer> ocupaciones = new HashSet<>();

        String query = """
            SELECT o.id_ocupacion
            FROM ocupacion o
            JOIN ocupacion_pasajero op
                ON o.id_ocupacion = op.id_ocupacion
            WHERE op.id_pasajero = ?
              AND o.check_out <= ?
            """;

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setInt(1, idPasajero);
            statement.setDate(2, java.sql.Date.valueOf(java.time.LocalDate.now()));

            try (ResultSet rs = statement.executeQuery()) {

                while (rs.next()) {
                    ocupaciones.add(rs.getInt("id_ocupacion"));
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException(
                    "Error al consultar ocupaciones antiguas: " + e.getMessage(), e
            );
        }

        return ocupaciones;
    }


    public Set<Integer> obtenerOcupacionesActuales(Integer idPasajero) {

        Set<Integer> ocupaciones = new HashSet<>();

        String query = """
            SELECT o.id_ocupacion
            FROM ocupacion o
            JOIN ocupacion_pasajero op
                ON o.id_ocupacion = op.id_ocupacion
            WHERE op.id_pasajero = ?
              AND o.check_in <= ?
              AND o.check_out > ?
            """;

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            java.sql.Date hoy = java.sql.Date.valueOf(java.time.LocalDate.now());

            statement.setInt(1, idPasajero);
            statement.setDate(2, hoy);
            statement.setDate(3, hoy);

            try (ResultSet rs = statement.executeQuery()) {

                while (rs.next()) {
                    ocupaciones.add(rs.getInt("id_ocupacion"));
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException(
                    "Error al consultar ocupaciones actuales: " + e.getMessage(), e
            );
        }

        return ocupaciones;
    }


}
