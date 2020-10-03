package net.dohaw.play.divisions.archetypes.spells.active;

import net.dohaw.play.divisions.PlayerData;
import net.dohaw.play.divisions.archetypes.ArchetypeWrapper;
import net.dohaw.play.divisions.archetypes.spells.Damageable;
import net.dohaw.play.divisions.archetypes.spells.SpellKey;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.craftbukkit.v1_16_R2.entity.CraftLivingEntity;
import org.bukkit.craftbukkit.v1_16_R2.entity.CraftSnowball;
import org.bukkit.craftbukkit.v1_16_R2.inventory.CraftItemStack;
import org.bukkit.entity.Player;
import org.bukkit.entity.Snowball;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;

import java.util.Arrays;
import java.util.List;

public class InvisibleStrike extends ActiveLaunchableSpell implements Damageable{

    public InvisibleStrike(String customItemBindedToKey, ArchetypeWrapper archetype, SpellKey KEY, int levelUnlocked) {
        super(customItemBindedToKey, archetype, KEY, levelUnlocked);
    }

    @Override
    protected void launchProjectile(Player player){
        CraftLivingEntity craftPlayer = (CraftLivingEntity) player;
        Snowball proj = craftPlayer.launchProjectile(Snowball.class);
        proj.setMetadata("spell_key", new FixedMetadataValue(plugin, getKEY().name()));
        ((CraftSnowball) proj).getHandle().setItem(CraftItemStack.asNMSCopy(new ItemStack(getProjectileMaterial())));
    }

    @Override
    public boolean displayCooldownMessage() {
        return false;
    }

    @Override
    public List<String> getCooldownLorePart(PlayerData pd) {
        return Arrays.asList("");
    }

    @Override
    public double alterDamage(double dmg, PlayerData pd) {
        return 0;
    }

    @Override
    public List<String> getDamageLorePart() {
        return Arrays.asList("");
    }

    @Override
    public Particle getSpellOwnerParticle() {
        return null;
    }

    @Override
    public Particle getSpellAffecterParticle() {
        return null;
    }

    @Override
    public List<String> getDescription() {
        return Arrays.asList("Hits your enemies with an attack that is obscure and undetectable");
    }

    @Override
    public double getBaseCooldown() {
        return 0.5;
    }

    @Override
    public double getPercentageRegenAffected() {
        return 0.10;
    }

    @Override
    public Material getProjectileMaterial() {
        return null;
    }
}
