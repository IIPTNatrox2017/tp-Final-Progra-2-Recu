package Enums;

public enum MenuKey
{
    LOGIN("login"),
    CREAR_ADMIN("crear_admin"),
    PRINCIPAL_ADMIN("principal_admin"),
    PRINCIPAL_RECEPCIONISTA("principal_recepcionista"),
    USUARIOS("usuarios"),
    HABITACIONES("habitaciones"),
    PERSONAS("personas"),
    PASAJEROS("pasajeros"),
    RESERVAS("reservas"),
    OCUPACIONES("ocupaciones"),
    EDIT_USUARIO("editar_usuario"),
    EDIT_HABITACION("editar_habitacion"),
    EDIT_PERSONA("editar_persona"),
    EDIT_PASAJERO("editar_pasajero"),
    EDIT_RESERVA("editar_reserva"),
    EDIT_OCUPACION("editar_ocupacion"),
    ESTADOS_HABITACION("estados_habitacion");

    private final String value;

    MenuKey(String value)
    {
        this.value = value;
    }

    public String getValue()
    {
        return this.value;
    }
}
