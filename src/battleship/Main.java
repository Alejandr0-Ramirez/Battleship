package battleship;

import java.util.Scanner;

public class Main {
    private static Scanner scanner = new Scanner(System.in);
    private static Battleship battleship = new Battleship();
    
    public static void main(String[] args) {
        mostrarMenuInicio();
    }
    
    // MENU DE INICIO Pruba JIJIJIJI
    private static void mostrarMenuInicio() {
        boolean salir = false;
        
        while (!salir) {
            System.out.println("\n  = BATTLESHIP DINAMICO =");
            System.out.println("    - MENU DE INICIO -");
            System.out.println("1- Login");
            System.out.println("2- Crear Player");
            System.out.println("3- Salir");
            System.out.print("\nSeleccione una opcion: ");
            int opcion = obtenerOpcion();
            
            switch (opcion) {
                case 1:
                    realizarLogin();
                    break;
                case 2:
                    crearPlayer();
                    break;
                case 3:
                    System.out.println("\nHasta luego!");
                    salir = true;
                    break;
                default:
                    System.out.println("Opcion invalida. Intente de nuevo.");
            }
        }       
    }
    
    private static void realizarLogin() {
        System.out.print("\nUsername: ");
        String username = scanner.nextLine();
        System.out.print("Password: ");
        String password = scanner.nextLine();
        
        if (Battleship.login(username, password)) {
            System.out.println("\nLogin exitoso! Bienvenido " + username);
            mostrarMenuPrincipal();
        } else {
            System.out.println("\nError: Usuario o contrasena incorrectos.");
        }
    }
    
    private static void crearPlayer() {
        System.out.print("\nNuevo Username: ");
        String username = scanner.nextLine();
        System.out.print("Nueva Password: ");
        String password = scanner.nextLine();
        
        if (Battleship.registrarPlayer(username, password)) {
            System.out.println("\nPlayer creado exitosamente!");
            mostrarMenuPrincipal();
        } else {
            System.out.println("\nError: El username ya existe. Intente con otro.");
        }
    }
    
    // MENU PRINCIPAL jahuohahkahkhjawoijlajljl
    private static void mostrarMenuPrincipal() {
        boolean salir = false;
        
        while (!salir) {
            System.out.println("\n   ==  MENU PRINCIPAL ==");
            System.out.println("Usuario: " + Battleship.getCurrentUser().getUsername());
            System.out.println("Puntos: " + Battleship.getCurrentUser().getPuntos());
            System.out.println("\n1- Jugar Battleship");
            System.out.println("2- Configuracion");
            System.out.println("3- Reportes");
            System.out.println("4- Mi Perfil");
            System.out.println("5- Cerrar Sesion");
            System.out.print("\nSeleccione una opcion: ");
            int opcion = obtenerOpcion();
            
            switch (opcion) {
                case 1:
                    jugarBattleship();
                    break;
                case 2:
                    mostrarMenuConfiguracion();
                    break;
                case 3:
                    mostrarMenuReportes();
                    break;
                case 4:
                    mostrarMenuPerfil();
                    break;
                case 5:
                    Battleship.cerrarSesion();
                    System.out.println("\nSesion cerrada.");
                    salir = true;
                    break;
                default:
                    System.out.println("Opcion invalida. Intente de nuevo.");
            }
        }
    }
    
    // JUGAR BATTLESHIP
    private static void jugarBattleship() {
        System.out.println("\n   == JUGAR BATTLESHIP ==");
        System.out.print("Ingrese el username del Player 2 (o EXIT para cancelar): ");
        String usernamePlayer2 = scanner.nextLine();
        
        if (usernamePlayer2.equalsIgnoreCase("EXIT")) {
            return;
        }
        
        // Validar que el jugador exista
        Player player2 = Battleship.buscarPlayerPorUsername(usernamePlayer2);
        if (player2 == null) {
            System.out.println("Error: El jugador no existe.");
            return;
        }
        
        if (player2 == Battleship.getCurrentUser()) {
            System.out.println("Error: No puedes jugar contra ti mismo.");
            return;
        }
        
        // Iniciar juego
        battleship.iniciarJuego(usernamePlayer2);
    }
    
    // MENU CONFIGURACION wsbjkaghjahukhakha
    private static void mostrarMenuConfiguracion() {
        boolean regresar = false;
        
        while (!regresar) {
            System.out.println("\n== CONFIGURACION ==");
            System.out.println("a- Dificultad (Actual: " + Battleship.getDificultad() + ")");
            System.out.println("b- Modo de Juego (Actual: " + Battleship.getModoJuego() + ")");
            System.out.println("c- Regresar al Menu Principal");
            System.out.print("\nSeleccione una opcion: ");
            String opcion = scanner.nextLine().toLowerCase();
            
            switch (opcion) {
                case "a":
                    configurarDificultad();
                    break;
                case "b":
                    configurarModoJuego();
                    break;
                case "c":
                    regresar = true;
                    break;
                default:
                    System.out.println("Opcion invalida.");
            }
        }
    }
    
