package net.dohaw.play.divisions.archetypes;

import java.util.HashMap;
import java.util.Map;

public class WrapperHolder {

    public static Map<Enum, Wrapper> wrappers = new HashMap<>();

    public static Wrapper getByKey(Enum key){
        return wrappers.get(key);
    }

    public static Wrapper getByAlias(String alias){
        for(Map.Entry<Enum, Wrapper> entry : wrappers.entrySet()){
            Wrapper wrapper = entry.getValue();
            if(wrapper.getAliases().contains(alias)){
                return wrapper;
            }
        }
        return null;
    }

    public static void registerWrapper(Wrapper wrapper){
        if(wrappers.containsKey(wrapper.getKEY())){
            throw new IllegalArgumentException("This archetype is already registered!");
        }
        wrappers.put((ArchetypeKey) wrapper.getKEY(), wrapper);
    }

}
