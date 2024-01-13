package bot;

import tools.*;

import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.List;
import java.util.ArrayList;
import java.io.File;

/**
 * This bot will check the seat of a quiz or practical test for a student.
 * 
 * This message bot will only work in private message. 
 * The user should first type "seat" to start the conversation. 
 * The bot will then ask for the student ID. The bot is expecting
 * a 8-digit number as the student ID and ignore any other message.
 * After received the 8-digit number in a private message, the bot 
 * will check the seat of the student and return the seat number.
 * 
 * The bot allows the user to check seat for other students or check
 * the seat even if the user did not register to UserManagementBot before.
 * 
 * We will assume the seat will never change during the execution of the 
 * program. Any change of seat will require the program to restart.
 */
public class SeatChecker implements MessageListener {
    //TODO: Add your private data member here
    private String filename = "seat.csv";
    private List<String> studentId = new ArrayList<>();
    private List<String> seat = new ArrayList<>();
    private boolean check = false;

    //TODO: Add your methods here

    public SeatChecker() {
        studentId = new ArrayList<>();
        seat = new ArrayList<>();

        try (Scanner scanner = new Scanner(new File(filename))) {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] data = line.split(",");
                studentId.add(data[0]);
                seat.add(data[1]);
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String onMessageReceived(Message message) {

        if (message.getContent().equals("seat")) {
            check = true;
            return "What is your ID?";
        }
        if (check == true) {
            if (message.getContent().length() == 8) {
                for (int i = 0; i < studentId.size(); i++) {
                   if (message.getContent().equals(studentId.get(i))) {
                        check = false;
                        return "Your seat is: " + seat.get(i);
                    }
                }return "Sorry I cannot find your seat";
            }

        }
        return null;
    }
}
