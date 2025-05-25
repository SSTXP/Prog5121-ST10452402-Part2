/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.mycompany.st10452402part2prog5121;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import javax.swing.*;
import java.io.*;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;
 /*
 * @author sohan
 * * Student Number: ST10452402    
 * Name and Surname: Sohan Seeth    
 * Module: PROG5121 
 * Module Code: Programming 1A 
 * Assessment Type: Assignment 1(Part 1 & 2 )
 * Lecturer Name: Mr Fakazi Ngema 
 * 
 * https://youtube/yHP4Cew7WXE?si=pAHC8LO4RF6wl33r -- video of working code (PART 1)
 * 
 */

public class ST10452402PART2PROG5121 {

    private String registeredFirstName;
    private String registeredLastName;
    private String registeredUsername;
    private String registeredPassword;

    private final ArrayList<Message> sentMessages = new ArrayList<>();

    private int messageCounter = 0;

    class Message {
        String messageNumber;
        String recipientNumber;
        String content;
        String messageHash;
        int status;

        Message(String messageNumber, String recipientNumber, String content) {
            this.messageNumber = messageNumber;
            this.recipientNumber = recipientNumber;
            this.content = content;
            this.messageHash = generateMessageHash(messageNumber, content);
            this.status = 0;
        }

        private String generateMessageHash(String messageNumber, String content) {
            String[] words = content.split("\\s+");
            String firstWord = words.length > 0 ? words[0] : "";
            String lastWord = words.length > 1 ? words[words.length - 1] : firstWord;
            String prefix = messageNumber.substring(0, 2);
            return (prefix + ":" + messageNumber + ":" + firstWord + lastWord).toUpperCase();
        }

        public String getStatusIcon() {
            return switch (status) {
                case 0 -> "\u23F3 Sending...";
                case 1 -> "\u2713 Sent";
                case 2 -> "\u2713\u2713 Received";
                case 3 -> "\u2713\u2713 (Read)";
                default -> "";
            };
        }

        @Override
        public String toString() {
            return "Message Number: " + messageNumber + "\n"
                    + "Recipient: " + recipientNumber + "\n"
                    + "Hash: " + messageHash + "\n"
                    + "Content: " + content + "\n"
                    + "Status: " + getStatusIcon();
        }
    }

    public boolean checkUserName(String username) {
        return username.contains("_") && username.length() <= 5;
    }

    public boolean checkPasswordComplexity(String password) {
        boolean length = password.length() >= 8;
        boolean capital = Pattern.compile("[A-Z]").matcher(password).find();
        boolean digit = Pattern.compile("\\d").matcher(password).find();
        boolean special = Pattern.compile("[!@#$%^&*()_+=|<>?{}\\[\\]~-]").matcher(password).find();
        return length && capital && digit && special;
    }

    public boolean checkCellPhoneNumber(String number) {
        return number.matches("^\\+27\\d{9}$");
    }

    public boolean isNameValid(String name) {
        return name.matches("^[A-Za-z]+$");
    }

    public String registerUser(String firstName, String lastName, String username, String password, String phone) {
        this.registeredFirstName = firstName;
        this.registeredLastName = lastName;
        this.registeredUsername = username;
        this.registeredPassword = password;
        return "User registered successfully!";
    }

    public boolean loginUser(String username, String password) {
        return username.equals(this.registeredUsername) && password.equals(this.registeredPassword);
    }

    public String returnLoginStatus(String username, String password) {
        if (loginUser(username, password)) {
            return "Welcome " + capitalize(registeredFirstName) + ", " + capitalize(registeredLastName) + ". It is great to see you again.";
        } else {
            return "Username or password is incorrect. Please try again.";
        }
    }

    private String capitalize(String word) {
        if (word == null || word.isEmpty()) return "";
        return word.substring(0, 1).toUpperCase() + word.substring(1).toLowerCase();
    }

