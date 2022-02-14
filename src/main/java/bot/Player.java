package bot;

import java.text.DecimalFormat;

public class Player implements Comparable<Player>{

    private final String discordId;
    private final String name;
    private final long phone;
    private final long loot;
    private final String preference;
    private final DecimalFormat f = new DecimalFormat("###,###,###");

    public Player(String discordId,String name,String phone,String loot,String preference){
        this.discordId = discordId;
        this.name = name;
        this.phone = phone!=null? Long.parseLong(phone):0;
        this.loot = loot!=null? Long.parseLong(loot):0;
        this.preference = preference;
    }

    public String getDiscordId() {
        return discordId;
    }

    public String getName() {
        return name;
    }

    public long getPhone() {
        return phone;
    }

    public long getLoot() {
        return loot;
    }

    public String getPreference(){
        return preference;
    }

    @Override
    public String toString() {
        return name +": "+f.format(loot);
    }

    @Override
    public int compareTo(Player player) {
        if(this.loot>player.getLoot())
            return -1;
        else
            return 0;
    }
}
