package net.dohaw.play.divisions.customitems;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.Material;

public class ItemCreationSession {

    @Getter @Setter private String displayName = "None";
    @Getter @Setter private Material material = Material.APPLE;
    @Getter @Setter private ItemType itemType = ItemType.CONSUMABLE;
    @Getter @Setter private Rarity rarity = Rarity.UNCOMMON;
    @Getter @Setter private String key = "none";

    public ItemCreationSession(){}

    public CustomItem toItem(){
        return new CustomItem(key, itemType, material, displayName);
    }

}
