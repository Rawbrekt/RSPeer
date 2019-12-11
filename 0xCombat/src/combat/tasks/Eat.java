package combat.tasks;

import combat.ZeroxCombat;
import org.rspeer.runetek.adapter.scene.Pickable;
import org.rspeer.runetek.adapter.scene.Player;
import org.rspeer.runetek.api.commons.Time;
import org.rspeer.runetek.api.commons.math.Random;
import org.rspeer.runetek.api.component.tab.Inventory;
import org.rspeer.runetek.api.movement.Movement;
import org.rspeer.runetek.api.movement.position.Area;
import org.rspeer.runetek.api.scene.Pickables;
import org.rspeer.runetek.api.scene.Players;
import org.rspeer.script.task.Task;
import org.rspeer.ui.Log;

public class Eat extends Task {

    Player me = Players.getLocal();
    String foodToCook = ZeroxCombat.target.getRawMeat();
    String foodToEat = ZeroxCombat.target.getCookedmeat();
    Area AREA_RANGE = Area.rectangular(3205, 3216, 3212, 3212);

    @Override
    public boolean validate() {
        return me.getHealthPercent() < 20;
    }

    @Override
    public int execute() {

        if (!Inventory.contains(foodToEat)) {
            if(Inventory.getCount(foodToCook) < 10) {
                Pickable rawfood = Pickables.getNearest(foodToCook);
                if (Movement.getRunEnergy() > 25 && !Movement.isRunEnabled()) {
                    Movement.toggleRun(!Movement.isRunEnabled());
                }

                if ((rawfood != null) && Movement.isWalkable(rawfood.getPosition())) {
                    Log.info("Looting: " + rawfood);
                    Time.sleepUntil(()-> rawfood.interact("Take"), Random.nextInt(1500,6000));
                }
            } else {
                if (!AREA_RANGE.contains(me)) {
                    Movement.walkToRandomized(AREA_RANGE.getCenter());
                    Time.sleep(Random.nextInt(1500,6000));
                } else {

                }


            }
        } else {
            Inventory.getFirst(foodToEat).interact("Eat");
        }
        return Random.nextInt(1500,6000);
    }
}
