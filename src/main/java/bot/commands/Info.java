package bot.commands;

import bot.Bot;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.util.Objects;

public class Info extends ListenerAdapter {
    public void onGuildMessageReceived(GuildMessageReceivedEvent event){
        String[] args = event.getMessage().getContentRaw().split("\\s+");
        Guild guild = event.getGuild();
        String generalCommands =
                "<:white_small_square:827279848853733406>**~info** - Displays the info you are currently reading.\n"+
                "<:white_small_square:827279848853733406>**~loot** [Player_Name] - Displays most recent loot information.\n" +
                "<:white_small_square:827279848853733406>**~lootList** - Displays alliance weekly loot info.\n" +
                "<:white_small_square:827279848853733406>**~add** - [Player_Name] [Phone_Number] (Optional 'call' preference) - Adds/Updates your user profile for loot and calling features. The default is 'sms' and does not to be specified. If you would like to receive calls instead of sms, add 'call' at the end of the command.\n";
        String contact =
                "<:white_small_square:827279848853733406>**~ping** [@Discord_User] - Will ping the specified user x10.\n"+
                "<:white_small_square:827279848853733406>**~incoming** [Player_Name] [Time Until Landing] - Contacts the player with the chosen method of contact.\n"+
                "<:white_small_square:827279848853733406>**~call** [Player_Name] [Time Until Landing] - Calls the specified player and should only be used in emergencies. Otherwise, use the incoming command.\n";
        String admin =
                "<:small_blue_diamond:891494983201325116>**~clear** [amount] - Deletes messages specified.\n"+
                "<:small_blue_diamond:891494983201325116>**~reactRole** \"[role]\" [Information About React Role] - Creates a react role with the role and information provided";

        if(args[0].equalsIgnoreCase(Bot.prefix + "info")){
            EmbedBuilder info = new EmbedBuilder();
            info.setTitle("DreamBot - DT");
            info.setDescription("A multi-purpose Bot designed for the DT server");
            info.setColor(0x0e37ed);
            info.addField("__General Commands__:",generalCommands,false);
            info.addField("__Contact Commands__:",contact,false);
            info.addField("__Admin Commands__:",admin,false);
            info.setFooter("Created by Colossus/reserver", Objects.requireNonNull(guild.getOwner()).getUser().getAvatarUrl());

            event.getChannel().sendTyping().queue();
            event.getChannel().sendMessage(info.build()).queue();
            info.clear();
        }
    }

}
