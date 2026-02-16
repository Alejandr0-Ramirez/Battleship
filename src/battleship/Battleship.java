package battleship;

import java.util.Random;
import java.util.Scanner;

public class Battleship {
    // Coleccion de jugadores
    private static Player[] players = new Player[100];
    private static int playerCount = 0;
    
    // Usuario actual logueado
    private static Player currentUser = null;
    
    // Configuracion del juego
    private static String dificultad = "NORMAL"; // por defecto NORMAL
    private static String modoJuego = "TUTORIAL"; // por defecto TUTORIAL
    
    // Tableros de juego en 8x8
    private String[][] tableroPlayer1;
    private String[][] tableroPlayer2;
    
    // Tableros con barcos ocultos
    private String[][] barcosPlayer1;
    private String[][] barcosPlayer2;
    
    // Estado de los barcos
    private int[][] estadoBarcosPlayer1;
    private int[][] estadoBarcosPlayer2;
    
    // Informacion de barcos
    private String[] tiposBarcosP1;
    private String[] tiposBarcosP2;
    private int[] vidasBarcosP1;
    private int[] vidasBarcosP2;
    
    // Contadores de barcos
    private int barcosRestantesP1;
    private int barcosRestantesP2;
    
    // Random para regeneracion
    private Random random;
    private Scanner scanner;
    
    // Constructor
    public Battleship() {
        random = new Random();
        scanner = new Scanner(System.in);
    }
    
    // GESTION DE JUGADORES
    
    public static boolean registrarPlayer(String username, String password) {
        // validar que el username sea unico
        if (buscarPlayerPorUsername(username) != null) {
            return false;
        }
        
        Player nuevoPlayer = new Player(username, password);
        players[playerCount] = nuevoPlayer;
        playerCount++;
        currentUser = nuevoPlayer;
        return true;
    }
    
    public static boolean login(String username, String password) {
        Player player = buscarPlayerPorUsername(username);
        if (player != null && player.validarPassword(password)) {
            currentUser = player;
            return true;
        }
        return false;
    }
    
    public static Player buscarPlayerPorUsername(String username) {
        for (int i = 0; i < playerCount; i++) {
            if (players[i].getUsername().equals(username)) {
                return players[i];
            }
        }
        return null;
    }
    
    public static void cerrarSesion() {
        currentUser = null;
    }
    
    public static Player getCurrentUser() {
        return currentUser;
    }
    
    public static boolean modificarDatosCurrentUser(String nuevoUsername, String nuevoPassword) {
        if (currentUser == null) return false;
        
        // si cambia el username validar que sea unico
        if (!currentUser.getUsername().equals(nuevoUsername)) {
            if (buscarPlayerPorUsername(nuevoUsername) != null) {
                return false;
            }
        }
        
        currentUser.setUsername(nuevoUsername);
        currentUser.setPassword(nuevoPassword);
        return true;
    }
    
    public static boolean eliminarCuentaCurrentUser() {
        if (currentUser == null) return false;
        
        // buscar y eliminar el player
        for (int i = 0; i < playerCount; i++) {
            if (players[i] == currentUser) {
                // mover todos los players una posicion hacia atras
                for (int j = i; j < playerCount - 1; j++) {
                    players[j] = players[j + 1];
                }
                players[playerCount - 1] = null;
                playerCount--;
                currentUser = null;
                return true;
            }
        }
        return false;
    }
    
    // CONFIGURACION
    
    public static void setDificultad(String nuevaDificultad) {
        dificultad = nuevaDificultad;
    }
    
    public static String getDificultad() {
        return dificultad;
    }
    
    public static void setModoJuego(String nuevoModo) {
        modoJuego = nuevoModo;
    }
    
    public static String getModoJuego() {
        return modoJuego;
    }
    
    public static int getCantidadBarcosPorDificultad() {
        switch (dificultad) {
            case "EASY":
                return 5;
            case "NORMAL":
                return 4;
            case "EXPERT":
                return 2;
            case "GENIUS":
                return 1;
            default:
                return 4;
        }
    }
    
    // REPORTES
    
    public static void mostrarUltimos10Juegos() {
        if (currentUser == null) return;
        
        System.out.println("\n   = ULTIMOS 10 JUEGOS =");
        String[] logs = currentUser.getLogs();
        int count = currentUser.getLogCount();
        
        if (count == 0) {
            System.out.println("No hay juegos registrados aun.");
        } else {
            for (int i = 0; i < count; i++) {
                System.out.println((i + 1) + "- " + logs[i]);
            }
        }
        System.out.println();
    }
    