    private static String getValidatedInput(String message, String error, java.util.function.Predicate<String> validator) {
        for (int i = 1; i <= 3; i++) {
            String input = JOptionPane.showInputDialog(message);
            if (input != null && validator.test(input)) return input;
            JOptionPane.showMessageDialog(null, error + " (" + i + "/3 attempts)");
        }
        JOptionPane.showMessageDialog(null, "Too many invalid attempts. The application will now exit.");
        System.exit(0);
        return null;
    }

    public void sendMessages() {
    int totalMessages = Integer.parseInt(JOptionPane.showInputDialog("How many messages would you like to compose?"));

    for (int i = 0; i < totalMessages; i++) {
        messageCounter++;
        String messageNumber = String.format("%010d", messageCounter);

        String recipient = getValidatedInput(
                "Enter recipient number (format: +27XXXXXXXXX):",
                "Invalid phone number. It must start with +27 and have exactly 9 digits after.",
                this::checkCellPhoneNumber
        );

        String content = JOptionPane.showInputDialog("Enter message content (max 50 characters):");

        if (content.length() > 50) {
            JOptionPane.showMessageDialog(null, "Please enter a message of less than 50 characters.");
            i--;
            continue;
        }

        Message message = new Message(messageNumber, recipient, content);

        String[] options = {"Send Message", "Disregard Message", "Store Message to Send Later"};
        int choice = JOptionPane.showOptionDialog(null, message.toString(), "Message Options",
                JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, options, options[0]);

        switch (choice) {
            case 0 -> {
                message.status = 1;
                sentMessages.add(message);
                // Requirement 7: Show full details after sending
                String details = "MessageID: " + message.messageNumber + "\n"
                        + "Message Hash: " + message.messageHash + "\n"
                        + "Recipient: " + message.recipientNumber + "\n"
                        + "Message: " + message.content;
                JOptionPane.showMessageDialog(null, "Message sent!\n\n" + details);
            }
            case 2 -> {
                message.status = 0;
                storeMessageToJson(message);  // ✅ Saves to stored_messages.json
                JOptionPane.showMessageDialog(null, "Message stored for later.");
            }
            default -> JOptionPane.showMessageDialog(null, "Message disregarded.");
        }
    }
    // Requirement 8: Show total messages sent (status == 1)
    long sentCount = sentMessages.stream().filter(m -> m.status == 1).count();
    JOptionPane.showMessageDialog(null, "Total messages sent: " + sentCount);
}


    public void showSentMessages() {
        if (sentMessages.isEmpty()) {
            JOptionPane.showMessageDialog(null, "No messages have been sent yet.");
            return;
        }
        StringBuilder log = new StringBuilder();
        for (Message msg : sentMessages) {
            log.append(msg).append("\n\n");
        }
        JOptionPane.showMessageDialog(null, log.toString());
    }

