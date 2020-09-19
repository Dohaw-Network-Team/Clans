package net.dohaw.play.divisions.archetypes;

import org.bukkit.Bukkit;

import java.util.HashMap;
import java.util.Map;

public class WrapperHolder {

    public static Map<Enum, Wrapper> wrappers = new HashMap<>();

    /*
        Has archetype, spell, and specialization wrappers
     */
    public static Wrapper getByKey(Enum key){
        return wrappers.get(key);
    }

    public static Wrapper getArchetypeByAlias(String alias){
        for(Map.Entry<Enum, Wrapper> entry : wrappers.entrySet()){
            Wrapper wrapper = entry.getValue();
            if(wrapper instanceof ArchetypeWrapper){
                if(wrapper.getAliases().contains(alias)){
                    return wrapper;
                }
            }
        }
        return null;
    }

    public static void registerWrapper(Wrapper wrapper){
        if(wrappers.containsKey(wrapper.getKEY())){
            throw new IllegalArgumentException("This wrapper (" + wrapper.KEY + ") is already registered!");
        }
        wrappers.put(wrapper.getKEY(), wrapper);
    }

}
