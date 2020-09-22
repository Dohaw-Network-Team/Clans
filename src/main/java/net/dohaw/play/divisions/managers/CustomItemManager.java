package net.dohaw.play.divisions.managers;

import net.dohaw.play.divisions.PlayerData;
import net.dohaw.play.divisions.archetypes.ArchetypeWrapper;
import net.dohaw.play.divisions.archetypes.Wrapper;
import net.dohaw.play.divisions.archetypes.spells.Cooldownable;
import net.dohaw.play.divisions.archetypes.spells.Spell;
import net.dohaw.play.divisions.archetypes.spells.SpellWrapper;
import net.dohaw.play.divisions.archetypes.spells.active.ActiveSpell;
import net.dohaw.play.divisions.customitems.CustomItem;
import net.dohaw.play.divisions.DivisionsPlugin;
import net.dohaw.play.divisions.customitems.ItemType;
import net.dohaw.play.divisions.customitems.Rarity;
import net.dohaw.play.divisions.customitems.types.Armor;
import net.dohaw.play.divisions.files.CustomItemsConfig;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
                String customItemBindedTo = spell.getCustomItemBindedToKey();
                CustomItem customItem = getByKey(customItemBindedTo);

                if(customItem != null){

                    String spellDesc = aSpell.getDescription();
                    List<String> spellLore = new ArrayList<>();
                    spellLore.add(spellDesc);

                    spellLore.add(" ");
                    List<String> cooldownPart = aSpell.getCooldownLorePart();
                    spellLore.addAll(cooldownPart);

                    customItem.setLore(spellLore);
                }
            }

        }
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
