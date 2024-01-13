package bot;

import java.io.FileNotFoundException;
import java.util.Scanner;
import java.io.File;
import java.util.List;
import java.util.ArrayList;
import tools.*;

/**
 * This bot handle the command '/score'.
 * When a user ask about his score, this bot will check the score of the user and return the score.
 * To know which student ID that the user has been registered, this bot will ask the UserManagementBot.
 * Prior to using this bot, you need to register this bot to the UserManagementBot using the command '/registration'.
 * 
 */
public class ScoreBot extends CommandBot {
    public static final String DEFAULT_FILE = "dictation.csv";
    //TODO: Add your private data member here
    private UserManagementBot r;
    private String filename;
    private String name = null;
    private List<String> scoreData = new ArrayList<>();
    private List<String> studentidData = new ArrayList<>();
    /**
     * The constructor of the bot, require a UserManagementBot object.
     * Since the filename of the score is not given, the default file name is used.
     */
    public ScoreBot(UserManagementBot r) {
        //TODO
        this.r = r;
        this.filename = DEFAULT_FILE;
    }
    /**
     * The constructor of the bot, require a UserManagementBot object and the filename of the score.
     */
    public ScoreBot(UserManagementBot r, String filename) {
        //TODO
        this.r = r;
        this.filename = filename;
    }


    /**
     * Which score that the object is listening to.
     */
    @Override
    public String getCommand() {
        //TODO
        return "score";
    }
    /**
     * The short description for this command.
     */
    @Override
    public String getCommandHelp() {
        //TODO
        return "Displays the user's score";
    }
    /**
     * This method is used to respond to a command.
     */
    @Override
    public String respond(Command command) {
        //TODO
        String userID = command.getSenderID();
        name = command.getName();

        try {
            Scanner scanner = new Scanner(new File(filename));
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] data = line.split(",");


                for (int i = 1; i < data.length; i++) {
                    scoreData.add(data[i]);
                }

                if (data[0].equals(r.getStudentID(userID))) {
                    int sum = 0;
                    String result = "";
                    for (int j = 0; j < scoreData.size(); j++) {
                        sum += Integer.parseInt(scoreData.get(j));
                        result += scoreData.get(j) + ",";
                    }
                    return "Your scores are: " + result + " and your average score is: " +
                            (sum/scoreData.size()); //+ score;
                }
            }
        } catch (Exception e) {
            System.out.println("File not found.");
        }
        return null;
    }
    /**
     * Print the last user who queried the score bot service to console.
     */
    @Override
    public void actionPerform() {
        //TODO
        if (name != null)
            System.out.println("The last user queried ScoreBot is:　" + this.name);
    }
}
