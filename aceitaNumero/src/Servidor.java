
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Random;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Servidor {

    public static void main(String[] args) {
        
        ServerSocket servidor;
        int contador = 1;
        Object mutex = new Object();
          List<Socket> clientes = new ArrayList<>();
                Random random = new Random();
                int numeroAleatorio = random.nextInt(10);
                
        try {
            servidor = new ServerSocket(12345);
            while (true) {
                // Esperando cliente
                Socket cliente = servidor.accept();
                System.out.println("Novo cliente conectado: " + contador);

                // Adiciona o socket do cliente à lista
                synchronized (mutex) {
                    clientes.add(cliente);
                }

                // Cria uma thread para cada cliente
                Thread t = new Thread(new AtendeCliente(cliente, contador, mutex, clientes, numeroAleatorio));
                t.start();

                contador++;
            }
        } catch (IOException ex) {
            Logger.getLogger(Servidor.class.getName()).log(Level.SEVERE, null, ex);
        }
        System.out.println("SERVIDOR FECHADO");
    }
}
