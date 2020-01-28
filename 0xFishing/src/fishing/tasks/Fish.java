package fishing.tasks;

import fishing.data.Fishtype;
import org.rspeer.runetek.adapter.scene.Npc;
import org.rspeer.runetek.api.commons.math.Random;
import org.rspeer.runetek.api.component.tab.Inventory;
import org.rspeer.runetek.api.component.tab.Skill;
import org.rspeer.runetek.api.component.tab.Skills;
import org.rspeer.runetek.api.scene.Npcs;
import org.rspeer.runetek.api.scene.Players;
import org.rspeer.script.task.Task;

import static fishing.ZeroxFishing.fishtype;
import java.util.function.Predicate;


public class Fish extends Task {

    private long anim = 0;

    @Override
    public boolean validate() {

        if (fishtype.getFishingArea().contains(Players.getLocal()) && Inventory.contains(fishtype.getItem()) && fishtype.getBait() == 0) {
            return true;
        } else if (fishtype.getFishingArea().contains(Players.getLocal()) && Inventory.contains(fishtype.getItem()) && Inventory.contains(fishtype.getBait()) && !(fishtype.getBait() == 0)) {
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
                fishSpot.interact(fishtype.getMethod());
            }
        } else {
            fishtype = newFish;
        }

        return Random.nextInt(3000, 7050);
    }

    private boolean isAnimating() {
        if (Players.getLocal().isAnimating()) {
            anim = System.currentTimeMillis();
            return true;
        }
        return System.currentTimeMillis() <= (anim + Random.high(2200, 3000));
    }


}
