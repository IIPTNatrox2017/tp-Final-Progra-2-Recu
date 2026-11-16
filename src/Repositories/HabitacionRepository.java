package Repositories;

import Database.DatabaseConnection;
import Enums.EstadoHabitacion;
import Exceptions.RepositoryWriteException;
import Models.Habitacion;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;

public class HabitacionRepository extends GenericRepository<Habitacion> {

    @Override
    protected Habitacion create(ResultSet rs) throws SQLException {
        Integer idHabitacion = rs.getInt("id_habitacion");
        String habitacion = rs.getString("habitacion");
        Integer capacidad = rs.getInt("capacidad");
        Double precio = rs.getDouble("precio");
        String estado = rs.getString("estado_habitacion");

        EstadoHabitacion estadoHabitacion = EstadoHabitacion.valueOf(rs.getString("estado_habitacion"));

        return new Habitacion(idHabitacion, habitacion, capacidad, precio, estadoHabitacion);
    }

    @Override
    public Habitacion insert(Habitacion habitacion) throws RepositoryWriteException {

        String query = "INSERT INTO habitacion " +
                "(habitacion, capacidad, precio, estado_habitacion) " +
                "VALUES (?, ?, ?, ?)";

        try{
            int idGenerado = write(query,
                    habitacion.getHabitacion(),
                    habitacion.getCapacidad(),
                    habitacion.getPrecio(),
                    habitacion.getEstadoHabitacion().name()
            );

            return new Habitacion(
                    idGenerado,
                    habitacion.getHabitacion(),
                    habitacion.getCapacidad(),
                    habitacion.getPrecio(),
                    habitacion.getEstadoHabitacion()
            );

        }catch(SQLException e){
            throw new RepositoryWriteException("Error al insertar habitacion: " + e.getMessage());
        }catch (RepositoryWriteException e) {
            throw new RuntimeException(
                    "Error de escritura al insertar habitacion: " + e.getMessage()
            );
        }
    }

    @Override
    public Set<Habitacion> selectAll() throws RuntimeException {

        Set<Habitacion> habitaciones = new HashSet<>();

        String query = "SELECT * FROM habitacion";

        try(Connection conn = DatabaseConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(query); ResultSet rs = ps.executeQuery();){

            while(rs.next()){
                Habitacion habitacion = create(rs);
                habitaciones.add(habitacion);
            }


        }catch (SQLException e){
            throw new RuntimeException("Error al consultar las habitaciones: " + e.getMessage());
        }

        return habitaciones;
    }

    @Override
    public void update(Habitacion habitacion) throws RepositoryWriteException{

        String query = "UPDATE habitacion " +
                "SET habitacion = ?, capacidad = ?, precio = ?, estado_habitacion = ? " +
                "WHERE id_habitacion = ?";

        try {
            write(query,
                    habitacion.getHabitacion(),
                    habitacion.getCapacidad(),
                    habitacion.getPrecio(),
                    habitacion.getEstadoHabitacion().name(),
                    habitacion.getIdHabitacion()
            );

        }catch (SQLException e) {
            throw new RepositoryWriteException(
                    "Error al actualizar habitacion: " + e.getMessage()
            );
        }catch (RepositoryWriteException e) {
            throw new RuntimeException(
                    "Error de escritura al insertar habitacion: " + e.getMessage()
            );
        }
    }

    @Override
    public void delete(Habitacion habitacion) throws RepositoryWriteException{
        String query = "DELETE FROM habitacion WHERE id_habitacion = ?";

        try {
            write(query, habitacion.getIdHabitacion());

        } catch (SQLException e) {
            throw new RepositoryWriteException(
                    "Error al eliminar habitacion: " + e.getMessage()
            );
        }catch (RepositoryWriteException e) {
            throw new RuntimeException(
                    "Error de escritura al insertar habitacion: " + e.getMessage()
            );
        }
    }
}
