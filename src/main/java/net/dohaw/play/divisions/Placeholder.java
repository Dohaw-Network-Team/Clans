package net.dohaw.play.divisions;

public enum Placeholder {

    PLAYER_NAME("%playerName%"),
    STATUS("%status%"),
    RANK("%rank%"),
    DIVISION_CHANNEL("%divisionChannel%"),
    DIVISION_NAME("%divisionName%");

    private String pHolder;
    Placeholder(String pHolder){
        this.pHolder = pHolder;
    }

    public String getPlaceholder(){
        return pHolder;
    }

}
