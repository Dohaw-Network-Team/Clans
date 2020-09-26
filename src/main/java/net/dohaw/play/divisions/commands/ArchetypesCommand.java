package net.dohaw.play.divisions.commands;

import me.c10coding.coreapi.chat.ChatFactory;
import me.c10coding.coreapi.helpers.PlayerHelper;
import net.dohaw.play.divisions.DivisionsPlugin;
import net.dohaw.play.divisions.PlayerData;
import net.dohaw.play.divisions.Stat;
import net.dohaw.play.divisions.archetypes.Archetype;
import net.dohaw.play.divisions.archetypes.ArchetypeWrapper;
import net.dohaw.play.divisions.customitems.CustomItem;
import net.dohaw.play.divisions.events.custom.LevelUpEvent;
import net.dohaw.play.divisions.files.DefaultConfig;
import net.dohaw.play.divisions.managers.CustomItemManager;
import net.dohaw.play.divisions.managers.PlayerDataManager;
import net.dohaw.play.divisions.utils.Calculator;
import net.dohaw.play.divisions.utils.EntityUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ArchetypesCommand implements CommandExecutor {

    private DivisionsPlugin plugin;
    private PlayerDataManager playerDataManager;
    private ChatFactory chatFactory;
    private CustomItemManager customItemManager;

    public ArchetypesCommand(DivisionsPlugin plugin){
        this.plugin = plugin;
        this.chatFactory = plugin.getAPI().getChatFactory();
        this.playerDataManager = plugin.getPlayerDataManager();
        this.customItemManager = plugin.getCustomItemManager();
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if(args[0].equalsIgnoreCase("set") && args.length >= 3){

            if(args[1].equals("level") && args.length == 4){

                String playerName = args[2];

                if(plugin.getAPI().getMathHelper().isInt(args[3])){
                    int level = Integer.parseInt(args[3]);
                    if(EntityUtils.isValidOnlinePlayer(playerName)){

                        Player player = Bukkit.getPlayer(playerName);
                        UUID playerUUID = player.getUniqueId();
                        PlayerData pd = playerDataManager.getPlayerByUUID(playerUUID);

                        pd.setLevel(level);
                        Bukkit.getPluginManager().callEvent(new LevelUpEvent(pd));
                        playerDataManager.updatePlayerData(playerUUID, pd);

                        chatFactory.sendPlayerMessage("You have set this player's level to " + level, false, sender, null);
                        chatFactory.sendPlayerMessage("Your level has been set to " + level, false, player, null);

                    }else{
                        chatFactory.sendPlayerMessage("This is not a valid player!", false, sender, null);
                    }
                }else{
                    chatFactory.sendPlayerMessage("This is not a valid integer!", false, sender, null);
                }

            }else{

                String playerName = args[1];
                String archetypeAlias = args[2];

                if(EntityUtils.isValidOnlinePlayer(playerName)){
                    if(Archetype.getArchetypeByAlias(archetypeAlias) != null){

                        Player playerGettingArch = Bukkit.getPlayer(playerName);
                        ArchetypeWrapper archetype = (ArchetypeWrapper) Archetype.getArchetypeByAlias(archetypeAlias);

                        UUID playerUUID = playerGettingArch.getUniqueId();
                        PlayerData pd = playerDataManager.getPlayerByUUID(playerUUID);

                        if(pd.getArchetype() == null){

                            pd.setArchetype(archetype);

                            pd.setStatLevels(archetype.getDefaultStats());
                            giveDefaultItems(playerGettingArch, archetype);
                            customItemManager.setSpellItemLores(pd);

                            pd.startRegener(plugin, Calculator.calculateRegenInterval(pd));
                            playerDataManager.updatePlayerData(playerUUID, pd);
                            chatFactory.sendPlayerMessage("You have given this player the archetype " + archetype.getName() + "!", true, sender, plugin.getPluginPrefix());

                        }else{
                            chatFactory.sendPlayerMessage("This player already has an archetype!", true, sender, plugin.getPluginPrefix());
                        }

                    }
                }else{
                    chatFactory.sendPlayerMessage("This player either isn't online or isn't valid!", true, sender, plugin.getPluginPrefix());
                }

            }

        }else if(args[0].equalsIgnoreCase("reset") && args.length <= 2){
            if(args.length == 2){
                String playerName = args[1];
                if(Bukkit.getPlayer(playerName) != null){

                    Player player = Bukkit.getPlayer(playerName);
                    UUID playerUUID = player.getUniqueId();
                    PlayerData pd = playerDataManager.getPlayerByUUID(playerUUID);
                    resetArchetype(pd);

                }else{
                    chatFactory.sendPlayerMessage("This player either isn't online or isn't valid!", true, sender, plugin.getPluginPrefix());
                }
            }else{
                if(sender instanceof Player){
                    Player pSender = (Player) sender;
                    PlayerData pd = playerDataManager.getPlayerByUUID(pSender.getUniqueId());
                    resetArchetype(pd);
                }else{
                    chatFactory.sendPlayerMessage("Only players can do this to themselves!", true, sender, plugin.getPluginPrefix());
                }
            }
        }
        return false;
    }

    private void resetArchetype(PlayerData pd){

        if(pd.getArchetype() != null){
            pd.setArchetype(null);
            pd.setLevel(1);
            pd.setExp(0);
            pd.setSpeciality(null);
            pd.setStatLevels(Stat.getDefaultStats());
            pd.stopRegener();
            playerDataManager.updatePlayerData(pd.getPLAYER_UUID(), pd);
            chatFactory.sendPlayerMessage("You have reset your archetype as well as your archetype stats!", true, pd.getPlayer().getPlayer(), plugin.getPluginPrefix());
        }else{
            chatFactory.sendPlayerMessage("You don't have an archetype right now!", true, pd.getPlayer().getPlayer(), plugin.getPluginPrefix());
        }

        UUID playerUUID = pd.getPLAYER_UUID();
        Player player = Bukkit.getPlayer(playerUUID);
        player.getInventory().clear();

        player.performCommand("/spawn");

    }

    private void giveDefaultItems(Player player, ArchetypeWrapper archetype){

        PlayerInventory playerInv = player.getInventory();
        List<Object> defaultItems = archetype.getDefaultItems();

        List<ItemStack> items = new ArrayList<>();
        for(Object obj : defaultItems){
            if(obj instanceof Material){
                Material mat = (Material) obj;
                ItemStack item = new ItemStack(mat);
                if(mat == Material.ARROW){
                    item.setAmount(128);
                }
                items.add(item);
            }else if(obj instanceof String){
                String customItemKey = (String) obj;
                if(customItemManager.hasExistingKey(customItemKey)){
                    CustomItem ci = customItemManager.getByKey(customItemKey);
                    ItemStack item = ci.toItemStack();
                    items.add(item);
                }
            }
        }

        for(ItemStack stack : items){
            playerInv.addItem(stack);
        }

    }

}
