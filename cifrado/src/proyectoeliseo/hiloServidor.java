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
        SecretKey aesKey;
        
	public hiloServidor(Socket so, PublicKey pu, PrivateKey pri){
		this.ClavePrivada = pri;
		this.ClavePublica = pu;
		this.cliSock = so;
	}
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
                byte[] leo = new byte[128];
                is.read(leo);
                String aLeo = new String(leo);
                System.out.println("[HSER]Lo que recivo en base64:\n" + Base64.encodeBase64String(leo));
                Cipher cp = Cipher.getInstance("RSA");
                cp.init(Cipher.DECRYPT_MODE, ClavePrivada);
                byte[] aja = cp.doFinal(leo);
                aesKey = new SecretKeySpec(aja, "AES");
                System.out.println("Supuesto Aes: " + Base64.encodeBase64String(aesKey.getEncoded()));
            }catch(Exception e){
                System.out.println("[HSER] Error en recepcionClaveSimetrica: ");
                e.printStackTrace();
            }
        }
}
