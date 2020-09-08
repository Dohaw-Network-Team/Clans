package net.dohaw.play.divisions.archetypes;

import lombok.Getter;

import java.util.List;

public abstract class Wrapper {

    @Getter
    protected final Enum KEY;

    public Wrapper(final Enum KEY){
        this.KEY = KEY;
    }

    public abstract List<String> getAliases();

    public String getName(){
        String archetypeStr = KEY.name();
        return archetypeStr.substring(0, 1).toUpperCase() + archetypeStr.substring(1).toLowerCase();
    }

}
