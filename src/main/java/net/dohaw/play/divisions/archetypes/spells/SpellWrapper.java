package net.dohaw.play.divisions.archetypes.spells;

import lombok.Getter;
import lombok.Setter;
import net.dohaw.play.divisions.DivisionsPlugin;
import net.dohaw.play.divisions.PlayerData;
import net.dohaw.play.divisions.archetypes.Archetype;
import net.dohaw.play.divisions.archetypes.ArchetypeKey;
import net.dohaw.play.divisions.archetypes.ArchetypeWrapper;
import net.dohaw.play.divisions.archetypes.Wrapper;
import net.dohaw.play.divisions.customitems.CustomItem;
import net.dohaw.play.divisions.files.DefaultConfig;
import net.dohaw.play.divisions.managers.CustomItemManager;
import net.dohaw.play.divisions.managers.PlayerDataManager;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.craftbukkit.v1_16_R2.entity.CraftLivingEntity;
import org.bukkit.craftbukkit.v1_16_R2.entity.CraftSnowball;
import org.bukkit.craftbukkit.v1_16_R2.inventory.CraftItemStack;
import org.bukkit.entity.Player;
import org.bukkit.entity.Snowball;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;

import java.util.ArrayList;
import java.util.List;

public abstract class SpellWrapper extends Wrapper {

    protected static DivisionsPlugin plugin;
    protected DefaultConfig defaultConfig;

    @Getter @Setter protected ArchetypeWrapper archetype;
    @Getter @Setter protected String customItemBindedToKey;
    @Getter protected int levelUnlocked;

    public SpellWrapper(String customItemBindedToKey, ArchetypeWrapper archetype, Enum KEY, int levelUnlocked) {
        super(KEY);
        this.customItemBindedToKey = customItemBindedToKey;
        this.archetype = archetype;
        this.levelUnlocked = levelUnlocked;

        plugin = DivisionsPlugin.getInstance();
        this.defaultConfig = plugin.getDefaultConfig();
    }

    @Override
    public List<String> getAliases() {
        return new ArrayList<String>(){{
            add(getName());
        }};
    }

    public abstract double getCooldown();

    public abstract boolean displayCooldownMessage();

    public abstract double alterDamage(double dmg, PlayerData pd);

    public abstract double getRange();

    public abstract Particle getSpellOwnerParticle();

    public abstract Particle getSpellAffecterParticle();

}
