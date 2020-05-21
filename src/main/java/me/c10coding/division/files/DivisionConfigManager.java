package me.c10coding.division.files;

import com.google.inject.Inject;
import me.c10coding.coreapi.chat.Chat;
import me.c10coding.coreapi.files.ConfigManager;
import me.c10coding.division.DivisionPlugin;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class DivisionConfigManager extends ConfigManager {

    private Economy e;
    private List<String> divisionList;
    private Chat chatFactory;
    private DivisionPlugin divPlugin;

    @Inject
    public DivisionConfigManager(JavaPlugin plugin) {
        super((DivisionPlugin) plugin, "divisions.yml");
        divPlugin = (DivisionPlugin) plugin;
        e = divPlugin.getEconomy();
        divisionList = getDivisionList();
        chatFactory = divPlugin.getCoreAPI().getChatFactory();
    }

    @Override
    public void validateConfigs() {
        File[] files = {new File(plugin.getDataFolder(), "divisions.yml"), new File(plugin.getDataFolder(), "config.yml")};
        for(File f : files){
            if(!f.exists()) {
                this.plugin.saveResource(f.getName(), false);
                Bukkit.getConsoleSender().sendMessage(DivisionPlugin.PREFIX + " Loading " + f.getName());
            }
        }
    }

    private boolean isDivisionPublic(String divisionName){
        return config.getBoolean("Divisions." + divisionName + ".Status");
    }

    private List<String> getDivisionList(){
        return getList("ListOfDivisions");
    }

    private boolean inSameDivision(UUID player1, UUID player2){
        return getPlayerDivision(player1).equals(getPlayerDivision(player2));
    }

    private void createDivision(String divisionName, Player p, boolean isPublic){
        if(!isInDivision(p.getUniqueId())){
            if(!isADivision(divisionName)){

                UUID playerUUID = p.getUniqueId();

                divisionList.add(divisionName);
                List<UUID> members = getMembers(divisionName);
                members.add(playerUUID);

                DivisionPlugin.getEconomy().createBank(divisionName + "_Bank", Bukkit.getOfflinePlayer(playerUUID));

                config.set("ListOfDivisions", divisionList);

                DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
                Date date = new Date();
                String rootPathInfo = "Divisions." + divisionName + ".Info.";
                String rootPathSettings = "Divisions." + divisionName + ".Settings.";
                String rootPathRanks = "Divisions." + divisionName + ".Ranks.";

                config.set(rootPathInfo + "Is_Public", isPublic);
                config.set(rootPathInfo + "Member_Count", 1);
                config.set(rootPathInfo + "Division_Leader", playerUUID);
                config.set(rootPathInfo + "Members", members);
                config.set(rootPathInfo + "Gold", 0);
                config.set(rootPathInfo + "Date_Created", dateFormat.format(date));
                config.set(rootPathInfo + "Kills", 0);
                config.set(rootPathInfo + "Casualties", 0);
                config.set(rootPathInfo + "Hearts_Destroyed", 0);
                config.set(rootPathInfo + "Power_Level", 0);

                config.set(rootPathSettings + "Can_Invite", new ArrayList<>());
                config.set(rootPathSettings + "Can_Kick", new ArrayList<>());

                for(DivisionRank rank : DivisionRank.values()){
                    config.set(rootPathSettings + rank.name(), new ArrayList<>());
                }

                saveConfig();

            }else{
                chatFactory.sendPlayerMessage("&c&lThis is already a division!", true, p, DivisionPlugin.PREFIX);
            }
        }else{
            chatFactory.sendPlayerMessage("&c&lYou are already in a division!", true, p, DivisionPlugin.PREFIX);
        }
    }

    private boolean isADivision(String divisionName){
        return false;
    }

    private String getPlayerDivision(UUID uuid){
        for(String division : divisionList){
            if(getMembers(division).contains(uuid)){
                return division;
            }
        }
        return null;
    }

    private boolean isOwner(){
        return false;
    }

    private void removeDivision(){}

    private boolean isInDivision(UUID u){
        return getPlayerDivision(u) == null ? false : true;
    }

    private void addMemberToDivision(){}

    private void removeMemberFromDivision(){}

    private void changeDivisionStatus(){}

    private List<UUID> getMembers(String divisionName){
        List<String> membersStrings = getList("Divisions." + divisionName + ".Members");
        return membersStrings.stream().map(UUID::fromString).collect(Collectors.toList());
    }

}
