package tools;


import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

import java.util.List;
import java.util.ArrayList;

/**
 * This class is to store the command sent by the user.
 * It shares the same similarity as a Message object except
 * that a command may also has options.
 * 
 * Each option is modeled as a TextPair object. A command
 * may have no option, one option, or multiple options.
 * 
 * This class support the following methods:
 * - addOption (to add an option)
 * - getOption (to get the value of an option)
 * - getOptions (to get all options)
 */
public class Command extends Message {
    //TODO - add data members and methods
    protected List<TextPair> optionData = new ArrayList<>();

    public Command(String name, String id, String content, boolean isPrivate) {
        super(name, id, content, isPrivate);
    }

    public void addOption(String optionName, String optionValue) {
        optionData.add(new TextPair(optionName,optionValue));
    }

   public String getOption(String optionName) {
        for (int i = 0; i < optionData.size(); i++) {
            TextPair option = optionData.get(i);
            if (option.getName().equals(optionName)) {
                return option.getValue();
            }
        }
        return null;
   }


   public List<TextPair> getOptions() {
        return optionData;
   }

}
