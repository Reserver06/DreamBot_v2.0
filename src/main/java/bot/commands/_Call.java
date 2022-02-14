package bot.commands;


import bot.Bot;
import bot.Player;
import bot.readers.Database;
import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Call;
import com.twilio.type.PhoneNumber;
import com.twilio.type.Twiml;
import io.github.cdimascio.dotenv.Dotenv;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.util.HashMap;
import java.util.Map;

public class _Call extends ListenerAdapter {
    Dotenv dotenv = Dotenv.load();
    public final String ACCOUNT_SID = dotenv.get("ACCOUNT_SID");
    public final String AUTH_TOKEN = dotenv.get("AUTH_TOKEN");
    public final String TWILIO_NUMBER = dotenv.get("TWILIO_NUMBER");
    private Map<String, Player> members = new HashMap<>();

    public void onGuildMessageReceived(GuildMessageReceivedEvent event) {
        String[] args = event.getMessage().getContentRaw().split("\\s+");

        if (args[0].equalsIgnoreCase(Bot.prefix+"call") && args.length >=3) {
            StringBuilder concat = new StringBuilder();
            args[1] = args[1].toLowerCase();

            for (int i = 2; i < args.length; i++)
                concat.append(args[i]).append(" ");
            try {
                readData(args);
                String num = "+" + members.get(args[1]).getPhone();
                Twilio.init(ACCOUNT_SID, AUTH_TOKEN);
                Call.creator(
                        new PhoneNumber(num),
                        new PhoneNumber(TWILIO_NUMBER),
                        new Twiml("<Response><Say voice=\"alice\">This is DreamBot. You have an incoming ...." + concat + "...." +
                                " This is DreamBot. You have an incoming ...." + concat + "</Say></Response>")).create();
                event.getChannel().sendMessage("Calling " + members.get(args[1]).getName()).queue();

            } catch (NullPointerException e) {
                EmbedBuilder notFound = new EmbedBuilder();
                notFound.setTitle("Player Not Found");
                notFound.setDescription("There is no number associated with the player: " + args[1]);
                notFound.setColor(0xf70505);
                event.getChannel().sendMessage(notFound.build()).queue();
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        members.clear();

        if (args[0].equalsIgnoreCase(Bot.prefix + "call") && args.length < 3) {
            EmbedBuilder error = new EmbedBuilder();
            error.setColor(0xf70505);
            error.setTitle("Incorrect Syntax");
            error.setDescription("~call [Player_Name] [Time Until Landing] [Optional Notes] -" +
                    " Make sure to remove any spaces in the name itself.");
            event.getChannel().sendMessage(error.build()).queue();
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
}
