package cooking.tasks;

import cooking.ZeroxCooking;
import org.rspeer.runetek.adapter.scene.SceneObject;
import org.rspeer.runetek.api.commons.Time;
import org.rspeer.runetek.api.commons.math.Random;
import org.rspeer.runetek.api.component.Bank;
import org.rspeer.runetek.api.component.tab.Inventory;
import org.rspeer.runetek.api.scene.Players;
import org.rspeer.runetek.api.scene.SceneObjects;
import org.rspeer.script.task.Task;

public class Banking extends Task {
    @Override
    public boolean validate() {
        return shouldBank();
    }

    @Override
    public int execute() {
        if (Bank.isOpen()) {

            if (!Inventory.isEmpty()) {
                Bank.depositAllExcept(ZeroxCooking.res.getItem());
                Time.sleepUntil(() -> Inventory.isEmpty(), Random.nextInt(150,2000));
            } else {
                Bank.withdrawAll(ZeroxCooking.res.getItem());
                Time.sleepUntil(() -> Inventory.contains(ZeroxCooking.res.getItem()),Random.nextInt(150,2000));
            }
        } else {
            SceneObject BANK = SceneObjects.getNearest(10355);
            if (!BANK.equals(null)) {
                BANK.interact("Bank");
                Time.sleepUntil(() -> Bank.isOpen(), Random.nextInt(3000, 6000));
            }

        }
        return Random.nextInt(750, 1500);
    }

    private boolean shouldBank() {
        return ZeroxCooking.res != null && ZeroxCooking.res.getBANKING_AREA().contains(Players.getLocal()) && !Inventory.contains(ZeroxCooking.res.getItem());
    }
}
