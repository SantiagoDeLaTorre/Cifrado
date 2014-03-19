package proyectoeliseo;

import java.io.*;
import java.net.*;
import javax.crypto.*;
import java.security.*;
import javax.crypto.spec.*;
import javax.xml.bind.DatatypeConverter;
import org.apache.commons.codec.binary.Base64;

public class hiloServidor extends Thread{

	private PublicKey  ClavePublica = null;
	private PrivateKey  ClavePrivada = null;
	private Socket cliSock = null;
	private SecretKey clavePriSi = null;
	private KeyPair miCipher = null;
        InputStream is;
        OutputStream os;
        
	public hiloServidor(Socket so, PublicKey pu, PrivateKey pri){
		this.ClavePrivada = pri;
		this.ClavePublica = pu;
		this.cliSock = so;
	}

        /* Hilo Run*/
	public void run(){
		try{
			envioClavePublica();
			recepcionClaveSimetrica();
		}catch(Exception e){
                        // hubo un error, mal vamos...
                    System.out.println("Error en HSER: ");
                    e.printStackTrace();
		}
	}
        void envioClavePublica(){
            try{
                System.out.println("[HSER] Run");
                is = cliSock.getInputStream();
                os = cliSock.getOutputStream();
                byte[] array = ClavePublica.getEncoded();
                Base64 encoder = new Base64();
                String a = encoder.encodeBase64String(array);
                os.write(array);
                System.out.println("[HSER] Clave publica: ");
                System.out.println(ClavePublica);
            }catch(Exception e){
                System.out.println("[HSER] Error en EnvioClavePublica: ");
                e.printStackTrace();
            }
        }
        void recepcionClaveSimetrica(){
            try{
                byte[] leo = new byte[256/8];
                is.read(leo);
                String aLeo = new String(leo);
                System.out.println("leo: " + leo);
                System.out.println("aLeo: " + aLeo);
                Cipher cp = Cipher.getInstance("RSA");
                cp.init(Cipher.DECRYPT_MODE, ClavePrivada);
                System.out.println("I no have idea");
                String descifro = new String(cp.doFinal(aLeo.getBytes()));
                System.out.println("Decodeado: " + descifro);
                byte[] aw = DatatypeConverter.parseHexBinary(descifro);
                byte[] encodedKey     = Base64.decodeBase64(descifro);
                SecretKey oKey = new SecretKeySpec(descifro.getBytes(), 0, encodedKey.length, "AES");
                String adw = Base64.encodeBase64String(oKey.getEncoded());
                System.out.println("clave privada " + adw);
            }catch(Exception e){
                System.out.println("[HSER] Error en recepcionClaveSimetrica: ");
                e.printStackTrace();
            }
        }
}
