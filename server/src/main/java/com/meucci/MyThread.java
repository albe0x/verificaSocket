package com.meucci;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.concurrent.ThreadLocalRandom;

public class MyThread extends Thread{
    BufferedReader in;
    PrintWriter out;
    Socket s;
    int min = 1;
    int max = 100;
    int tries = 0;
    int secretNumber;


    public MyThread (Socket s) {
        this.s = s;
        secretNumber = ThreadLocalRandom.current().nextInt(min, max + 1);
    }


    @Override
    public void run() {
        try {
            myrun();
        } catch (IOException e) {
            out.println("ERR INTERNAL");
            System.out.println("Problemi problemi !!");
        }
    }

    public void myrun() throws IOException {
        in = new BufferedReader(new InputStreamReader(s.getInputStream()));
        out = new PrintWriter(s.getOutputStream(), true);
        out.println("WELCOME INDOVINA v1 RANGE "+ min + " " + max);

        while(true){
        String line = in.readLine();          
        String[] parts = line.split(" ");
        switch(parts[0]){
            case "GUESS":
                guess(parts);
            break;
            case "RANGE":
                range(parts);
            break;
            case "STATS":
                stats();
            break;
            case "NEW":
                newGame();
            break;
            case "QUIT":
                out.println("BYE");
            return;
            default:
            out.println("ERR UNKNOWNCMD");
            break;
        }
        }


    }

    public void range(String[] parts)
    {
        if(!(tries == 0)){
            out.println("ERR NOTALLOWED");
            return;
        }
        try {
            int newMin = Integer.parseInt(parts[1]);
            int newMax = Integer.parseInt(parts[2]);
            if(newMin < newMax){
                min = newMin;
                max = newMax;
            }else{
            out.println("ERR SYNTAX");
            return;
            }
        } catch (NumberFormatException ex) {
            out.println("ERR SYNTAX");
            return;
        } catch (ArrayIndexOutOfBoundsException ex){
            out.println("ERR SYNTAX");
            return;
        }    
        secretNumber = ThreadLocalRandom.current().nextInt(min, max + 1);   
        out.println("OK RANGE "+ min + " " + max);
    }

    public void guess(String[] parts)
    {
        int value;
        try {
            value = Integer.parseInt(parts[1]);
        } catch (NumberFormatException ex) {
            out.println("ERR SYNTAX");
            return;
        } catch (ArrayIndexOutOfBoundsException ex){
            out.println("ERR SYNTAX");
            return;
        }
        if(value < min || value > max){
            out.println("ERR OUTOFRANGE "+ min + " " + max);
            return;
        }
        tries ++;
        if (value == secretNumber){
            out.println("OK CORRECT in T="+tries);
            try {
                s.close();
            } catch (Exception e) {
                System.out.println("Probl// TODO: handle exceptionemi problemi !!");
            }
            return;
        }
        if (value < secretNumber){
            out.println("HINT HIGHER");
        }else{
            out.println("HINT LOWER");
        } 
    }

    public void stats()
    {
        out.println("INFO RANGE " + min +" "+ max +"; TRIES " + tries);
    }

    public void newGame()
    {
        out.println("OK NEW");
        secretNumber = ThreadLocalRandom.current().nextInt(min, max + 1); 
        tries = 0;  
        out.println("WELCOME INDOVINA v1 RANGE "+ min + " " + max);
    }
}
