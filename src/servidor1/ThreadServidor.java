package servidor1;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Formatter;
import java.util.concurrent.Semaphore;

public class ThreadServidor extends Thread {
    private DataInputStream in = null; //Canal de datos de entrada
    private DataOutputStream out = null; //canal de datos de salida
    private Semaphore sem; //Objeto semáforo
    private ArrayList<String[]> lista; //Objeto semáforo

    /**
     * Método constructor que crea un hilo del servidor
     * @param nombre -> String nombre del servidor
     * @param in -> datos de entrada
     * @param out -> datos de salida
     * @param t -> 
     * @param sem -> semáforo
     * @throws IOException
     */
    public ThreadServidor(DataInputStream in, DataOutputStream out, ArrayList<String[]> lista, Semaphore sem) throws IOException {
        this.in = in;
        this.out = out;
        this.sem = sem;
        this.lista = lista;
    }

    /**
     * Método que se ejecuta cuando se inicia el hilo con start()
     * Recibe de forma continua una petición de numero de fila + letra de asiento 
     * y comprueba si el avión está lleno, si no lo está si ese sitio 
     * está ocupado ya o no, y si no lo está lo adjudicará a ese cliente.
     * Según los resultados puede seguir recibiendo o acaba el proceso cuando el avión esté completo.
     */
    public void run() {
        try {
            // Se le envía un mensaje al cliente usando su flujo de salida
            while (true) {
                // Split para gestionar el sitio
                String msg = in.readUTF();
                System.out.println(msg);
				//Cambiarlo de String al modelo de datos
				String msg_separado[] = msg.split("-");
                String tupla[] = msg_separado[0].split(",");
				String tipo = msg_separado[1];
				System.out.println("Datos recibidos: ");
				for (int i = 0; i < tupla.length; i++) {
					System.out.println(tupla[i]);
				}
               
				if(Integer.parseInt(tipo) == 1){
                    //METODO LEER
                    String enviar = "";
                    //METODO PARA BUSCAR TUPLAS
                    String[] devuelve = readNote(tupla);
                    for (int i = 0; i < devuelve.length; i++) {
                        if(i == devuelve.length - 1){
                            enviar = enviar + devuelve[i];
                        }else{
                            enviar = enviar + devuelve[i]+ ",";
                        }
                    }
                    out.writeUTF(enviar);
                }
                if(Integer.parseInt(tipo) == 2){
                    //METODO INSERTAR
                    //sem acquire
                    this.sem.acquire();
                    this.lista.add(tupla);
                    //Escribir en el archivo
                    guardarResultados();
                    //sem release
                    this.sem.release();
                    //ENVIO RESPUESTA
                    out.writeUTF("Tupla introducida correctamente");

                }
                if(Integer.parseInt(tipo) == 3){
                    //METODO BORRAR
                    String enviar = "";
                    this.sem.acquire();
                    int indice = readNoteIndice(tupla);
                    if (indice == -1){
                        enviar = "NO existe, no se pudo eliminar";
                    }else{
                        this.lista.remove(indice);
                        enviar = "Eliminado correctamente";
                    }
                    //Guardamos resultados
                    guardarResultados();
                    this.sem.release();
                    out.writeUTF(enviar);
                }
    
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Método al que pasamos una lista de tuplas y la lee para comparar
     * @param tuplita
     * @return
     */
    public String[] readNote(String[] tuplita){
        //REcorre el array de las tuplas.
        String[] resultado = new String[tuplita.length];
        Boolean encontrado = true;
        int suma = 0;
        //Recorremos la lista
        for (int i = 0; i < this.lista.size(); i++) {
            //Ponemos el boolean en true
            encontrado = true;
            String[] tuplaRecorre = this.lista.get(i);
            if (tuplaRecorre.length == tuplita.length){
                //Si coindicen en longitud
                for (int j = 0; j < tuplaRecorre.length; j++) {
                    //Comprobamos los valores y si no coinciden
                    if(!tuplita[j].equals(tuplaRecorre[j]) && !tuplita[j].substring(0,1).equals("?")){
                        //Ponemos el boolean en false
                        encontrado = false;
                        break;
                    }
                    //Si coinciden sumamos uno al contador
                    resultado[j] = tuplaRecorre[j];
                    suma++;
                }
            }
            if(suma == tuplita.length){
                break;
            }
          
        }
        return resultado;
    }

    /**
     * Método al que pasamos una lista de tuplas para que las lea con el índice
     * @param tuplita
     * @return
     */
    public int readNoteIndice(String[] tuplita){
        //Recorre el array de las tuplas.
        String[] resultado = new String[tuplita.length];
        Boolean encontrado = true;
        int index = -1;
        int suma = 0;
        //Recorremos la lista y ponemos el boolean a true
        for (int i = 0; i < this.lista.size(); i++) {
            encontrado = true;
            //Recogemos el índice de la tupla
            String[] tuplaRecorre = this.lista.get(i);
            if (tuplaRecorre.length == tuplita.length){
                //Si coindicen en longitud
                for (int j = 0; j < tuplaRecorre.length; j++) {
                    //Si no son iguales ponemos el boolean false
                    if(!tuplita[j].equals(tuplaRecorre[j]) && !tuplita[j].substring(0,1).equals("?")){
                        encontrado = false;
                        break;
                    }
                    //Si coinciden sumamos uno al contador
                    resultado[j] = tuplaRecorre[j];
                    suma++;
                }
            }
            if(suma == tuplita.length){
                index = i;
                break;
            }
          
        }
        return index;
    }


    public void guardarResultados() {
		String ruta = "files/servidor1.txt";
		//creamos un archivo
		// File archivo = new File(ruta);
        Formatter f;
        try {
            //formater con la ruta para leer
            f = new Formatter (ruta);
            // recorremos la lista
            for (int i = 0; i < this.lista.size(); i++) {
                String envio = "";
                // sacamos el elemento de la iteración
                String[] tupla = this.lista.get(i);
                // recorremos los String que tenga la tupla
                for (String item : tupla) {
                    envio += item + ",";
                }
                // le quitamos la coma del final que no interesa
                envio = envio.substring(0, envio.length()-1);
                //Hacemos la escritura de esta línea.
                f.format(envio + "\n");
                f.flush();
            }
            //Cerramos el objeto formatter para hacer el guardado
            f.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
	}


}
