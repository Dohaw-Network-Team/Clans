package net.dohaw.play.divisions.archetypes.spells;

import lombok.Getter;
import lombok.Setter;
import net.dohaw.play.divisions.archetypes.ArchetypeKey;
import net.dohaw.play.divisions.archetypes.Wrapper;
import org.bukkit.Material;

import java.util.ArrayList;
import java.util.List;

public class SpellWrapper extends Wrapper {

    @Getter @Setter private ArchetypeKey archetype;
    @Getter @Setter private String customItemBindedToKey;

    public SpellWrapper(String customItemBindedToKey, ArchetypeKey archetype, Enum KEY) {
        super(KEY);
        this.customItemBindedToKey = customItemBindedToKey;
        this.archetype = archetype;
    }

    @Override
    public List<String> getAliases() {
        return new ArrayList<String>(){{
            add(getName());
        }};
    }

}
