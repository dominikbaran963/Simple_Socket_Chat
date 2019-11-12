package Główny;

import javax.swing.*;
import javax.swing.text.DefaultCaret;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Klient extends JFrame implements ActionListener, KeyListener {
    public static final int PORT = 5000;
    public static final String IP = "127.0.0.1";
    public BufferedReader bufferedReader;
    public PrintWriter printWriter;
    public String imie;
    public Socket socket;

    @Override
    public void actionPerformed(ActionEvent e) {
        Object a = e.getSource();
        if (a == send) {
            try {
                String str = textField.getText();
                if (!str.equalsIgnoreCase("q")) {
                    printWriter.println(imie + ":  " + str); // wysyłam na serwer
                    //scrollLogger.dodajLinie(imie + ":  " + str);
                    printWriter.flush();
                    textField.setText("");
                } else {
                    printWriter.println(imie + " rozłączył się ");
                    scrollLogger.dodajLinie(imie + " rozłączył się ");
                    printWriter.flush();
                    printWriter.close();
                    socket.close();
                }
            } catch (Exception ek) {
            }
        } else if (a == end_session) {
            int p = Sprawdz_Plec(imie);
            if (p == 0) {
                System.out.println("Użytkownik " + imie + " rozłączyła się. ");
                printWriter.println("Użytkownik " + imie + " rozłączyła się. ");
            } else if (p == 1) {
                System.out.println("Użytkownik " + imie + " rozłączył się");
                printWriter.println("Użytkownik " + imie + " rozłączył się. ");
            }
            printWriter.flush();
            try {
                socket.close();
                printWriter.flush();
                printWriter.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    public int Sprawdz_Plec(String nick) {
        char[] spr = nick.toCharArray();
        if (spr[spr.length - 1] == 'a') {
            return 0;
        } else {
            return 1;
        }
    }


    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int a = e.getKeyCode();
        if (a == KeyEvent.VK_ENTER) {
            try {
                String str = textField.getText();
                if (!str.equalsIgnoreCase("q")) {
                    printWriter.println(imie + ":  " + str); // wysyłam na serwer
                    //scrollLogger.dodajLinie(imie + ":  " + str);
                    printWriter.flush();
                    textField.setText("");
                } else {
                    printWriter.println(imie + " rozłączył się ");
                    scrollLogger.dodajLinie(imie + " rozłączył się ");
                    printWriter.flush();
                    printWriter.close();
                    socket.close();
                }

            } catch (Exception ek) {
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }

    // gui atrybuty
    JFrame app;
    ScrollLogger scrollLogger;
    JTextField textField;
    JButton send, end_session;

    public Klient(String nick) throws IOException {
        imie = nick;
        app = new JFrame("Chat");
        app.setSize(450, 750);
        app.setLayout(null);
        app.setVisible(true);
        app.setDefaultCloseOperation(EXIT_ON_CLOSE);
        //app.setFocusableWindowState(false);
        app.setLocationRelativeTo(null);

        //textArea = new JTextArea();
        //textArea.setWrapStyleWord(true);
        scrollLogger = new ScrollLogger();
        scrollLogger.setBounds(50, 50, 350, 500);
        scrollLogger.setVisible(true);
        app.add(scrollLogger);

        // text field
        textField = new JTextField(" ");
        textField.setBounds(50, 550, 300, 50);
        textField.setVisible(true);
        app.add(textField);

        send = new JButton("Wyślij");
        send.setBounds(350, 575, 80, 50);
        app.add(send);
        send.addActionListener(this);

        Toolkit zestaw = Toolkit.getDefaultToolkit(); // dodajemy ikonke
        Image rys = zestaw.getImage("images.jpg");
        app.setIconImage(rys);

        app.addKeyListener(this);
        textField.addKeyListener(this);

        socket = new Socket(IP, PORT);
        System.out.println("Podłączono do " + socket);
        scrollLogger.dodajLinie("Podłączono do " + socket);
        printWriter = new PrintWriter(socket.getOutputStream()); // przechwytuje wyjście z socketa , wysyła
        bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream())); // przechwytuje wejscie z socketa , odbiera
        Thread tt = new Thread(new Odbiorca());
        tt.start();
        printWriter.println("Witaj " + imie); // wysyłam na serwer
        printWriter.flush();

        end_session = new JButton("Rozłącz");
        end_session.setBounds(330, 0, 100, 30);
        end_session.setVisible(true);
        end_session.addActionListener(this);
        app.add(end_session);
    }

    class ScrollLogger extends JScrollPane {
        JTextArea out = new JTextArea();
        DefaultCaret caret = (DefaultCaret) out.getCaret();

        public ScrollLogger() {
            super();
            caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
            setViewportView(out);
            setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
            out.setDisabledTextColor(Color.BLACK);
            out.setEnabled(false);
        }

        public void dodajLinie(String text) {
            out.append(">> " + text + "\n");
        }

    }

    class Odbiorca implements Runnable {
        @Override
        public void run() {
            String wiadomość;
            try {
                while ((wiadomość = bufferedReader.readLine()) != null) {
                    String subString[] = wiadomość.split(":");
                    if (!subString[0].equals(imie)) {
                        System.out.println(wiadomość);
                        scrollLogger.dodajLinie(wiadomość);
                    } else {
                        scrollLogger.dodajLinie(wiadomość);
                    }
                }
            } catch (Exception ex) {
                System.out.println("Połączenie zostało zakończone");
                scrollLogger.dodajLinie("Połączenie zostało zakończone");
            }
        }
    }
}
