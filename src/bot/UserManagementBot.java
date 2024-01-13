package bot;


import java.io.*;
import java.util.*;

import tools.*;

/**
 * This bot is to manage the registration of the user with the command '/registration'.
 * The user will be asked to provide a registration code. If the registration code is 
 * correct, the user will be registered to the system.
 * 
 * The user cannot register again if the user has already registered (One discord ID 
 * to one student ID).
 * 
 * We assume the registration code will be sent to students via email in advanced. 
 * The registration information is given in a file (please check users.csv)
 * 
 * The file format of users.csv is as follows:
 * Each row may have three columns or five columns.
 * Three Columns, e.g.:
 *      20100001,g8xa9s,Bruce Lee
 * That represents the student ID is 20100001, the registration code is g8xa9s, and the name is Bruce Lee.
 * Five Columns, e.g.:
 *      20100002,-,Chan Tai Man,1004553330619580487,Kevin Wang
 * That represents the student ID is 20100002, the registration code is empty (registered already),
 * the student name is Chan Tai Man, the discord ID is 1004553330619580487, and the discord name is Kevin Wang.
 */
public class UserManagementBot extends CommandBot {
    //Add your data member here
    private String filename;
    private List<User> users;
    //Constructor
    public UserManagementBot(String filename) {
        //TODO
        this.filename = filename;
        addOption("regcode", "to register yourself", true);
        users = new ArrayList<>();

        try (Scanner scanner = new Scanner(new File(filename))) {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] data = line.split(",");

                if (data.length == 3) {
                    String studentID = data[0];
                    String registrationCode = data[1];
                    String studentName = data[2];
                    users.add(new User(studentID, registrationCode, studentName));
                }

                if (data.length == 5) {
                    String studentID = data[0];
                    String studentName = data[1];
                    String id = data[2];
                    String userName = data[3];
                    users.add(new User(studentID, studentName, id, userName));
                }

            }
        } catch (FileNotFoundException e) {
            System.out.println("File not found.");
        }
    }


    /**
     * Written for you. No need to change it
     */
    @Override
    public String getCommand() {
        return "registration";
    }

    /**
     * Written for you. No need to change it
     */
    @Override
    public String getCommandHelp() {
        return "Registers the user";
    }


    /**
     * to check if a user has been registered
     */
    boolean isRegistered(String id) {
        //TODO
        for (int i = 0; i < users.size(); i++) {
            if (Objects.equals(users.get(i).getID(),id)) {
                return true;
            }
        }
        return false;
    }


    /**
     * To respond to the command '/registration'.
     * 
     * If the user has already registered, return "You are already registered!"
     * If the registration code is correct, register the user and return "You are registered!". 
     * If the registration code is incorrect, return "Invalid registration code!"
     * 
     * To avoid data lost, remember to save the data to the file after each user's registration.
     */
    @Override
    public String respond(Command command) {
        //TODO
        String senderID = command.getSenderID();
        String userName = command.getName();
        if (isRegistered(senderID)) {
            return "You are already registered!";
        }

        String registrationCode = command.getOption("regcode");
        try{FileWriter fileWriter = new FileWriter(filename,false);
        PrintWriter out = new PrintWriter(fileWriter);
        out.close();}catch (Exception e){}
        for (int i = 0; i < users.size(); i++) {
            User user = users.get(i);
            if (Objects.equals(user.getRegistrationCode(),registrationCode)) {
                user.setID(senderID);
                user.setUsername(userName);
                user.setRegistrationCode("");

                try {
                    FileWriter fileWriter = new FileWriter(filename, true);
                    PrintWriter out = new PrintWriter(fileWriter);

                    //for (int j = 0; j < users.size(); j++) {

                    if (!Objects.equals(users.get(i).getRegistrationCode(), null) ||
                            !users.get(i).getRegistrationCode().isEmpty()) {
                        if (user.getRegistrationCode().equals(registrationCode)) {
                            out.println(users.get(i).getStudentID() + "," + users.get(i).getRegistrationCode() +
                                    "," + users.get(i).getStudentName());
                        } else {
                            out.println(users.get(i).getStudentID() + "," + "-" + "," + users.get(i).getStudentName() +
                                    "," + users.get(i).getID() + "," + users.get(i).getUsername());

                        }
                    }
                    //}
                    out.close();
                    fileWriter.close();


                } catch (Exception e) {
                    throw new RuntimeException(e);
                }


            } else {
                try {
                    FileWriter fileWriter = new FileWriter(filename, true);

                    PrintWriter out = new PrintWriter(fileWriter);
                    if (!Objects.equals(users.get(i).getRegistrationCode(), null) ||
                            !users.get(i).getRegistrationCode().isEmpty()) {

                        out.println(users.get(i).getStudentID() + "," + users.get(i).getRegistrationCode() +
                                "," + users.get(i).getStudentName());
                    }else {
                            out.println(users.get(i).getStudentID() + "," + "-" + "," + users.get(i).getStudentName() +
                                    "," + users.get(i).getID() + "," + users.get(i).getUsername());

                        }

                    out.close();
                    fileWriter.close();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
            return "You are registered!";
        }
            return "Invalid registration code!";

    }

    /**
     * return the student ID of the user with the given discord ID
     * 
     * throws an IDNotFoundException if the discord ID cannot be found
     */
    public String getStudentID(String id) throws IDNotFoundException {
        //TODO
        for (int i = 0; i < users.size(); i++) {
            if (users.get(i).getID().equals(id)) {
                return users.get(i).getStudentID();
            }
        }
        throw new IDNotFoundException("Discord ID not found:　" + id);
    }
    
    /**
     * return the user object with the given discord ID
     * 
     * throws an IDNotFoundException if the discord ID cannot be found
     */
    public User getStudent(String id) throws IDNotFoundException {
        //TODO
        for (int i = 0; i < users.size(); i++) {
            if (users.get(i).getID().equals(id)) {
                return users.get(i);
            }
        }
        throw new IDNotFoundException("Discord ID not found:　" + id);
    }


    /**
     * Output how many number of users have registered.
     */
    @Override
    public void actionPerform() {
        System.out.println("Registration bot: ");
        int count = 0;
        for (int i = 0; i < users.size(); i++) {

            if (isRegistered(users.get(i).getRegistrationCode())) {
                count++;
            }
        }
        System.out.println("There are " + count + "/" + users.size() + " users has registered the system");
    }
}
