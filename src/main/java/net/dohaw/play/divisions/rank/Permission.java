package net.dohaw.play.divisions.rank;

public enum Permission {

    CAN_EDIT_PERMS("The ability to edit permissions"),
    CAN_TAKE_FROM_BANK("The ability to take items from the bank"),
    CAN_KICK_PLAYERS("The ability to kick players from a division"),
    CAN_RECRUIT_MEMBERS("The ability to recruit members into a division"),
    CAN_SEND_DIVISION_ANNOUNCEMENTS("The ability to send division announcements to all members"),
    CAN_INVITE_PLAYERS("The ability to invite players to a division"),
    CAN_DECLARE_WAR("The ability to declare war"),
    CAN_STOP_WAR("The ability to stop any wars between your division and another one"),
    CAN_ALLY("The ability to ally a division"),
    CAN_DISCONTINUE_ALLY("The ability to stop being allies with a division"),
    CAN_ALTER_STATUS("The ability to choose whether the division is a public or private division"),
    CAN_PROMOTE_MEMBERS("The ability to promote members (rank)"),
    CAN_DEMOTE_MEMBERS("The ability to demote members (rank)"),
    CAN_UPGRADE_STRUCTURES("The ability to upgrade Garrison structures"),
    CAN_COLLECT_FROM_MINER("The ability to collect resources from the NPC Miner in your Garrison"),
    CAN_COLLECT_FROM_WOODCUTTER("The ability to collect resources from the NPC Woodcutter in your Garrison"),
    CAN_PURCHASE_STRUCTURES("The ability to purchase new structures in your Garrison"),
    CAN_ACCESS_WORTHY_CHAT("The ability to access \"Worthy\" chat in your Division"),
    CAN_ACCEPT_DIVISION_QUESTS("The ability to accept division quests"),
    CAN_ABANDON_DIVISION_QUESTS("The ability to abandon division quests"),
    CAN_SET_DIVISION_MOTD("The ability to set the division MOTD. This message is sent to a player every time they join the server (Message of the day)"),
    CAN_HIRE_WORKERS("The ability to hire NPC workers (Woodcutter, Miner, etc)"),
    ITEM_TAKE_AMOUNT("The amount of items a player can take from the Garrison bank"),
    GOLD_TAKE_AMOUNT("The amount of gold a player can take from the Garrison bank");

    private String description;
    Permission(String description){
        this.description = description;
    }

    public String getDescription(){
        return description;
    }

}
