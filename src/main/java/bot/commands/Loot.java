package bot.commands;

import bot.Bot;
import bot.Player;
import bot.readers.Database;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;

public class Loot extends ListenerAdapter {
    private final DecimalFormat f = new DecimalFormat("###,###,###");
    private Map<String,Player> members = new HashMap<>();

    //Event Listener
    public void onGuildMessageReceived(GuildMessageReceivedEvent event) {
        String[] args = event.getMessage().getContentRaw().split("\\s+");

        if (args[0].equalsIgnoreCase(Bot.prefix + "loot")) {
            try {
                args[1] = args[1].toLowerCase();
                readData(args);
                lootTotal(args,event);
            } catch (IOException | GeneralSecurityException e) {
                e.printStackTrace();
            } catch (IndexOutOfBoundsException e) {
                EmbedBuilder error = new EmbedBuilder();
                error.setColor(0xf70505);
                error.setTitle("Incorrect Usage");
                error.setDescription("~loot [name of player] -- Both in-game names and @'ing will work.");
                event.getChannel().sendMessage(error.build()).queue();
            }
            members.clear();
        }
    }

    //Database reader
    private void readData(String[] args) {
        Database data = new Database();
        if(args[1].startsWith("<@!"))
            members = data.readData(members,true);
        else{
            members = data.readData(members,false);
        }
    }

    //Sends embedded message of loot total
    private void lootTotal(String [] args,GuildMessageReceivedEvent event) throws IOException, GeneralSecurityException {
        if(checkLoot(args)) {
            EmbedBuilder loot = new EmbedBuilder();
            loot.setTitle("Loot stats for "+ members.get(args[1]).getName());
            loot.addField("Weekly Loot: ", f.format(members.get(args[1]).getLoot()),false);
            loot.addField("Overall Average: ",f.format(memberAverage(members.get(args[1]))),false);
            loot.setColor(0x00ff08);


            event.getChannel().sendTyping().queue();
            event.getChannel().sendMessage(loot.build()).queue();
            loot.clear();
        }
        else{
            EmbedBuilder error = new EmbedBuilder();
            error.setColor(0xf70505);
            error.setTitle("Player Not Found");
            error.setDescription("~loot [name of player] -- Make sure to remove all spaces from the name.");
            event.getChannel().sendMessage(error.build()).queue();
        }

    }
    //Confirms player and checks loot
    private boolean checkLoot(String [] args){
        return members.get(args[1]) != null;
    }
    //Gets player specific loot average
    private long memberAverage(Player player){
        Database database = new Database();
        return database.readAvg(player);
    }
}
