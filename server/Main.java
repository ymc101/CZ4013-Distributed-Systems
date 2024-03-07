package server;

import java.util.Scanner;
import java.io.IOException;
import java.net.SocketException;

public class Main {
    public static void main (String[] args){
        
        Scanner sc = new Scanner(System.in);
        System.out.println("Select invocation semantic type: (Enter 1 or 2)");
        System.out.println("1. At-least-once invocation semantic");
        System.out.println("2. At-most-once invocation semantic");
        String userInput = sc.nextLine();
        while (userInput != "1" && userInput != "2") {
            System.out.println("Invalid Input! Please enter 1 or 2:");
            userInput = sc.nextLine();
        }
        sc.close();

        Server server = null;
        try {
            server = new Server();
        }
        catch (SocketException e) {
            e.printStackTrace();
        }

        if(userInput == "1") {
            try {
                server.execute_ALO();
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
        else if(userInput == "2") {
            try {
                server.execute_AMO();
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

}