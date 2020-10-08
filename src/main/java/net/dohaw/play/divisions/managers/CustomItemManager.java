package net.dohaw.play.divisions.managers;

import net.dohaw.corelib.StringUtils;
import net.dohaw.play.divisions.PlayerData;
import net.dohaw.play.divisions.archetypes.ArchetypeWrapper;
import net.dohaw.play.divisions.archetypes.spells.*;
import net.dohaw.play.divisions.archetypes.spells.active.ActiveSpell;
import net.dohaw.play.divisions.customitems.CustomItem;
import net.dohaw.play.divisions.DivisionsPlugin;
import net.dohaw.play.divisions.customitems.ItemType;
import net.dohaw.play.divisions.customitems.Rarity;
import net.dohaw.play.divisions.files.CustomItemsConfig;
import net.dohaw.play.divisions.utils.Calculator;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.*;

public class CustomItemManager implements Manager{

    private DivisionsPlugin plugin;
    private Map<String, CustomItem> customItems = new HashMap<>();
    private CustomItemsConfig customItemsConfig;

    public CustomItemManager(DivisionsPlugin plugin){
        this.customItemsConfig = new CustomItemsConfig(plugin);
        this.plugin = plugin;
    }

    @Override
    public Object getContents() {
        return new ArrayList<>(customItems.values());
    }

    public List<CustomItem> getByRarity(Rarity rarity){
        List<CustomItem> cItems = new ArrayList<>();
        for(Map.Entry<String, CustomItem> entry : customItems.entrySet()){
            CustomItem item = entry.getValue();
            if(item.getRarity() == rarity){
                cItems.add(item);
            }
        }
        return cItems;
    }

    public List<CustomItem> getSpellItems(){
        List<CustomItem> cItems = new ArrayList<>();
        for(Map.Entry<String, CustomItem> entry : customItems.entrySet()){
            CustomItem item = entry.getValue();
            if(item.isSpellItem()){
                cItems.add(item);
            }
        }
        return cItems;
    }

    public List<CustomItem> getByItemType(ItemType type){
        List<CustomItem> cItems = new ArrayList<>();
        for(Map.Entry<String, CustomItem> entry : customItems.entrySet()){
            CustomItem item = entry.getValue();
            if(item.getItemType() == type){
                cItems.add(item);
            }
        }
        return cItems;
    }

    public CustomItem getByKey(String key){
        return customItems.get(key);
    }

    public boolean hasExistingKey(String key){
        return customItems.containsKey(key);
    }

    public void addCustomItem(CustomItem cItem){
        String key = cItem.getKEY();
        customItems.put(key, cItem);
    }

    public void deleteItem(String key){
        ItemType itemType = getByKey(key).getItemType();
        customItems.remove(key);
        customItemsConfig.deleteItem(key, itemType);
    }

    public void setSpellItemLores(PlayerData pd){

        ArchetypeWrapper archetype = pd.getArchetype();
        int playerLevel = pd.getLevel();

        List<SpellWrapper> spells = Spell.getArchetypeSpellsUnlocked(archetype, playerLevel);
        for(SpellWrapper spell : spells){

            if(spell instanceof ActiveSpell){

                ActiveSpell aSpell = (ActiveSpell) spell;
                String customItemBindedToKey = spell.getCustomItemBindedToKey();

                Player player = pd.getPlayer().getPlayer();
                TreeMap<Integer, ItemStack> bindedItemMap = CustomItem.getPlayerItemWithKey(player, customItemBindedToKey);
                if(bindedItemMap != null){
                    ItemStack bindedItem = alterMeta(pd, aSpell, bindedItemMap.firstEntry().getValue());
                    int slot = bindedItemMap.firstKey();
                    player.getInventory().setItem(slot, bindedItem);
                }

            }

        }
    }

    public ItemStack alterMeta(PlayerData pd, ActiveSpell aSpell, ItemStack bindedItem){

        if(bindedItem != null) {

            ItemMeta meta = bindedItem.getItemMeta();

            ArchetypeWrapper archetype = aSpell.getArchetype();
            ChatColor archColor = archetype.getColor();
            String displayName = archColor + aSpell.getName();
            meta.setDisplayName(displayName);

            final String LORE_HEADER_COLOR = "&8&l&n";
            final String LORE_COLOR = aSpell.getLORE_COLOR();

            List<String> spellLore = new ArrayList<>();
            spellLore = combineLore(LORE_COLOR, spellLore, aSpell.getDescription());

            double regenPercentageAffected = Calculator.getSpellPercentageRegenCost(pd, aSpell.getPercentageRegenAffected()) * 100;
            spellLore.add(LORE_COLOR + "Uses &c" + regenPercentageAffected + "%" + LORE_COLOR + " regen");

            spellLore.add(" ");
            spellLore.add(LORE_HEADER_COLOR + "COOLDOWN");
            spellLore = combineLore(LORE_COLOR, spellLore, aSpell.getCooldownLorePart(pd));

            if (aSpell instanceof Damageable) {
                Damageable damageable = (Damageable) aSpell;
                spellLore.add(" ");
                spellLore.add(LORE_HEADER_COLOR + "DAMAGE");
                spellLore = combineLore(LORE_COLOR, spellLore, damageable.getDamageLorePart());
            }

            if (aSpell instanceof Rangeable) {
                Rangeable rangeable = (Rangeable) aSpell;
                spellLore.add(" ");
                spellLore.add(LORE_HEADER_COLOR + "RANGE");
                spellLore = combineLore(LORE_COLOR, spellLore, rangeable.getRangeLorePart());
            }

            if(aSpell instanceof Affectable){
                Affectable affectable = (Affectable) aSpell;
                spellLore.add(" ");
                spellLore.add(LORE_HEADER_COLOR + "DURATION");
                spellLore = combineLore(LORE_COLOR, spellLore, affectable.getDurationLorePart());
            }

            if(aSpell instanceof Healable){
                Healable healable = (Healable) aSpell;
                spellLore.add(" ");
                spellLore.add(LORE_HEADER_COLOR + "HEALING");
                spellLore = combineLore(LORE_COLOR, spellLore, healable.getHealingLorePart());
            }

            spellLore = StringUtils.colorLore(spellLore);
            meta.setLore(spellLore);

            bindedItem.setItemMeta(meta);
        }
        return bindedItem;
    }

    private List<String> combineLore(final String LORE_COLOR, List<String> spellLore, List<String> propertyLore){
        for(String s : propertyLore){
            spellLore.add(LORE_COLOR + s);
        }
        return spellLore;
    }

    @Override
    public boolean hasContent(Object obj) {
        return false;
    }

    @Override
    public void saveContents() {
        customItemsConfig.saveCustomItems(customItems);
    }

    @Override
    public void loadContents() {
        this.customItems = customItemsConfig.loadCustomItems();
        plugin.getLogger().info("Loaded " + customItems.size() + " custom item(s) into memory");
    }

    @Override
    public boolean addContent(Object content) {
        return false;
    }

    @Override
    public boolean removeContent(Object content) {
        return false;
    }
}
