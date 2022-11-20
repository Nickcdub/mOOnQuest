package Model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;

import static Model.CharacterConstants.MonsterType.*;

import static org.junit.jupiter.api.Assertions.*;

public class CharacterTest {

    /*
    *
    MONSTER TESTS
    *
     */
    //MONSTER TESTS WILL BE TESTING THE SAME METHODS FOR DIFFERENT INPUT ENUMS
    //THIS PROVIDES REDUNDANCY IN CASE THERE IS A PROBLEM WITH ONE OF THESE METHODS, THERE ARE MORE ANGLES TO GATHER INFO
    @Test
    @DisplayName("Ogre Test")
    public void ogre() throws SQLException {
        Monster ogre = new Monster(OGRE);

        ogre.damage(15);
        assertEquals(185, ogre.getHealth(), "DAMAGE TEST: monster should take 15 damage but took: " + (ogre.getMaxHealth() - ogre.getHealth()) + " damage.");

        //heal is a chance based method, it may not work everytime, but we need it to work just once.
        //This will check whether a monster will heal past their health cap
        while (ogre.getHealth() == 185) ogre.heal();
        assertEquals(ogre.getHealth(), ogre.getMaxHealth(), "HEAL TEST: Monster should not heal beyond max health, current health: " + ogre.getHealth() + "/" + ogre.getMaxHealth());

        //Attack is also a chance based method, however we need it to work this time.
        //This is testing Ogre's damage output
        Monster defender = new Monster(OGRE);
        while (defender.getHealth() == defender.getMaxHealth()) ogre.attack(defender);
        assertTrue(defender.getHealth() >= 140 && defender.getHealth() <= 170, "ATTACK TEST: Expected health between 140 and 170, actual:" + defender.getHealth());


    }

    @Test
    @DisplayName("Direwolf Test")
    public void direwolf() throws SQLException {
        Monster direwolf = new Monster(DIREWOLF);

        direwolf.damage(15);
        assertEquals(85, direwolf.getHealth(), "DAMAGE TEST: monster should take 15 damage but took: " + (direwolf.getMaxHealth() - direwolf.getHealth()) + " damage.");

        while (direwolf.getHealth() == 85) direwolf.heal();
        assertEquals(direwolf.getHealth(), direwolf.getMaxHealth(), "HEAL TEST: Monster should not heal beyond max health, current health: " + direwolf.getHealth() + "/" + direwolf.getMaxHealth());

        Monster defender = new Monster(DIREWOLF);
        while (defender.getHealth() == defender.getMaxHealth()) direwolf.attack(defender);
        assertTrue(defender.getHealth() >= 50 && defender.getHealth() <= 70, "ATTACK TEST: Expected health between 140 and 170, actual:" + defender.getHealth());

    }

    @Test
    @DisplayName("Goblin Test")
    public void goblin() throws SQLException {
        Monster goblin = new Monster(GOBLIN);

        goblin.damage(15);
        assertEquals(55, goblin.getHealth(), "DAMAGE TEST: monster should take 15 damage but took: " + (goblin.getMaxHealth() - goblin.getHealth()) + " damage.");

        while (goblin.getHealth() == 55) goblin.heal();
        assertEquals(goblin.getHealth(), goblin.getMaxHealth(), "HEAL TEST: Monster should not heal beyond max health, current health: " + goblin.getHealth() + "/" + goblin.getMaxHealth());

        Monster defender = new Monster(GOBLIN);
        while (defender.getHealth() == defender.getMaxHealth()) goblin.attack(defender);
        assertTrue(defender.getHealth() >= 40 && defender.getHealth() <= 55, "ATTACK TEST: Expected health between 140 and 170, actual:" + defender.getHealth());

    }

