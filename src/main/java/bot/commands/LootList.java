package bot.commands;

import bot.Bot;
import bot.Player;
import bot.readers.Database;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class LootList extends ListenerAdapter {
    private final DecimalFormat f = new DecimalFormat("###,###,###");
    private Map<String,Player> members = new HashMap<>();

    //Read messages in chat and handle ~lootlist command
    public void onGuildMessageReceived(GuildMessageReceivedEvent event) {
        String[] args = event.getMessage().getContentRaw().split("\\s+");

        if (args[0].equalsIgnoreCase(Bot.prefix + "lootList")) {
            readData();
            EmbedBuilder list = new EmbedBuilder();
            list.setTitle("This Week's Loot");
            list.setColor(0x00ff08);
            list.addField("*<:fireworks:827276473026215976>Top Looter<:fireworks:827276473026215976>*", getFirst(members), false);
            list.addField("The Next Five:", getTopFive(members), false);
            list.addField("Honorable Mentions:", honorableMentions(members), true);
            list.addField("Stats:", stats(members, f), false);
            list.setFooter("\nLoot stats are refreshed automatically every 30min");

            event.getChannel().sendTyping().queue();
            event.getChannel().sendMessage(list.build()).queue();
            list.clear();
            members.clear();
        }
    }
    //Database reader
    private void readData() {
        Database data = new Database();
        members = data.readData(members,false);
    }
    //Returns toString of Player with the highest loot score
    private static String getFirst(Map<String,Player> members){
        long highest = -1;
        Player top = null;
        for(Player player : members.values())
            if(player.getLoot()>highest) {
                highest = player.getLoot();
                top = player;
            }
        assert top != null;
        return top.toString();
    }
    //Returns the next top 5 loot scores after the first.
    private static String getTopFive(Map<String,Player> members){
        ArrayList<Player> players= new ArrayList<>(members.values());
        Collections.sort(players);

        StringBuilder total = new StringBuilder();
        for (int i=1; i<6;i++) {
            total.append("<:white_small_square:827279848853733406>").append(players.get(i).toString()).append("\n");
        }
        return total.toString();
    }
    //Returns players whose loot exceeded 30mil (weekly)
    private static String honorableMentions(Map<String,Player> members) {
        ArrayList<Player> players= new ArrayList<>(members.values());
        Collections.sort(players);

        StringBuilder total = new StringBuilder();
        for (int i=6;i< players.size();i++) {
            if (players.get(i).getLoot() >= 20000000)
                total.append("<:white_small_square:827279848853733406>").append(players.get(i).toString()).append("\n");
        }
        return total.toString();
    }
    //Returns Loot Goal, # who reached the goal, and the alliance weekly average
    private static String stats(Map<String,Player> members, DecimalFormat f){
        String total = "Loot Goal: 15mil";
        int made=0;
        for (Player member : members.values()) {
            if (member.getLoot() >= 15000000)
                made++;
        }
        total+= "\n# who succeeded: "+made+"\nAverage: "+getAverage(members, f);

        return total;
    }
    //Returns average of alliance loot for the week
    private static String getAverage(Map<String,Player> members, DecimalFormat f) {
        long total = 0;

        for (Player member : members.values()) {
            total += member.getLoot();
        }
        return f.format(total / members.size());
    }

}
