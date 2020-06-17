package net.dohaw.play.divisions.managers;

import net.dohaw.play.divisions.Division;

import java.util.List;

public class DivisionsManager implements Manager {

    private List<Division> divisions;

    @Override
    public Object getContents() {
        return divisions;
    }

    @Override
    public boolean hasContent(Object obj) {
        return divisions.contains(obj);
    }

    @Override
    public void saveContents() {

    }

    @Override
    public void loadContents() {

    }

    @Override
    public void addContent() {

    }

    @Override
    public void removeContent() {

    }

}
