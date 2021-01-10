package de.daver.unihub.command;

import de.davermc.universehub.UniverseException;
import de.davermc.universehub.util.Reflections;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandMap;
import org.bukkit.command.CommandSender;
import org.bukkit.command.defaults.BukkitCommand;
import org.bukkit.entity.Player;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author Daver_Mc
 * @version 1.0.0
 * @since 1.0.0
 */
public class CommandHandler {

    private final CommandMap bukkitCommandMap;

    /**
     * Gets the bukkit {@link CommandMap} via Reflection
     */
    public CommandHandler(){
        this.bukkitCommandMap = (CommandMap) Reflections.getFieldValue(Bukkit.getServer(), "commandMap");
    }

    /**
     * To register a Command simply create a new Instance as parameter
     * <p>
     * Searches for SubCommands and permission and if sender is a player
     * @param abstractCommand an Instance of the command
     */
    public void registerCommand(AbstractCommand abstractCommand){
        Command command = abstractCommand.getClass().getAnnotation(Command.class);
        if(command == null){
            try {
                throw new UniverseException("Command can not be registered!");
            }catch (UniverseException e){
                e.printStackTrace();
            }
            return;
        }
        List<String> alias = new ArrayList<>();
        CommandAlias aliasAnnotation = abstractCommand.getClass().getAnnotation(CommandAlias.class);
        if(aliasAnnotation != null){
            alias = Arrays.asList(aliasAnnotation.alias());
        }
        BukkitCommand bukkitCommand = new BukkitCommand(command.name(), "A Universe Command!", "/", alias) {
            @Override
            public boolean execute(CommandSender sender, String s, String[] args) {
                executeMethod(abstractCommand, sender, args);
                return true;
            }
        };
        this.bukkitCommandMap.register(command.name(), bukkitCommand);
    }

    /**
     * Searches for the method to execute
     * <p>
     * Checks for SubCommands
     * and executes the method which matches the most
     * @param command the Object to check
     * @param sender the sender of the Command
     * @param args the arguments of the command
     */
    private void executeMethod(AbstractCommand command, CommandSender sender, String[] args){
        Command commandAnno = command.getClass().getAnnotation(Command.class);
        if(args.length == 0){
            if(sender.hasPermission(commandAnno.perm()) && commandAnno.player() && isPlayer(sender)){
                command.run(sender, args);
            }
            return;
        }
        Method subCommandMethod = null;
        int maxWeight = 0;
        for(Method method : command.getClass().getMethods()){
            if(!method.isAnnotationPresent(SubCommand.class)) continue;
            int tmpWeight = getWeight(method, args);
            if(maxWeight < tmpWeight){
                maxWeight = tmpWeight;
                subCommandMethod = method;
            }
        }

        if(subCommandMethod != null){
            SubCommand subCommand = subCommandMethod.getAnnotation(SubCommand.class);
            if(subCommand.player() && !isPlayer(sender)) return;
            if(sender.hasPermission(subCommand.perm())) {
                Reflections.invoke(command,subCommandMethod, sender, trimArgs(args, maxWeight));
                return;
            }
        }
        if(commandAnno.player() && !isPlayer(sender)) return;
        if(sender.hasPermission(commandAnno.perm())){
            command.run(sender, args);
        }
    }



    /**
     * Gets the weight of the SubCommand regarding all Alias
     * @param method the subcommand method to check Weight of
     * @param args the given arguments
     * @return the weight of the subCommand
     */
    private int getWeight(Method method, String[] args){
        SubCommand subCommand = method.getAnnotation(SubCommand.class);
        SubCommandAlias alias = method.getAnnotation(SubCommandAlias.class);
        int weight = 0;
        String[] name = subCommand.name().split(" ");
        if(name.length > args.length) return 0;
        for(int i = 0; i < name.length; i++){
            if(!name[i].equalsIgnoreCase(args[i])){
                break;
            }
            weight++;
        }
        if(alias == null){
            return weight;
        }
        int aliasWeight = getAliasWeight(alias, name, args);
        return Math.max(weight, aliasWeight);
    }

    /**
     * Gets the highest weight of an alias
     * <p>
     * Searching trough all alias and comparing it to the args
     * it is possible that a part is an vanilla part
     * @param alias an annotation holding all alias
     * @param name the vanilla subcommand name
     * @param args the given arguments to compare to
     * @return the maxWeight of the alias from the subcommand
     */
    private int getAliasWeight(SubCommandAlias alias, String[] name, String[] args){
        int aliasWeight = 0;
        for(String aliasName : alias.alias()){
            int tmpWeight = 0;
            String[] aliasArray = aliasName.split(" ");
            if(aliasArray.length > args.length) continue;
            for(int i = 0; i < aliasArray.length; i++){
                if(!aliasArray[i].equalsIgnoreCase(args[i]) && name.length > i){
                    if(!name[i].equalsIgnoreCase(args[i])){
                        break;
                    }
                }
                tmpWeight++;
            }
            if(tmpWeight > aliasWeight){
                aliasWeight = tmpWeight;
            }
        }
        return aliasWeight;
    }

    /**
     * Trims the argument Array to a new size
     * @param oldArgs the arguments given by the command
     * @param size the length of the subCommand
     * @return a trimmed form of the arguments
     */
    private String[] trimArgs(String[] oldArgs, int size){
        String[] newArgs = new String[oldArgs.length - size];
        System.arraycopy(oldArgs, size, newArgs, 0 , newArgs.length);
        return newArgs;
    }

    /**
     * Checks if a {@link CommandSender} is a {@link Player}
     * if it is not a Exception is thrown and catched
     * @param sender the {@link CommandSender} to check
     * @return if sender is a Player
     */
    private boolean isPlayer(CommandSender sender){
        if(!(sender instanceof Player)){
            try {
                throw new UniverseException("You have to be a player to perform that command!");
            }catch (UniverseException e){
                e.printStackTrace();
                return false;
            }
        }
        return true;
    }
}
