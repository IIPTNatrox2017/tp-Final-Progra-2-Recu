package Repositories;

import Exceptions.RepositoryWriteException;
import Models.Ocupacion;
import Models.Habitacion;
import Models.Pasajero;
import Enums.EstadoHabitacion;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import Database.DatabaseConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;

public class OcupacionRepository extends GenericRepository<Ocupacion>{



    @Override
    protected Ocupacion create(ResultSet rs) throws SQLException {

        Integer idOcupacion = rs.getInt("id_ocupacion");

        Habitacion habitacion = new Habitacion(
                rs.getInt("id_habitacion"),
                rs.getString("habitacion"),
                rs.getInt("capacidad"),
                rs.getDouble("precio"),
                EstadoHabitacion.valueOf(rs.getString("estado_habitacion"))
        );

        LocalDate checkIn = rs.getDate("check_in").toLocalDate();
        LocalDate checkOut = rs.getDate("check_out").toLocalDate();

        Set<Pasajero> pasajeros = new HashSet<>();

        String query = """
            SELECT p.id_persona, p.dni, p.nombre,
                   p.origen, p.domicilio, pa.id_pasajero
            FROM ocupacion_pasajero op
            JOIN pasajero pa ON pa.id_pasajero = op.id_pasajero
            JOIN persona p ON p.id_persona = pa.id_persona
            WHERE op.id_ocupacion = ?
            """;

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setInt(1, idOcupacion);

            try (ResultSet rsPasajeros = statement.executeQuery()) {

                while (rsPasajeros.next()) {

                    Pasajero pasajero = new Pasajero(
                            rsPasajeros.getInt("id_persona"),
                            rsPasajeros.getString("dni"),
                            rsPasajeros.getString("nombre"),
                            rsPasajeros.getString("origen"),
                            rsPasajeros.getString("domicilio"),
                            rsPasajeros.getInt("id_pasajero"),
                            new HashSet<>(),
                            new HashSet<>()
                    );

                    pasajeros.add(pasajero);
                }
            }
        }

        return new Ocupacion(
                idOcupacion,
                habitacion,
                pasajeros,
                checkIn,
                checkOut
        );
    }


    @Override
    public Ocupacion insert(Ocupacion element) throws RepositoryWriteException {

        String query = "INSERT INTO ocupacion " +
                "(id_habitacion, check_in, check_out) " +
                "VALUES (?, ?, ?)";

        try {
            int idOcupacion = this.write(
                    query,
                    element.getHabitacion().getIdHabitacion(),
                    element.getCheckIn(),
                    element.getCheckOut()
            );

            return new Ocupacion(
                    idOcupacion,
                    element.getHabitacion(),
                    new HashSet<>(element.getPasajeros()),
                    element.getCheckIn(),
                    element.getCheckOut()
            );

        } catch (RepositoryWriteException | SQLException e) {
            throw new RepositoryWriteException(
                    "Error al insertar ocupación: " + e.getMessage()
            );
        }
    }



    @Override
    public Set<Ocupacion> selectAll() throws RuntimeException {

        Set<Ocupacion> ocupaciones = new HashSet<>();

        String query = """
            SELECT o.id_ocupacion, o.id_habitacion,
                   o.check_in, o.check_out,
                   h.habitacion, h.capacidad, h.precio,
                   h.estado_habitacion
            FROM ocupacion o
            JOIN habitacion h ON h.id_habitacion = o.id_habitacion
            """;

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query);
             ResultSet rs = statement.executeQuery()) {

            while (rs.next()) {
                ocupaciones.add(create(rs));
            }

        } catch (SQLException e) {
            throw new RuntimeException(
                    "Error al consultar las ocupaciones: " + e.getMessage()
            );
        }

        return ocupaciones;
    }



    @Override
    public void update(Ocupacion element) throws RepositoryWriteException {

        String query = "UPDATE ocupacion SET id_habitacion = ?, " +
                "check_in = ?, check_out = ? " +
                "WHERE id_ocupacion = ?";

        try {
            this.write(
                    query,
                    element.getHabitacion().getIdHabitacion(),
                    element.getCheckIn(),
                    element.getCheckOut(),
                    element.getIdOcupacion()
            );

        } catch (RepositoryWriteException | SQLException e) {
            throw new RepositoryWriteException(
                    "Error al actualizar ocupación: " + e.getMessage()
            );
        }
    }



    @Override
    public void delete(Ocupacion element) throws RepositoryWriteException {

        String query = "DELETE FROM ocupacion WHERE id_ocupacion = ?";

        try {
            this.write(query, element.getIdOcupacion());

        } catch (RepositoryWriteException | SQLException e) {
            throw new RepositoryWriteException(
                    "Error al eliminar ocupación: " + e.getMessage()
            );
        }
    }


    public void insertRelacionPasajero(Integer idOcupacion, Integer idPasajero)
            throws RepositoryWriteException {

        String query = "INSERT INTO ocupacion_pasajero " +
                "(id_ocupacion, id_pasajero) VALUES (?, ?)";

        try {
            this.write(query, idOcupacion, idPasajero);

        } catch (RepositoryWriteException | SQLException e) {
            throw new RepositoryWriteException(
                    "Error al relacionar pasajero con ocupación: " + e.getMessage()
            );
        }
    }

    public void deleteRelacionPasajero(Integer idOcupacion, Integer idPasajero)
            throws RepositoryWriteException {

        String query = "DELETE FROM ocupacion_pasajero " +
                "WHERE id_ocupacion = ? AND id_pasajero = ?";

        try {
            this.write(query, idOcupacion, idPasajero);

        } catch (RepositoryWriteException | SQLException e) {
            throw new RepositoryWriteException(
                    "Error al eliminar relación entre ocupación y pasajero: " + e.getMessage()
            );
        }
    }

}
