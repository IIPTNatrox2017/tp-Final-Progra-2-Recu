import Controllers.HabitacionesController;
import Controllers.MenuController;
import Controllers.OcupacionesController;
import Controllers.PasajerosController;
import Controllers.PersonasController;
import Controllers.ReservasController;
import Controllers.UsuariosController;
import Enums.EstadoHabitacion;
import Enums.IdConst;
import Enums.MenuConst;
import Enums.MenuKey;
import Enums.UsuarioRol;
import Exceptions.GetItemsInPageException;
import Exceptions.MenuDoNotExistsException;
import Models.*;
import Views.ScannerView;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

void main()
{
    Scanner scanner = new Scanner(System.in);
    ScannerView input = new ScannerView(scanner);
    MenuController menus = new MenuController(scanner);
    
    createMenus(menus);
    
    try
    {
        PersonasController personas = new PersonasController();
        UsuariosController usuarios = new UsuariosController(personas);
        HabitacionesController habitaciones = new HabitacionesController();
        PasajerosController pasajeros = new PasajerosController(personas);
        ReservasController reservas = new ReservasController();
        OcupacionesController ocupaciones = new OcupacionesController();
        
        if (usuarios.isEmpty())
        {
            if (createFirstAdministrator(menus, input, usuarios, personas) == false)
            {
                return;
            }
        }
        
        Usuario usuario = login(input, usuarios);
        
        if (usuario != null)
        {
            showMainMenu(menus, input, usuario, usuarios, habitaciones, personas,
                    pasajeros, reservas, ocupaciones
            );
        }
    }
    catch (RuntimeException e)
    {
        System.out.println("No se pudo iniciar la aplicación: " + e.getMessage());
    }
    finally
    {
        scanner.close();
    }
}

void createMenus(MenuController menus)
{
    menus.addMenu(MenuKey.LOGIN.getValue(), new Menu(
                    "Inicio de sesión",
                    items("Ingresar")
            )
    );
    menus.addMenu(MenuKey.CREAR_ADMIN.getValue(), new Menu(
                    "Crear administrador",
                    items("Crear administrador")
            )
    );
    menus.addMenu(MenuKey.PRINCIPAL_ADMIN.getValue(), new Menu(
                    "Menu principal - Administrativo",
                    items("Usuarios", "Habitaciones", "Personas", "Pasajeros",
                            "Reservas", "Ocupaciones"
                    )
            )
    );
    menus.addMenu(MenuKey.PRINCIPAL_RECEPCIONISTA.getValue(), new Menu(
                    "Menu principal - Recepcionista",
                    items("Pasajeros", "Habitaciones", "Reservas", "Ocupaciones")
            )
    );
    
    addCrudMenu(menus, MenuKey.USUARIOS, "Usuarios");
    addCrudMenu(menus, MenuKey.HABITACIONES, "Habitaciones");
    addCrudMenu(menus, MenuKey.PERSONAS, "Personas");
    addCrudMenu(menus, MenuKey.PASAJEROS, "Pasajeros");

    menus.addMenu(MenuKey.OCUPACIONES.getValue(), new Menu(
            "Ocupaciones",
            items(
                    "Crear ocupaciones",
                    "Modificar ocupaciones",
                    "Borrar ocupaciones",
                    "Listar ocupaciones",
                    "Listar ocupaciones del día"
            )
    ));

    menus.addMenu(MenuKey.RESERVAS.getValue(), new Menu(
            "Reservas",
            items(
                    "Crear reservas",
                    "Modificar reservas",
                    "Borrar reservas",
                    "Listar reservas",
                    "Listar reservas del día"
            )
    ));
    
    menus.addMenu(MenuKey.EDIT_USUARIO.getValue(), new Menu("Atributo del usuario",
                    items("DNI", "Nombre", "Origen", "Domicilio", "Usuario", "Contraseña")
            )
    );
    menus.addMenu(MenuKey.EDIT_HABITACION.getValue(), new Menu("Atributo de la habitación",
                    items("Habitación", "Capacidad", "Precio", "Estado")
            )
    );
    menus.addMenu(MenuKey.EDIT_PERSONA.getValue(), new Menu("Atributo de la persona",
                    items("DNI", "Nombre", "Origen", "Domicilio")
            )
    );
    menus.addMenu(MenuKey.EDIT_PASAJERO.getValue(), new Menu("Atributo del pasajero",
                    items("DNI", "Nombre", "Origen", "Domicilio")
            )
    );
    menus.addMenu(MenuKey.EDIT_RESERVA.getValue(), new Menu("Atributo de la reserva",
                    items("Habitación", "Fecha de reserva", "Check-in", "Check-out")
            )
    );
    menus.addMenu(MenuKey.EDIT_OCUPACION.getValue(), new Menu("Atributo de la ocupación",
                    items("Habitación", "Pasajero", "Check-in", "Check-out")
            )
    );
    menus.addMenu(MenuKey.ESTADOS_HABITACION.getValue(), new Menu("Estado de la habitación",
                    estadoItems()
            )
    );
}