    public static void mostrarRankingJugadores() {
        if (playerCount == 0) {
            System.out.println("No hay jugadores registrados.");
            return;
        }
        
        // Ordenar jugadores por puntos
        Player[] jugadoresOrdenados = new Player[playerCount];
        for (int i = 0; i < playerCount; i++) {
            jugadoresOrdenados[i] = players[i];
        }
        
        // Ordenamiento descendente por puntos
        for (int i = 0; i < playerCount - 1; i++) {
            for (int j = 0; j < playerCount - i - 1; j++) {
                if (jugadoresOrdenados[j].getPuntos() < jugadoresOrdenados[j + 1].getPuntos()) {
                    Player temp = jugadoresOrdenados[j];
                    jugadoresOrdenados[j] = jugadoresOrdenados[j + 1];
                    jugadoresOrdenados[j + 1] = temp;
                }
            }
        }
        
        System.out.println("\n===== RANKING DE JUGADORES =====");
        System.out.println("Posicion | Username | Puntos");
        System.out.println("--------------------------------");
        for (int i = 0; i < playerCount; i++) {
            System.out.println((i + 1) + " | " + jugadoresOrdenados[i].getUsername() + 
                             " | " + jugadoresOrdenados[i].getPuntos());
        }
        System.out.println();
    }
    
    //  JUEGO 
    
    public void iniciarJuego(String usernamePlayer2) {
        Player player2 = buscarPlayerPorUsername(usernamePlayer2);
        if (player2 == null) {
            System.out.println("El jugador no existe.");
            return;
        }
        
        System.out.println("\n   = BATTLESHIP DINAMICO =");
        System.out.println("Player 1: " + currentUser.getUsername());
        System.out.println("Player 2: " + player2.getUsername());
        System.out.println("Dificultad: " + dificultad);
        System.out.println("Modo: " + modoJuego);
        System.out.println();
        
        // Inicializar tableros
        inicializarTableros();
        
        int cantidadBarcos = getCantidadBarcosPorDificultad();
        
        // Player 1 coloca sus barcos
        System.out.println("\n" + currentUser.getUsername() + ", coloca tus barcos:");
        colocarBarcos(1, cantidadBarcos);
        
        // espacios para ocultar los barcos del Player 1
        imprimirEspacios(30);
        
        // Player 2 coloca sus barcos
        System.out.println("\n" + player2.getUsername() + ", coloca tus barcos:");
        colocarBarcos(2, cantidadBarcos);
        
        // espacios para ocultar los barcos del Player 2
        imprimirEspacios(30);
        
        // Iniciar partida
        jugarPartida(player2);
    }
    
    // Metodo para imprimir espacios en blanco
    private void imprimirEspacios(int cantidad) {
        for (int i = 0; i < cantidad; i++) {
            System.out.println();
        }
    }
    
