package servidor1;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Conexion {
    private final int PUERTO = 1233; //Puerto para la conexión
    private final String HOST = "192.168.13.25";  //Host para la conexión con servidor IP PC EDURNE
    protected ServerSocket ss; //Socket del servidor
    protected Socket cs; //Socket del cliente
    
    /**
     * Constructor de Conexión que dependiendo del tipo que se envíe 
     * Inicializará el socket de servidor o de cliente.
     * @param tipo
     * @throws IOException
     */
    public Conexion(String tipo) throws IOException {
        if(tipo.equalsIgnoreCase("servidor")) {
            ss = new ServerSocket(PUERTO);//Se crea el socket para el servidor en puerto 1234
        } else {
            cs = new Socket(HOST, PUERTO); //Socket para el cliente en localhost en puerto 1234
        }
    }
}