    /*
   *
   HERO TESTS
   *
    */
    //Knight tests
    @Test
    @DisplayName("Knight Tests")
    public void knight() throws SQLException {
        Knight knight = new Knight();
        Monster enemy = new Monster(OGRE);

        //Use While loops for actions that have a chance of missing

        //Test attack
        while (enemy.getHealth() == enemy.getMaxHealth()) knight.attack(enemy);//0.8 chance of landing, 35 - 60 dmg
        assertTrue(enemy.getHealth() <= 165 && enemy.getHealth() >= 140, "ATTACK TEST: Enemy health should be between 165 and 140, enemy health:" + enemy.getHealth());

        //Test Ultimate
        enemy = new Monster(OGRE);
        while (enemy.getHealth() == enemy.getMaxHealth()) knight.ultimate(enemy);//0.4 chance of landing, 175 - 75 dmg
        assertTrue(enemy.getHealth() <= 125 && enemy.getHealth() >= 25, "ULTIMATE TEST: Enemy health should be between 125 and 25, enemy health:" + enemy.getHealth());

        //Test damage
        while (knight.getHealth() == knight.getMaxHealth())
            enemy.attack(knight);//0.6 chance of landing,0.2 block chance, 60 - 30 dmg
        assertTrue(knight.getHealth() <= 95 && knight.getHealth() >= 65, "DAMAGE TEST: Knight health should be between 95 and 65, enemy health: " + knight.getHealth());

        //Test Heal
        knight = new Knight();
        while (knight.getHealth() == knight.getMaxHealth()) knight.damage(50);//Health should now be 75
        knight.heal(35, 25);
        assertTrue(knight.getHealth() <= 110 && knight.getHealth() >= 100, "HEAL TEST 1: Knight health should be between 100 and 110, knight health: " + knight.getHealth());
        knight.heal(300, 200);
        assertEquals(knight.getHealth(), knight.getMaxHealth(), "HEAL TEST 2: Knight should not heal beyond max health, current health: " + knight.getHealth() + "/" + knight.getMaxHealth());

        //Test block, will loop forever if a block does not occur
        knight.damage(10);
        while (knight.getHealth() != knight.getMaxHealth()) {
            knight.heal(20, 10);
            knight.damage(10);
        }
    }

    @Test
    @DisplayName("Assassin Tests")
    public void assassin() throws SQLException {
        Assassin assassin = new Assassin();
        Monster enemy = new Monster(OGRE);

        //Use While loops for actions that have a chance of missing
        //Test attack
        while (enemy.getHealth() == enemy.getMaxHealth()) assassin.attack(enemy);// 20 - 40 dmg
        assertTrue(enemy.getHealth() <= 180 && enemy.getHealth() >= 160, "ATTACK TEST: Enemy health should be between 180 and 160, enemy health:" + enemy.getHealth());

        //Test Ultimate
        boolean miss, single, duo;
        miss = duo = single = false;
        while (!miss || !single || !duo) {//Keep doing ultimates until each of the three outcomes occurs: misses, single hit, double hit.
            assassin.ultimate(enemy);
            if (!miss && enemy.getHealth() == enemy.getMaxHealth()) miss = true;
            else if (!single && enemy.getHealth() >= 160 && enemy.getHealth() < 200) single = true;
            else if (!duo && enemy.getHealth() < 160) duo = true;
            enemy = new Monster(OGRE);
        }

        assertTrue(miss && single && duo, "The related while loop above should be infinite if ultimate does not work correctly.");

        //Test damage
        while (assassin.getHealth() == assassin.getMaxHealth())
            enemy.attack(assassin);//0.6 chance of landing,0.2 block chance, 60 - 30 dmg
        assertTrue(assassin.getHealth() <= 45 && assassin.getHealth() >= 15, "DAMAGE TEST: Assassin health should be between 45 and 15, enemy health: " + assassin.getHealth());

        //Test Heal
        assassin = new Assassin();
        while (assassin.getHealth() == assassin.getMaxHealth()) assassin.damage(50);//Health should now be 25
        assassin.heal(35, 25);
        assertTrue(assassin.getHealth() <= 60 && assassin.getHealth() >= 50, "HEAL TEST 1: Assassin health should be between 100 and 110, knight health: " + assassin.getHealth());
        assassin.heal(300, 200);
        assertEquals(assassin.getHealth(), assassin.getMaxHealth(), "HEAL TEST 2: Assassin should not heal beyond max health, current health: " + assassin.getHealth() + "/" + assassin.getMaxHealth());

        //Test block, will loop forever if a block does not occur
        assassin.damage(10);
        while (assassin.getHealth() != assassin.getMaxHealth()) {
            assassin.heal(20, 10);
            assassin.damage(10);
        }
    }

