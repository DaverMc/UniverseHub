package de.daver.unihub.command;

import org.bukkit.command.CommandSender;

import java.util.List;

/**
 * @author Daver_Mc
 * @version 1.0.0
 * @since 1.0.0
 * interface to create a command
 */
public interface AbstractCommand {

    /**
     * is running when the command is executed
     * @param sender the Sender of the Command
     * @param args the arguments
     */
    void run(CommandSender sender, String[] args);

    /**
     * Sets the description of the command
     * @return a {@link List} one line equals one String
     */
    List<String> description();

}
