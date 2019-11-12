package Główny;

import Główny.Klient;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.IOException;

public class Logowanie extends JFrame implements ActionListener, KeyListener {
    JFrame logowanie;
    JLabel nick;
    JTextField nick1;
    JButton accept;

    @Override
    public void actionPerformed(ActionEvent e) {
        Object c = e.getSource();
        String nick;
        if (c == accept) {
            nick = nick1.getText();
            try {
                Klient app = new Klient(nick);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            logowanie.dispose();
        }
    }


    public Logowanie() {
        logowanie = new JFrame("Chat-Menu");
        logowanie.setLayout(null);
        logowanie.setSize(400, 150);
        logowanie.setDefaultCloseOperation(EXIT_ON_CLOSE);
        logowanie.setVisible(true);
        logowanie.setLocationRelativeTo(null);
        logowanie.setFocusableWindowState(true);

        nick = new JLabel("Podaj Nick: ");
        nick.setBounds(20, 40, 80, 20);
        nick.setVisible(true);
        logowanie.add(nick);

        nick1 = new JTextField();
        nick1.setBounds(100, 40, 100, 20);
        nick1.setVisible(true);
        logowanie.add(nick1);

        accept = new JButton("Zatwierdź");
        accept.setBounds(100, 70, 100, 20);
        logowanie.add(accept);
        accept.addActionListener(this);

    }

    public static void main(String[] args) {
        Logowanie start = new Logowanie();
    }


    @Override
    public void keyTyped(KeyEvent e) {
        int a = e.getKeyCode();
        if (a == KeyEvent.VK_ENTER) {
            String nick;
            nick = nick1.getText();
            try {
                Klient app = new Klient(nick);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            logowanie.dispose();
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int a = e.getKeyCode();
        if (a == KeyEvent.VK_ENTER) {
            String nick;
            nick = nick1.getText();
            try {
                Klient app = new Klient(nick);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            logowanie.dispose();
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }
}
