package net.dohaw.play.divisions.managers;

import net.dohaw.play.divisions.CustomItem;
import net.dohaw.play.divisions.DivisionsPlugin;

import java.util.ArrayList;
import java.util.List;

public class CustomItemManager implements Manager{

    private List<CustomItem> customItems = new ArrayList<>();

    public CustomItemManager(DivisionsPlugin plugin){

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

    }

    @Override
    public void loadContents() {

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
