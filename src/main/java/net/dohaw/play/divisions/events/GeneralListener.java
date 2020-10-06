package net.dohaw.play.divisions.events;

import net.dohaw.play.corelib.ChatSender;
import net.dohaw.play.divisions.DivisionChannel;
import net.dohaw.play.divisions.DivisionsPlugin;
import net.dohaw.play.divisions.PlayerData;
import net.dohaw.play.divisions.archetypes.ArchetypeKey;
import net.dohaw.play.divisions.archetypes.ArchetypeWrapper;
import net.dohaw.play.divisions.archetypes.spells.CooldownDecreasable;
import net.dohaw.play.divisions.archetypes.spells.Cooldownable;
import net.dohaw.play.divisions.archetypes.spells.Spell;
import net.dohaw.play.divisions.archetypes.spells.active.ActiveHittableSpell;
import net.dohaw.play.divisions.archetypes.spells.active.ActiveSpell;
import net.dohaw.play.divisions.archetypes.spells.bowspell.BowSpell;
import net.dohaw.play.divisions.archetypes.types.Archer;
import net.dohaw.play.divisions.customitems.CustomItem;
import net.dohaw.play.divisions.division.Division;
import net.dohaw.play.divisions.events.custom.NewMemberEvent;
import net.dohaw.play.divisions.events.custom.SpellCooldownDoneEvent;
import net.dohaw.play.divisions.events.custom.TemporaryPlayerDataCreationEvent;
import net.dohaw.play.divisions.files.DefaultConfig;
import net.dohaw.play.divisions.managers.CustomItemManager;
import net.dohaw.play.divisions.managers.DivisionsManager;
import net.dohaw.play.divisions.managers.PlayerDataManager;
import net.dohaw.play.divisions.utils.Calculator;
import net.dohaw.play.divisions.utils.DivisionChat;
import net.dohaw.play.divisions.utils.EntityUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;

import java.util.List;
import java.util.UUID;

public class GeneralListener implements Listener {

    private DivisionsPlugin plugin;
    private PlayerDataManager playerDataManager;
    private DivisionsManager divisionsManager;
    private DefaultConfig defaultConfig;

