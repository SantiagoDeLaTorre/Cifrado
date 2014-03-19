package proyectoeliseo;

import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;

public class servidorPrincipal extends Thread{
    
        PublicKey clavePub;
        PrivateKey clavePriv;
        
    public void run() {        
		try{
                    KeyPairGenerator keygen = KeyPairGenerator.getInstance("RSA");
                    KeyPair kp = keygen.generateKeyPair();
                    clavePriv = kp.getPrivate();
                    clavePub = kp.getPublic();
                    ServerSocket miServer = new ServerSocket();
                    InetSocketAddress add = new InetSocketAddress("localhost", 37773);
                    miServer.bind(add);
                    while(miServer != null){
                        Socket nuevoSocket = miServer.accept();
                        System.out.println("[Main Server] Connection Done.");
                        new hiloServidor(nuevoSocket, clavePub, clavePriv).start();
                    }	
		}catch(Exception e){
			e.printStackTrace();
		}
	}
}
