package net.dohaw.play.divisions.managers;

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

    public boolean hasExistingKey(String key){
        return customItems.containsKey(key);
    }

    public void addCustomItem(CustomItem cItem){
        String key = cItem.getKEY();
        customItems.put(key, cItem);
    }

    public void deleteItem(String key){
        customItems.remove(key);
        customItemsConfig.saveConfig();
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
