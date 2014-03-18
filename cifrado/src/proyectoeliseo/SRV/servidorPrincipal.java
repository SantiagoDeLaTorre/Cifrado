package proyectoeliseo.SRV;

import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.security.KeyPair;
import java.security.KeyPairGenerator;

public class servidorPrincipal {
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
}
