package bot.commands;

import bot.Bot;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.events.message.guild.react.GuildMessageReactionAddEvent;
import net.dv8tion.jda.api.events.message.guild.react.GuildMessageReactionRemoveEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.awt.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class ReactRole extends ListenerAdapter {
    public ArrayList<String> roleName = new ArrayList<>();
    public ArrayList<String> messageId = new ArrayList<>();
    public String tempRole;
    public long tempId;
    public long messageNum;

    public void onGuildMessageReceived(GuildMessageReceivedEvent event) {
        String[] args = event.getMessage().getContentRaw().split("\\s+");

        if (args[0].equalsIgnoreCase(Bot.prefix + "reactrole") && isAdmin(event)) {
            StringBuilder concat = new StringBuilder();

            for (int i = 1; i < args.length; i++)
                concat.append(args[i]).append(" ");

            tempRole = concat.substring(concat.indexOf("[")+1,concat.indexOf("]")).trim().replace(" ","_");

            event.getMessage().delete().complete();

            EmbedBuilder embed = new EmbedBuilder();
            embed.setTitle(tempRole.replace("_"," ") + " React Role");
            embed.setDescription(concat.substring(concat.indexOf("]")+1));
            embed.setColor(Color.blue);
            event.getChannel().sendMessage(embed.build()).queue((message)->{
                messageNum = message.getIdLong();
                event.getChannel().addReactionById(messageNum,"\u2705").queue();
                tempId = messageNum;

                try {
                    PrintWriter file = new PrintWriter(new FileWriter("roleLogs.txt",true),true);
                    file.println(tempRole+" "+tempId);

                } catch (IOException e) {
                    e.printStackTrace();
                }

            });
        }
    }

    public void onGuildMessageReactionAdd(GuildMessageReactionAddEvent event) {
        if (event.getReactionEmote().getName().equals("\u2705") && !event.getUser().isBot()) {
            readCache(roleName,messageId);

            for(int i=0;i<messageId.size();i++){
                if (event.getMessageId().equals(messageId.get(i))) {
                    List<Role> member = event.getGuild().getRoles();
                    for (Role role : member) {
                        if (role.getName().equals(roleName.get(i))) {
                            event.getGuild().addRoleToMember(event.getUserId(), role).queue();
                            break;
                        }
                    }
                }
            }
        }
    }
    public void onGuildMessageReactionRemove(GuildMessageReactionRemoveEvent event){
        if (event.getReactionEmote().getName().equals("\u2705")){
            readCache(roleName,messageId);
            for(int i=0;i<messageId.size();i++) {
                if (event.getMessageId().equals(messageId.get(i))) {
                    List<Role> member = event.getGuild().getRoles();
                    for (Role role : member) {
                        if (role.getName().equals(roleName.get(i))) {
                            event.getGuild().removeRoleFromMember(event.getUserId(), role).queue();
                            break;
                        }
                    }
                }
            }
        }
    }
    public void readCache(ArrayList<String> roleName,ArrayList<String> messageId){

        try {
            Scanner sc = new Scanner(new File("roleLogs.txt"));

            while (sc.hasNext()) {
                roleName.add(sc.next().replace("_"," "));
                messageId.add(sc.next());
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
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