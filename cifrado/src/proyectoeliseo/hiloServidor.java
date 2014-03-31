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
                        menu();
		}catch(Exception e){
                        // hubo un error, mal vamos...
                    System.out.println("Error en HSER: ");
                    e.printStackTrace();
		}
	}
        
        void menu(){
            try{
            byte[] Opcion = new byte[256];
            String men = "MENU";
            os.write(Base64.encodeBase64(men.getBytes()));
            men = "Menu\n\t[1]Cifrado MD5\n\t[2]Cifrado SHA1";
            os.write(Base64.encodeBase64(men.getBytes()));
            is.read(Opcion);
            String opc = new String(Base64.decodeBase64(Opcion));
                switch (opc) {
                    case "1":
                        opcionMD5();
                        break;
                    case "2":
                        opcionSHA1();
                        break;
                    default:
                        throw new AssertionError();
                }
            }catch(Exception e){
                e.printStackTrace();
            }
        }
        
        void opcionMD5(){
            try{
                byte[] leo = new byte[256];
                os.write(Base64.encodeBase64("Frase a transformar en MD5: ".getBytes()));
                MessageDigest md = MessageDigest.getInstance("MD5");
                is.read(leo);
                md.update(leo);
                leo = md.digest();
                os.write(Base64.encodeBase64(leo));
            }catch(Exception e){
            e.printStackTrace();
        }
            
        }
        void opcionSHA1(){
            try{
                os.write(Base64.encodeBase64("Frase a transformar en SHA1: ".getBytes()));
            }catch(Exception e){
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
