import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class ChatServer{
    private ServerSocket serverSocked;
    static ArrayList<Client> clients = new ArrayList<>();

    private ChatServer(int port) {
        try{
            // создаем серверный сокет
            serverSocked = new ServerSocket(port);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args){
        new ChatServer(1222).run();
    }

    public void run() throws NoSuchElementException {
        while(true) {
            System.out.println("Waiting...");
            // создаем клиента на своей стороне
            clients.add(new Client());
            System.out.println("Client connected!");
        }
    }

    public void sendAll(String message){
        for(Client client : clients) {
            client.receive(message);
        }
    }

    class Client implements Runnable {
        private Socket socket;
        private Scanner in;
        private PrintStream out;

        public Client() {
            try{
                // ждем клиента из сети
                socket = serverSocked.accept();
                // получаем потоки ввода и вывода, создаем удобные средства ввода и вывода
                in = new Scanner(socket.getInputStream());
                out = new PrintStream(socket.getOutputStream());
            } catch (IOException e) {
                e.printStackTrace();
            }
            // запускаем поток
            new Thread(this).start();
        }

        public void receive(String message){
            out.println(message);
        }

        @Override
        public void run() {
            try {
            out.println("Welcome to chat!");
                String input = in.nextLine();
                while (!input.equals("bye")) {
                    sendAll(input);
                    input = in.nextLine();
                }
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}


