package crafting.tasks;

import crafting.ZeroxGoldJewel;
import org.rspeer.runetek.adapter.scene.SceneObject;
import org.rspeer.runetek.api.commons.Time;
import org.rspeer.runetek.api.commons.math.Random;
import org.rspeer.runetek.api.component.Bank;
import org.rspeer.runetek.api.component.tab.Inventory;
import org.rspeer.runetek.api.scene.Players;
import org.rspeer.runetek.api.scene.SceneObjects;
import org.rspeer.script.task.Task;
import org.rspeer.ui.Log;

import static crafting.ZeroxGoldJewel.bankArea;
import static crafting.ZeroxGoldJewel.toCraft;

public class Banking extends Task {
    @Override
    public boolean validate() {
        return (ZeroxGoldJewel.toCraft != null) && bankArea.contains(Players.getLocal()) && (!Inventory.contains(toCraft.getRawMaterial()) || !Inventory.contains(toCraft.getMould()));
    }

    @Override
    public int execute() {
        Log.info("Banking");
        if (Bank.isOpen()) {
            if (!Inventory.isEmpty()) {
                Bank.depositAllExcept(toCraft.getRawMaterial(), toCraft.getMould());
                Time.sleep(Random.nextInt(300, 900));
            }

            if (!Inventory.contains(toCraft.getMould())) {
                Bank.withdraw(toCraft.getMould(), 1);
                Time.sleepUntil(() -> Inventory.contains(toCraft.getMould()), Random.nextInt(1500, 3000));
            } else if(!Inventory.isFull()) {
                Bank.withdrawAll(toCraft.getRawMaterial());
                Time.sleepUntil(() -> Inventory.isFull(), Random.nextInt(1500, 3000));
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
}
