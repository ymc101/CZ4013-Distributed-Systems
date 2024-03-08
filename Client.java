package remote_file_access; 

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;

public class Client {

    private static final String FILE_NAME = "data.txt";
    private static final int DEFAULT_FRESHNESS_INTERVAL = 30; 

    private Map<String, String> cache;
    private long lastUpdated;
    private int freshnessInterval;

    public Client(int freshnessInterval) {
        this.cache = new HashMap<>();
        this.lastUpdated = 0;
        this.freshnessInterval = freshnessInterval;
    }

    private String readFile() throws IOException {
        if (isCacheFresh()) {
            System.out.println("Reading from cache...");
            return cache.get(FILE_NAME);
        }

        System.out.println("Reading from server...");
        String content = "";
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_NAME))) {
            String line;
            while ((line = reader.readLine()) != null) {
                content += line + "\n";
            }
        }
        cache.put(FILE_NAME, content);
        lastUpdated = System.currentTimeMillis();
        System.out.println("Writing to cache...");
        return content;
    }

    private boolean isCacheFresh() {
        return System.currentTimeMillis() - lastUpdated < freshnessInterval * 1000;
    }

    private void writeFile(String content) throws IOException {
        System.out.println("Writing to server...");
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_NAME))) {
            writer.write(content);
        }
        cache.put(FILE_NAME, content);
        lastUpdated = System.currentTimeMillis();
        System.out.println("Writing to cache...");
    }

    private void printMenu() {
        System.out.println("\nMain Menu:");
        System.out.println("1. Read File");
        System.out.println("2. Write File");
        System.out.println("3. Exit");
        System.out.print("Enter your choice: ");
    }

    public static void main(String[] args) throws Exception {
        int freshnessInterval = DEFAULT_FRESHNESS_INTERVAL;
        if (args.length > 0) {
            freshnessInterval = Integer.parseInt(args[0]);
        }

        Client client = new Client(freshnessInterval);

        Scanner scanner = new Scanner(System.in);
        int choice;

        do {
            client.printMenu();
            choice = scanner.nextInt();

            switch (choice) {
                case 1:
                    String content = client.readFile();
                    System.out.println("\nFile Content:\n" + content);

                    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    String formattedTime = formatter.format(client.lastUpdated);
                    System.out.println("Last Updated Time: " + formattedTime);

                    break;
                case 2:
                    System.out.print("Enter new content: ");
                    scanner.nextLine(); 
                    String newContent = scanner.nextLine();
                    client.writeFile(newContent);
                    break;
                case 3:
                    System.out.println("Exiting...");
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        } while (choice != 3);
    }
}