    private void inicializarTableros() {
        tableroPlayer1 = new String[8][8];
        tableroPlayer2 = new String[8][8];
        barcosPlayer1 = new String[8][8];
        barcosPlayer2 = new String[8][8];
        estadoBarcosPlayer1 = new int[8][8];
        estadoBarcosPlayer2 = new int[8][8];
        
        int cantidadBarcos = getCantidadBarcosPorDificultad();
        tiposBarcosP1 = new String[cantidadBarcos];
        tiposBarcosP2 = new String[cantidadBarcos];
        vidasBarcosP1 = new int[cantidadBarcos];
        vidasBarcosP2 = new int[cantidadBarcos];
        
        barcosRestantesP1 = cantidadBarcos;
        barcosRestantesP2 = cantidadBarcos;
        
        // Inicializar con agua (~)
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                tableroPlayer1[i][j] = "~";
                tableroPlayer2[i][j] = "~";
                barcosPlayer1[i][j] = "~";
                barcosPlayer2[i][j] = "~";
                estadoBarcosPlayer1[i][j] = -1;
                estadoBarcosPlayer2[i][j] = -1;
            }
        }
    }
    
    private void colocarBarcos(int jugador, int cantidadBarcos) {
        String[] barcosUsados = new String[cantidadBarcos];
        int barcosColocados = 0;
        
        while (barcosColocados < cantidadBarcos) {
            System.out.println("\nBarco " + (barcosColocados + 1) + " de " + cantidadBarcos);
            System.out.println("Tipos disponibles: PA (Portaaviones-5), AZ (Acorazado-4), SM (Submarino-3), DT (Destructor-2)");
            System.out.print("Codigo del barco: ");
            String codigo = scanner.nextLine().toUpperCase();
            
            // Validar codigo
            if (!codigo.equals("PA") && !codigo.equals("AZ") && !codigo.equals("SM") && !codigo.equals("DT")) {
                System.out.println("Codigo invalido. Intenta de nuevo.");
                continue;
            }
            
            // Validar que no este repetido excepto en EASY con DT
            boolean yaUsado = false;
            for (int i = 0; i < barcosColocados; i++) {
                if (barcosUsados[i].equals(codigo)) {
                    if (dificultad.equals("EASY") && codigo.equals("DT")) {
                        // Permitir
                        break;
                    } else {
                        yaUsado = true;
                        break;
                    }
                }
            }
            
            if (yaUsado) {
                System.out.println("Ya has usado ese tipo de barco. Intenta con otro.");
                continue;
            }
            
            // Obtener tamano del barco
            int tamano = obtenerTamanoBarco(codigo);
            
            System.out.print("Fila inicial (0-7): ");
            int fila;
            try {
                fila = Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Valor invalido. Intenta de nuevo.");
                continue;
            }
            
            System.out.print("Columna inicial (0-7): ");
            int columna;
            try {
                columna = Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Valor invalido. Intenta de nuevo.");
                continue;
            }
            
            System.out.print("Orientacion (H=Horizontal, V=Vertical): ");
            String orientacion = scanner.nextLine().toUpperCase();
            
            // Validar y colocar barco
            if (validarColocacionBarco(jugador, fila, columna, tamano, orientacion)) {
                colocarBarcoEnTablero(jugador, fila, columna, tamano, orientacion, codigo, barcosColocados);
                barcosUsados[barcosColocados] = codigo;
                barcosColocados++;
                
                if (jugador == 1) {
                    tiposBarcosP1[barcosColocados - 1] = codigo;
                    vidasBarcosP1[barcosColocados - 1] = tamano;
                } else {
                    tiposBarcosP2[barcosColocados - 1] = codigo;
                    vidasBarcosP2[barcosColocados - 1] = tamano;
                }
                
                System.out.println("Barco colocado exitosamente!");
                mostrarTablero(jugador == 1 ? barcosPlayer1 : barcosPlayer2, true);
            } else {
                System.out.println("No se puede colocar el barco ahi. Intenta de nuevo.");
            }
        }
    }
    
    private int obtenerTamanoBarco(String codigo) {
        switch (codigo) {
            case "PA": return 5;
            case "AZ": return 4;
            case "SM": return 3;
            case "DT": return 2;
            default: return 0;
        }
    }
    
    private boolean validarColocacionBarco(int jugador, int fila, int columna, int tamano, String orientacion) {
        String[][] tablero = jugador == 1 ? barcosPlayer1 : barcosPlayer2;
        
        // Validar limites
        if (orientacion.equals("H")) {
            if (fila < 0 || fila >= 8 || columna < 0 || columna + tamano > 8) {
                return false;
            }
            // Validar que no haya barcos
            for (int j = columna; j < columna + tamano; j++) {
                if (!tablero[fila][j].equals("~")) {
                    return false;
                }
            }
        } else if (orientacion.equals("V")) {
            if (fila < 0 || fila + tamano > 8 || columna < 0 || columna >= 8) {
                return false;
            }
            // Validar que no haya barcos
            for (int i = fila; i < fila + tamano; i++) {
                if (!tablero[i][columna].equals("~")) {
                    return false;
                }
            }
        } else {
            return false;
        }
        
        return true;
    }
    
    private void colocarBarcoEnTablero(int jugador, int fila, int columna, int tamano, 
                                      String orientacion, String codigo, int indiceBarco) {
        String[][] tablero = jugador == 1 ? barcosPlayer1 : barcosPlayer2;
        int[][] estadoBarcos = jugador == 1 ? estadoBarcosPlayer1 : estadoBarcosPlayer2;
        
        if (orientacion.equals("H")) {
            for (int j = columna; j < columna + tamano; j++) {
                tablero[fila][j] = codigo;
                estadoBarcos[fila][j] = indiceBarco;
            }
        } else {
            for (int i = fila; i < fila + tamano; i++) {
                tablero[i][columna] = codigo;
                estadoBarcos[i][columna] = indiceBarco;
            }
        }
    }
    
    private void jugarPartida(Player player2) {
        boolean juegoActivo = true;
        boolean turnoPlayer1 = true;
        
        while (juegoActivo) {
            if (turnoPlayer1) {
                System.out.println("\n  = TURNO DE " + currentUser.getUsername() + " =");
                System.out.println(player2.getUsername() + barcosRestantesP2 + " barcos restantes");
                
                // Mostrar tablero del oponente
                mostrarTablero(modoJuego.equals("TUTORIAL") ? barcosPlayer2 : tableroPlayer2, false);
                
                System.out.print("Fila de bomba (0-7, -1 para retirarse): ");
                int fila;
                try {
                    fila = Integer.parseInt(scanner.nextLine());
                } catch (NumberFormatException e) {
                    System.out.println("Valor invalido. Intenta de nuevo.");
                    continue;
                }
                
                if (fila == -1) {
                    System.out.print("Estas seguro de retirarte? (S/N): ");
                    String resp = scanner.nextLine().toUpperCase();
                    if (resp.equals("S")) {
                        registrarResultado(player2.getUsername(), currentUser.getUsername(), "RETIRO");
                        player2.agregarPuntos(3);
                        System.out.println(player2.getUsername() + " gano por retiro!");
                        juegoActivo = false;
                        continue;
                    } else {
                        continue;
                    }
                }
                
                System.out.print("Columna de bomba (0-7): ");
                int columna;
                try {
                    columna = Integer.parseInt(scanner.nextLine());
                } catch (NumberFormatException e) {
                    System.out.println("Valor invalido. Intenta de nuevo.");
                    continue;
                }
                
                if (fila < 0 || fila >= 8 || columna < 0 || columna >= 8) {
                    System.out.println("Coordenadas invalidas.");
                    continue;
                }
                
                // Procesar bomba
                if (procesarBomba(2, fila, columna)) {
                    if (barcosRestantesP2 == 0) {
                        System.out.println("\n" + currentUser.getUsername() + " HA GANADO!");
                        currentUser.agregarPuntos(3);
                        registrarResultado(currentUser.getUsername(), player2.getUsername(), dificultad);
                        juegoActivo = false;
                    }
                }
                
                turnoPlayer1 = false;
            } else {
                System.out.println("\n = TURNO DE " + player2.getUsername() + " =");
                System.out.println(currentUser.getUsername() + barcosRestantesP1 + " barcos restantes");
                
                // Mostrar tablero del oponente
                mostrarTablero(modoJuego.equals("TUTORIAL") ? barcosPlayer1 : tableroPlayer1, false);
                
                System.out.print("Fila de bomba (0-7, -1 para retirarse): ");
                int fila;
                try {
                    fila = Integer.parseInt(scanner.nextLine());
                } catch (NumberFormatException e) {
                    System.out.println("Valor invalido. Intenta de nuevo.");
                    continue;
                }
                
                if (fila == -1) {
                    System.out.print("Estas seguro de retirarte? (S/N): ");
                    String resp = scanner.nextLine().toUpperCase();
                    if (resp.equals("S")) {
                        registrarResultado(currentUser.getUsername(), player2.getUsername(), "RETIRO");
                        currentUser.agregarPuntos(3);
                        System.out.println(currentUser.getUsername() + " gano por retiro!");
                        juegoActivo = false;
                        continue;
                    } else {
                        continue;
                    }
                }
                
                System.out.print("Columna de bomba (0-7): ");
                int columna;
                try {
                    columna = Integer.parseInt(scanner.nextLine());
                } catch (NumberFormatException e) {
                    System.out.println("Valor invalido. Intenta de nuevo.");
                    continue;
                }
                
                if (fila < 0 || fila >= 8 || columna < 0 || columna >= 8) {
                    System.out.println("Coordenadas invalidas.");
                    continue;
                }
                
                // Procesar bomba
                if (procesarBomba(1, fila, columna)) {
                    if (barcosRestantesP1 == 0) {
                        System.out.println("\n" + player2.getUsername() + " HA GANADO!");
                        player2.agregarPuntos(3);
                        registrarResultado(player2.getUsername(), currentUser.getUsername(), dificultad);
                        juegoActivo = false;
                    }
                }
                
                turnoPlayer1 = true;
            }
        }
    }
    
    private boolean procesarBomba(int jugadorObjetivo, int fila, int columna) {
        String[][] tableroVisible = jugadorObjetivo == 1 ? tableroPlayer1 : tableroPlayer2;
        String[][] barcosTablero = jugadorObjetivo == 1 ? barcosPlayer1 : barcosPlayer2;
        int[][] estadoBarcos = jugadorObjetivo == 1 ? estadoBarcosPlayer1 : estadoBarcosPlayer2;
        int[] vidasBarcos = jugadorObjetivo == 1 ? vidasBarcosP1 : vidasBarcosP2;
        String[] tiposBarcos = jugadorObjetivo == 1 ? tiposBarcosP1 : tiposBarcosP2;
        
        if (!barcosTablero[fila][columna].equals("~")) {
            // Impacto
            String codigoBarco = barcosTablero[fila][columna];
            int indiceBarco = estadoBarcos[fila][columna];
            
            tableroVisible[fila][columna] = "X";
            System.out.println("\nSE HA BOMBARDEADO UN " + getNombreBarco(codigoBarco) + "!");
            
            // Reducir vida del barco
            vidasBarcos[indiceBarco]--;
            
            if (vidasBarcos[indiceBarco] == 0) {
                System.out.println("SE HUNDIO EL " + getNombreBarco(codigoBarco) + "!");
                if (jugadorObjetivo == 1) {
                    barcosRestantesP1--;
                } else {
                    barcosRestantesP2--;
                }
            }
            
            // REGENERAR TABLERO
            regenerarTablero(jugadorObjetivo);
            
            return true;
        } else {
            // Fallo
            tableroVisible[fila][columna] = "F";
            System.out.println("\nAgua! Bomba fallida.");
            mostrarTablero(tableroVisible, false);
            
            // Esperar y limpiar la F
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {}
            
            tableroVisible[fila][columna] = "~";
            return false;
        }
    }
    
    private void regenerarTablero(int jugador) {
        System.out.println("\nREGENERANDO TABLERO!");
        
        String[][] barcosTablero = jugador == 1 ? barcosPlayer1 : barcosPlayer2;
        int[][] estadoBarcos = jugador == 1 ? estadoBarcosPlayer1 : estadoBarcosPlayer2;
        String[] tiposBarcos = jugador == 1 ? tiposBarcosP1 : tiposBarcosP2;
        int[] vidasBarcos = jugador == 1 ? vidasBarcosP1 : vidasBarcosP2;
        
        // Limpiar tablero
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                barcosTablero[i][j] = "~";
                estadoBarcos[i][j] = -1;
            }
        }
        
        // Recolocar barcos que aun tienen vida
        int cantidadBarcos = getCantidadBarcosPorDificultad();
        for (int b = 0; b < cantidadBarcos; b++) {
            if (vidasBarcos[b] > 0) {
                boolean colocado = false;
                int intentos = 0;
                
                while (!colocado && intentos < 100) {
                    int fila = random.nextInt(8);
                    int columna = random.nextInt(8);
                    String orientacion = random.nextBoolean() ? "H" : "V";
                    int tamano = vidasBarcos[b];
                    
                    if (validarColocacionBarco(jugador, fila, columna, tamano, orientacion)) {
                        colocarBarcoEnTablero(jugador, fila, columna, tamano, orientacion, tiposBarcos[b], b);
                        colocado = true;
                    }
                    intentos++;
                }
            }
        }
        
        System.out.println("Tablero regenerado!\n");
    }
    
    private String getNombreBarco(String codigo) {
        switch (codigo) {
            case "PA": return "PORTAAVIONES";
            case "AZ": return "ACORAZADO";
            case "SM": return "SUBMARINO";
            case "DT": return "DESTRUCTOR";
            default: return "BARCO";
        }
    }
    
    private void registrarResultado(String ganador, String perdedor, String modo) {
        String log = ganador + " hundio todos los barcos de " + perdedor + " en modo " + modo + ".";
        
        // Registrar en ambos jugadores
        Player jugadorGanador = buscarPlayerPorUsername(ganador);
        Player jugadorPerdedor = buscarPlayerPorUsername(perdedor);
        
        if (jugadorGanador != null) {
            jugadorGanador.agregarLog(log);
        }
        if (jugadorPerdedor != null) {
            jugadorPerdedor.agregarLog(log);
        }
    }
    
    private void mostrarTablero(String[][] tablero, boolean mostrarTodo) {
        System.out.println("\n  0 1 2 3 4 5 6 7");
        for (int i = 0; i < 8; i++) {
            System.out.print(i + " ");
            for (int j = 0; j < 8; j++) {
                System.out.print(tablero[i][j] + " ");
            }
            System.out.println();
        }
        System.out.println();
    }
}