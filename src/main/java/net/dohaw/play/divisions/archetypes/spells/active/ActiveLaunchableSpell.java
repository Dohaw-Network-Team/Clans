package net.dohaw.play.divisions.archetypes.spells.active;

import net.dohaw.play.divisions.archetypes.ArchetypeKey;
import net.dohaw.play.divisions.archetypes.ArchetypeWrapper;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_16_R2.entity.CraftLivingEntity;
import org.bukkit.craftbukkit.v1_16_R2.entity.CraftSnowball;
import org.bukkit.craftbukkit.v1_16_R2.inventory.CraftItemStack;
import org.bukkit.entity.Player;
import org.bukkit.entity.Snowball;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;

public abstract class ActiveLaunchableSpell extends ActiveSpell{

    public ActiveLaunchableSpell(String customItemBindedToKey, ArchetypeWrapper archetype, Enum KEY, int levelUnlocked) {
        super(customItemBindedToKey, archetype, KEY, levelUnlocked);
    }

    @Override
    public boolean isBowSpell() {
        return false;
    }

    public abstract Material getProjectileMaterial();

    protected void launchProjectile(Player player){
        CraftLivingEntity craftPlayer = (CraftLivingEntity) player;
        Snowball proj = craftPlayer.launchProjectile(Snowball.class);
        proj.setMetadata("spell_key", new FixedMetadataValue(plugin, getKEY().name()));
        ((CraftSnowball) proj).getHandle().setItem(CraftItemStack.asNMSCopy(new ItemStack(getProjectileMaterial())));
    }

    @Override
    public void execute(Player player){
        launchProjectile(player);
    }

}
