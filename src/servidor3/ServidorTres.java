package servidor3;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.concurrent.Semaphore;


public class ServidorTres extends Conexion { //Se hereda de conexión para hacer uso de los sockets
	
	private Semaphore sem1;
	private ArrayList<String[]> datos;
	
    public ServidorTres() throws IOException {
		super("servidor");
		//Creamos un semáforo
		sem1 = new Semaphore(1);
		//Creamos un ArrayList de datos de tipo String
		datos = new ArrayList<String[]>();
		
    }

	/**
	 * Método que se ejecuta una vez se iniciliza un objeto de la clase servidor
	 * en el que se está esperando conexiones hasta que se para la ejecución del programa 
	 * Por cada conexión creará un hilo y lo enviará a hacer las peticiones de reserva de plazas.
	 */
    public void startServer() {
		int cont = 1; // Variable para contar los hilos de server que se pudiesen crear
        System.out.println("SERVIDOR TRES ON...");
		try {
			//siempre estará esperando clientes.
			while(true){
				System.out.println("Esperando...");
				// Si encuentra un cliente creará unos thread con esa conexion.
				cs = ss.accept(); //Accept comienza el socket y espera una conexión desde un cliente
				//llamamos al método para cargar los resultados
				leerResultados();
				// Crea los canales de conexión
				DataInputStream in = new DataInputStream(cs.getInputStream());
				DataOutputStream out = new DataOutputStream(cs.getOutputStream());
				System.out.println("Conexión establecida cliente " + cont);
				ThreadServidor mini = new ThreadServidor(in, out, datos, sem1);
				mini.start();
				// Enviará los in y out a un nuevo THREAD
				cont++;
			}
        }
        catch (Exception e) {
			System.out.println(e);
			//Si encuentra un error eleva el error al main
			throw new RuntimeException();
        }
    }


	
	public void leerResultados() {
		String ruta = "files/servidor3.txt";
        File file = new File(ruta);
        try {
            //se crea objeto Scanner para leer la ruta
            Scanner in = new Scanner (file);
            //se leen las líneas y se van almacenando en el array
            while (in.hasNextLine()) {
                String linea = in.nextLine();
                String[] linea_separada = linea.split(",");
                this.datos.add(linea_separada);
            }
            in.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
	}
}