void addCrudMenu(MenuController menus, MenuKey key, String title)
{
    menus.addMenu(key.getValue(), new Menu(title, items(
                    "Crear " + title.toLowerCase(),
                    "Modificar " + title.toLowerCase(),
                    "Borrar " + title.toLowerCase(),
                    "Listar " + title.toLowerCase()
            ))
    );
}

ArrayList<String> items(String... values)
{
    return new ArrayList<>(List.of(values));
}

ArrayList<String> estadoItems()
{
    ArrayList<String> estados = new ArrayList<>();
    for (EstadoHabitacion estado : EstadoHabitacion.values())
    {
        estados.add(estado.getDescripcion());
    }
    return estados;
}

Boolean createFirstAdministrator(MenuController menus, ScannerView input,
                                        UsuariosController usuarios, PersonasController personas
)
{
    int option = show(menus, MenuKey.CREAR_ADMIN);
    
    if (option == MenuConst.RET_EXIT.getValue())
    {
        return false;
    }

    System.out.println("No existen usuarios. Debe crear el administrador inicial.");
    
    String dni = input.getString("DNI");
    String nombre = input.getString("Nombre");
    String origen = input.getString("Origen");
    String domicilio = input.getString("Domicilio");
    String username = input.getString("Usuario");
    String password = input.getString("Contraseña");
    
    Usuario administrador = new Administrativo(IdConst.SIN_ID.getValue(), dni, nombre, origen,
            domicilio, IdConst.SIN_ID.getValue(), username, password
    );
    
    usuarios.add(administrador);
    personas.addToElementos(administrador);
    
    System.out.println("Administrador creado.");
    
    return true;
}

static Usuario login(ScannerView input, UsuariosController usuarios)
{
    while (true)
    {
        String username = input.getString("Usuario (vacio para salir)");
        
        if (username.isEmpty())
        {
            return null;
        }
        
        String password = input.getString("Contraseña");
        
        Usuario usuario = usuarios.getAll().stream()
                .filter(u -> u.getUsuario().equals(username) && u.getContrasenia().equals(password))
                .findFirst()
                .orElse(null);
        
        if (usuario != null)
        {
            return usuario;
        }
        
        System.out.println("Usuario o contraseña incorrectos.");
    }
}

