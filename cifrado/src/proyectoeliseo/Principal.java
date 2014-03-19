package proyectoeliseo;

public class Principal {
    
    public static void main(String[] args) {
        try{
            new servidorPrincipal().start();
            Thread.sleep(400);
            cliente a = new cliente();
        }catch(Exception e){
            e.printStackTrace();
        }

    }
}
