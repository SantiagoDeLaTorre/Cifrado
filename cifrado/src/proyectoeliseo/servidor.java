package proyectoeliseo;

import java.io.*;
import java.net.*;
import javax.crypto.*;
import java.security.*;
import java.security.spec.*;
import javax.xml.bind.DatatypeConverter;
import org.apache.commons.codec.binary.Base64;



public class servidor extends Thread{

	private PublicKey  miClavePublica = null;
	private PrivateKey  miClavePrivada = null;
	private Socket cliSock = null;
	private SecretKey clavePriSi = null;
	private KeyPair miCipher = null;
	
	public servidor(Socket so, PublicKey pu, PrivateKey pri, KeyPair ci){
		this.miClavePrivada = pri;
		this.miClavePublica = pu;
		this.cliSock = so;
		this.miCipher = ci;
	}
	
	public static void main(String[] args) {
		try{
			/*ĝenero claves */
			
			KeyPairGenerator keygen = KeyPairGenerator.getInstance("RSA");
			KeyPair kp = keygen.generateKeyPair();
			ServerSocket miServer = new ServerSocket();
			InetSocketAddress add = new InetSocketAddress("localhost", 37773);
			miServer.bind(add);
			while(miServer != null){
				Socket nuevoSocket = miServer.accept();
				System.out.println("Connection Done.");
				new servidor(nuevoSocket, kp.getPublic(), kp.getPrivate(), kp).start();
			}
			
		}catch(Exception e){
			e.printStackTrace();
		}
		

	}

	public void run(){
		try{
			System.out.println("run");
			InputStream is = cliSock.getInputStream();
			System.out.println("is conectsdo");
			OutputStream os = cliSock.getOutputStream();
			System.out.println("os conectado");
			byte[] array = miClavePublica.getEncoded();
			Base64 encoder = new Base64();
			String a = encoder.encodeBase64String(array);
			os.write(array);
			System.out.println(new String(a));
			KeyFactory kf = KeyFactory.getInstance("RSA");
			X509EncodedKeySpec xeks = new X509EncodedKeySpec(array);
			PublicKey we = kf.generatePublic(xeks);
			System.out.println(we);
			
			//recepcion de clave simetrica 
			
			byte[] leo = new byte[256/8];
			is.read(leo);
			String aLeo = new String(leo);
			System.out.println("leo: " + leo);
			System.out.println("aLeo: " + aLeo);
			Cipher cp = Cipher.getInstance("RSA");
			cp.init(Cipher.DECRYPT_MODE, miClavePrivada);
			System.out.println("I no have idea");
			String descifro = new String(cp.doFinal(aLeo.getBytes()));
			System.out.println("Decodeado: " + descifro);
			byte[] aw = DatatypeConverter.parseHexBinary(descifro);
			byte[] encodedKey     = Base64.decodeBase64(descifro);
		    SecretKey oKey = new SecretKeySpec(descifro.getBytes(), 0, encodedKey.length, "AES");
			String adw = Base64.encodeBase64String(oKey.getEncoded());
			System.out.println("clave privada " + adw);
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	

}
