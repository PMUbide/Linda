package servidor1;

import java.io.IOException;

//Clase principal que hará uso del servidor
public class MainServidor {
    public static void main(String[] args) throws IOException {
        // Se crea el servidor
        ServidorUno servidor = new ServidorUno(); 
        System.out.println("Iniciando servidor\n"); 
        // Inicio del servidor para recibir conexiones
        try{
            servidor.startServer(); 
        }catch(Exception e){
            //Duplica el servidor y lo ejecuta
            ServidorUno serverEmergencia = servidor;
            System.out.println("Iniciando servidor1");
            serverEmergencia.startServer();
        }
        
    }
}