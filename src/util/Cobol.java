/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;
import modelo.Notificacion;

/**
 *
 * @author FLAVIO
 */
public class Cobol {
    public static File buscaNotificaciones() throws IOException, InterruptedException {
        Process process = Runtime.getRuntime().exec(Status.CBL_LLAMADA + Status.CBL_NOTIFICACION_BUSCAR);
        int resul = process.waitFor();
        process.destroy();
        if (resul > 0) {
            return null;
        }else{
            return Status.FILE_NOTIFICACIONES;
        }
    }
    
    public static int marcaNotificacion(List<Notificacion> notificaciones) throws IOException, InterruptedException {
        String parametros = notificaciones.stream()
                .map(n -> n.toString())
                .collect(Collectors.joining("|"));
        System.out.println("ENVIADAS: " + Status.CBL_LLAMADA + Status.CBL_NOTIFICACIONES_JAVA + " " + parametros);
        return Runtime.getRuntime().exec(Status.CBL_LLAMADA + Status.CBL_NOTIFICACIONES_JAVA + " " + parametros).waitFor();
    }
}
