package pe.edu.cibertec.cateringdemo.Conexion;

import java.security.MessageDigest;

/**
 * Created by Sistemas on 26/10/2017.
 */

public class ConexionWs {
        public static String urlservice = "http://192.168.0.15:8080/SW_AppCateringCarlos/";
    public static String cod_usr = "";
    public static String nombre_usr = "";
    public static String apellido_usr = "";
    public static String correo_usr = "";
    public static int perfil_usr = 0;



    /**
     * METODO MD5
     * @param cadena
     * @return
     * @throws Exception
     */
    public String getMD5(String cadena) throws Exception {

        MessageDigest md = MessageDigest.getInstance("MD5");
        byte[] b = md.digest(cadena.getBytes());

        int size = b.length;
        StringBuilder h = new StringBuilder(size);
        for (int i = 0; i < size; i++) {

            int u = b[i] & 255;

            if (u < 16)
            {
                h.append("0").append(Integer.toHexString(u));
            }
            else
            {
                h.append(Integer.toHexString(u));
            }
        }
        return h.toString();
    }
}
