import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;


public class MainLinda   { //Se hereda de conexión para hacer uso de los sockets y demás
	
	//CONNEXION PA LOS CLIENTES
	private Conexion conCliente = new Conexion("servidor", 1234, "192.168.13.29");

    public MainLinda() throws IOException {

    }

	/**
	 * Método que se ejecuta una vez se iniciliza un objeto de la clase servidor
	 * en el que se está esperando conexiones hasta que se para la ejecución del programa 
	 * Por cada conexión creará un hilo y lo enviará a hacer las peticiones de reserva de plazas.
	 */
    public void startServer() {
		int cont = 1; // Variable para contar los hilos de server que se pudiesen crear
        System.out.println("LINDA ON..."); 
		try {
			//siempre estará esperando clientes.
			while(true){
				System.out.println("Esperando..."); 
				//Si encuentra un cliente creará unos thread con esa conexion.
				this.conCliente.cs = this.conCliente.ss.accept(); //Accept comienza el socket y espera una conexión desde un cliente
				// Crea los canales de conexión
				DataInputStream in = new DataInputStream(this.conCliente.cs.getInputStream());
				DataOutputStream out = new DataOutputStream(this.conCliente.cs.getOutputStream());
				System.out.println("Conexión establecida cliente " + cont);
				// RECIBE CLIENTE Y LANZA HILO LINDA PARA COMUNICACION
				// ThreadLinda hilo = new ThreadLinda(in, out);
				ThreadLinda hilo = new ThreadLinda(this.conCliente);
				hilo.start();
				//Enviará los in y out a un nuevo THREAD, con el AVION y SEMAFORO COMPARTIDO
				cont++;
			}
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
