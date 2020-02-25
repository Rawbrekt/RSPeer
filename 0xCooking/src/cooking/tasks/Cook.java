package cooking.tasks;

import cooking.ZeroxCooking;
import org.rspeer.runetek.adapter.scene.SceneObject;
import org.rspeer.runetek.api.commons.Time;
import org.rspeer.runetek.api.commons.math.Random;
import org.rspeer.runetek.api.component.Bank;
import org.rspeer.runetek.api.component.Production;
import org.rspeer.runetek.api.component.tab.Inventory;
import org.rspeer.runetek.api.scene.Players;
import org.rspeer.runetek.api.scene.SceneObjects;
import org.rspeer.script.task.Task;
import org.rspeer.ui.Log;


public class Cook extends Task {
    private long anim = 0;

    @Override
    public boolean validate() {
        return shouldCook();
    }

    @Override
    public int execute() {

        if (!isAnimating()) {
            if (Production.isOpen()) {
                if (!Production.getAmount().equals(Production.Amount.ALL)) {
                    Log.info("Setting amount to all");
                    Production.initiate(Production.Amount.ALL);
                } else {
                    Production.initiate();
                }
            } else {
                SceneObject RANGE = SceneObjects.getNearest(26181);
                if (!RANGE.equals(null)) {
                    RANGE.interact("Cook");
                    Time.sleepUntil(() -> Production.isOpen(), Random.nextInt(3000, 6000));
                }
            }
        }
        return Random.nextInt(750,1250);
    }

    private boolean shouldCook() {
        return ZeroxCooking.res != null && ZeroxCooking.res.getCOOKING_AREA().contains(Players.getLocal()) && Inventory.contains(ZeroxCooking.res.getItem());
    }

    private boolean isAnimating() {
        if (Players.getLocal().isAnimating()) {
            anim = System.currentTimeMillis();
            return true;
        }
        return System.currentTimeMillis() <= (anim + Random.high(2200, 3000));
    }
}
