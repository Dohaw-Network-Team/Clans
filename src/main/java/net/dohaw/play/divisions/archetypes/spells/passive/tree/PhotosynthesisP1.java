package net.dohaw.play.divisions.archetypes.spells.passive.tree;

import net.dohaw.play.divisions.PlayerData;
import net.dohaw.play.divisions.archetypes.ArchetypeKey;
import net.dohaw.play.divisions.archetypes.ArchetypeWrapper;
import net.dohaw.play.divisions.archetypes.spells.SpellKey;
import net.dohaw.play.divisions.archetypes.spells.passive.PassiveSpell;
import net.minecraft.server.v1_16_R2.NBTTagCompound;
import org.bukkit.Bukkit;
import org.bukkit.Particle;
import org.bukkit.craftbukkit.v1_16_R2.inventory.CraftItemStack;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class PhotosynthesisP1 extends PassiveSpell implements Listener {

    public PhotosynthesisP1(ArchetypeWrapper archetype, SpellKey KEY, int levelUnlocked) {
        super(archetype, KEY, levelUnlocked);
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    @Override
    public Particle getSpellOwnerParticle() {
        return Particle.VILLAGER_HAPPY;
    }

    @Override
    public Particle getSpellAffecterParticle() {
        return null;
    }

    @Override
    public List<String> getDescription() {
        return null;
    }

    @EventHandler
    public void onDropFood(PlayerItemConsumeEvent e){

        Player player = e.getPlayer();
        PlayerData pd = plugin.getPlayerDataManager().getPlayerByUUID(player.getUniqueId());
        ArchetypeWrapper archetype = pd.getArchetype();
        if(archetype != null){
            if(archetype.getKEY() != ArchetypeKey.TREE){

                ItemStack stack = e.getItem();

                net.minecraft.server.v1_16_R2.ItemStack nmsStack = CraftItemStack.asNMSCopy(stack);
                NBTTagCompound nmsComp = nmsStack.getOrCreateTag();

                if(nmsComp.getBoolean("Is Photo Food") && nmsComp.getString("Owner").equalsIgnoreCase(player.getName())){
                    e.setCancelled(true);
                    player.sendMessage("Only Tree archetypes can eat this food!");
                }

            }
        }

    }

}
