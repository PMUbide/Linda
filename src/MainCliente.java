import java.io.IOException;

//Clase principal que hará uso del cliente
public class MainCliente {
	public static void main(String[] args) throws IOException {
		//En esta ocasión se crea un cliente para que se ejecute el proceso 
		Cliente cli = new Cliente("Cliente 1"); 
		System.out.println("Iniciando cliente\n");
		// Se inicia el cliente empezando la conexión al servidor
		cli.startClient(); 
	}
}