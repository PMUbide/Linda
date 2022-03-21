package servidor1_Replica;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Formatter;
import java.util.Scanner;

//Clase principal que hará uso del servidor
public class MainServidor {
    public static void main(String[] args) throws IOException {
        // Se crea el servidor
       ServidorUno servidor = new ServidorUno(); 
        System.out.println("Iniciando servidor\n"); 
        // Inicio del servidor para recibir conexiones
        servidor.startServer(); 

      

       
    }


      


}