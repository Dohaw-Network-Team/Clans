package net.dohaw.play.divisions.files;

import me.c10coding.coreapi.files.Config;
import net.dohaw.play.divisions.Stat;
import net.dohaw.play.divisions.archetypes.ArchetypeName;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.EnumMap;

public class StatsConfig extends Config {

    public StatsConfig(JavaPlugin plugin) {
        super(plugin, "stats.yml");
    }

    public EnumMap<ArchetypeName, EnumMap<Stat, Double>> getDefaultSkillData(){
        EnumMap<ArchetypeName, EnumMap<Stat, Double>> skillData = new EnumMap<>(ArchetypeName.class);
        for(ArchetypeName aName : ArchetypeName.values()){
            EnumMap<Stat, Double> skills = new EnumMap<>(Stat.class);
            for(Stat stat : Stat.values()){
                double skillValue = config.getDouble("Default Skills." + aName.toString() + "." + stat.toString());
                skills.put(stat, skillValue);
            }
            skillData.put(aName, skills);
        }
        return skillData;
    }

    public EnumMap<Stat, Double> getDefaultArchetypeSkillData(ArchetypeName archetypeName){
        EnumMap<Stat, Double> skillData = new EnumMap<>(Stat.class);
        for(Stat stat : Stat.values()){
            double skillValue = config.getDouble(archetypeName.toString() + "." + stat.toString());
            skillData.put(stat, skillValue);
        }
        return skillData;
    }

}
