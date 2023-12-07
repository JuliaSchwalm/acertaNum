
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

public class AtendeCliente implements Runnable {

    Socket cliente;
    int numeroCliente;
    Object mutex;
    String mensagem;
    private final List<Socket> clientes;

    // Random random = new Random();
    int numeroAleatorio;

    public AtendeCliente(Socket cliente, int numeroCliente, Object mutex, List<Socket> clientes, int numeroAleatorio) {
        this.cliente = cliente;
        this.numeroCliente = numeroCliente;
        this.mutex = mutex;
        this.clientes = clientes;
        this.numeroAleatorio = numeroAleatorio;
    }

    // criar um array de socket para atender clientes?
    // pra acessar os outros clientes, pra eles ouvirem
    public int teste(PrintStream pt, String mensagem) throws IOException {
        if (mensagem.equals("@")) {
            // Se o cliente optar por jogar novamente, sorteia um novo número
            numeroAleatorio = new Random().nextInt(10);
            System.out.println(numeroAleatorio);
            pt.println("Novo número sorteado. Tente novamente.");
            return numeroAleatorio;
        } else {
            // Se o cliente optar por sair, fecha o socket do cliente
            synchronized (mutex) {
                clientes.remove(cliente);
                pt.println("SAIR");
            }
            pt.close(); // Fechar o PrintStream após o uso
            return -1; // Sinalizando que o cliente optou por sair
        }
    }

    @Override
    public void run() {
        try {
            PrintStream pt = new PrintStream(cliente.getOutputStream());
            pt.println("Oi cliente " + numeroCliente + "! Tente acertar o número de 0 a 20");
            System.out.println(numeroAleatorio);

            do {
                BufferedReader leitor = new BufferedReader(new InputStreamReader(cliente.getInputStream()));
                mensagem = leitor.readLine();
                int numMsg = Integer.parseInt(mensagem);

                for (Socket clienteAtual : clientes) {
                    if (clienteAtual != cliente) {
                        PrintStream ptClienteAtual = new PrintStream(clienteAtual.getOutputStream());

                        if (numMsg == numeroAleatorio) {
                            ptClienteAtual.println("Cliente " + numeroCliente + " chutou: " + numMsg
                                    + " e venceu!!! Digite @ para jogar novamente e # para sair.");
                            pt.println("Parabéns você acertou, o número é " + numeroAleatorio
                                    + "! Digite @ para jogar novamente e # para sair.");
                            mensagem = leitor.readLine();
                            numeroAleatorio = teste(pt, mensagem);
                            break;
                        } else {
                            ptClienteAtual.println("Cliente " + numeroCliente + " chutou: " + numMsg);
                            pt.println("Aguarde o outro jogador chutar");
                        }
                    }
                }

            } while (true);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
