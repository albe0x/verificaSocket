package com.client;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class Main {
    static BufferedReader in;
    static PrintWriter out;
    static Scanner inputTastiera;

    public static void main(String[] args) throws Exception {
        System.out.println("Indovina Numero");
        inputTastiera = new Scanner(System.in);

        System.out.println("ip server o invio per localhost:3000");
        String ip = inputTastiera.nextLine();
        int porta;
        if (ip.equals("")) {
            ip = "127.0.0.1";
            porta = 3000;
        } else {
            System.out.println("porta server");
            porta = inputTastiera.nextInt();
            inputTastiera.nextLine();
        }

        Socket s = new Socket(ip, porta);
        in = new BufferedReader(new InputStreamReader(s.getInputStream()));
        out = new PrintWriter(s.getOutputStream(), true);

        while (true) {
            String serverWelcome = in.readLine();
            String[] serverSplit = serverWelcome.split(" ");
            System.out.println("Indovina Numero tra " + serverSplit[4] + " e " + serverSplit[5]);
            while (true) {
                System.out.println("Scrivi:");
                System.out.println("un numero per provare a indovinare");
                System.out.println("R -> cambio range numeri");
                System.out.println("S -> stato della partita");
                System.out.println("N -> nuova partita");
                System.out.println("Q -> per uscire");
                String comando = inputTastiera.nextLine();
                System.out.println("\n\n\n\n\n\n\n\n");
                switch (comando) {
                    case "R":
                        changeRange();
                        break;
                    case "S":
                        gameStatus();
                        break;
                    case "N":
                        newGame();
                        break;
                    case "Q":
                        quit();
                        break;
                    default:
                        try {
                            gioca(Integer.parseInt(comando));
                        } catch (Exception e) {
                            System.out.println("Non e' stata selezionata un' opzione corretta");
                        }
                        break;
                }
            }

        }

    }

    public static void changeRange() throws Exception{
        System.out.println("Inserire numero minimo");
        String min = inputTastiera.nextLine();
        System.out.println("Inserire numero massimo");
        String max = inputTastiera.nextLine();
        out.println("RANGE " + min + " " + max);
        String inputServer = in.readLine();
        System.out.println("\n\n\n\n\n\n");
        if (inputServer.contains("OK")){
            System.out.println("Valori aggiornati");
            return;
        }
        if (inputServer.contains("ERR NOTALLOWED")){
            System.out.println("Al momento non e' possibile cambiare il range di numeri.");
            return;
        }
        System.out.println("Range inserito sbagliato");
    }

    public static void gioca(int n) throws Exception {
        out.println("GUESS " + n);
        String inputServer = in.readLine();
        if (inputServer.contains("OK CORRECT")) {
            System.out.println("numero segreto indovinato in " + inputServer.split("=")[1] + " tentativi");
            return;
        }
        switch (inputServer) {
            case "HINT HIGHER":
                System.out.println("il numero segreto e' piu' alto");
                break;

            case "HINT LOWER":
                System.out.println("il numero segreto e' piu' basso");
                break;

            default:
                if (inputServer.contains("OUTOFRANGE")){
                    System.out.println("valore inserito fuori range");
                    break;
                }
                System.out.println("valore inserito non valido");
                break;
        }

    }

    public static void gameStatus() throws Exception {
        out.println("STATS");
        String[] inputServer = in.readLine().split(" ");
        System.out.println("minimo : " + inputServer[2] + "\nmassimo : " + inputServer[3].split(";", 1)
                + "Tentativi usati : " + inputServer[5]);
    }

    public static void newGame() throws Exception {
        out.println("NEW");
        String serverWelcome = in.readLine();
        String[] serverSplit = serverWelcome.split(" ");
        System.out.println("Indovina Numero tra " + serverSplit[4] + " e " + serverSplit[5]);
    }

    public static void quit() {
        out.println("QUIT");
        System.out.println("CONNESSIONE AL SERVER INTERROTTA DALL' UTENTE");
    }

}