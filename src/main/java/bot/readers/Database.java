package bot.readers;

import bot.Player;
import io.github.cdimascio.dotenv.Dotenv;

import java.sql.*;
import java.util.ArrayList;
import java.util.Map;

public class Database {
    public static Dotenv dotenv = Dotenv.load();
    static long loot = 0;
    //Adds data to member table and returns true when successful
    public void addData(String discord_id, String name, long phone, String preference) throws SQLException {

        Statement statement = startConnection();
        if(preference!=null){
            statement.executeUpdate("INSERT INTO member(discord_id,name,phone,loot,contact) VALUES('" +
                    discord_id + "','" + name + "','" + phone + "','" + loot + "','"+preference+"');");
        }
        else {
            statement.executeUpdate("INSERT INTO member(discord_id,name,phone,loot) VALUES('" +
                    discord_id + "','" + name + "','" + phone + "','" + loot + "');");
        }
    }
    public void updateData(String discord_id,String name, long phone, String preference) throws SQLException {

        Statement statement = startConnection();
        if(preference!=null){
            statement.executeUpdate("UPDATE member SET phone="+phone+",name='"+name+"',contact='"+preference+"' WHERE discord_id='<@!"+discord_id+">';");
        }
        else{
            statement.executeUpdate("UPDATE member SET phone="+phone+",name='"+name+"' WHERE discord_id='<@!"+discord_id+">';");
        }
    }
    //Reads data from member table and returns a list of Players if successful
    public Map<String,Player> readData(Map<String,Player> members,boolean is_id) {
        try {
            Statement statement = startConnection();

            ResultSet resultSet = statement.executeQuery("select * from member;");
            while(resultSet.next()) {
                String discordID = resultSet.getString("discord_id");
                String name = resultSet.getString("name");
                String phone = resultSet.getString("phone");
                String loot = resultSet.getString("loot");
                String preference = resultSet.getString("contact");
                if(is_id)
                    members.put(discordID,new Player(discordID,name,phone,loot,preference));
                else
                    members.put(name.toLowerCase(),new Player(discordID,name,phone,loot,preference));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return members;
    }
    public long readAvg(Player player) {
        ArrayList<String> columns = new ArrayList<>();
        String temp = "0";
        try {
            Statement statement = startConnection();

            ResultSet resultSet = statement.executeQuery(
                    "SELECT COLUMN_NAME\n" +
                            "FROM INFORMATION_SCHEMA.COLUMNS\n" +
                            "WHERE TABLE_NAME = 'archive' AND ORDINAL_POSITION<>1\n" +
                            "ORDER BY ORDINAL_POSITION;");
            while (resultSet.next()) {
                columns.add(resultSet.getString("COLUMN_NAME"));
            }
            StringBuilder cols = new StringBuilder();
            cols.append("`").append(columns.get(0)).append("`");
            for (int i = 1; i < columns.size(); i++)
                cols.append("+`").append(columns.get(i)).append("`");

            String sql = "SELECT FLOOR((" + cols + ")/" + (columns.size()) + ") AS Average FROM archive WHERE name='" + player.getName() + "';";
            ResultSet results = statement.executeQuery(sql);
            temp = "0";
            while (results.next())
                temp = results.getString("Average");

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Long.parseLong(temp);
    }


    //Starts connection to SQL Database
    private Statement startConnection() throws SQLException {
        String url = "jdbc:mysql://na05-sql.pebblehost.com/customer_183968_dreambot";
        String user = "customer_183968_dreambot";
        String password = dotenv.get("DATABASE_PASSWORD");

        Connection connection = DriverManager.getConnection(url,user,password);
        return connection.createStatement();
    }


}