void showMainMenu(MenuController menus, ScannerView input, Usuario usuario,
                         UsuariosController usuarios, HabitacionesController habitaciones,
                         PersonasController personas, PasajerosController pasajeros,
                         ReservasController reservas, OcupacionesController ocupaciones
)
{
    MenuKey key = usuario.getRol() == UsuarioRol.ADMINISTRATIVO
            ? MenuKey.PRINCIPAL_ADMIN
            : MenuKey.PRINCIPAL_RECEPCIONISTA;
    
    while (true)
    {
        int option = show(menus, key);
        
        if (option == MenuConst.RET_EXIT.getValue())
        {
            return;
        }
        
        if (usuario.getRol() == UsuarioRol.ADMINISTRATIVO)
        {
            switch (option)
            {
                case 0 -> crudUsuarios(menus, input, usuarios, personas);
                case 1 -> crudHabitaciones(menus, input, habitaciones);
                case 2 -> crudPersonas(menus, input, personas);
                case 3 -> crudPasajeros(menus, input, pasajeros, personas);
                case 4 -> crudReservas(menus, input, reservas, habitaciones);
                case 5 -> crudOcupaciones(menus, input, ocupaciones, habitaciones, pasajeros);
            }
        }
        else
        {
            switch (option)
            {
                case 0 -> crudPasajeros(menus, input, pasajeros, personas);
                case 1 -> crudHabitaciones(menus, input, habitaciones);
                case 2 -> crudReservas(menus, input, reservas, habitaciones);
                case 3 -> crudOcupaciones(menus, input, ocupaciones, habitaciones, pasajeros);
            }
        }
    }
}

int show(MenuController menus, MenuKey key)
{
    try
    {
        return menus.showMenu(key.getValue());
    }
    catch (MenuDoNotExistsException | GetItemsInPageException e)
    {
        System.out.println(e.getMessage());
        
        return MenuConst.RET_EXIT.getValue();
    }
}

void crudUsuarios(MenuController menus, ScannerView input, UsuariosController controller,
                         PersonasController personas
)
{
    while (true)
    {
        int option = show(menus, MenuKey.USUARIOS);
        
        if (option == MenuConst.RET_EXIT.getValue())
        {
            return;
        }
        
        switch (option)
        {
            case 0 -> addUsuario(input, controller, personas);
            case 1 -> updateUsuario(menus, input, controller);
            case 2 -> deleteUsuario(menus, controller);
            case 3 -> controller.getAll().forEach(System.out::println);
        }
    }
}

void addUsuario(ScannerView input, UsuariosController controller, PersonasController personas)
{
    String dni = input.getString("DNI");
    String nombre = input.getString("Nombre");
    String origen = input.getString("Origen");
    String domicilio = input.getString("Domicilio");
    String username = input.getString("Usuario");
    String password = input.getString("Contraseña");
    Integer rol = input.getInteger("1 Administrativo / 2 Recepcionista");
    
    Usuario usuario = rol == 1
            ? new Administrativo(IdConst.SIN_ID.getValue(), dni, nombre, origen, domicilio,
            IdConst.SIN_ID.getValue(), username, password
    )
            : new Recepcionista(IdConst.SIN_ID.getValue(), dni, nombre, origen, domicilio,
            IdConst.SIN_ID.getValue(), username, password
    );
    
    controller.add(usuario);
    personas.addToElementos(usuario);
}

void updateUsuario(MenuController menus, ScannerView input, UsuariosController controller)
{
    Usuario usuario = selectElement(menus, "Seleccione un usuario", controller.getAll());
    
    if (usuario == null)
    {
        return;
    }
    
    int attribute = show(menus, MenuKey.EDIT_USUARIO);
    
    if (attribute == MenuConst.RET_EXIT.getValue())
    {
        return;
    }
    
    editPersonaAttribute(input, usuario, attribute);
    
    controller.update(usuario);
}

void deleteUsuario(MenuController menus, UsuariosController controller)
{
    Usuario usuario = selectElement(menus, "Seleccione un usuario", controller.getAll());
    
    if (usuario != null)
    {
        controller.delete(usuario.getIdUsuario());
    }
}

<T> T selectElement(MenuController menus, String title, Set<T> elements)
{
    if (elements.isEmpty())
    {
        System.out.println("No hay elementos para seleccionar.");
        
        return null;
    }
    
    ArrayList<T> list = new ArrayList<>(elements);
    ArrayList<String> labels = new ArrayList<>();
    
    for (T element : list)
    {
        labels.add(element.toString());
    }
    
    int selected = menus.showMenuTemp(new Menu(title, labels));
    
    if (selected == MenuConst.RET_EXIT.getValue())
    {
        return null;
    }
    
    return list.get(selected);
}

