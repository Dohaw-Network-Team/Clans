package net.dohaw.play.divisions.events;

import me.c10coding.coreapi.chat.ChatFactory;
import net.dohaw.play.divisions.DivisionsPlugin;
import net.dohaw.play.divisions.PlayerData;
import net.dohaw.play.divisions.archetypes.ArchetypeWrapper;
import net.dohaw.play.divisions.archetypes.spells.Spell;
import net.dohaw.play.divisions.archetypes.spells.SpellWrapper;
import net.dohaw.play.divisions.archetypes.spells.active.ActiveSpell;
import net.dohaw.play.divisions.customitems.CustomItem;
import net.dohaw.play.divisions.events.custom.LevelUpEvent;
import net.dohaw.play.divisions.exceptions.InvalidCustomItemKeyException;
import net.dohaw.play.divisions.managers.CustomItemManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import java.util.List;
import java.util.TreeMap;

public class ProgressListener implements Listener {

    private CustomItemManager customItemManager;
    private ChatFactory chatFactory;

    public ProgressListener(DivisionsPlugin plugin){
        this.customItemManager = plugin.getCustomItemManager();
        this.chatFactory = plugin.getAPI().getChatFactory();
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    /*
        Gives a player their new spells.
     */
    @EventHandler
    public void onPlayerLevelUp(LevelUpEvent e){

        PlayerData pd = e.getPlayerData();
        int level = pd.getLevel();

        Player player = pd.getPlayer().getPlayer();
        ArchetypeWrapper archetype = pd.getArchetype();
        List<SpellWrapper> unlockedSpells = Spell.getArchetypeSpellsUnlocked(archetype, level);

        for(SpellWrapper spell : unlockedSpells){

            if(spell instanceof ActiveSpell){

                ActiveSpell aSpell = (ActiveSpell) spell;

                String customItemBindedToKey = spell.getCustomItemBindedToKey();
            /*
                This may be null if I either gave the spell the wrong key or gave the custom item the wrong key, so checking for null just in case
             */
                CustomItem customItem = customItemManager.getByKey(customItemBindedToKey);
                if(customItem != null){

                    /*
                        Adds the CustomItem to the inventory, then it goes through the player's inventory, find's the item, and alters it's lore to mirror what's hard-coded for the spell.
                     */
                    PlayerInventory inv = player.getInventory();
                    ItemStack spellItem = customItem.toItemStack();
                    inv.addItem(spellItem);

                    TreeMap<Integer, ItemStack> alteredSpellItem = CustomItem.getPlayerItemWithKey(player, customItemBindedToKey);
                    spellItem = customItemManager.alterMeta(aSpell, alteredSpellItem);

                    int slot = alteredSpellItem.firstKey();
                    inv.setItem(slot, spellItem);


                }else{
                    try {
                        throw(new InvalidCustomItemKeyException(customItemBindedToKey));
                    } catch (InvalidCustomItemKeyException ex) {
                        ex.printStackTrace();
                    }
                }

            }

        }

        if(!unlockedSpells.isEmpty()){
            chatFactory.sendPlayerMessage("You have leveled up to level &e" + level + ".You have unlocked the following spells: ", false, player, null);
            for(SpellWrapper spell : unlockedSpells){
                chatFactory.sendPlayerMessage("Spell: " + spell.getName(), false, player, null);
            }
        }

    }

}
