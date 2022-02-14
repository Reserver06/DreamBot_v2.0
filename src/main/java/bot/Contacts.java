package bot;

import bot.commands.Incoming;


public class Contacts extends Incoming {

    private final String name;
    private final String num;



    public Contacts(String name,String num){
        this.name = name;
        this.num = num;
    }
    public String getName(){
        return name;
    }
    public String getNum(){
        return num;
    }

    @Override
    public String toString() {
        return name +" "+num;
    }

}
