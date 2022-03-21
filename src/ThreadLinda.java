import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;


public class ThreadLinda extends Thread {

	// HOST y puerto del SERVIDOR 1 
	private Conexion conServidor1;
	private Conexion conServidor2;
	private Conexion conServidor3;
	private String msg;

	// CANAL DE DATOS CON el CLIENTE
    private DataInputStream inHILO = null; //Canal de datos de entrada
    private DataOutputStream outHILO = null; //canal de datos de salida
	private Conexion conCliente = null;
    /**
     * Método constructor que crea un hilo del servidor
     * @param nombre -> String nombre del servidor
     * @param in -> datos de entrada
     * @param out -> datos de salida
     * @param t -> 
     * @param sem -> semaforó 
     * @throws IOException
     */
    public ThreadLinda(Conexion con) {
        try {
        	conServidor1 = new Conexion("cliente", 1233, "192.168.13.25");
        	conServidor2 = new Conexion("cliente", 1233, "192.168.13.25");
        	conServidor3 = new Conexion("cliente", 1233, "192.168.13.25");
		} catch (IOException e) {
			//si FALLA LA conexión al SERVIDOR 1 - crea un servidor réplica
			System.out.println("LANZO SERVER REPLICA");
			try {
				conServidor1 = new Conexion("cliente", 1234, "192.168.13.25");
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
		this.conCliente = con;
		// this.inHILO = in;
        // this.outHILO = out;
		this.msg = "";
    }

      
    public void run() {
        try {
            // Se le envía un mensaje al cliente usando su flujo de salida
            while (true) {
				DataInputStream inHILO = null;
				DataOutputStream outHILO = null;
				if(this.conCliente.cs.isClosed()){
					System.out.println("Conexion cerrada.");
					break;
				}else{
					inHILO = new DataInputStream(this.conCliente.cs.getInputStream());
					outHILO = new DataOutputStream(this.conCliente.cs.getOutputStream());
				}
				//Se conecta un cliente y crea una miniLinda con ese cliente
				if(this.msg.equalsIgnoreCase("")){
					this.msg = inHILO.readUTF();
				}
				//Cambiarlo de String al modelo de datos
				// "hola,pepe,26-2"
				if(msg.equalsIgnoreCase("4")){
					System.out.println("FIN");
					this.conCliente.cs.close();
					break;
				}
				String msg_separado[] = msg.split("-");
				String tupla[] = msg_separado[0].split(",");
				String tipo = msg_separado[1];
				String tuplaString = "";
				System.out.println("Datos recibidos: ");
				for (int i = 0; i < tupla.length; i++) {
					System.out.println(tupla[i]);
				}
				if(tupla.length < 1 || tupla.length > 6){
					// FIN no hace nada
					outHILO.writeUTF("ERROR");
					break;
				}
				
				if(tupla.length  <= 3){
					//MANDA MENSAJE AL SERVER TIPO 1
					DataInputStream in = new DataInputStream(conServidor1.cs.getInputStream());
					DataOutputStream out = new DataOutputStream(conServidor1.cs.getOutputStream());
					//ENVIO
					out.writeUTF(msg);
					String mensaje = in.readUTF();
					String enviarse = "";
					if(mensaje.contains("null")){
						System.out.println("NO ESTA");
						enviarse = "NO ESTA";
					}else{
						System.out.println("SI ESTA: " + mensaje);
						enviarse = "SI ESTA: " + mensaje;
					}
					outHILO.writeUTF(enviarse);
				}
				else if (tupla.length <= 5){
					//MAnda thread tipo 2
					DataInputStream in = new DataInputStream(conServidor2.cs.getInputStream());
					DataOutputStream out = new DataOutputStream(conServidor2.cs.getOutputStream());
					//ENVIO
					out.writeUTF(msg);
					String mensaje = in.readUTF();
					String enviarse = "";
					if(mensaje.contains("null")){
						System.out.println("NO ESTA");
						enviarse = "NO ESTA";
					}else{
						System.out.println("SI ESTA: " + mensaje);
						enviarse = "SI ESTA: " + mensaje;
					}
					outHILO.writeUTF(enviarse);
				}else{
					//MANDA THREAD 3	
					DataInputStream in = new DataInputStream(conServidor3.cs.getInputStream());
					DataOutputStream out = new DataOutputStream(conServidor3.cs.getOutputStream());
					//ENVIO
					out.writeUTF(msg);
					String mensaje = in.readUTF();
					String enviarse = "";
					if(mensaje.contains("null")){
						System.out.println("NO ESTA");
						enviarse = "NO ESTA";
					}else{
						System.out.println("SI ESTA: " + mensaje);
						enviarse = "SI ESTA: " + mensaje;
					}
					outHILO.writeUTF(enviarse);
				}	
				msg = "";
            }
        } catch (Exception e) {
			System.out.println(e + " Conectando a REPLICA");
            try {
				this.conServidor1 = new Conexion("cliente", 1234, "192.168.13.25");
				this.run();
			} catch (IOException e1) {
				e1.printStackTrace();
			}

        }
    }

}
