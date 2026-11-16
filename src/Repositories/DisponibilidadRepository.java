package Repositories;

import Database.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Collection;

final class DisponibilidadRepository
{
    private DisponibilidadRepository()
    {
    }

    static boolean habitacionesDisponibles(Collection<Integer> idsHabitacion,
                                           LocalDate checkIn, LocalDate checkOut,
                                           Integer idReservaExcluir,
                                           Integer idOcupacionExcluir) throws SQLException
    {
        try (Connection connection = DatabaseConnection.getConnection())
        {
            for (Integer idHabitacion : idsHabitacion)
            {
                if (hayReservaSolapada(connection, idHabitacion, checkIn, checkOut,
                        idReservaExcluir))
                {
                    return true;
                }

                if (hayOcupacionSolapada(connection, idHabitacion, checkIn, checkOut,
                        idOcupacionExcluir))
                {
                    return true;
                }
            }

            return false;
        }
    }

    private static boolean hayReservaSolapada(Connection connection, Integer idHabitacion,
                                               LocalDate checkIn, LocalDate checkOut,
                                               Integer idReservaExcluir) throws SQLException
    {
        String query;
        
        if (idReservaExcluir == null)
        {
            query = """
                    SELECT 1
                    FROM reserva_habitacion rh
                    JOIN reserva r ON r.id_reserva = rh.id_reserva
                    WHERE rh.id_habitacion = ?
                      AND r.fecha_check_in < ?
                      AND r.fecha_check_out > ?
                    LIMIT 1
                    """;
        }
        else
        {
            query = """
                    SELECT 1
                    FROM reserva_habitacion rh
                    JOIN reserva r ON r.id_reserva = rh.id_reserva
                    WHERE rh.id_habitacion = ?
                      AND r.fecha_check_in < ?
                      AND r.fecha_check_out > ?
                      AND r.id_reserva <> ?
                    LIMIT 1
                    """;
        }

        try (PreparedStatement statement = connection.prepareStatement(query))
        {
            statement.setInt(1, idHabitacion);
            statement.setDate(2, java.sql.Date.valueOf(checkOut));
            statement.setDate(3, java.sql.Date.valueOf(checkIn));
            
            if (idReservaExcluir != null)
            {
                statement.setInt(4, idReservaExcluir);
            }

            try (ResultSet resultSet = statement.executeQuery())
            {
                return resultSet.next();
            }
        }
    }

    private static boolean hayOcupacionSolapada(Connection connection, Integer idHabitacion,
                                                 LocalDate checkIn, LocalDate checkOut,
                                                 Integer idOcupacionExcluir) throws SQLException
    {
        String query;
        
        if (idOcupacionExcluir == null)
        {
            query = """
                    SELECT 1
                    FROM ocupacion
                    WHERE id_habitacion = ?
                      AND check_in < ?
                      AND check_out > ?
                    LIMIT 1
                    """;
        }
        else
        {
            query = """
                    SELECT 1
                    FROM ocupacion
                    WHERE id_habitacion = ?
                      AND check_in < ?
                      AND check_out > ?
                      AND id_ocupacion <> ?
                    LIMIT 1
                    """;
        }

        try (PreparedStatement statement = connection.prepareStatement(query))
        {
            statement.setInt(1, idHabitacion);
            statement.setDate(2, java.sql.Date.valueOf(checkOut));
            statement.setDate(3, java.sql.Date.valueOf(checkIn));
            
            if (idOcupacionExcluir != null)
            {
                statement.setInt(4, idOcupacionExcluir);
            }

            try (ResultSet resultSet = statement.executeQuery())
            {
                return resultSet.next();
            }
        }
    }
}
