import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Iterator;

public class chat {

    ArrayList klientArrayList;
    PrintWriter printwrtiter;

    //uruchomienie program
    public static void main(String[] args) {
        chat ch = new chat();
        ch.startSerwer();
    }

    // start serwera
    public void startSerwer() {
        klientArrayList = new ArrayList();
        try {
            Server_spec ServerSocket = new Server_spec();
            while (true) {
                Socket socket = ServerSocket.main_socket.accept();
                ServerSocket.klient_size++;
                System.out.println("Połączono nowego użytkownika " + ServerSocket.main_socket);
                System.out.println("Obecna ilość użytkowników na serwerze: " + ServerSocket.klient_size);
                printwrtiter = new PrintWriter(socket.getOutputStream());
                klientArrayList.add(printwrtiter);
                Thread t = new Thread(new SerwerKlient(socket));
                t.start();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    class Server_spec implements Runnable {
        ServerSocket main_socket;
        int klient_size = 0;

        public Server_spec() throws IOException {
            ServerSocket serversocket = new ServerSocket(5000);
            main_socket = serversocket;
        }

        @Override
        public void run() {

        }
    }

    // klasa wewnetrzna
    class SerwerKlient implements Runnable {

        Socket socket;
        BufferedReader bufferedreader;

        //konstruktor
        public SerwerKlient(Socket sockerklient) {
            try {
                socket = sockerklient;
                System.out.println("Połączono nowego klienta ");
                bufferedreader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            } catch (Exception ex) {
                ex.printStackTrace();
            }

        }

        @Override
        public void run() {
            String str;
            PrintWriter pw = null;
            try {
                while ((str = bufferedreader.readLine()) != null) {
                    if (str == "EXIT_CONS!") {
                        socket.close();
                        System.out.println("Użytkowni rozłączony!");

                    } else {
                        System.out.println("Odebrano>> " + str);
                        Iterator it = klientArrayList.iterator();
                        while (it.hasNext()) {
                            pw = (PrintWriter) it.next();
                            pw.println(str);
                            pw.flush();
                        }
                    }
                }
            } catch (Exception exc) {
// można dać
            }
        }
    }
}
