package net.dohaw.play.divisions.archetypes.specializations;


import net.dohaw.play.divisions.archetypes.ArchetypeKey;
import net.dohaw.play.divisions.archetypes.ArchetypeWrapper;
import net.dohaw.play.divisions.archetypes.WrapperHolder;
import net.dohaw.play.divisions.archetypes.specializations.archer.Cloak;
import net.dohaw.play.divisions.archetypes.specializations.archer.Control;
import net.dohaw.play.divisions.archetypes.specializations.archer.Deception;
import net.dohaw.play.divisions.archetypes.specializations.archer.SoulPiercing;
import net.dohaw.play.divisions.archetypes.specializations.assassin.Proactive;
import net.dohaw.play.divisions.archetypes.specializations.assassin.Shadow;
import net.dohaw.play.divisions.archetypes.specializations.assassin.Venom;
import net.dohaw.play.divisions.archetypes.specializations.cleric.Direct;
import net.dohaw.play.divisions.archetypes.specializations.cleric.Spread;
import net.dohaw.play.divisions.archetypes.specializations.cleric.Vampiric;
import net.dohaw.play.divisions.archetypes.specializations.crusader.Order;
import net.dohaw.play.divisions.archetypes.specializations.crusader.Protection;
import net.dohaw.play.divisions.archetypes.specializations.duelist.Psychotic;
import net.dohaw.play.divisions.archetypes.specializations.duelist.Soul;
import net.dohaw.play.divisions.archetypes.specializations.duelist.Uniform;
import net.dohaw.play.divisions.archetypes.specializations.evoker.Conscious;
import net.dohaw.play.divisions.archetypes.specializations.evoker.Destruction;
import net.dohaw.play.divisions.archetypes.specializations.evoker.Elemental;
import net.dohaw.play.divisions.archetypes.specializations.wizard.Fire;
import net.dohaw.play.divisions.archetypes.specializations.wizard.Ice;
import net.dohaw.play.divisions.archetypes.specializations.wizard.Tempest;

public abstract class Speciality extends WrapperHolder {

    public static final SpecialityWrapper CLOAK = new Cloak(SpecialityKey.CLOAK);

    public static final SpecialityWrapper CONTROL = new Control(SpecialityKey.CONTROL);

    public static final SpecialityWrapper DECEPTION = new Deception(SpecialityKey.DECEPTION);

    public static final SpecialityWrapper SOUL_PIERCING = new SoulPiercing(SpecialityKey.SOUL_PIERCING);

    public static final SpecialityWrapper PROACTIVE = new Proactive(SpecialityKey.PROACTIVE);

    public static final SpecialityWrapper SHADOW = new Shadow(SpecialityKey.SHADOW);

    public static final SpecialityWrapper VENOM = new Venom(SpecialityKey.VENOM);

    public static final SpecialityWrapper DIRECT = new Direct(SpecialityKey.DIRECT);

    public static final SpecialityWrapper SPREAD = new Spread(SpecialityKey.SPREAD);

    public static final SpecialityWrapper VAMPIRIC = new Vampiric(SpecialityKey.VAMPIRIC);

    public static final SpecialityWrapper ORDER = new Order(SpecialityKey.ORDER);

    public static final SpecialityWrapper PROTECTION = new Protection(SpecialityKey.PROTECTION);

    public static final SpecialityWrapper PSYCHOTIC = new Psychotic(SpecialityKey.PSYCHOTIC);

    public static final SpecialityWrapper SOUL = new Soul(SpecialityKey.SOUL);

    public static final SpecialityWrapper UNIFORM = new Uniform(SpecialityKey.UNIFORM);

    public static final SpecialityWrapper CONSCIOUS = new Conscious(SpecialityKey.CONSCIOUS);

    public static final SpecialityWrapper DESTRUCTION = new Destruction(SpecialityKey.DESTRUCTION);

    public static final SpecialityWrapper ELEMENTAL = new Elemental(SpecialityKey.ELEMENTAL);

    public static final SpecialityWrapper FIRE = new Fire(SpecialityKey.FIRE);

    public static final SpecialityWrapper ICE = new Ice(SpecialityKey.ICE);

    public static final SpecialityWrapper TEMPEST = new Tempest(SpecialityKey.TEMPEST);

}
