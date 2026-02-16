package battleship;

public class Player {
    private String username;
    private String password;
    private int puntos;
    private String[] logs; // Arreglo para los ultimos 10 juegos
    private int logCount;
    
    public Player(String username, String password) {
        this.username = username;
        this.password = password;
        this.puntos = 0;
        this.logs = new String[10];
        this.logCount = 0;
    }
    
    // Getters y Setters
    public String getUsername() {
        return username;
    }
    
    public void setUsername(String username) {
        this.username = username;
    }
    
    public String getPassword() {
        return password;
    }
    
    public void setPassword(String password) {
        this.password = password;
    }
    
    public int getPuntos() {
        return puntos;
    }
    
    public void setPuntos(int puntos) {
        this.puntos = puntos;
    }
    
    public void agregarPuntos(int puntos) {
        this.puntos += puntos;
    }
    
    public String[] getLogs() {
        return logs;
    }
    
    public int getLogCount() {
        return logCount;
    }
    
    // Metodo para agregar un juego al historial
    public void agregarLog(String log) {
        // Si ya hay 10 juegos, mover todos una posicion hacia atras
        if (logCount == 10) {
            for (int i = 9; i > 0; i--) {
                logs[i] = logs[i - 1];
            }
            logs[0] = log;
        } else {
            // Mover juegos existentes una posicion hacia atras
            for (int i = logCount; i > 0; i--) {
                logs[i] = logs[i - 1];
            }
            logs[0] = log;
            logCount++;
        }
    }
    
    // Metodo para validar password
    public boolean validarPassword(String password) {
        return this.password.equals(password);
    }
    
    @Override
    public String toString() {
        return "Username: " + username + " | Puntos: " + puntos;
    }
}