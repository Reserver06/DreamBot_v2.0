package bot.commands;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class NoUReader extends ListenerAdapter {
    public void onGuildMessageReceived(GuildMessageReceivedEvent event) {
        String[] args = event.getMessage().getContentRaw().split("\\s+");

        if (args[0].equalsIgnoreCase("illegal") && args.length<2) {
            EmbedBuilder response = new EmbedBuilder();
            response.setImage("https://cdn.discordapp.com/emojis/823591050991239229.png?v=1");
            event.getChannel().sendTyping().queue();
            event.getChannel().sendMessage(response.build()).queue();
            response.clear();

        }
    }
}
