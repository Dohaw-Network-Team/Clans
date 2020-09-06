package net.dohaw.play.divisions.events;

import me.c10coding.coreapi.chat.ChatFactory;
import net.dohaw.play.divisions.DamageType;
import net.dohaw.play.divisions.DivisionChannel;
import net.dohaw.play.divisions.DivisionsPlugin;
import net.dohaw.play.divisions.division.Division;
import net.dohaw.play.divisions.events.custom.NewMemberEvent;
import net.dohaw.play.divisions.events.custom.TemporaryPlayerDataCreationEvent;
import net.dohaw.play.divisions.files.DefaultConfig;
import net.dohaw.play.divisions.managers.DivisionsManager;
import net.dohaw.play.divisions.managers.PlayerDataManager;
import net.dohaw.play.divisions.PlayerData;
import net.dohaw.play.divisions.utils.DamageCalculator;
import net.dohaw.play.divisions.utils.DivisionChat;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.projectiles.ProjectileSource;

import java.util.List;
import java.util.UUID;

public class GeneralListener implements Listener {

    private PlayerDataManager playerDataManager;
    private ChatFactory chatFactory;
    private DivisionsManager divisionsManager;
    private DefaultConfig defaultConfig;

    public GeneralListener(DivisionsPlugin plugin){
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
        if(playerDataManager.getPlayerByUUID(e.getPlayer().getUniqueId()) != null){
            playerDataManager.removePlayerData(e.getPlayer().getUniqueId());
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
                    OfflinePlayer op = pData.getPLAYER();

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

    @EventHandler
    public void onPlayerTakeDamage(EntityDamageByEntityEvent e){

        Entity eDamager = e.getDamager();
        Entity eDamageTaker = e.getEntity();

        double dmg = e.getFinalDamage();

        if(isEntityInvolvedAPlayer(eDamager)){

            DamageType damageType;
            Player damager;

            if(eDamager instanceof Projectile){
                damageType = DamageType.RANGED;
                damager = (Player) getPotentialPlayerFromProjectile(eDamager);
            }else{
                damageType = DamageType.MELEE;
                damager = (Player) eDamager;
            }

            PlayerData pd = playerDataManager.getPlayerByUUID(damager.getUniqueId());

            double dmgScale = damageType == DamageType.RANGED ? defaultConfig.getBowDamageScale() : defaultConfig.getMeleeDamageScale();
            double dmgDivisionScale = damageType == DamageType.RANGED ? defaultConfig.getRangedDamageDivisionScale() : defaultConfig.getMeleeDamageDivisionScale();

            dmg = DamageCalculator.factorInDamage(pd, dmg, dmgScale, dmgDivisionScale);

        }

        if(isEntityInvolvedAPlayer(eDamageTaker)){

            Player damageTaker;

            if(eDamageTaker instanceof Projectile){
                damageTaker = (Player) getPotentialPlayerFromProjectile(eDamageTaker);
            }else{
                damageTaker = (Player) eDamageTaker;
            }
            PlayerData pd = playerDataManager.getPlayerByUUID(damageTaker.getUniqueId());

            double toughnessScale = defaultConfig.getToughnessScale();
            dmg = DamageCalculator.factorInToughness(pd, dmg, toughnessScale);

        }
        e.setDamage(dmg);

    }

    private Entity getPotentialPlayerFromProjectile(Entity entity){
        ProjectileSource source = ((Projectile) entity).getShooter();
        return source instanceof Entity ? (Entity) source : null;
    }

    private boolean isEntityInvolvedAPlayer(Entity entityInvolved){
        if(entityInvolved instanceof Player){
            return true;
        }else{
            if(getPotentialPlayerFromProjectile(entityInvolved) != null){
                Entity potentialPlayer = getPotentialPlayerFromProjectile(entityInvolved);
                return potentialPlayer instanceof Player;
            }
        }
        return false;
    }

}
