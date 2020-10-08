package net.dohaw.play.divisions.archetypes.spells.passive.tree;

import net.dohaw.corelib.StringUtils;
import net.dohaw.play.divisions.DivisionsPlugin;
import net.dohaw.play.divisions.PlayerData;
import net.dohaw.play.divisions.archetypes.ArchetypeKey;
import net.dohaw.play.divisions.archetypes.spells.Spell;
import net.dohaw.play.divisions.managers.PlayerDataManager;
import net.minecraft.server.v1_16_R2.NBTTagCompound;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_16_R2.inventory.CraftItemStack;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;

public class PhotosynthesisRunner extends BukkitRunnable {

    private PlayerDataManager playerDataManager;
    private Map<Material, Integer> foodLevels = new HashMap<>();
    final private List<Material> EXLUSIONS = Arrays.asList(new Material[]{Material.CHORUS_FRUIT, Material.ENCHANTED_GOLDEN_APPLE, Material.GOLDEN_APPLE, Material.ROTTEN_FLESH});

    public PhotosynthesisRunner(DivisionsPlugin plugin){
        this.playerDataManager = plugin.getPlayerDataManager();
        compileFoodLevels();
    }

    /*
        First is light level
        Second is food level
     */
    final private Map<Integer, Integer> FOOD_LEVEL_PER_LIGHT_LEVEL = new HashMap<Integer, Integer>(){{
        put(10, 3);
        put(11, 4);
        put(12, 5);
        put(13, 6);
        put(14, 7);
        put(15, 8);
    }};

    @Override
    public void run() {

        for(Player player : Bukkit.getOnlinePlayers()){

            PlayerData pd = playerDataManager.getPlayerByUUID(player.getUniqueId());
            if(pd.getArchetype() != null){

                if(pd.getArchetype().getKEY() == ArchetypeKey.TREE){

                    if(pd.getLevel() == Spell.PHOTOSYNTHESIS_P1.getLevelUnlocked()) {

                        Location playerLoc = player.getLocation();
                        int lightLevel = playerLoc.getBlock().getLightLevel();

                        if (lightLevel >= 10) {

                            int foodLevelDesired = FOOD_LEVEL_PER_LIGHT_LEVEL.get(lightLevel);
                            List<Material> foodLevelMaterials = getFoodLevelMaterials(foodLevelDesired);

                            Material randomFood = foodLevelMaterials.get(new Random().nextInt(foodLevelMaterials.size()));
                            ItemStack food = new ItemStack(randomFood);

                            ItemMeta foodMeta = food.getItemMeta();
                            List<String> lore = Arrays.asList(StringUtils.colorString("&9Photosynthesis"));
                            foodMeta.setLore(lore);
                            food.setItemMeta(foodMeta);

                            net.minecraft.server.v1_16_R2.ItemStack nmsStack = CraftItemStack.asNMSCopy(food);
                            NBTTagCompound nmsComp = nmsStack.getOrCreateTag();
                            nmsComp.setBoolean("Is Photo Food", true);
                            nmsComp.setString("Owner", player.getName());
                            nmsStack.setTag(nmsComp);

                            food = CraftItemStack.asBukkitCopy(nmsStack);

                            player.getInventory().addItem(food);
                            player.getWorld().spawnParticle(Spell.PHOTOSYNTHESIS_P1.getSpellOwnerParticle(), player.getLocation(), 40, 1, 1, 1);

                        }
                    }
                }
            }

        }

    }

    private List<Material> getFoodItems(){
        List<Material> food = new ArrayList<>();
        for(Material mat : Material.values()){
            if(mat.isEdible() && !EXLUSIONS.contains(mat)){
                food.add(mat);
            }
        }
        return food;
    }

    private void compileFoodLevels(){
        for(Material mat : getFoodItems()){
            int nutrition = CraftItemStack.asNMSCopy(new ItemStack(mat)).getItem().getFoodInfo().getNutrition();
            foodLevels.put(mat, nutrition);
        }
    }

    private List<Material> getFoodLevelMaterials(int foodLevel){
        List<Material> foodMaterials = new ArrayList<>();
        for(Map.Entry<Material, Integer> entry : foodLevels.entrySet()){
            int matFoodLevel = entry.getValue();
            if(matFoodLevel == foodLevel){
                foodMaterials.add(entry.getKey());
            }
        }
        return foodMaterials;
    }

}
