package Repositories;

import Database.DatabaseConnection;
import Exceptions.RepositoryWriteException;
import Models.Habitacion;
import Models.Reserva;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import Enums.EstadoHabitacion;

public class ReservaRepository extends GenericRepository<Reserva> {

    @Override
    protected Reserva create(ResultSet rs) throws SQLException {

        Integer idReserva = rs.getInt("id_reserva");

        Set<Habitacion> habitaciones = obtenerHabitaciones(idReserva);

        return new Reserva(
                idReserva,
                habitaciones,
                rs.getDate("fecha_reserva").toLocalDate(),
                rs.getDate("fecha_check_in").toLocalDate(),
                rs.getDate("fecha_check_out").toLocalDate()
        );
    }


    private Set<Habitacion> obtenerHabitaciones(Integer idReserva)
            throws SQLException {

        Set<Habitacion> habitaciones = new HashSet<>();

        String query = """
            SELECT h.*
            FROM habitacion h
            JOIN reserva_habitacion rh
                ON h.id_habitacion = rh.id_habitacion
            WHERE rh.id_reserva = ?
            """;

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setInt(1, idReserva);

            try (ResultSet rs = statement.executeQuery()) {

                while (rs.next()) {

                    Habitacion habitacion = new Habitacion(
                            rs.getInt("id_habitacion"),
                            rs.getString("habitacion"),
                            rs.getInt("capacidad"),
                            rs.getDouble("precio"),
                            EstadoHabitacion.valueOf(
                                    rs.getString("estado_habitacion")
                            )
                    );

                    habitaciones.add(habitacion);
                }
            }
        }

        return habitaciones;
    }

    @Override
    public Reserva insert(Reserva element) throws RepositoryWriteException {

        String query = "INSERT INTO reserva " +
                "(fecha_reserva, fecha_check_in, fecha_check_out) " +
                "VALUES (?, ?, ?)";

        try {
            int idReserva = this.write(
                    query,
                    element.getFechaReserva(),
                    element.getFechaCheckIn(),
                    element.getFechaCheckOut()
            );

            return new Reserva(
                    idReserva,
                    new HashSet<>(),
                    element.getFechaReserva(),
                    element.getFechaCheckIn(),
                    element.getFechaCheckOut()
            );

        } catch (RepositoryWriteException | SQLException e) {
            throw new RepositoryWriteException(
                    "Error al insertar reserva: " + e.getMessage()
            );
        }
    }


    @Override
    public Set<Reserva> selectAll() throws RuntimeException {

        Set<Reserva> reservas = new HashSet<>();

        String query = "SELECT * FROM reserva";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query);
             ResultSet rs = statement.executeQuery()) {

            while (rs.next()) {
                reservas.add(create(rs));
            }

        } catch (SQLException e) {
            throw new RuntimeException(
                    "Error al consultar las reservas: " + e.getMessage(),
                    e
            );
        }

        return reservas;
    }


    @Override
    public void update(Reserva element) throws RepositoryWriteException {

        String query = "UPDATE reserva SET fecha_reserva = ?, " +
                "fecha_check_in = ?, fecha_check_out = ? " +
                "WHERE id_reserva = ?";

        try {
            this.write(
                    query,
                    element.getFechaReserva(),
                    element.getFechaCheckIn(),
                    element.getFechaCheckOut(),
                    element.getIdReserva()
            );

        } catch (RepositoryWriteException | SQLException e) {
            throw new RepositoryWriteException(
                    "Error al actualizar reserva: " + e.getMessage()
            );
        }
    }


    @Override
    public void delete(Reserva element) throws RepositoryWriteException {

        String query = "DELETE FROM reserva WHERE id_reserva = ?";

        try {
            this.write(query, element.getIdReserva());

        } catch (RepositoryWriteException | SQLException e) {
            throw new RepositoryWriteException(
                    "Error al eliminar reserva: " + e.getMessage()
            );
        }
    }


    public void insertRelacionPasajero(Integer idReserva, Integer idPasajero) throws RepositoryWriteException {

        String query = "INSERT INTO pasajero_reserva " +
                "(id_pasajero, id_reserva) VALUES (?, ?)";

        try {
            this.write(query, idPasajero, idReserva);

        } catch (RepositoryWriteException | SQLException e) {
            throw new RepositoryWriteException(
                    "Error al relacionar pasajero con reserva: " + e.getMessage()
            );
        }
    }


    public void deleteRelacionPasajero(Integer idReserva, Integer idPasajero)
            throws RepositoryWriteException {

        String query = "DELETE FROM pasajero_reserva " +
                "WHERE id_pasajero = ? AND id_reserva = ?";

        try {
            this.write(query, idPasajero, idReserva);

        } catch (RepositoryWriteException | SQLException e) {
            throw new RepositoryWriteException(
                    "Error al eliminar relación entre pasajero y reserva: " + e.getMessage()
            );
        }
    }


    public void insertRelacionHabitacion(Integer idReserva, Integer idHabitacion)
            throws RepositoryWriteException {

        String query = "INSERT INTO reserva_habitacion " +
                "(id_reserva, id_habitacion) VALUES (?, ?)";

        try {
            this.write(query, idReserva, idHabitacion);

        } catch (RepositoryWriteException | SQLException e) {
            throw new RepositoryWriteException(
                    "Error al relacionar habitación con reserva: " + e.getMessage()
            );
        }
    }


    public void deleteRelacionHabitacion(Integer idReserva, Integer idHabitacion)
            throws RepositoryWriteException {

        String query = "DELETE FROM reserva_habitacion " +
                "WHERE id_reserva = ? AND id_habitacion = ?";

        try {
            this.write(query, idReserva, idHabitacion);

        } catch (RepositoryWriteException | SQLException e) {
            throw new RepositoryWriteException(
                    "Error al eliminar relación entre reserva y habitación: " + e.getMessage()
            );
        }
    }

}
