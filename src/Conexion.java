import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Conexion {
    private final int puerto; //Puerto para la conexión
    private final String host; //Host para la conexión
    protected ServerSocket ss; //Socket del servidor
    protected Socket cs; //Socket del cliente
    
    /**
     * Constructor de Conexion que dependiendo del tipo que se envíe 
     * Inicializará el socket de servidor o de cliente.
     * @param tipo
     * @throws IOException
     */
    public Conexion(String tipo, int puerto, String host) throws IOException {
        this.puerto = puerto;
        this.host = host;
        if(tipo.equalsIgnoreCase("servidor")) {
            ss = new ServerSocket(puerto);//Se crea el socket para el servidor en puerto 1234
        } else {
            cs = new Socket(host, puerto); //Socket para el cliente en localhost en puerto 1234
        }
    }
}
