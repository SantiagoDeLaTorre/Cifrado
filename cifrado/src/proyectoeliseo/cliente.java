package proyectoeliseo;

import java.io.*;
import java.net.*;
import javax.crypto.*;
import java.security.*;
import java.security.spec.*;
import org.apache.commons.codec.binary.Base64;

public class cliente{

	public static void main(String[] args) {
		try
		{
			Socket cli = new Socket();
			InetSocketAddress add = new InetSocketAddress("localhost", 37773);
			cli.connect(add);
			InputStream is = cli.getInputStream();;
			OutputStream os = cli.getOutputStream();
                        
			//  Recibir Key publica
			System.out.println("Recibiendo Key Publica");
			byte[] leo = new byte[256];
			is.read(leo);
			KeyFactory kf = KeyFactory.getInstance("RSA");
			X509EncodedKeySpec xeks = new X509EncodedKeySpec(leo);
			PublicKey we = kf.generatePublic(xeks);
			System.out.println("Key publica recibida: ");
			System.out.println(we);
			
			// enviar key simetrica encriptada
			
			KeyGenerator generoClave = KeyGenerator.getInstance("AES");
			SecretKey clavePrivadaAES = generoClave.generateKey();
			String a = Base64.encodeBase64String(clavePrivadaAES.getEncoded());
			System.out.println("Mi clave privada: " + a);
			Cipher cp = Cipher.getInstance("RSA");
			cp.init(Cipher.ENCRYPT_MODE, we);
			String mCifra = new String(cp.doFinal(a.getBytes()));
			os.write(mCifra.getBytes());
			System.out.println("Enviado");
			
			
			//cierre
			cli.close();
		}catch(Exception e){
			e.printStackTrace();
		}
	}
}
