package proyectoeliseo;

public class Principal {
    
    public static void main(String[] args) {
        try{
            new servidorPrincipal().start();
        }catch(Exception e){
            e.printStackTrace();
        }

    }
}
