package net.dohaw.play.divisions.rank;

public enum Rank {

    FRESH_MEAT("FreshMeat"),
    PROVEN_MEMBER("ProvenMember"),
    LOYAL("Loyal"),
    ELDER("Elder"),
    OVERLORD("Overlord");

    public String configValue;
    Rank(String configValue){
        this.configValue = configValue;
    }

}
