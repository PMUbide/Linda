package servidor3;

import java.io.IOException;

//Clase principal que hará uso del servidor
public class MainServidor {
    public static void main(String[] args) throws IOException {
        // Se crea el servidor
        ServidorTres servidor = new ServidorTres(); 
        System.out.println("Iniciando servidor \n"); 
        // Inicio del servidor para recibir conexiones
        servidor.startServer(); 
        
        
    }
}