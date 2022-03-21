import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Scanner;

public class Cliente {
	private String nombreCliente = ""; // variable para almacenar el nombre

	Conexion conLinda = new Conexion("cliente", 1234, "192.168.13.29");

	// Se usa el constructor para cliente de Conexion
	public Cliente(String cliente) throws IOException {
		this.nombreCliente = cliente;
	}

	/**
	 * Método para iniciar un cliente y conectarse a la conexión con el servidor
	 */
	public void startClient() {
		try {
			// Flujo de datos de entrada y salida
			String eleccion = "1";
			// Recibe 1º mensaje de conexión exitosa con el servidor
			// OPERACIONES
			DataInputStream in = new DataInputStream(conLinda.cs.getInputStream());
			DataOutputStream out = new DataOutputStream(conLinda.cs.getOutputStream());
			Scanner sc = new Scanner(System.in);
			while (true) {
				String recibido = " ";
				boolean envioBool = false;

				System.out.println("Que quieres hacer ?");
				System.out.println("1. Leer tupla");
				System.out.println("2. Insertar tupla");
				System.out.println("3. Borrar tupla");
				System.out.println("4. Cerrar");
				String tipo = sc.nextLine();
				if (tipo.equals("4")) {
					// ACABA EL PROGRAMA
					System.out.println("FIN PROGRAMA");
					out.writeUTF("4");
					conLinda.cs.close();
					conLinda.ss.close();
					break;
				}

				String envio;

				// LEER
				if (tipo.equals("1")) {
					System.out.println("Introduce los datos separados por comas: ('A','2')");
					envio = sc.nextLine();
					// envio = "E,?X,?X"; //SIN ESPACIOS
					envioBool = compruebaTupla(envio);
					if (envioBool) {
						// UTILIZARÁ LA CONEXION
						out.writeUTF(envio + "-" + 1);
					} else {
						System.out.println("TUPLA mal introducida");
					}
				}

				// INSERTAR
				if (tipo.equals("2")) {
					System.out.println("Introduce los datos separados por comas: ('A','2')");
					envio = sc.nextLine();
					// envio = "E,?X,?X"; //SIN ESPACIOS
					envioBool = compruebaTupla(envio);
					if (envioBool) {
						// UTILIZARÁ LA CONEXION
						out.writeUTF(envio + "-" + 2);
					} else {
						System.out.println("TUPLA mal introducida");
					}
				}

				//BORRAR
				if (tipo.equals("3")) {
					System.err.println("Introduce los datos separados por comas: ('A','2')");
					envio = sc.nextLine();
					// envio = "E,?X,?X"; //SIN ESPACIOS
					envioBool = compruebaTupla(envio);
					if (envioBool) {
						// UTILIZARÁ LA CONEXION
						out.writeUTF(envio + "-" + 3);
					} else {
						System.out.println("TUPLA mal introducida");
					}
				}

				// RECIBE LA RESPUESTA
				if (envioBool) {
					String respuesta = in.readUTF();
					System.out.println(respuesta);
				}
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Getter del nombre del cliente
	 * 
	 * @return -> Devuelve un String del nombre
	 */
	public String getNombreCliente() {
		return nombreCliente;
	}

	public boolean compruebaTupla(String t) {
		if (t.contains(",")) {
			return true;
		}
		return false;
	}

}
