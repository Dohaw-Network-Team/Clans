package net.dohaw.play.divisions.customitems;

import lombok.Getter;
import lombok.Setter;
import net.dohaw.play.divisions.Stat;
import net.dohaw.play.divisions.archetypes.spells.SpellWrapper;
import net.dohaw.play.divisions.utils.Calculator;
import net.minecraft.server.v1_16_R2.NBTTagCompound;
import net.minecraft.server.v1_16_R2.NBTTagList;
import net.minecraft.server.v1_16_R2.NBTTagString;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_16_R2.inventory.CraftItemStack;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;
import org.bukkit.inventory.meta.ItemMeta;

import javax.annotation.Nullable;
import java.util.*;

public class CustomItem {

    @Getter private final String KEY;
    @Getter @Setter private String displayName;
    @Getter @Setter private List<String> lore = new ArrayList<>();
    @Getter @Setter private Material material;
    @Getter @Setter private boolean isSpellItem;
    @Getter @Setter private ItemType itemType;
    @Getter @Setter private Rarity rarity;
    @Getter @Setter private Map<Enchantment, Integer> enchants = new HashMap<>();
    @Getter @Setter private Map<Stat, Double> addedStats = new HashMap<>();

    @Getter @Setter private SpellWrapper linkedSpell;

    public CustomItem(final String KEY, ItemType itemType, Material material, String displayName){
        this.material = material;
        this.displayName = displayName;
        this.itemType = itemType;
        this.KEY = KEY;
    }

    public ItemStack toItemStack(){

        ItemStack stack = new ItemStack(material);
        ItemMeta meta = stack.getItemMeta();

        if(!displayName.equalsIgnoreCase("None")){
            meta.setDisplayName(displayName);
        }

        if(!lore.isEmpty()){
            meta.setLore(lore);
        }

        if(!enchants.isEmpty()){
            for(Map.Entry<Enchantment, Integer> entry : enchants.entrySet()){
                Enchantment ench = entry.getKey();
                int level = entry.getValue();
                if(meta instanceof EnchantmentStorageMeta){
                    EnchantmentStorageMeta esm = (EnchantmentStorageMeta) meta;
                    esm.addStoredEnchant(ench, level, true);
                }else{
                    meta.addEnchant(ench, level, true);
                }
            }
        }

        stack.setItemMeta(meta);
        stack = addNBTData(stack);

        return stack;
    }

    private ItemStack addNBTData(ItemStack stack){

        net.minecraft.server.v1_16_R2.ItemStack nmsStack = CraftItemStack.asNMSCopy(stack);
        NBTTagCompound nmsComp = nmsStack.getOrCreateTag();
        nmsComp.setString("Key", KEY);

        for(Map.Entry<Stat, Double> entry : addedStats.entrySet()){
            Stat stat = entry.getKey();
            double value = entry.getValue();
            nmsComp.setDouble(stat.name(), value);
        }

        nmsComp.setString("Rarity", rarity.name());
        nmsComp.setBoolean("Is Spell Item", isSpellItem);

        double speedStatValue = addedStats.get(Stat.QUICKNESS);
        NBTTagList modifiers = new NBTTagList();

        NBTTagCompound quickness = new NBTTagCompound();
        quickness.setString("AttributeName", "generic.movementSpeed");
        quickness.setString("Name", "generic.movementSpeed");
        quickness.setDouble("Amount", Calculator.getStatFromItem(speedStatValue));

        UUID randQuicknessUUID = UUID.randomUUID();
        quickness.setLong("UUIDLeast", randQuicknessUUID.getLeastSignificantBits());
        quickness.setLong("UUIDMost", randQuicknessUUID.getMostSignificantBits());
        modifiers.add(quickness);

        NBTTagCompound maxHealth = new NBTTagCompound();

        double maxHealthStatValue = addedStats.get(Stat.MAX_HEALTH);
        maxHealth.setString("AttributeName", "generic.maxHealth");
        maxHealth.setString("Name", "generic.maxHealth");
        maxHealth.setDouble("Amount", Calculator.getStatFromItem(maxHealthStatValue));

        UUID randMaxHealthUUID = UUID.randomUUID();
        maxHealth.setLong("UUIDLeast", randMaxHealthUUID.getLeastSignificantBits());
        maxHealth.setLong("UUIDMost", randMaxHealthUUID.getMostSignificantBits());
        modifiers.add(maxHealth);

        nmsStack.setTag(nmsComp);
        return CraftItemStack.asBukkitCopy(nmsStack);

    }

    public static String getCustomItemKey(ItemStack stack){
        net.minecraft.server.v1_16_R2.ItemStack nmsStack = CraftItemStack.asNMSCopy(stack);
        NBTTagCompound nmsComp = nmsStack.getTag();
        if(nmsComp != null){
            return nmsComp.getString("Key");
        }
        return null;
    }

    public static double getCustomItemStat(ItemStack stack, Stat stat){
        net.minecraft.server.v1_16_R2.ItemStack nmsStack = CraftItemStack.asNMSCopy(stack);
        NBTTagCompound nmsComp = nmsStack.getTag();
        if(nmsComp != null){
            return nmsComp.getDouble(stat.name());
        }
        return 0;
    }

    public static TreeMap<Integer, ItemStack> getPlayerItemWithKey(Player player, String customItemKey){
        PlayerInventory inv = player.getInventory();
        for(int x = 0; x < inv.getContents().length; x++){
            ItemStack is = inv.getContents()[x];
            if(getCustomItemKey(is).equalsIgnoreCase(customItemKey)){
                int finalX = x;
                return new TreeMap<Integer, ItemStack>(){{
                    put(finalX, is);
                }};
            }
        }
        return null;
    }

    @Override
    public String toString(){
        return "[key: " + KEY + ", name: " + displayName + ", type: " + itemType + ", material: " + material;
    }

}