    public static void main(String[] args) {
        ST10452402PART2PROG5121 app = new ST10452402PART2PROG5121();

        String firstName = getValidatedInput(
                "Enter your first name (letters only):", "Invalid input.", app::isNameValid);
        String lastName = getValidatedInput(
                "Enter your last name (letters only):", "Invalid input.", app::isNameValid);
        String username = getValidatedInput(
                "Create a username (underscore, max 5 chars):", "Invalid username.", app::checkUserName);
        String password = getValidatedInput(
                "Create a password (8+ chars, 1 uppercase, 1 digit, 1 special char):", "Invalid password.", app::checkPasswordComplexity);
        String phone = getValidatedInput(
                "Enter your phone number (+27XXXXXXXXX):", "Invalid phone number.", app::checkCellPhoneNumber);

        JOptionPane.showMessageDialog(null, app.registerUser(firstName, lastName, username, password, phone));

        for (int attempt = 1; attempt <= 3; attempt++) {
            String loginUsername = JOptionPane.showInputDialog("Login attempt " + attempt + "/3\nUsername:");
            String loginPassword = JOptionPane.showInputDialog("Password:");

            if (app.loginUser(loginUsername, loginPassword)) {
                JOptionPane.showMessageDialog(null, app.returnLoginStatus(loginUsername, loginPassword));

                while (true) {
                    String menu = """
                            QuickChat Menu:
                            1. Send Message
                            2. Show Sent Messages
                            3. Exit""";
                    int option = Integer.parseInt(JOptionPane.showInputDialog(menu));

                    switch (option) {
                        case 1 -> app.sendMessages();
                        case 2 -> JOptionPane.showMessageDialog(null, "Coming Soon.");
                        case 3 -> {
                            JOptionPane.showMessageDialog(null, "Thank you for using QuickChat. Goodbye!");
                            System.exit(0);
                        }
                        default -> JOptionPane.showMessageDialog(null, "Invalid option. Please select 1, 2, or 3.");
                    }
                }
            } else {
                JOptionPane.showMessageDialog(null, "Incorrect username or password. (" + attempt + "/3)");
            }
        }

        JOptionPane.showMessageDialog(null, "Too many failed login attempts. The application will now exit.");
        System.exit(0);
    }
    public void storeMessageToJson(Message message) {
    Gson gson = new GsonBuilder().setPrettyPrinting().create();
    List<Message> storedMessages = new ArrayList<>();

    // Try to read existing messages
    try (Reader reader = new FileReader("stored_messages.json")) {
        Type listType = new TypeToken<List<Message>>() {}.getType();
        storedMessages = gson.fromJson(reader, listType);
        if (storedMessages == null) storedMessages = new ArrayList<>();
    } catch (FileNotFoundException e) {
        // File doesn't exist — that's OK
    } catch (IOException e) {
        JOptionPane.showMessageDialog(null, "Error reading stored messages: " + e.getMessage());
    }

    // Add new message and write to file
    storedMessages.add(message);

    try (Writer writer = new FileWriter("stored_messages.json")) {
        gson.toJson(storedMessages, writer);
    } catch (IOException e) {
        JOptionPane.showMessageDialog(null, "Error saving message: " + e.getMessage());
    }
  }
}





/*
REFERENCE:
OpenAI. (2024) ChatGPT (GPT-4) [Large language model]. Available at: https://chat.openai.com/ (Accessed: 21 April 2025).

JUnit. (n.d.) JUnit 5 User Guide. Available at: https://junit.org/junit5/docs/current/user-guide/ (Accessed: 21 April 2025).

IIEVC School of Computer Science, 2022. PROG51212/5111 POE 1: Create your repository. [video online] Available at: https://youtu.be/2JzEhwpg_0U [Accessed 21 April 2025].

IIEVC School of Computer Science, 2022. Link your local repository (NetBeans folder) with your GitHub repo. [video online] Available at: https://youtu.be/M9DzeAw3uMY?si=08PiED3nAYkcM9Vo [Accessed 21 April 2025].

IIEVC School of Computer Science, 2022. Add junit5 jar files. [video online] Available at: https://youtu.be/fQaUsfEzGdw?si=JX4uSEFqpayJUVSM [Accessed 21 April 2025].

IIEVC School of Computer Science, 2022. Create and run your first unit test. [video online] Available at: https://youtu.be/1Pa15vDWG-8?si=UlkhpNblwGAnkMvs [Accessed 21 April 2025].

IIEVC School of Computer Science, 2022. Push your code to GitHub. [video online] Available at: https://youtu.be/SqHkWHtmMJo?si=LL9E7Li1isq1Y2Xq [Accessed 21 April 2025].

IIEVC School of Computer Science, 2022. Automate your unit tests. [video online] Available at: https://youtu.be/dWbDN7lxWu4?si=2j3DlIEyqOoEAR84 [Accessed 21 April 2025].

IIEVC School of Computer Science, 2022. Test more than one value using a loop. [video online] Available at: https://youtu.be/omSrINZdSDU?si=pZZkHzqwLFpCsjaJ [Accessed 21 April 2025].

IIEVC School of Computer Science, 2022. Write additional unit tests and update your .yaml file. [video online] Available at: https://youtu.be/DmL4gG9vG0A?si=DEferX706znUkDE0 [Accessed 21 April 2025].
*/
