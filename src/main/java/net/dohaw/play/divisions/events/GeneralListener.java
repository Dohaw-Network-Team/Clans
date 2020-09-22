package net.dohaw.play.divisions.events;

import me.c10coding.coreapi.chat.ChatFactory;
import net.dohaw.play.divisions.DamageType;
import net.dohaw.play.divisions.DivisionChannel;
import net.dohaw.play.divisions.DivisionsPlugin;
import net.dohaw.play.divisions.archetypes.ArchetypeWrapper;
import net.dohaw.play.divisions.archetypes.spells.Spell;
import net.dohaw.play.divisions.archetypes.spells.active.ActiveSpell;
import net.dohaw.play.divisions.customitems.CustomItem;
import net.dohaw.play.divisions.division.Division;
import net.dohaw.play.divisions.events.custom.NewMemberEvent;
import net.dohaw.play.divisions.events.custom.SpellCooldownDoneEvent;
import net.dohaw.play.divisions.events.custom.TemporaryPlayerDataCreationEvent;
import net.dohaw.play.divisions.files.DefaultConfig;
import net.dohaw.play.divisions.managers.DivisionsManager;
import net.dohaw.play.divisions.managers.PlayerDataManager;
import net.dohaw.play.divisions.PlayerData;
import net.dohaw.play.divisions.utils.Calculator;
import net.dohaw.play.divisions.utils.DivisionChat;
import net.dohaw.play.divisions.utils.EntityUtils;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.UUID;

public class GeneralListener implements Listener {

    private DivisionsPlugin plugin;
    private PlayerDataManager playerDataManager;
    private ChatFactory chatFactory;
    private DivisionsManager divisionsManager;
    private DefaultConfig defaultConfig;

    public GeneralListener(DivisionsPlugin plugin){
        this.plugin = plugin;
        this.playerDataManager = plugin.getPlayerDataManager();
        this.chatFactory = plugin.getAPI().getChatFactory();
        this.divisionsManager = plugin.getDivisionsManager();
        this.defaultConfig = plugin.getDefaultConfig();
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e){

        Player player = e.getPlayer();
        if(playerDataManager.getByPlayerObj(player) == null){
            playerDataManager.addPlayerData(player.getUniqueId());
            playerDataManager.setPlayerDivision(playerDataManager.getPlayerByUUID(player.getUniqueId()));
        }

        PlayerData pd = playerDataManager.getPlayerByUUID(player.getUniqueId());

        /*
            Possibly add a delay to this
         */
        if(pd.getDivision() != null){
            Division division = divisionsManager.getDivision(pd.getDivision());
            DivisionChat.sendMOTD(chatFactory, division, player);
        }

    }

    @EventHandler
    public void onPlayerLeave(PlayerQuitEvent e){
        UUID playerUUID = e.getPlayer().getUniqueId();
        if(playerDataManager.getPlayerByUUID(playerUUID) != null){
            playerDataManager.removePlayerData(playerUUID);
        }
    }

    @EventHandler
    public void onAdditionOfMember(NewMemberEvent e){

        UUID uuid = e.getUuid();
        OfflinePlayer op = Bukkit.getOfflinePlayer(uuid);

        if(op.isOnline()){
            Player player = op.getPlayer();
            Division division = e.getDivision();
            DivisionChat.sendMOTD(chatFactory, division, player);
        }

    }

    @EventHandler
    public void onTemporaryPlayerDataCreation(TemporaryPlayerDataCreationEvent e){
        UUID tempDataUUID = e.getUuidTempData();
        playerDataManager.removePlayerData(tempDataUUID);
    }

    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent e){

        Player player = e.getPlayer();
        UUID uuid = player.getUniqueId();
        PlayerData pd = playerDataManager.getPlayerByUUID(uuid);

        if(pd.getDivision() != null){
            DivisionChannel channel = pd.getChannel();
            if(channel != DivisionChannel.NONE){

                e.setCancelled(true);
                Division division = divisionsManager.getDivision(pd.getDivision());
                List<UUID> members = division.getPlayers();

                String msg = "<" + player.getName() + "> " + "&7" + e.getMessage();
                for(UUID memberUUID : members){

                    PlayerData pData = playerDataManager.getPlayerByUUID(memberUUID);

                    DivisionChannel pChannel = pData.getChannel();
                    OfflinePlayer op = pData.getPlayer();

                    if(op.isOnline()){
                        if(pChannel == channel){
                            String channelPrefix = channel.getPrefix();
                            Player recipient = op.getPlayer();
                            chatFactory.sendPlayerMessage(msg, true, recipient, channelPrefix);
                        }
                    }

                }

            }
        }

    }

    @EventHandler (priority = EventPriority.LOWEST)
    public void onPlayerTakeDamage(EntityDamageByEntityEvent e){
        double newDmg = Calculator.getNewDamage(e, playerDataManager);
        e.setDamage(newDmg);
    }

    /*
        Thing that casts spells.
     */
    @EventHandler
    public void onSpellUse(PlayerInteractEvent e){
        Player player = e.getPlayer();
        if(playerDataManager.getPlayerByUUID(player.getUniqueId()) != null){
            PlayerData pd = playerDataManager.getPlayerByUUID(player.getUniqueId());
            ArchetypeWrapper archetype = pd.getArchetype();
            if(archetype != null){
                if(e.getItem() != null){

                    ItemStack stack = e.getItem();
                    String customItemKey = CustomItem.getCustomItemKey(stack);

                    if(customItemKey != null){
                        if(!customItemKey.isEmpty()){
                            ActiveSpell spell = Spell.getSpellByItemKey(customItemKey);
                            String spellKey = spell.getKEY().toString();
                            if(archetype.getKEY() == spell.getArchetype().getKEY()){
                                if(spell != null){
                                    int playerLevel = pd.getLevel();
                                    if(spell.getLevelUnlocked() <= playerLevel){
                                        if(!pd.isOnCooldown(spellKey)) {

                                            if (Spell.hasEnoughRegen(pd, spell)) {

                                                /*
                                                    Adds the cooldown to a map within the PlayerData object. Delays a task to remove the spell from the map to signal that it's not on cooldown
                                                 */
                                                double spellCooldown = spell.getCooldown();
                                                long schedulerDelay = (long) (spellCooldown * 20);
                                                UUID playerUUID = player.getUniqueId();

                                                pd.addCoolDown(spellKey, spellCooldown);

                                                double regenCost = Calculator.getSpellRegenCost(pd, spell);
                                                double playerRegen = pd.getRegen();
                                                pd.setRegen(playerRegen - regenCost);
                                                playerDataManager.updatePlayerData(playerUUID, pd);

                                                spell.execute(player, true);

                                                Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, () -> {
                                                    pd.removeCoolDown(spellKey);
                                                    Bukkit.getPluginManager().callEvent(new SpellCooldownDoneEvent(spellKey, playerUUID));
                                                    playerDataManager.updatePlayerData(playerUUID, pd);
                                                }, schedulerDelay);

                                            }

                                        }else{
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }



}
