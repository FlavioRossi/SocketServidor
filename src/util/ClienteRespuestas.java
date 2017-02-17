
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util;

import java.io.DataInputStream;
import java.time.LocalDateTime;
import javafx.concurrent.Task;
import modelo.Cliente;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

/**
 *
 * @author FLAVIO
 */
public class ClienteRespuestas extends Task{
    private final Cliente cliente;
    private final DataInputStream input;
    
    public ClienteRespuestas(Cliente cliente, DataInputStream input) {
        this.cliente = cliente;
        this.input = input;
    }

    @Override
    protected Object call() throws Exception {
        boolean socketActivo = true;
        
        while(socketActivo){
            String mensaje = input.readUTF();
            JSONObject respond = (JSONObject) new JSONParser().parse(mensaje);
            System.out.println("Llego del cliente " + mensaje);
            
            int valor = Integer.parseInt(respond.get("parametro").toString());
            if (valor == 9999) socketActivo = false;
            responder(respond);
        }
        return null;
    }
    
    public void responder(JSONObject json){
        int valor = Integer.parseInt(json.get("parametro").toString());
        JSONObject resul = (JSONObject) json.get("resul");
        
        switch(valor){
            case 0:
                /**
                 * pulso de vida (verifica conexion de socket)
                 * no devuelve valores ya que el error lo toma el cliente 
                 * cuando solicita el pulso de vida
                 */
                break;
            case 1:
                //logueo usuario
                registroCliente(resul);
                break;
            case 2:
                //envia notificaciones
                break;
            case 9999:
                //cierra socket
                JSONObject respond = new JSONObject();
                cliente.enviar(9999, respond);
                ListaClientes.getInstancia().removeCliente(cliente);
                cliente.stopCliente();
        }
    }
    
    private void registroCliente(JSONObject resul){
        String nombre = (String) resul.get("usuario");
        String clave = (String) resul.get("clave");

        JSONObject respond = new JSONObject();
        if ((nombre.equals("FLAVIO") && clave.equals("FNR")) || (nombre.equals("FRANCOR") && clave.equals("JFR"))
                || (nombre.equals("ALE77") && clave.equals("LI77"))) {
            cliente.setNombre(nombre);
            cliente.setClave(clave);
            cliente.setIngreso(LocalDateTime.now());
            cliente.setEstado(true);

            respond.put("logueo", "ok");
            respond.put("id", cliente.getId());
            respond.put("nombre", cliente.getNombre());
            respond.put("ingreso", cliente.getIngreso().format(Status.FORMATO_FECHA_HORA));
        }else{
            respond.put("logueo", "cancel");
            cliente.setEstado(false);
        }
        cliente.enviar(1, respond);
    }
    
    
}