void crudHabitaciones(MenuController menus, ScannerView input, HabitacionesController controller)
{
    while (true)
    {
        int option = show(menus, MenuKey.HABITACIONES);
        
        if (option == MenuConst.RET_EXIT.getValue())
        {
            return;
        }
        
        switch (option)
        {
            case 0 ->
            {
                EstadoHabitacion estado = selectEstadoHabitacion(menus);
                if (estado != null)
                {
                    controller.add(new Habitacion(IdConst.SIN_ID.getValue(),
                            input.getString("Habitación"), input.getInteger("Capacidad"),
                            input.getDouble("Precio"), estado
                    ));
                }
            }
            case 1 ->
            {
                Habitacion h = selectElement(menus, "Seleccione una habitación", controller.getAll());
                
                if (h != null)
                {
                    int attribute = show(menus, MenuKey.EDIT_HABITACION);
                    
                    if (attribute == MenuConst.RET_EXIT.getValue())
                    {
                        continue;
                    }
                    
                    editHabitacionAttribute(menus, input, h, attribute);
                    controller.update(h);
                }
            }
            case 2 ->
            {
                Habitacion h = selectElement(menus, "Seleccione una habitación", controller.getAll());
                
                if (h != null)
                {
                    controller.delete(h.getIdHabitacion());
                }
            }
            case 3 -> controller.getAll().forEach(System.out::println);
        }
    }
}

void crudPersonas(MenuController menus, ScannerView input, PersonasController controller)
{
    while (true)
    {
        int option = show(menus, MenuKey.PERSONAS);
        
        if (option == MenuConst.RET_EXIT.getValue())
        {
            return;
        }
        
        switch (option)
        {
            case 0 -> controller.add(new Persona(IdConst.SIN_ID.getValue(),
                    input.getString("DNI"), input.getString("Nombre"), input.getString("Origen"),
                    input.getString("Domicilio")
            ) {});
            case 1 ->
            {
                Persona p = selectElement(menus, "Seleccione una persona", controller.getAll());
                
                if (p != null)
                {
                    int attribute = show(menus, MenuKey.EDIT_PERSONA);
                    
                    if (attribute == MenuConst.RET_EXIT.getValue())
                    {
                        continue;
                    }
                    
                    editPersonaAttribute(input, p, attribute);
                    
                    controller.update(p);
                }
            }
            case 2 ->
            {
                Persona p = selectElement(menus, "Seleccione una persona", controller.getAll());
                
                if (p != null)
                {
                    controller.delete(p.getIdPersona());
                }
            }
            case 3 -> controller.getAll().forEach(System.out::println);
        }
    }
}

void editPersonaAttribute(ScannerView input, Persona persona, int attribute)
{
    switch (attribute)
    {
        case 0 -> persona.setDni(input.getString("DNI"));
        case 1 -> persona.setNombre(input.getString("Nombre"));
        case 2 -> persona.setOrigen(input.getString("Origen"));
        case 3 -> persona.setDomicilio(input.getString("Domicilio"));
        case 4 -> ((Usuario) persona).setUsuario(input.getString("Usuario"));
        case 5 -> ((Usuario) persona).setContrasenia(input.getString("Contraseña"));
    }
}

EstadoHabitacion selectEstadoHabitacion(MenuController menus)
{
    int selected = show(menus, MenuKey.ESTADOS_HABITACION);
    if (selected == MenuConst.RET_EXIT.getValue())
    {
        return null;
    }
    return EstadoHabitacion.values()[selected];
}

void editHabitacionAttribute(MenuController menus, ScannerView input,
                             Habitacion habitacion, int attribute)
{
    switch (attribute)
    {
        case 0 -> habitacion.setHabitacion(input.getString("Habitación"));
        case 1 -> habitacion.setCapacidad(input.getInteger("Capacidad"));
        case 2 -> habitacion.setPrecio(input.getDouble("Precio"));
        case 3 ->
        {
            EstadoHabitacion estado = selectEstadoHabitacion(menus);
            if (estado != null)
            {
                habitacion.setEstadoHabitacion(estado);
            }
        }
    }
}

