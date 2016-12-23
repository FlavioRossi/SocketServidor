/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelo;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import org.json.simple.JSONObject;
import util.ClienteRespuestas;

/**
 *
 * @author FLAVIO
 */
public class Cliente extends Usuario{
    private final Socket socket;
    private final DataInputStream input;
    private final DataOutputStream output;
    
    private final Thread RECIBE_CLIENTE;
    
    public Cliente(int id, Socket socket) throws IOException {
        super(id);
        
        this.socket = socket;
        this.input = new DataInputStream(this.socket.getInputStream());
        this.output = new DataOutputStream(this.socket.getOutputStream());
              
        RECIBE_CLIENTE = new Thread(new ClienteRespuestas(this, input));
    }
    
    /**
     * Envia petición al cliente
     * @param parametro ->Tipo de operación que va a realizar el cliente
     * @param resul ->Parametros para la resolución
     * @return ->Retorna la respuesta del SERVIDOR
         ->Formato de respuesta Json: "parametro", "resul"
     */
    public boolean enviar(int parametro, JSONObject resul){
        JSONObject json = new JSONObject();
        json.put("parametro", parametro);
        json.put("resul", resul);
        
        try {
            output.writeUTF(json.toString());
            return true;
        } catch (IOException ex) {
            System.out.println("El cliente " + getNombre() + " se ha desconectado");
            stopCliente();
            return false;
        }
    }
    
    /**
     * Comienza a escuchar las peticiones del cliente 
     */
    public void startCliente(){
        RECIBE_CLIENTE.start();
    }
    
    /**
     * Cancela la escucha de peticiones del cliente
     */
    public void stopCliente(){
        RECIBE_CLIENTE.interrupt();
    }
    
    /**
     * Devuelve el estado del cliente
     * @return 
     */
    public boolean isConected(){
        return !RECIBE_CLIENTE.isInterrupted();
    }
}
