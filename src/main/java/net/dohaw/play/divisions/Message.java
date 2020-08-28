package net.dohaw.play.divisions;

public enum Message {

    /*
        General stuff
     */
    TARGET_PLAYER_HIGHER_RANK("General.Target Player Higher Rank"),
    TARGET_PLAYER_SAME_RANK("General.Target Player Same Rank"),
    ACTION_YOURSELF_DENY("General.Action Yourself Deny"),
    ACTION_LEADER_DENY("General.Action Leader Deny"),
    TARGET_PLAYER_NOT_IN_YOUR_DIVISION("General.Target Player Not In Your Division"),
    TARGET_PLAYER_ALREADY_IN_DIVISION("General.Target Player Already In Division"),
    TARGET_PLAYER_NO_DIVISION("General.Target Player No Division"),
    INVALID_PLAYER("General.Invalid Player"),
    INVITER("General.Inviter"),
    NOT_IN_DIVISION("General.Not In Division"),
    ALREADY_IN_DIVISION("General.Already In Division"),
    ALTER_STATUS("General.Alter Status"),
    PLAYER_AFFECTED_PROMOTION("General.Player Affected Promotion"),
    PLAYER_AFFECTED_DEMOTION("General.Player Affected Demotion"),
    PROMOTER("General.Promoter"),
    DEMOTER("General.Demoter"),
    DIVISION_JOIN("General.Division Join"),
    KICKED_PLAYER_NOTIFIER("General.Kicked Player Notifier"),
    DIVISION_CREATED("General.Division Created"),
    DIVISION_NAME_ABORT("General.Division Name Abort"),
    DIVISION_STATUS("General.Division Status"),
    DIVISION_ALREADY_CREATED("General.Division Already Created"),
    MUST_BE_LEADER("General.Must Be Leader"),
    NOT_A_DIVISION("General.Not A Division"),
    DIVISION_NOT_PUBLIC("General.Division Not Public"),
    ALREADY_ON_CHANNEL("General.Already On Channel"),
    SET_MOTD("General.Set MOTD"),

    /*
        No perm stuff
     */
    NO_PERM_EDIT_PERMISSIONS("No-Perm.Edit Perms"),
    NO_PERM_INVITE("No-Perm.Invite"),
    NO_PERM_PROMOTING("No-Perm.Promoting"),
    NO_PERM_DEMOTING("No-Perm.Demoting"),
    NO_PERM_KICK("No-Perm.Kick"),
    NO_PERM_ANNOUNCE("No-Perm.Announce");

    private String configKey;
    Message(String configKey){
        this.configKey = configKey;
    }

    public String getConfigKey(){
        return configKey;
    }

}
