package net.dohaw.play.divisions.utils;

import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class AttributeModifierHelper {

    public static List<AttributeModifier> getAttributeModifiersByName(Player player, Attribute attr, String attributeModifierName){

        AttributeInstance attrInstance = player.getAttribute(attr);
        List<AttributeModifier> attrModifiers = new ArrayList<>(attrInstance.getModifiers());

        for(AttributeModifier modifier : attrModifiers){
            if(modifier.getName() == ){

            }
        }


    }

}
