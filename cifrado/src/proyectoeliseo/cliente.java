package proyectoeliseo;

import java.io.*;
import java.net.*;
import javax.crypto.*;
import java.security.*;
import java.security.spec.*;
import org.apache.commons.codec.binary.Base64;

public class cliente{
    
        static InputStream is;
        static OutputStream os;
        static Socket sockCli;
        static PublicKey keyPub;
        static SecretKey clavePrivadaAES;

        public static void main(String[] args){
            try
		{
                        System.out.println("[CLI]Realizando conexion...");
                        Conexion();
                        System.out.println("[CLI]Connection done.");
                        recibiendoKeyPublica();
                        envioClavePrivada();
                        
			//cierre
			sockCli.close();
		}catch(Exception e){
                    System.out.println("Error en CLIENTE - Constructor: ");
                    e.printStackTrace();
		}
        }
        
        static void Conexion(){
            try{
                sockCli = new Socket();
                InetSocketAddress add = new InetSocketAddress("localhost", 37773);
                sockCli.connect(add);
                is = sockCli.getInputStream();
                os = sockCli.getOutputStream();
            }catch(Exception e){
                System.out.println("Error en CLIENTE - Conexion: ");
                e.printStackTrace();
            }
        }
        static void recibiendoKeyPublica(){
            try{
                System.out.println("[CLI] Recibiendo Key Publica");
                byte[] leo = new byte[256];
                is.read(leo);
                KeyFactory kf = KeyFactory.getInstance("RSA");
                X509EncodedKeySpec xeks = new X509EncodedKeySpec(leo);
                keyPub = kf.generatePublic(xeks);
                System.out.println("[CLI] Key pública recibida: ");
                System.out.println(keyPub);
            }catch(Exception e){
                System.out.println("Error en CLIENTE - recibiendoKeyPublica: ");
                e.printStackTrace();
            }
        }
        static void envioClavePrivada(){
            try{
                KeyGenerator generoClave = KeyGenerator.getInstance("AES");
                clavePrivadaAES = generoClave.generateKey();
                String aesLeible = Base64.encodeBase64String(clavePrivadaAES.getEncoded());
                System.out.println("[CLI] AES: " + aesLeible);
                Cipher cp = Cipher.getInstance("RSA");
                cp.init(Cipher.ENCRYPT_MODE, keyPub);
                byte[] aesEncriptado = cp.doFinal(clavePrivadaAES.getEncoded());
                System.out.println("[CLI] Aes encriptado con key publica: ");
                System.out.println(Base64.encodeBase64String(aesEncriptado));
                os.write(aesEncriptado);
                System.out.println("[CLI] Enviado Clave privada");
                        
            }catch(Exception e){
                System.out.println("Error en CLIENTE - envioClavePrivada: ");
                e.printStackTrace();
            }
        }
}
