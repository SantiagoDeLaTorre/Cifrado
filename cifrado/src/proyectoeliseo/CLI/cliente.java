package proyectoeliseo.CLI;

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

	public static void main(String[] args) {
		try
		{
                        Conexion();
                        recibiendoKeyPublica();
                        envioClavePrivada();
                        
			//cierre
			sockCli.close();
		}catch(Exception e){
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
                e.printStackTrace();
            }
        }
        static void recibiendoKeyPublica(){
            try{
                System.out.println("Recibiendo Key Publica");
                byte[] leo = new byte[256];
                is.read(leo);
                KeyFactory kf = KeyFactory.getInstance("RSA");
                X509EncodedKeySpec xeks = new X509EncodedKeySpec(leo);
                keyPub = kf.generatePublic(xeks);
                System.out.println("Key pública recibida: ");
                System.out.println(keyPub);
            }catch(Exception e){
                e.printStackTrace();
            }
        }
        
        static void envioClavePrivada(){
            try{
                KeyGenerator generoClave = KeyGenerator.getInstance("AES");
                SecretKey clavePrivadaAES = generoClave.generateKey();
                String a = Base64.encodeBase64String(clavePrivadaAES.getEncoded());
                System.out.println("Mi clave privada: " + a);
                Cipher cp = Cipher.getInstance("RSA");
                cp.init(Cipher.ENCRYPT_MODE, keyPub);
                String mCifra = new String(cp.doFinal(a.getBytes()));
                os.write(mCifra.getBytes());
                System.out.println("Enviado");
            }catch(Exception e){
                e.printStackTrace();
            }
        }
}