void editReservaAttribute(MenuController menus, ScannerView input, Reserva reserva,
                                 HabitacionesController habitaciones, int attribute
)
{
    switch (attribute)
    {
        case 0 ->
        {
            for (Habitacion habitacion : reserva.getHabitaciones())
            {
                reserva.removeHabitacion(habitacion.getIdHabitacion());
            }
            
            Habitacion habitacion = selectElement(menus, "Seleccione una habitación",
                    habitaciones.getAll()
            );
            
            if (habitacion != null)
            {
                reserva.addHabitacion(habitacion);
            }
        }
        case 1 -> reserva.setFechaReserva(input.getLocalDate("Fecha de reserva"));
        case 2 -> reserva.setFechaCheckIn(input.getLocalDate("Check-in"));
        case 3 -> reserva.setFechaCheckOut(input.getLocalDate("Check-out"));
    }
}

void editOcupacionAttribute(MenuController menus, ScannerView input, Ocupacion ocupacion,
                                   HabitacionesController habitaciones,
                                   PasajerosController pasajeros, int attribute
)
{
    switch (attribute)
    {
        case 0 -> ocupacion.setHabitacion(selectElement(menus,
                "Seleccione una habitación", habitaciones.getAll()
        ));
        case 1 ->
        {
            for (Pasajero pasajero : ocupacion.getPasajeros())
            {
                ocupacion.removePasajero(pasajero.getIdPasajero());
            }
            
            Pasajero pasajero = selectElement(menus,
                    "Seleccione un pasajero", pasajeros.getAll()
            );
            
            if (pasajero != null)
            {
                ocupacion.addPasajero(pasajero);
            }
        }
        case 2 -> ocupacion.setCheckIn(input.getLocalDate("Check-in"));
        case 3 -> ocupacion.setCheckOut(input.getLocalDate("Check-out"));
    }
}

void crudPasajeros(MenuController menus, ScannerView input, PasajerosController controller,
                          PersonasController personas
)
{
    while (true)
    {
        int option = show(menus, MenuKey.PASAJEROS);
        
        if (option == MenuConst.RET_EXIT.getValue())
        {
            return;
        }
        
        switch (option)
        {
            case 0 ->
            {
                Pasajero pasajero = new Pasajero(IdConst.SIN_ID.getValue(),
                        input.getString("DNI"), input.getString("Nombre"), input.getString("Origen"),
                        input.getString("Domicilio"), IdConst.SIN_ID.getValue(),
                        new HashSet<>(), new HashSet<>()
                );
                
                controller.add(pasajero);
                personas.addToElementos(pasajero);
            }
            case 1 ->
            {
                Pasajero p = selectElement(menus, "Seleccione un pasajero", controller.getAll());
                
                if (p != null)
                {
                    int attribute = show(menus, MenuKey.EDIT_PASAJERO);
                    
                    if (attribute == MenuConst.RET_EXIT.getValue())
                    {
                        continue;
                    }
                    
                    editPersonaAttribute(input, p, attribute);
                    controller.update(p);
                }
            }
            case 2 ->
            {
                Pasajero p = selectElement(menus, "Seleccione un pasajero", controller.getAll());
                
                if (p != null)
                {
                    controller.delete(p.getIdPasajero());
                }
            }
            case 3 -> controller.getAll().forEach(System.out::println);
        }
    }
}

