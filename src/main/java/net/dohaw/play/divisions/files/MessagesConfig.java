package net.dohaw.play.divisions.files;

import net.dohaw.corelib.Config;
import net.dohaw.play.divisions.Message;
import net.dohaw.play.divisions.Placeholder;
import org.bukkit.plugin.java.JavaPlugin;

public class MessagesConfig extends Config {

    public MessagesConfig(JavaPlugin plugin) {
        super(plugin, "messages.yml");
    }

    public String getMessage(Message msg){
        return config.getString(msg.getConfigKey());
    }

    public static String replacePlaceholder(String msg, Placeholder pHolder, String replacement){
        return msg.replace(pHolder.getPlaceholder(), replacement);
    }

}