    @Test
    @DisplayName("Mender Tests")
    public void mender() throws SQLException {
        Mender mender = new Mender();
        Monster enemy = new Monster(OGRE);

        //Use While loops for actions that have a chance of missing
        //Test attack
        while (enemy.getHealth() == enemy.getMaxHealth()) mender.attack(enemy);//0.8 chance of landing, 20 - 40 dmg
        assertTrue(enemy.getHealth() <= 175 && enemy.getHealth() >= 155, "ATTACK TEST: Enemy health should be between 180 and 160, enemy health:" + enemy.getHealth());
        //Test Ultimate
        /*
         * Menders Ultimate a heal and will only need to be tested in the heal category.
         */

        //Test damage
        while (mender.getHealth() == mender.getMaxHealth())
            enemy.attack(mender);//0.6 chance of landing,0.2 block chance, 60 - 30 dmg
        assertTrue(mender.getHealth() <= 45 && mender.getHealth() >= 15, "DAMAGE TEST: Mender health should be between 95 and 65, enemy health: " + mender.getHealth());

        //Test Heal
        mender = new Mender();
        while (mender.getHealth() == mender.getMaxHealth()) mender.damage(50);//Health should now be 25
        mender.heal(35, 25);
        assertTrue(mender.getHealth() <= 60 && mender.getHealth() >= 50, "HEAL TEST 1: Mender health should be between 100 and 110, knight health: " + mender.getHealth());
        mender.heal(300, 200);
        assertEquals(mender.getHealth(), mender.getMaxHealth(), "HEAL TEST 2: Mender should not heal beyond max health, current health: " + mender.getHealth() + "/" + mender.getMaxHealth());

        //Test block, will loop forever if a block does not occur
        mender.damage(10);
        while (mender.getHealth() != mender.getMaxHealth()) {
            mender.heal(20, 10);
            mender.damage(10);
        }
    }

    /*
    *
    GUARDIAN TESTS
    *
    */
    //Hydra test

    @Test
    @DisplayName("Hydra Tests")
    public void hydra() throws SQLException {
        Hydra hydra = new Hydra();
        Monster enemy = new Monster(OGRE);

        //Use While loops for actions that have a chance of missing
        //Test attack
        while (enemy.getHealth() == enemy.getMaxHealth()) hydra.attack(enemy);// 30 - 50 dmg
        assertTrue(enemy.getHealth() <= 170 && enemy.getHealth() >= 150, "ATTACK TEST: Enemy health should be between 180 and 160, enemy health:" + enemy.getHealth());
        //Test Ultimate
        /*
         * Hydra's Ultimate is a heal and will only need to be tested in the heal category.
         */

        //Test damage
        while (hydra.getHealth() == hydra.getMaxHealth())
            enemy.attack(hydra);//0.6 chance of landing,0.2 block chance, 60 - 30 dmg
        assertTrue(hydra.getHealth() <= 120 && hydra.getHealth() >= 90, "DAMAGE TEST: Hydra health should be between 120 and 90, enemy health: " + hydra.getHealth());

        //Test Heal
        hydra = new Hydra();
        while (hydra.getHealth() == hydra.getMaxHealth()) hydra.damage(50);//Health should now be 100
        hydra.heal(35, 25);
        assertTrue(hydra.getHealth() <= 135 && hydra.getHealth() >= 125, "HEAL TEST 1: Hydra health should be between 135 and 125, knight health: " + hydra.getHealth());
        hydra.heal(300, 200);
        assertEquals(hydra.getHealth(), hydra.getMaxHealth(), "HEAL TEST 2: Hydra should not heal beyond max health, current health: " + hydra.getHealth() + "/" + hydra.getMaxHealth());

    }
    //Tom test

