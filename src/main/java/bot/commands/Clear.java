package bot.commands;


import bot.Bot;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.util.List;

public class Clear extends ListenerAdapter {
    public void onGuildMessageReceived(GuildMessageReceivedEvent event){
        String[] args = event.getMessage().getContentRaw().split("\\s+");

        if(args[0].equalsIgnoreCase(Bot.prefix + "clear")&& isAdmin(event)){
            if(args.length<2){
                EmbedBuilder usage = new EmbedBuilder();
                usage.setColor(0xf70505);
                usage.setTitle("Specify amount to delete");
                usage.setDescription("Usage: "+ Bot.prefix +"clear"+ " [# of messages]");
                event.getChannel().sendMessage(usage.build()).queue();
            }
            else{
                try{
                   List<Message> messages = event.getChannel().getHistory().retrievePast(Integer.parseInt(args[1])+1).complete();
                   event.getChannel().deleteMessages(messages).queue();

                   //success message
                   EmbedBuilder success = new EmbedBuilder();
                   success.setColor(0x05f711);
                   success.setTitle("Successfully deleted "+args[1]+" messages.");
                   event.getChannel().sendMessage(success.build()).queue();

               }
               catch(Exception e){
                    e.printStackTrace();
                    if(e.toString().startsWith("java.lang.IllegalArgumentException: Must provide") ||
                            e.toString().startsWith("java.lang.IllegalArgumentException: Message retrieval")){
                       //too many messages
                       EmbedBuilder error = new EmbedBuilder();
                       error.setColor(0xf70505);
                       if(Integer.parseInt(args[1])>2)
                           error.setTitle("Too many messages selected");
                       else
                           error.setTitle("Not enough messages selected");
                       error.setDescription("Between 2-100 messages can be deleted at once");
                       event.getChannel().sendMessage(error.build()).queue();

                   }
                   else{
                       //messages too old
                       EmbedBuilder error = new EmbedBuilder();
                       error.setColor(0xf70505);
                       error.setTitle("Selected messages are older than 2 weeks");
                       error.setDescription("Messages older than 2 weeks cannot be deleted");
                       event.getChannel().sendMessage(error.build()).queue();
                   }

               }
            }
        }
        if(args[0].equalsIgnoreCase(Bot.prefix + "clear")&& !isAdmin(event)){
            EmbedBuilder illegal = new EmbedBuilder();
            illegal.setColor(0xf70505);
            illegal.setTitle("You cannot use this command");
            illegal.setDescription("Must have role: 'Admin'");
            event.getChannel().sendMessage(illegal.build()).queue();
        }
    }
    public boolean isAdmin(GuildMessageReceivedEvent event) {
        boolean isAdmin=false;
        try {
            List<Role> member = event.getMember().getRoles();
        for(Role role:member)
            if(role.getName().compareTo("Admin")==0) {
                isAdmin = true;
            }
        }catch(NullPointerException e) {
            System.err.println("User has no roles");
        }
        return isAdmin;
    }
}

