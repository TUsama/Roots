package epicsquid.roots.spell;

import epicsquid.roots.Roots;
import epicsquid.roots.entity.spell.EntityFireJet;
import epicsquid.roots.init.ModItems;
import epicsquid.roots.modifiers.*;
import epicsquid.roots.modifiers.instance.staff.StaffModifierInstanceList;
import epicsquid.roots.properties.Property;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.oredict.OreIngredient;

public class SpellWildfire extends SpellBase {
  public static Property.PropertyCooldown PROP_COOLDOWN = new Property.PropertyCooldown(24);
  public static Property.PropertyCastType PROP_CAST_TYPE = new Property.PropertyCastType(EnumCastType.INSTANTANEOUS);
  public static Property.PropertyCost PROP_COST_1 = new Property.PropertyCost(0, new SpellCost("infernal_bulb", 0.125));
  public static Property.PropertyDamage PROP_DAMAGE = new Property.PropertyDamage(4.5f);
  public static Property<Integer> PROP_FIRE_DURATION = new Property<>("fire_duration", 4);

  public static Modifier PURPLE = ModifierRegistry.register(new Modifier(new ResourceLocation(Roots.MODID, "purple_flame"), ModifierCores.PERESKIA, ModifierCost.of(CostType.ADDITIONAL_COST, ModifierCores.PERESKIA, 0.05)));
  public static Modifier PEACEFUL = ModifierRegistry.register(new Modifier(new ResourceLocation(Roots.MODID, "peaceful_flame"), ModifierCores.WILDEWHEET, ModifierCost.of(CostType.ADDITIONAL_COST, ModifierCores.WILDEWHEET, 0.1)));
  public static Modifier PARALYSIS = ModifierRegistry.register(new Modifier(new ResourceLocation(Roots.MODID, "thorns"), ModifierCores.WILDROOT, ModifierCost.of(CostType.ADDITIONAL_COST, ModifierCores.WILDROOT, 0.35)));
  public static Modifier SLOW = ModifierRegistry.register(new Modifier(new ResourceLocation(Roots.MODID, "slow_fire"), ModifierCores.MOONGLOW_LEAF, ModifierCost.of(CostType.ADDITIONAL_COST, ModifierCores.MOONGLOW_LEAF, 0.125)));
  public static Modifier GREEN = ModifierRegistry.register(new Modifier(new ResourceLocation(Roots.MODID, "green_flame"), ModifierCores.SPIRIT_HERB, ModifierCost.of(CostType.ADDITIONAL_COST, ModifierCores.SPIRIT_HERB, 0.05)));
  public static Modifier GROWTH = ModifierRegistry.register(new Modifier(new ResourceLocation(Roots.MODID, "post_mortem_growth"), ModifierCores.TERRA_MOSS, ModifierCost.of(CostType.ADDITIONAL_COST, ModifierCores.TERRA_MOSS, 0.125)));
  public static Modifier POISON = ModifierRegistry.register(new Modifier(new ResourceLocation(Roots.MODID, "poisoned_fire"), ModifierCores.BAFFLE_CAP, ModifierCost.of(CostType.ADDITIONAL_COST, ModifierCores.BAFFLE_CAP, 0.125)));
  // LEVITATE?
  public static Modifier LEVITATE = ModifierRegistry.register(new Modifier(new ResourceLocation(Roots.MODID, "floating_cinders"), ModifierCores.CLOUD_BERRY, ModifierCost.of(CostType.ADDITIONAL_COST, ModifierCores.CLOUD_BERRY, 1)));
  public static Modifier WILDFIRE = ModifierRegistry.register(new Modifier(new ResourceLocation(Roots.MODID, "pyroclastic_cloud"), ModifierCores.STALICRIPE, ModifierCost.of(CostType.ADDITIONAL_COST, ModifierCores.STALICRIPE, 0.45)));
  // TODO:
  public static Modifier ICICLES = ModifierRegistry.register(new Modifier(new ResourceLocation(Roots.MODID, "fire_and_ice"), ModifierCores.DEWGONIA, ModifierCost.of(CostType.ADDITIONAL_COST, ModifierCores.DEWGONIA, 1)));

  public static ResourceLocation spellName = new ResourceLocation(Roots.MODID, "spell_wild_fire");
  public static SpellWildfire instance = new SpellWildfire(spellName);

  public float damage;
  public int fire_duration;

  public SpellWildfire(ResourceLocation name) {
    super(name, TextFormatting.GOLD, 255f / 255f, 128f / 255f, 32f / 255f, 255f / 255f, 64f / 255f, 32f / 255f);
    properties.addProperties(PROP_COOLDOWN, PROP_CAST_TYPE, PROP_COST_1, PROP_DAMAGE, PROP_FIRE_DURATION);
    acceptsModifiers(PURPLE, PEACEFUL, PARALYSIS, SLOW, GREEN, GROWTH, POISON, LEVITATE, WILDFIRE, ICICLES);
  }

  @Override
  public void init() {
    addIngredients(
        new OreIngredient("dyeOrange"),
        new ItemStack(Items.COAL, 1, 1),
        new OreIngredient("gunpowder"),
        new ItemStack(ModItems.infernal_bulb),
        new ItemStack(Item.getItemFromBlock(Blocks.TNT))
    );
  }

  @Override
  public boolean cast(EntityPlayer player, StaffModifierInstanceList modifiers, int ticks) {
    if (!player.world.isRemote) {
      EntityFireJet fireJet = new EntityFireJet(player.world);
      fireJet.setPlayer(player.getUniqueID());
      fireJet.setPosition(player.posX, player.posY, player.posZ);
      fireJet.setAmplifier(getAmplifyValue());
      fireJet.setSpeedy(getSpeedValue());
      fireJet.setModifiers(modifiers);
      player.world.spawnEntity(fireJet);
    }
    return true;
  }

  @Override
  public void doFinalise() {
    this.castType = properties.get(PROP_CAST_TYPE);
    this.cooldown = properties.get(PROP_COOLDOWN);
    this.damage = properties.get(PROP_DAMAGE);
    this.fire_duration = properties.get(PROP_FIRE_DURATION);
  }
}