    @Test
    @DisplayName("Tom Tests")
    public void tom() throws SQLException {
        Tom tom = new Tom();
        Monster enemy = new Monster(OGRE);

        //Use While loops for actions that have a chance of missing
        //Test attack
        while (enemy.getHealth() == enemy.getMaxHealth()) tom.attack(enemy);// 40 - 60 dmg
        assertTrue(enemy.getHealth() <= 160 && enemy.getHealth() >= 140, "ATTACK TEST: Enemy health should be between 160 and 140, enemy health:" + enemy.getHealth());

        //Test damage
        tom.damage(60);
        assertTrue(tom.getHealth() == 65, "DAMAGE TEST: Tom health should be 65, enemy health: " + tom.getHealth());

        //Test Ultimate: attack plus heal
        enemy = new Monster(OGRE);
        while (enemy.getHealth() == enemy.getMaxHealth()) tom.ultimate(enemy);//0.4 chance of landing, 40 - 60 dmg
        assertTrue(enemy.getHealth() <= 160 && enemy.getHealth() >= 140, "ULTIMATE ATTACK TEST: Enemy health should be between 160 and 140, enemy health:" + enemy.getHealth());
        assertTrue(tom.getHealth() <= 115 && tom.getHealth() >= 95, "ULTIMATE HEAL TEST : Tom health should be between 115 and 95, knight health: " + tom.getHealth());

    }
    //Red Dragon test

    @Test
    @DisplayName("Red Dragon Tests")
    public void red_Dragon() throws SQLException {
        RedDragon redDragon = new RedDragon();
        Monster enemy = new Monster(OGRE);

        //Use While loops for actions that have a chance of missing
        //Test attack
        while (enemy.getHealth() == enemy.getMaxHealth()) redDragon.attack(enemy);// 30 - 60 dmg
        assertTrue(enemy.getHealth() <= 170 && enemy.getHealth() >= 140, "ATTACK TEST: Enemy health should be between 170 and 140, enemy health:" + enemy.getHealth());

        //Test damage
        redDragon.damage(60);
        assertTrue(redDragon.getHealth() == 165, "DAMAGE TEST: Dragon health should be 165, enemy health: " + redDragon.getHealth());

        //Test Ultimate: attack plus heal
        enemy = new Monster(OGRE);
        while (enemy.getHealth() == enemy.getMaxHealth()) redDragon.ultimate(enemy);//0.3 chance of landing, 70 dmg
        assertTrue(enemy.getHealth() == 130, "ULTIMATE ATTACK TEST: Enemy health should be 130, enemy health:" + enemy.getHealth());

    }
    //Tom test

    @Test
    @DisplayName("Cerberus Tests")
    public void cerberus() throws SQLException {
        Cerberus cerberus = new Cerberus();
        Monster enemy = new Monster(OGRE);

        //Use While loops for actions that have a chance of missing
        //Test attack
        while (enemy.getHealth() == enemy.getMaxHealth()) cerberus.attack(enemy);// 30 - 50 dmg
        assertTrue(enemy.getHealth() <= 170 && enemy.getHealth() >= 150, "ATTACK TEST: Enemy health should be between 170 and 150, enemy health:" + enemy.getHealth());

        //Test damage
        while (cerberus.getHealth() == cerberus.getMaxHealth())
            cerberus.damage(60);//0.6 chance of landing,0.2 block chance, 60 - 30 dmg
        assertTrue(cerberus.getHealth() == 90, "DAMAGE TEST: Cerberus health should be 90, enemy health: " + cerberus.getHealth());

        //Test Ultimate: attack
        enemy = new Monster(OGRE);
        while (enemy.getHealth() == enemy.getMaxHealth()) cerberus.ultimate(enemy);//0.2 chance of landing, 50 - 65 dmg
        assertTrue(enemy.getHealth() <= 150 && enemy.getHealth() >= 135, "ULTIMATE ATTACK TEST: Enemy health should be between 150 and 135, enemy health:" + enemy.getHealth());

        //Test block, will loop forever if a block does not occur
        while (cerberus.getHealth() != cerberus.getMaxHealth()) {
            cerberus = new Cerberus();
            cerberus.damage(10);
        }
    }


}
