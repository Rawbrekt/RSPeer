package fishlooting.tasks;

import org.rspeer.runetek.api.Worlds;
import org.rspeer.runetek.api.commons.math.Random;
import org.rspeer.runetek.adapter.scene.Pickable;
import org.rspeer.runetek.api.commons.Time;
import org.rspeer.runetek.api.component.WorldHopper;
import org.rspeer.runetek.api.component.tab.Inventory;
import org.rspeer.runetek.api.movement.Movement;
import org.rspeer.runetek.api.scene.Pickables;
import org.rspeer.runetek.api.scene.Players;
import org.rspeer.script.task.Task;

import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Predicate;

import static fishlooting.ZeroxFishlooting.*;


public class Collect extends Task {

    private int toggleNextRun = 20;

    @Override
    public boolean validate() {
        return !Inventory.isFull() && lootArea.contains(Players.getLocal()) && tutProgress == 1000;
    }

    @Override
    public int execute() {

        currentTask = "Collecting fish";

        Predicate<Pickable> fishPred = f -> f.getId() == 329 || f.getId() == 331 || f.getId() == 333 || f.getId() == 335;
        Pickable fish = Pickables.getNearest(fishPred);

        if (fish != null) {
            int freeSlots = Inventory.getFreeSlots();
            checkRunEnergy();
            fish.interact("Take");
            Time.sleepUntil(() -> Inventory.getFreeSlots() != freeSlots, Random.nextInt(600,1800));
            if (Inventory.getFreeSlots() != freeSlots) {
                lastFish = System.currentTimeMillis();
            }
        }
        return Random.nextInt(600,1200);
    }

    private void checkRunEnergy() {
        if (Movement.getRunEnergy() > toggleNextRun && !Movement.isRunEnabled()) {
            Movement.toggleRun(true);
            toggleNextRun = ThreadLocalRandom.current().nextInt(20, 30 + 1);
        }
    }
}
