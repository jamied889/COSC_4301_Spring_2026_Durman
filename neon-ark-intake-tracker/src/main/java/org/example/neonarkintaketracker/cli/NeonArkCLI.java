package org.example.neonarkintaketracker.cli;

import java.util.Scanner;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class NeonArkCLI {

    private final Scanner scanner;
    private static final String BASE_URL = "http://localhost:8080";

    public NeonArkCLI() {
        scanner = new Scanner(System.in);
    }

    private static final ObjectMapper objectMapper = new ObjectMapper();

    public void start() {
        boolean running = true;

        while (running) {
            printMenu();

            System.out.print("Select an option: ");
            String input = scanner.nextLine();

            switch (input) {
                case "1":
                    listCreatures();
                    break;
                case "2":
                    viewCreatureById();
                    break;
                case "3":
                    createCreature();
                    break;
                case "4":
                    renameCreature();
                    break;
                case "5":
                    removeCreature();
                    break;
                case "6":
                    viewObservations();
                    break;
                case "7":
                    findByFeedingTime();
                    break;
                case "8":
                    viewUsers();
                    break;
                case "0":
                    running = confirmExit();
                    break;
                default:
                    System.out.println("Invalid option.");
            }
        }
    }

    private void printMenu() {
        System.out.println("\n=====================================");
        System.out.println("       NEON ARK CLI SYSTEM");
        System.out.println("=====================================");
        System.out.println("1. List all creatures");
        System.out.println("2. View creature by ID");
        System.out.println("3. Register new creature");
        System.out.println("4. Rename creature");
        System.out.println("5. Remove creature");
        System.out.println("6. View creature observations/notes");
        System.out.println("7. Find creatures by feeding time");
        System.out.println("--- Admin Only ---");
        System.out.println("8. View all system users");
        System.out.println("0. Exit");
        System.out.println("-------------------------------------");
    }

    private boolean confirmExit() {
        System.out.print("Are you sure you want to exit? (Y/N): ");
        String input = scanner.nextLine();
        return !input.equalsIgnoreCase("Y");
    }

    private void listCreatures() {
        String response;

        response = sendRequest("GET", "/api/creatures", null);

        printCreatureTable(response);
    }

    private void viewCreatureById() {
        Long id;

        id = readLong("Enter creature ID: ");

        if (id == null) {
            return;
        }

        String response = sendRequest("GET", "/api/creatures/" + id, null);
        System.out.println(response);
    }

    private void createCreature() {
        String name;
        String species;
        String dangerLevel;
        String condition;
        String notes;
        Long habitatId;
        String jsonBody;
        String response;

        name = readRequiredText("Enter creature name: ");
        if (name == null) {
            return;
        }

        species = readRequiredText("Enter species: ");
        if (species == null) {
            return;
        }

        dangerLevel = readRequiredText("Enter danger level: ");
        if (dangerLevel == null) {
            return;
        }

        condition = readRequiredText("Enter condition: ");
        if (condition == null) {
            return;
        }

        System.out.print("Enter notes: ");
        notes = scanner.nextLine().trim();

        habitatId = readLong("Enter habitat ID: ");
        if (habitatId == null) {
            return;
        }

        jsonBody = "{"
                + "\"name\":\"" + escapeJson(name) + "\","
                + "\"species\":\"" + escapeJson(species) + "\","
                + "\"dangerLevel\":\"" + escapeJson(dangerLevel) + "\","
                + "\"condition\":\"" + escapeJson(condition) + "\","
                + "\"notes\":\"" + escapeJson(notes) + "\","
                + "\"habitatId\":" + habitatId
                + "}";

        response = sendRequest("POST", "/api/creatures", jsonBody);

        printApiResponse(response);
    }

    private void renameCreature() {
        Long id;
        String newName;
        String jsonBody;
        String response;

        id = readLong("Enter creature ID to rename: ");

        if (id == null) {
            return;
        }

        newName = readRequiredText("Enter new creature name: ");

        if (newName == null) {
            return;
        }

        if (!confirmAction("Are you sure you want to rename this creature? (Y/N): ")) {
            System.out.println("Rename canceled.");
            return;
        }

        jsonBody = "{"
                + "\"newName\":\"" + escapeJson(newName) + "\""
                + "}";

        response = sendRequest("PUT", "/api/creatures/" + id + "/name", jsonBody);

        printApiResponse(response);
    }

    private void removeCreature() {
        Long id;
        String response;

        id = readLong("Enter creature ID to remove: ");

        if (id == null) {
            return;
        }

        if (!confirmAction("Are you sure you want to remove this creature? (Y/N): ")) {
            System.out.println("Remove canceled.");
            return;
        }

        response = sendRequest("DELETE", "/api/creatures/" + id, null);

        printApiResponse(response);
    }

    private void viewObservations() {
        Long id;
        String response;

        id = readLong("Enter creature ID: ");

        if (id == null) {
            return;
        }

        response = sendRequest("GET", "/api/creatures/" + id + "/observations", null);

        printObservationTable(response);
    }

    private void findByFeedingTime() {
        String time;
        String response;

        time = readRequiredText("Enter feeding time (HH:MM): ");

        if (time == null) {
            return;
        }

        if (!time.matches("\\d{2}:\\d{2}")) {
            System.out.println("Invalid time format. Please use HH:MM.");
            return;
        }

        response = sendRequest("GET", "/api/feedings?time=" + time, null);

        printApiResponse(response);
    }

    private void viewUsers() {
        String response;

        response = sendRequest("GET", "/api/admin/users", null);

        printUserTable(response);
    }

    private String sendRequest(String method, String endpoint, String jsonBody) {
        try {
            URI uri = URI.create(BASE_URL + endpoint);
            HttpURLConnection connection = (HttpURLConnection) uri.toURL().openConnection();

            connection.setRequestMethod(method);
            connection.setRequestProperty("Accept", "application/json");

            if (jsonBody != null) {
                connection.setRequestProperty("Content-Type", "application/json");
                connection.setDoOutput(true);

                try (OutputStream outputStream = connection.getOutputStream()) {
                    outputStream.write(jsonBody.getBytes());
                }
            }

            int statusCode = connection.getResponseCode();

            BufferedReader reader;
            if (statusCode >= 200 && statusCode < 300) {
                reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            } else {
                reader = new BufferedReader(new InputStreamReader(connection.getErrorStream()));
            }

            StringBuilder response = new StringBuilder();
            String line;

            while ((line = reader.readLine()) != null) {
                response.append(line);
            }

            return "HTTP " + statusCode + "\n" + response;
        } catch (Exception exception) {
            return "Error: Unable to connect to the Neon Ark API. Make sure the Spring Boot server is running.";
        }
    }

    private Long readLong(String prompt) {
        System.out.print(prompt);

        try {
            return Long.parseLong(scanner.nextLine().trim());
        } catch (NumberFormatException exception) {
            System.out.println("Invalid input. Please enter a whole number.");
            return null;
        }
    }

    private String readRequiredText(String prompt) {
        String input;

        System.out.print(prompt);
        input = scanner.nextLine().trim();

        if (input.isEmpty()) {
            System.out.println("Invalid input. This value is required.");
            return null;
        }

        return input;
    }

    private boolean confirmAction(String prompt) {
        String input;

        System.out.print(prompt);
        input = scanner.nextLine().trim();

        return input.equalsIgnoreCase("Y");
    }

    private void printApiResponse(String response) {
        System.out.println();
        System.out.println("-------------------------------------");
        System.out.println(response);
        System.out.println("-------------------------------------");
    }

    private String escapeJson(String value) {
        return value.replace("\\", "\\\\")
                .replace("\"", "\\\"");
    }

    private void printCreatureTable(String response) {
        try {
            String json;
            JsonNode creatures;

            json = response.substring(response.indexOf("\n") + 1);
            creatures = objectMapper.readTree(json);

            System.out.println();
            System.out.printf("%-5s %-18s %-18s %-15s %-12s%n",
                    "ID", "Name", "Species", "Habitat", "Status");
            System.out.println("-----------------------------------------------------------------------");

            if (!creatures.isArray() || creatures.isEmpty()) {
                System.out.println("No creatures found.");
                return;
            }

            for (JsonNode creature : creatures) {
                System.out.printf("%-5s %-18s %-18s %-15s %-12s%n",
                        creature.path("id").asText(""),
                        creature.path("name").asText(""),
                        creature.path("species").asText(""),
                        creature.path("habitatName").asText(""),
                        creature.path("status").asText(""));
            }

        } catch (Exception exception) {
            printApiResponse(response);
        }
    }

    private void printObservationTable(String response) {
        try {
            String json;
            JsonNode root;
            JsonNode observations;

            json = response.substring(response.indexOf("\n") + 1);
            root = objectMapper.readTree(json);
            observations = root.path("observations");

            System.out.println();
            System.out.println("Creature: " + root.path("creatureName").asText(""));
            System.out.println("Habitat:  " + root.path("habitatName").asText(""));
            System.out.println();

            System.out.printf("%-5s %-18s %-22s %-40s%n",
                    "ID", "Author", "Created At", "Note");
            System.out.println("-----------------------------------------------------------------------------------------");

            if (!observations.isArray() || observations.isEmpty()) {
                System.out.println("No observations found for this creature.");
                return;
            }

            for (JsonNode observation : observations) {
                System.out.printf("%-5s %-18s %-22s %-40s%n",
                        observation.path("id").asText(""),
                        observation.path("author").asText(""),
                        observation.path("createdAt").asText(""),
                        observation.path("note").asText(""));
            }
        } catch (Exception exception) {
            printApiResponse(response);
        }
    }

    private void printUserTable(String response) {
        try {
            String json;
            JsonNode users;

            json = response.substring(response.indexOf("\n") + 1);
            users = objectMapper.readTree(json);

            System.out.println();
            System.out.printf("%-5s %-22s %-28s %-14s %-12s%n",
                    "ID", "Full Name", "Email", "Phone", "Role");
            System.out.println("-------------------------------------------------------------------------------------");

            if (!users.isArray() || users.isEmpty()) {
                System.out.println("No system users found.");
                return;
            }

            for (JsonNode user : users) {
                System.out.printf("%-5s %-22s %-28s %-14s %-12s%n",
                        user.path("id").asText(""),
                        user.path("fullName").asText(""),
                        user.path("email").asText(""),
                        user.path("phone").asText(""),
                        user.path("role").asText(""));
            }
        } catch (Exception exception) {
            printApiResponse(response);
        }
    }
}