void crudReservas(MenuController menus, ScannerView input, ReservasController controller,
                         HabitacionesController habitaciones
)
{
    while (true)
    {
        int option = show(menus, MenuKey.RESERVAS);
        
        if (option == MenuConst.RET_EXIT.getValue())
        {
            return;
        }
        
        switch (option)
        {
            case 0 ->
            {
                Set<Habitacion> seleccionadas = new HashSet<>();
                
                Habitacion habitacion = selectElement(menus, "Seleccione una habitación",
                        habitaciones.getAll());
                
                if (habitacion != null)
                {
                    seleccionadas.add(habitacion);
                }else
                {
                    System.out.println("No se seleccionó ninguna habitación.");
                    continue;
                }
                
                controller.add(new Reserva(IdConst.SIN_ID.getValue(), seleccionadas,
                        LocalDate.now(), input.getLocalDate("Check-in"), input.getLocalDate("Check-out")
                ));
            }
            case 1 ->
            {
                Reserva r = selectElement(menus, "Seleccione una reserva", controller.getAll());
                
                if (r != null)
                {
                    int attribute = show(menus, MenuKey.EDIT_RESERVA);
                    
                    if (attribute == MenuConst.RET_EXIT.getValue())
                    {
                        continue;
                    }
                    
                    editReservaAttribute(menus, input, r, habitaciones, attribute);
                    controller.update(r);
                }
            }
            case 2 ->
            {
                Reserva r = selectElement(menus, "Seleccione una reserva", controller.getAll());
                
                if (r != null)
                {
                    controller.delete(r.getIdReserva());
                }
            }
            case 3 -> controller.getAll().forEach(System.out::println);
            case 4 ->
            {
                LocalDate fecha = input.getLocalDate("Fecha a consultar");

                Set<Reserva> reservasDelDia = controller.obtenerReservasDelDia(fecha);

                if (reservasDelDia.isEmpty())
                {
                    System.out.println("No hay reservas para esa fecha.");
                }
                else
                {
                    for (Reserva reserva : reservasDelDia)
                    {
                        System.out.println("Reserva ID: " + reserva.getIdReserva());
                        System.out.println("Check-in: " + reserva.getFechaCheckIn());
                        System.out.println("Check-out: " + reserva.getFechaCheckOut());
                        System.out.println("--------------------");
                    }
                }
            }
        }
    }
}

void crudOcupaciones(MenuController menus, ScannerView input, OcupacionesController controller,
                            HabitacionesController habitaciones, PasajerosController pasajeros
)
{
    while (true)
    {
        int option = show(menus, MenuKey.OCUPACIONES);
        
        if (option == MenuConst.RET_EXIT.getValue())
        {
            return;
        }
        
        switch (option)
        {
            case 0 ->
            {
                Habitacion habitacion = selectElement(menus, "Seleccione una habitación",
                        habitaciones.getAll());
                
                Set<Pasajero> seleccionados = new HashSet<>();
                
                Pasajero pasajero = selectElement(menus, "Seleccione un pasajero",
                        pasajeros.getAll());
                
                if (pasajero != null)
                {
                    seleccionados.add(pasajero);
                }
                
                controller.add(new Ocupacion(IdConst.SIN_ID.getValue(), habitacion, seleccionados,
                        input.getLocalDate("Check-in"), input.getLocalDate("Check-out")
                ));
            }
            case 1 ->
            {
                Ocupacion o = selectElement(menus, "Seleccione una ocupación", controller.getAll());
                
                if (o != null)
                {
                    int attribute = show(menus, MenuKey.EDIT_OCUPACION);
                    
                    if (attribute == MenuConst.RET_EXIT.getValue())
                    {
                        continue;
                    }
                    
                    editOcupacionAttribute(menus, input, o, habitaciones, pasajeros, attribute);
                    controller.update(o);
                }
            }
            case 2 ->
            {
                Ocupacion o = selectElement(menus, "Seleccione una ocupación", controller.getAll());
                
                if (o != null)
                {
                    controller.delete(o.getIdOcupacion());
                }
            }
            case 3 -> controller.getAll().forEach(System.out::println);
            case 4 ->
            {
                LocalDate fecha = input.getLocalDate("Fecha a consultar");

                Set<Ocupacion> ocupacionesDelDia = controller.obtenerOcupacionesDelDia(fecha);

                if (ocupacionesDelDia.isEmpty())
                {
                    System.out.println("No hay ocupaciones para esa fecha.");
                }
                else
                {
                    for (Ocupacion ocupacion : ocupacionesDelDia)
                    {
                        System.out.println("Ocupación ID: " + ocupacion.getIdOcupacion());
                        System.out.println("Check-in: " + ocupacion.getCheckIn());
                        System.out.println("Check-out: " + ocupacion.getCheckOut());
                        System.out.println("--------------------");
                    }
                }
            }

        }
    }
}
