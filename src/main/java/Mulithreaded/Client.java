package Mulithreaded;


import DTOs.Fighter;
import com.google.gson.Gson;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.InputMismatchException;
import java.util.Scanner;

public class Client {
    public static void main(String[] args) {
        Client client = new Client();
        client.start();
    }

    public void start() {
        Scanner in = new Scanner(System.in);
        try {
            Socket socket = new Socket("localhost", 8080);  // connect to server socket
            System.out.println("Mulithreaded.Client: Port# of this client : " + socket.getLocalPort());
            System.out.println("Mulithreaded.Client: Port# of Mulithreaded.Server :" + socket.getPort());

            System.out.println("Mulithreaded.Client message: The Mulithreaded.Client is running and has connected to the server");

            OutputStream os = socket.getOutputStream();
            PrintWriter socketWriter = new PrintWriter(os, true);   // true => auto flush buffers
            Scanner socketReader = new Scanner(socket.getInputStream());  // wait for, and retrieve the reply


            final String MENU_ITEMS = "\n*** Main Menu ***\n" + "1. Find by ID\n" + "2. Find all \n" + "3. Add a fighter\n" + "4. Delete by ID\n" + "5. Fighters with positive win ratio \n" + "6. Exit\n" + "Enter Option [1-6]\n";


            final int FIND_BY_ID = 1;
            final int FIND_ALL_JSON = 2;
            final int ADD_A_FIGHTER = 3;
            final int DELETE_BY_ID = 4;
            final int POSITIVE_RATIO = 5;
            final int EXIT = 6;

            Scanner keyboard = new Scanner(System.in);
            int option = 0;

            do {
                System.out.println("\n" + MENU_ITEMS);
                try {
                    String userInput = keyboard.nextLine();
                    option = Integer.parseInt(userInput);

                    switch (option) {

                        case FIND_BY_ID:

                            System.out.print("Enter ID of Fighter: ");
                            String id = keyboard.nextLine();

                            socketWriter.println("findByID " + id);

                            System.out.println("Message from server: \n" + socketReader.nextLine());

                            promptEnterKey();
                            break;
                        case FIND_ALL_JSON:
                            System.out.println();
                            socketWriter.println("findAll");
                            String allFighters = socketReader.nextLine();
                            Gson gsonParser = new Gson();
                            Fighter[] fighters = gsonParser.fromJson(allFighters, Fighter[].class);
                            System.out.println("Mulithreaded.Client message: All fighters in JSON format ");

                            if (fighters.length < 1) {
                                System.out.println("No Fighters found");
                            } else {
                                for (Fighter fighter : fighters) {
                                    System.out.println(fighter);
                                }
                            }


                            promptEnterKey();
                            break;

                        case ADD_A_FIGHTER:
                            String json = createNewFighterJSON();
                            socketWriter.println("addFighter");
                            socketWriter.println(json);

                            System.out.println("Message from server: " + socketReader.nextLine());

                            promptEnterKey();
                            break;
                        case DELETE_BY_ID:

                            System.out.print("Enter ID of Fighter: ");
                            String dID = keyboard.nextLine();

                            socketWriter.println("deleteByID " + dID);

                            System.out.println("Message from server: " + socketReader.nextLine());

                            promptEnterKey();
                            break;
                        case POSITIVE_RATIO:
                            socketWriter.println("positiveWin");

                            Gson gsonParse = new Gson();

                            String allFighter = socketReader.nextLine();

                            Fighter[] fighter = gsonParse.fromJson(allFighter, Fighter[].class);
                            System.out.println("Mulithreaded.Client message: All fighters in JSON format ");

                            if (fighter.length < 1) {
                                System.out.println("No Fighters found");
                            } else {
                                for (Fighter fight : fighter) {
                                    System.out.println(fight);
                                }
                            }
                            promptEnterKey();

                        case EXIT:
                            System.out.println("Exit menu option chosen");
                            break;
                        default:
                            System.out.println("Invalid Option - please enter number in range");
                            break;
                    }

                } catch (InputMismatchException | NumberFormatException e) {
                    System.out.print("Invalid option - please enter number in range");
                }
            } while (option != EXIT);

            System.out.println("\nExiting Main Menu, goodbye.");

            socketWriter.close();
            socketReader.close();
            socket.close();

        } catch (IOException e) {
            System.out.println("Mulithreaded.Client message: IOException: " + e);
        }
    }

    public void promptEnterKey() {
        System.out.println("\nPress \"ENTER\" to continue...");
        Scanner scanner = new Scanner(System.in);
        scanner.nextLine();
    }

    public String createNewFighterJSON() {
        boolean check = false;
        String name;
        int wins = 0;
        int losses = 0;
        Scanner keyboard = new Scanner(System.in);

        System.out.println("Enter name of fighter");
        name = keyboard.nextLine();


        while (true) {
            System.out.print("Enter wins: ");

            while (!keyboard.hasNextInt()) {
                String input = keyboard.next();

                System.out.println("Invalid input! - Please enter only int values");
                System.out.print("Enter wins: ");
            }
            if (check) {
                break;
            }
            wins = keyboard.nextInt();
            break;
        }

        while (true) {
            System.out.print("Enter losses: ");

            while (!keyboard.hasNextInt()) {
                String input = keyboard.next();

                System.out.println("Invalid input! - Please enter only int values");
                System.out.print("Enter losses: ");
            }
            if (check) {
                break;
            }
            losses = keyboard.nextInt();
            break;
        }

        Fighter newFighter = new Fighter(name, wins, losses);

        Gson gsonParser = new Gson();

        return gsonParser.toJson(newFighter);


    }
}
