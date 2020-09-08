package net.dohaw.play.divisions.managers;

import net.dohaw.play.divisions.customitems.CustomItem;
import net.dohaw.play.divisions.DivisionsPlugin;
import net.dohaw.play.divisions.files.CustomItemsConfig;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CustomItemManager implements Manager{

    private Map<String, CustomItem> customItems = new HashMap<>();
    private CustomItemsConfig customItemsConfig;

    public CustomItemManager(DivisionsPlugin plugin){
        this.customItemsConfig = new CustomItemsConfig(plugin);
    }

    @Override
    public Object getContents() {
        return null;
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
