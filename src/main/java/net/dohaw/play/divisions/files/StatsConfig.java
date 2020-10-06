package net.dohaw.play.divisions.files;

import net.dohaw.corelib.Config;
import net.dohaw.play.divisions.Stat;
import net.dohaw.play.divisions.archetypes.ArchetypeKey;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.EnumMap;

public class StatsConfig extends Config {

    public StatsConfig(JavaPlugin plugin) {
        super(plugin, "stats.yml");
    }

    public EnumMap<ArchetypeKey, EnumMap<Stat, Double>> getDefaultSkillData(){
        EnumMap<ArchetypeKey, EnumMap<Stat, Double>> skillData = new EnumMap<>(ArchetypeKey.class);
        for(ArchetypeKey aName : ArchetypeKey.values()){
            EnumMap<Stat, Double> skills = new EnumMap<>(Stat.class);
            for(Stat stat : Stat.values()){
                double skillValue = config.getDouble("Default Skills." + aName.toString() + "." + stat.toString());
                skills.put(stat, skillValue);
            }
            skillData.put(aName, skills);
        }
        return skillData;
    }

    public EnumMap<Stat, Double> getDefaultArchetypeSkillData(ArchetypeKey archetypeKey){
        EnumMap<Stat, Double> skillData = new EnumMap<>(Stat.class);
        for(Stat stat : Stat.values()){
            double skillValue = config.getDouble(archetypeKey.toString() + "." + stat.toString());
            skillData.put(stat, skillValue);
        }
        return skillData;
    }

}