    private static void configurarDificultad() {
        System.out.println("\n   = DIFICULTAD =");
        System.out.println("1- EASY (5 barcos)");
        System.out.println("2- NORMAL (4 barcos)");
        System.out.println("3- EXPERT (2 barcos)");
        System.out.println("4- GENIUS (1 barco)");
        System.out.print("\nSeleccione dificultad: ");
        int opcion = obtenerOpcion();
        String dificultad = Battleship.getDificultad();
        
        switch (opcion) {
            case 1:
                dificultad = "EASY";
                break;
            case 2:
                dificultad = "NORMAL";
                break;
            case 3:
                dificultad = "EXPERT";
                break;
            case 4:
                dificultad = "GENIUS";
                break;
            default:
                System.out.println("Opcion invalida.");
                return;
        }
        
        Battleship.setDificultad(dificultad);
        System.out.println("Dificultad cambiada a: " + dificultad);
    }
    
    private static void configurarModoJuego() {
        System.out.println("\n   = MODO DE JUEGO =");
        System.out.println("1- ARCADE (barcos ocultos)");
        System.out.println("2- TUTORIAL (barcos visibles)");
        System.out.print("\nSeleccione modo: ");
        int opcion = obtenerOpcion();
        String modo = Battleship.getModoJuego();
        
        switch (opcion) {
            case 1:
                modo = "ARCADE";
                break;
            case 2:
                modo = "TUTORIAL";
                break;
            default:
                System.out.println("Opcion invalida.");
                return;
        }
        
        Battleship.setModoJuego(modo);
        System.out.println("Modo de juego cambiado a: " + modo);
    }
    
    // MENU REPORTES
    
    private static void mostrarMenuReportes() {
        boolean regresar = false;
        
        while (!regresar) {
            System.out.println("\n        = REPORTES =      ");
            System.out.println("a- Descripcion de mis ultimos 10 juegos");
            System.out.println("b- Ranking de Jugadores");
            System.out.println("c- Regresar al Menu Principal");
            System.out.print("\nSeleccione una opcion: ");
            String opcion = scanner.nextLine().toLowerCase();
            
            switch (opcion) {
                case "a":
                    Battleship.mostrarUltimos10Juegos();
                    break;
                case "b":
                    Battleship.mostrarRankingJugadores();
                    break;
                case "c":
                    regresar = true;
                    break;
                default:
                    System.out.println("Opcion invalida.");
            }
        }
    }
    
    // MENU PERFIL 
    private static void mostrarMenuPerfil() {
        boolean regresar = false;
        
        while (!regresar) {
            System.out.println("\n        = MI PERFIL =    ");
            System.out.println("a- Ver mis Datos");
            System.out.println("b- Modificar mis Datos");
            System.out.println("c- Eliminar mi Cuenta");
            System.out.println("d- Regresar al Menu Principal");
            System.out.print("\nSeleccione una opcion: ");
            String opcion = scanner.nextLine().toLowerCase();
            
            switch (opcion) {
                case "a":
                    verMisDatos();
                    break;
                case "b":
                    modificarMisDatos();
                    break;
                case "c":
                    if (eliminarMiCuenta()) {
                        regresar = true;
                    }
                    break;
                case "d":
                    regresar = true;
                    break;
                default:
                    System.out.println("Opcion invalida.");
            }
        }
    }
    
    private static void verMisDatos() {
        Player user = Battleship.getCurrentUser();
        System.out.println("\n   = MIS DATOS =");
        System.out.println("Username: " + user.getUsername());
        System.out.println("Puntos: " + user.getPuntos());
        System.out.println();
    }
    
    private static void modificarMisDatos() {
        System.out.println("\n   = MODIFICAR DATOS =");
        System.out.print("Nuevo Username: ");
        String nuevoUsername = scanner.nextLine();
        System.out.print("Nuevo Password: ");
        String nuevoPassword = scanner.nextLine();
        
        if (Battleship.modificarDatosCurrentUser(nuevoUsername, nuevoPassword)) {
            System.out.println("Datos modificados exitosamente!");
        } else {
            System.out.println("Error: El username ya existe.");
        }
    }
    
    private static boolean eliminarMiCuenta() {
        System.out.print("\nEstas seguro de eliminar tu cuenta? (S/N): ");
        String confirmacion = scanner.nextLine().toUpperCase();
        
        if (confirmacion.equals("S")) {
            if (Battleship.eliminarCuentaCurrentUser()) {
                System.out.println("Cuenta eliminada exitosamente.");
                return true;
            }
        }
        return false;
    }
    
    private static int obtenerOpcion() {
        try {
            return Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            return -1;
        }
    }
}