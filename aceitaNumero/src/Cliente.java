
import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;
import java.util.Scanner;

public class Cliente {
    public static void main(String[] args) {
        Socket cliente;

        try {
            // Conecta-se ao servidor
            cliente = new Socket("localhost", 12345);
            System.out.println("Conectado ao servidor.");

            // Configuração de leitura do servidor
            Scanner leitorServidor = new Scanner(cliente.getInputStream());
            // Configuração de envio para o servidor
            PrintStream envioServidor = new PrintStream(cliente.getOutputStream());

            boolean recebeMensagem = true;
            String resposta = "";

            // Loop principal do cliente
            while (recebeMensagem) {
                // Recebe mensagem do servidor
                String mensagemDoServidor = leitorServidor.nextLine();
                System.out.println(mensagemDoServidor);

                Scanner scannerCliente = new Scanner(System.in);
                resposta = scannerCliente.nextLine();
                envioServidor.println(resposta);
                
                if (leitorServidor.hasNextLine()) {
                    // Recebe mensagem do servidor
                    mensagemDoServidor = leitorServidor.nextLine();
                    System.out.println(mensagemDoServidor);

                    // // Adiciona uma condição de saída se necessário
                    // if ("SAIR".equalsIgnoreCase(mensagemDoServidor)) {
                    // recebeMensagem = false;
                    // break;
                    // }

                    // } else {
                }
                // Envia a resposta para o servidor
                if ("#".equalsIgnoreCase(resposta.trim())) {
                    recebeMensagem = false;
                }
            }

            // Fecha o socket do cliente
            cliente.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
