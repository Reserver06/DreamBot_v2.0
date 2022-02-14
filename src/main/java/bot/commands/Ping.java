package bot.commands;

import bot.Bot;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.util.List;

public class Ping extends ListenerAdapter {
        public void onGuildMessageReceived(GuildMessageReceivedEvent event) {
            String[] args = event.getMessage().getContentRaw().split("\\s+");

            if (args[0].equalsIgnoreCase(Bot.prefix+"ping") && args.length>1) {
                List<Member> call = event.getMessage().getMentionedMembers();
                for(int i=0;i<10;i++)
                    event.getChannel().sendMessage(call.get(0).getAsMention()).queue();
            }
            if (args[0].equalsIgnoreCase(Bot.prefix+"ping") && args.length<2) {
                EmbedBuilder error = new EmbedBuilder();
                error.setColor(0xf70505);
                error.setTitle("Incorrect Syntax");
                error.setDescription("~ping [@Discord_User] - Will ping the specified user x10.");

                event.getChannel().sendMessage(error.build()).queue();
            }
        }
    }

