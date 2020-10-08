package net.dohaw.play.divisions.archetypes.spells.passive.duelist;

import net.dohaw.corelib.helpers.CustomInvHelper;
import net.dohaw.play.divisions.PlayerData;
import net.dohaw.play.divisions.archetypes.ArchetypeKey;
import net.dohaw.play.divisions.archetypes.ArchetypeWrapper;
import net.dohaw.play.divisions.archetypes.spells.SpellKey;
import net.dohaw.play.divisions.archetypes.spells.passive.PassiveSpell;
import org.bukkit.Bukkit;
import org.bukkit.Particle;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;
import org.bukkit.inventory.Inventory;

import java.util.Arrays;
import java.util.List;

public class DuelWielder extends PassiveSpell implements Listener {

    public DuelWielder(ArchetypeWrapper archetype, SpellKey KEY, int levelUnlocked) {
        super(archetype, KEY, levelUnlocked);
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onEquipOffHand(PlayerSwapHandItemsEvent e){
        Player player = e.getPlayer();
        PlayerData pd = plugin.getPlayerDataManager().getPlayerByUUID(player.getUniqueId());
        if(pd.getArchetype() != null) {
            if (pd.getArchetype().getKEY() != ArchetypeKey.DUELIST) {
                e.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onEquipOffHandInInv(InventoryClickEvent e){

        HumanEntity he = e.getWhoClicked();
        if(he instanceof Player){

            Player player = (Player) he;
            Inventory clickedInventory = e.getClickedInventory();
            Inventory playerInv = player.getInventory();
            if(clickedInventory != null){
                if(clickedInventory.equals(playerInv)){
                    int slotClicked = e.getSlot();
                    if(slotClicked == 40){
                        PlayerData pd = plugin.getPlayerDataManager().getPlayerByUUID(player.getUniqueId());
                        if(pd.getArchetype() != null) {
                            if (pd.getArchetype().getKEY() != getArchetype().getKEY()) {

                                e.setCancelled(true);
                                e.setResult(Event.Result.DENY);
                                player.updateInventory();

                            }
                        }
                    }
                }
            }
        }

    }

    @EventHandler
    public void onEquipOffHandInInvDrag(InventoryDragEvent e){

        HumanEntity he = e.getWhoClicked();
        if(he instanceof Player){

            Player player = (Player) he;
            Inventory clickedInventory = e.getInventory();
            Inventory playerInv = player.getInventory();
            if(clickedInventory != null){
                if(clickedInventory.equals(playerInv)){
                    int slotClicked = CustomInvHelper.getSlotClickedInDragEvent(e);
                    if(slotClicked == 40){
                        PlayerData pd = plugin.getPlayerDataManager().getPlayerByUUID(player.getUniqueId());
                        if(pd.getArchetype() != null) {
                            if (pd.getArchetype().getKEY() != getArchetype().getKEY()) {
                                e.setCancelled(true);
                                e.setResult(Event.Result.DENY);
                            }
                        }
                    }
                }
            }
        }

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
        return Arrays.asList("Decides whether a player can duel-wield or not");
    }

}
