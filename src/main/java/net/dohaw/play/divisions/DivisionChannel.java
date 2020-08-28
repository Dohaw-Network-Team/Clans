package net.dohaw.play.divisions;

import lombok.Getter;
import org.bukkit.ChatColor;

import java.util.Arrays;
import java.util.List;

public enum DivisionChannel {

    GENERAL(new String[]{"g", "gen", "general", "ge"}, ChatColor.GREEN),
    WORTHY(new String[]{"w", "wo", "worth", "worthy"}, ChatColor.AQUA),
    NONE(new String[]{"none", "n", "no", "off"}, null);

    @Getter private ChatColor prefixColor;
    @Getter private String[] channelAliases;
    DivisionChannel(String[] channelAliases, ChatColor prefixColor){
        this.prefixColor = prefixColor;
        this.channelAliases = channelAliases;
    }

    public static DivisionChannel getChannelByAlias(String alias){
        for(DivisionChannel channel : DivisionChannel.values()){
            List<String> aliases = Arrays.asList(channel.getChannelAliases());
            if(aliases.contains(alias)){
                return channel;
            }
        }
        return null;
    }

    public String getPrefix(){
        return "[" + prefixColor + this.name() + "&f]";
    }

}
