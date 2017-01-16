/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util;

import java.io.File;
import java.time.format.DateTimeFormatter;

/**
 *
 * @author FLAVIO
 */
public class Status {
    /**
     * PUERTO de la conexión del socket
     */
    public static final int PUERTO_SOCKET = 2222;
    
    /**
     * RUTA del RUNTIME de COBOL
     */
    public static final String CBL_RUNTIME = "c:\\Acucorp\\Acucbl701\\AcuGT\\bin\\wrun32.exe";
    /**
     * RUTA del CONFIG del APOLO
     */
    public static final String CBL_CONFIG = "z:\\config\\instdiag.cfg";
    /**
     * RUTA de la LLAMADA a un programa COBOL
     */
    public static final String CBL_LLAMADA = CBL_RUNTIME + " -c " + CBL_CONFIG + " ";
    /**
     * PROGRAMA COBOL 
     * Busca en APOLO notificaciones nuevas
     * Devuelve las notificaciones en el archivo FILE_NOTIFICACIONES en formato JSON 
     */
    public static final String CBL_NOTIFICACION_BUSCAR = "z:\\InstDiag\\object\\notificacionBuscar.acu";
    /**
     * PROGRAMA COBOL
     * Marca las notificaciones en APOLO segun el estado (1. Enviado, 2.Recibido, 3.Leido)
     */
    public static final String CBL_NOTIFICACIONES_JAVA = "z:\\InstDiag\\object\\notificacionJAVA.acu";
    /**
     * ARCHIVO JSON
     * Archivo donde APOLO devuelve las notificaciones nuevas en formato JSON
     */
    public static final File FILE_NOTIFICACIONES = new File("Z:\\java\\SocketServidor\\src\\modelo\\notificaciones.json");
    
    
    /**
     * NOMBRE del usuario
     */
    public static String nombre;
    /**
     * CLAVE del usuario
     */
    public static String clave;
    
    /**
     * FORMATO fecha y hora del sistema
     */
    public static final DateTimeFormatter FORMATO_FECHA_HORA = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    /**
     * FORMATO fecha del sistema
     */
    public static final DateTimeFormatter FORMATO_FECHA = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    /**
     * FORMATO hora del sistema
     */
    public static final DateTimeFormatter HORA = DateTimeFormatter.ofPattern("HH:mm:ss");    
}
