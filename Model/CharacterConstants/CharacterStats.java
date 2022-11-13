package Model.CharacterConstants;

public class CharacterStats {
    /**
     * These are the characters that the player will only encounter at a Tower of OO.
     * They all possess special skills and will serve as a worthy challenge in battle.
     * One guardian will be assigned to each Tower.
     */

    //Hydra Stats
    public static final int HYDRA_HEALTH_POINTS = 150;
    public static final int HYDRA_ATTACK_SPEED = 3;
    public static final double HYDRA_CHANCE_TO_HIT = 0.8;
    //public static DamageRange HYDRA_DAMAGE_RANGE = new DamageRange(30,50);
    public static final double HYDRA_CHANCE_FOR_SKILL = 0.4;
    //public static SpecialSkillRange HYDRA_SKILL_RANGE = new SpecialSkillRange(80,100); //Skill = Regeneration

    //Tom Stats
    public static final int TOM_HEALTH_POINTS = 125;
    public static final int TOM_ATTACK_SPEED = 4;
    public static final double TOM_CHANCE_TO_HIT = 0.8;
    //public static DamageRange TOM_DAMAGE_RANGE = new DamageRange(40,60);
    public static final double TOM_CHANCE_FOR_SKILL = 0.3;
    //public static SpecialSkillRange TOM_SKILL_RANGE = new SpecialSkillRange(30,50); //Skill = mOOn Lecture
    /**
     * How to implement Tom's skill that gives him health but also deals damage
     */

    //Red Dragon Stats
    public static final int RED_DRAGON_HEALTH_POINTS = 225;
    public static final int RED_DRAGON_ATTACK_SPEED = 2;
    public static final double RED_DRAGON_CHANCE_TO_HIT = 0.4;
    //public static DamageRange RED_DRAGON_DAMAGE_RANGE = new DamageRange(60,70);
    public static final double RED_DRAGON_CHANCE_FOR_SKILL = 0.1;
    //public static SpecialSkillRange RED_DRAGON_SKILL_RANGE = new SpecialSkillRange(100,100); //Skill = Incinerate

    //Cerberus Stats
    public static final int CERBERUS_HEALTH_POINTS = 150;
    public static final int CERBERUS_ATTACK_SPEED = 5;
    public static final double CERBERUS_CHANCE_TO_HIT = 0.8;
    //public static DamageRange CERBERUS_DAMAGE_RANGE = new DamageRange(30,50);
    public static final double CERBERUS_CHANCE_FOR_SKILL = 0.5;
    /**
     * Skill = Block attack
     * How to implement blocking an attack.
     */

    /**
     * These are the most common characters that the player will encounter as they play through the game.
     * They all possess the ability to regenerate (at varying degrees).
     */


    //Ogre stats
    public static final int OGRE_HEALTH_POINTS = 200;
    public static final int OGRE_ATTACK_SPEED = 2;
    public static final double OGRE_CHANCE_TO_HIT = 0.6;
    public static final double OGRE_CHANCE_TO_HEAL = 0.1;
    //public static DamageRange OGRE_DAMAGE_RANGE = new DamageRange(30,60);
    //public static HealRange OGRE_HEAL_RANGE = new HealRange(30,60);

    //Goblin stats
    public static final int GOBLIN_HEALTH_POINTS = 70;
    public static final int GOBLIN_ATTACK_SPEED = 5;
    public static final double GOBLIN_CHANCE_TO_HIT = 0.8;
    public static final double GOBLIN_CHANCE_TO_HEAL = 0.4;
    //public static DamageRange GOBLIN_DAMAGE_RANGE = new DamageRange(15,30);
    //public static HealRange GOBLIN_HEAL_RANGE = new HealRange(20,40);

    //Dire Wolf stats
    public static final int DIREWOLF_HEALTH_POINTS = 100;
    public static final int DIREWOLF_ATTACK_SPEED = 3;
    public static final double DIREWOLF_CHANCE_TO_HIT = 0.8;
    public static final double DIREWOLF_CHANCE_TO_HEAL = 0.3;
    //public static DamageRange DIREWOLF_DAMAGE_RANGE = new DamageRange(30,50);
    //public static HealRange DIREWOLF_HEAL_RANGE = new HealRange(30,50);

    /**
     *A player chooses a hero to play as.
     * These characters each have their own unique special skill
     * and a chance to block an attack.
     */

    //Knight Stats
    private static final int KNIGHT_HEALTH_POINTS = 125;
    private static final int KNIGHT_ATTACK_SPEED = 4;
    private static final double KNIGHT_CHANCE_TO_HIT = 0.8;
    private static final double KNIGHT_CHANCE_TO_BLOCK = 0.2;
    //private static DamageRange KNIGHT_DAMAGE_RANGE = new DamageRange (35,60);
    //private static SpecialSkillRange KNIGHT_SS_RANGE = new SpecialSkillRange(75, 175); //Special skill = Fatal slash
    private static final int KNIGHT_SPECIAL_CHANCE = 40;

    //Mender Stats
    private static final int MENDER_HEALTH_POINTS = 75;
    private static final int MENDER_ATTACK_SPEED = 5;
    private static final double MENDER_CHANCE_TO_HIT = 0.7;
    private static final double MENDER_CHANCE_TO_BLOCK = 0.3;
    //private static DamageRange MENDER_DAMAGE_RANGE = new DamageRange(25,45);
    //private static SpecialSkillRange MENDER_SS_RANGE = new SpecialSkillRange(15,75); //Special skill = Heal
    /**
     * Does the mender have a chance percentage to heal?
     */

    //Assassin Stats
    private static final int ASSASSIN_HEALTH_POINTS = 75;
    private static final int ASSASSIN_ATTACK_SPEED = 60;
    private static final double ASSASSIN_CHANCE_TO_HIT = 0.8;
    private static final double ASSASSIN_CHANCE_TO_BLOCK = 0.4;
    //private static DamageRange ASSASSIN_DAMAGE_RANGE = new DamageRange(20,40);
    private static double SKILL_ADDITIONAL_ATTACK_CHANCE = 0.4;
    private static double SKILL_REGULAR_ATTACK_CHANCE = 0.4;
    private static double SKILL_MISS_ATTACK_CHANCE = 0.2;
}
