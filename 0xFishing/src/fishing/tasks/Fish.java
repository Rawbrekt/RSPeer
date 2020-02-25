package fishing.tasks;

import fishing.data.Fishtype;
import org.rspeer.runetek.adapter.scene.Npc;
import org.rspeer.runetek.api.commons.math.Random;
import org.rspeer.runetek.api.component.tab.Inventory;
import org.rspeer.runetek.api.component.tab.Skill;
import org.rspeer.runetek.api.component.tab.Skills;
import org.rspeer.runetek.api.movement.Movement;
import org.rspeer.runetek.api.scene.Npcs;
import org.rspeer.runetek.api.scene.Players;
import org.rspeer.script.task.Task;

import static fishing.ZeroxFishing.fishtype;
import static fishing.ZeroxFishing.tutProgress;

import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Predicate;


public class Fish extends Task {

    private long anim = 0;
    private int toggleNextRun = 20;

    @Override
    public boolean validate() {

        if (fishtype.getFishingArea().contains(Players.getLocal()) && Inventory.contains(fishtype.getItem()) && fishtype.getBait() == 0 && tutProgress == 1000) {
            return true;
        } else if (fishtype.getFishingArea().contains(Players.getLocal()) && Inventory.contains(fishtype.getItem()) && Inventory.contains(fishtype.getBait()) && !(fishtype.getBait() == 0) && tutProgress == 1000) {
            return true;
        } else {
            return false;
        }

    }

    @Override
    public int execute() {

        int fishingLvl = Skills.getCurrentLevel(Skill.FISHING);
        Fishtype newFish = Fishtype.getBestFishType(fishingLvl);

        if (fishtype == newFish) {

            Predicate<Npc> fishSpotPred =  f -> f.containsAction(fishtype.getMethod());
            Npc fishSpot = Npcs.getNearest(fishSpotPred);

            if (!(fishSpot == null) && !isAnimating() && fishtype.getFishingArea().contains(Players.getLocal())) {
                checkRunEnergy();
                fishSpot.interact(fishtype.getMethod());
            }
        } else {
            fishtype = newFish;
        }

        return Random.nextInt(600, 3000);
    }

    private boolean isAnimating() {
        if (Players.getLocal().isAnimating()) {
            anim = System.currentTimeMillis();
            return true;
        }
        return System.currentTimeMillis() <= (anim + Random.high(2200, 3000));
    }

    private void checkRunEnergy() {
        if (Movement.getRunEnergy() > toggleNextRun && !Movement.isRunEnabled()) {
            Movement.toggleRun(true);
            toggleNextRun = ThreadLocalRandom.current().nextInt(20, 30 + 1);
        }
    }

}
