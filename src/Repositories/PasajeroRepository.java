package Repositories;

import Database.DatabaseConnection;
import Exceptions.RepositoryWriteException;
import Models.Pasajero;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;

public class PasajeroRepository extends GenericRepository<Pasajero>{

    @Override
    protected Pasajero create(ResultSet rs) throws SQLException {

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
    public Pasajero insert(Pasajero pasajero) throws RepositoryWriteException {
        String queryPersona = "INSERT INTO persona" +
                "(dni, nombre, origen, domicilio) " +
                "VALUES (?, ?, ?, ?)";

        String queryPasajero = "INSERT INTO pasajero " +
                "(id_persona) VALUES (?)";


        try{
            int idPersona = write(
                    queryPersona,
                    pasajero.getDni(),
                    pasajero.getNombre(),
                    pasajero.getOrigen(),
                    pasajero.getDomicilio()
            );

            int idPasajero = write(queryPasajero, idPersona);

            return new Pasajero(
                    idPersona,
                    pasajero.getDni(),
                    pasajero.getNombre(),
                    pasajero.getOrigen(),
                    pasajero.getDomicilio(),
                    idPasajero,
                    pasajero.getReservas(),
                    pasajero.getOcupaciones()
            );
        }catch (SQLException e) {
            throw new RepositoryWriteException(
                    "Error al insertar pasajero: " + e.getMessage()
            );
        }catch (RepositoryWriteException e) {
            throw new RuntimeException(
                    "Error de escritura al insertar pasajero: " + e.getMessage()
            );
        }
    }

    @Override
    public Set<Pasajero> selectAll() {

        Set<Pasajero> pasajeros = new HashSet<>();

        String query = "SELECT p.id_persona, p.dni, p.nombre, " +
                "p.origen, p.domicilio, pa.id_pasajero " +
                "FROM pasajero pa " +
                "INNER JOIN persona p ON pa.id_persona = p.id_persona";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Pasajero pasajero = create(rs);
                pasajeros.add(pasajero);
            }

        } catch (SQLException e) {
            throw new RuntimeException(
                    "Error al consultar los pasajeros: " + e.getMessage(), e
            );
        }

        return pasajeros;
    }

    @Override
    public void update(Pasajero pasajero) throws RepositoryWriteException {

        String query = "UPDATE persona " +
                "SET dni = ?, nombre = ?, origen = ?, domicilio = ? " +
                "WHERE id_persona = ?";

        try {
            write(
                    query,
                    pasajero.getDni(),
                    pasajero.getNombre(),
                    pasajero.getOrigen(),
                    pasajero.getDomicilio(),
                    pasajero.getIdPersona()
            );

        } catch (SQLException e) {
            throw new RepositoryWriteException(
                    "Error al actualizar pasajero: " + e.getMessage()
            );
        }catch (RepositoryWriteException e) {
            throw new RuntimeException(
                    "Error de escritura al insertar pasajero: " + e.getMessage()
            );
        }
    }

    @Override
    public void delete(Pasajero pasajero) throws RepositoryWriteException {

        String queryPasajero = "DELETE FROM pasajero " +
                "WHERE id_pasajero = ?";

        String queryPersona = "DELETE FROM persona " +
                "WHERE id_persona = ?";

        try {
            write(queryPasajero, pasajero.getIdPasajero());

            write(queryPersona, pasajero.getIdPersona());

        } catch (SQLException e) {
            throw new RepositoryWriteException(
                    "Error al eliminar pasajero: " + e.getMessage()
            );
        }catch (RepositoryWriteException e) {
            throw new RuntimeException(
                    "Error de escritura al insertar pasajero: " + e.getMessage()
            );
        }
    }
}