    public GeneralListener(DivisionsPlugin plugin){
        this.plugin = plugin;
        this.playerDataManager = plugin.getPlayerDataManager();
        this.divisionsManager = plugin.getDivisionsManager();
        this.defaultConfig = plugin.getDefaultConfig();
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e){

        Player player = e.getPlayer();
        if(playerDataManager.getPlayerByUUID(player.getUniqueId()) == null){
            playerDataManager.addPlayerData(player.getUniqueId());
            playerDataManager.setPlayerDivision(playerDataManager.getPlayerByUUID(player.getUniqueId()));
        }

        PlayerData pd = playerDataManager.getPlayerByUUID(player.getUniqueId());
        /*
            Possibly add a delay to this
         */
        if(pd.getDivision() != null){
            Division division = divisionsManager.getDivision(pd.getDivision());
            DivisionChat.sendMOTD(division, player);
        }

        if(pd.getArchetype() != null){
            CustomItemManager customItemManager = plugin.getCustomItemManager();
            customItemManager.setSpellItemLores(pd);
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

        if(EntityUtils.isValidOnlinePlayer(uuid)){
            Player player = Bukkit.getPlayer(uuid);
            Division division = e.getDivision();
            DivisionChat.sendMOTD(division, player);
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
                            ChatSender.sendPlayerMessage(msg, true, recipient, channelPrefix);
                        }
                    }

                }

            }
        }

    }

    /*
        This runs first. Calculates base damage upon the initial hit

        DAMAGE IS PASSED BETWEEN EVENTS.
     */
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
                    if(e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK){

                        ItemStack stackInHand = e.getItem();
                        String customItemKey = CustomItem.getCustomItemKey(stackInHand);

                        if(customItemKey != null){
                            if(!customItemKey.isEmpty()) {

                                ActiveSpell spell = Spell.getSpellByItemKey(customItemKey);
                                /*
                                    Don't want to waste resources if this spell is supposed to be activated via hitting a player instead of right clicking
                                 */
                                if (spell != null) {

                                    if (!(spell instanceof ActiveHittableSpell)) {

                                        if (archetype.getKEY() == spell.getArchetype().getKEY()) {

                                            int playerLevel = pd.getLevel();
                                            if (spell.getLevelUnlocked() <= playerLevel) {
                                                if (!pd.isOnCooldown(spell)) {

                                                    if (pd.hasEnoughRegen(spell)) {

                                                    /*
                                                        Adds the cooldown to a map within the PlayerData object. Delays a task to remove the spell from the map to signal that it's not on cooldown
                                                     */
                                                        if(!defaultConfig.isInNoCooldownMode()){
                                                            pd.addCoolDown(spell);
                                                        }

                                                        pd.subtractRegen(spell);
                                                        spell.execute(pd);
                                                        playerDataManager.updatePlayerData(pd);

                                                        startCooldownScheduler(pd, spell);

                                                    }

                                                } else {
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

    }

    /*
        For active spells that are triggered by hitting a player while the custom item is in their hand
     */
    @EventHandler
    public void onPlayerHit(EntityDamageByEntityEvent e){

        Entity enDamager = e.getDamager();

        if(enDamager instanceof Player){

            Player player = (Player) enDamager;
            PlayerData pd = playerDataManager.getPlayerByUUID(player.getUniqueId());
            ArchetypeWrapper archetype = pd.getArchetype();
            if(archetype != null) {
                ItemStack itemInHand = player.getInventory().getItemInMainHand();
                if(itemInHand != null){

                    String customItemKey = CustomItem.getCustomItemKey(itemInHand);
                    if(customItemKey != null) {
                        if (!customItemKey.isEmpty()) {

                            ActiveSpell spell = Spell.getSpellByItemKey(customItemKey);
                            if(spell != null){

                                if(spell instanceof ActiveHittableSpell){

                                    ArchetypeKey spellArchetype = spell.getArchetype().getKEY();
                                    ArchetypeKey playerArchetype = pd.getArchetype().getKEY();

                                    if(spellArchetype == playerArchetype){

                                        int playerLevel = pd.getLevel();
                                        if (spell.getLevelUnlocked() <= playerLevel) {

                                            if (!pd.isOnCooldown(spell)) {
                                                if (pd.hasEnoughRegen(spell)) {

                                                    pd.addCoolDown(spell);
                                                    pd.subtractRegen(spell);
                                                    playerDataManager.updatePlayerData(pd);

                                                    Entity hitEntity = e.getEntity();
                                                    ((ActiveHittableSpell)spell).executeHit(hitEntity, player);

                                                    startCooldownScheduler(pd, spell);

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

    }

    /*
        Allows Archers to cycle through bow spells.
     */
    @EventHandler
    public void onArcherBowLeftClick(PlayerInteractEvent e){

        Player player = e.getPlayer();

        Action action = e.getAction();
        ItemStack itemInHand = e.getItem();

        if(action == Action.LEFT_CLICK_AIR || action == Action.LEFT_CLICK_BLOCK){

            if(itemInHand != null){

                if(itemInHand.getType() == Material.BOW || itemInHand.getType() == Material.CROSSBOW){

                    PlayerData pd = playerDataManager.getPlayerByUUID(player.getUniqueId());
                    ArchetypeWrapper archetype = pd.getArchetype();

                    if(archetype instanceof Archer){

                        Archer archer = (Archer) archetype;
                        BowSpell currentBowSpell = archer.getBowSpell();

                        /*
                            If they shift left click then they can un-select any bow spell
                         */
                        if(!player.isSneaking()){

                            List<BowSpell> unlockedBowSpells = Spell.getUnlockedBowSpells(archetype, pd.getLevel());
                            if(!unlockedBowSpells.isEmpty()){

                                if(currentBowSpell != null){
                                    currentBowSpell = getNextBowSpell(unlockedBowSpells, currentBowSpell);
                                }else{
                                    currentBowSpell = unlockedBowSpells.get(0);
                                }

                                player.sendMessage("Bow spell: " + currentBowSpell);
                            }

                        }else{
                            currentBowSpell = null;
                        }

                        archer.setBowSpell(currentBowSpell);
                        pd.setArchetype(archer);
                        playerDataManager.updatePlayerData(pd);

                    }

                }

            }

        }

    }

    @EventHandler
    public void onArrowShoot(EntityShootBowEvent e){

        Entity en = e.getEntity();
        if(en instanceof Player){

            UUID playerUUID = en.getUniqueId();
            boolean isArcher = playerDataManager.isClass(playerUUID, ArchetypeKey.ARCHER);

            if(isArcher){

                PlayerData pd = playerDataManager.getPlayerByUUID(playerUUID);
                Archer archer = (Archer) pd.getArchetype();
                BowSpell bowSpell = archer.getBowSpell();

                if(bowSpell != null){

                    Entity proj = e.getProjectile();

                    if(!pd.isOnCooldown(bowSpell)){

                        if(proj instanceof Arrow){

                            String bowSpellKey = bowSpell.getKEY().toString();
                            proj.setMetadata("bow_spell", new FixedMetadataValue(plugin, bowSpellKey));
                            e.setProjectile(proj);

                            /*
                                Not all bow spells implement cooldownable. This is done just in case I
                             */
                            pd.addCoolDown(bowSpell);
                            pd.subtractRegen(bowSpell);

                            playerDataManager.updatePlayerData(pd);

                        }

                    }else{
                        Bukkit.broadcastMessage("Cooldown time: " + pd.getCooldownEnd(bowSpell));
                    }

                }

            }

        }
    }

    @EventHandler
    public void onPlayerTakeArrowDamage(EntityDamageByEntityEvent e){

        Entity enDamager = e.getDamager();
        Entity enDamageTaker = e.getEntity();

        if(enDamager instanceof Arrow){

            Arrow arrow = (Arrow) enDamager;
            if(arrow.hasMetadata("bow_spell")){

                Player damager = (Player) arrow.getShooter();

                String bowSpellKey = enDamager.getMetadata("bow_spell").get(0).asString();
                BowSpell bowSpell = Spell.getBowSpellByKey(bowSpellKey);

                PlayerData shootersData = playerDataManager.getPlayerByUUID(damager.getUniqueId());

                bowSpell.affectHitEntity(enDamageTaker, shootersData);

            }
        }

    }

    private BowSpell getNextBowSpell(List<BowSpell> unlockedBowSpells, BowSpell currentBowSpell){

        for(BowSpell sw : unlockedBowSpells){
            if(sw.getKEY() == currentBowSpell.getKEY()){

                int index = unlockedBowSpells.indexOf(sw);

                if(index == (unlockedBowSpells.size() - 1) ){
                    return unlockedBowSpells.get(0);
                }else{
                    return unlockedBowSpells.get(index + 1);
                }

            }
        }

        // Only runs if unlocked bow spells is empty
        return currentBowSpell;

    }

    private double getCooldown(PlayerData pd, Cooldownable spell){

        double cooldown;
        if (spell instanceof CooldownDecreasable) {
            cooldown = Spell.getSchedulerInterval(pd, (CooldownDecreasable) spell);
        } else {
            cooldown = Spell.getSchedulerInterval(spell);
        }

        return cooldown;

    }

    public void startCooldownScheduler(PlayerData pd, ActiveSpell spell){
        double cooldown = getCooldown(pd, spell);
        Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, () -> {
            pd.removeCoolDown(spell);
            Bukkit.getPluginManager().callEvent(new SpellCooldownDoneEvent(spell, pd.getPLAYER_UUID()));
            playerDataManager.updatePlayerData(pd);
        }, (long) cooldown);
    }

}
