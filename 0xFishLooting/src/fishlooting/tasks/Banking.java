package fishlooting.tasks;

import org.rspeer.runetek.adapter.scene.SceneObject;
import org.rspeer.runetek.api.commons.Time;
import org.rspeer.runetek.api.commons.math.Random;
import org.rspeer.runetek.api.component.Bank;
import org.rspeer.runetek.api.component.tab.Inventory;
import org.rspeer.runetek.api.scene.Players;
import org.rspeer.runetek.api.scene.SceneObjects;
import org.rspeer.script.task.Task;

import java.util.function.Predicate;

import static fishlooting.ZeroxFishlooting.bankArea;
import static fishlooting.ZeroxFishlooting.currentTask;
import static fishlooting.ZeroxFishlooting.tutProgress;

public class Banking extends Task {
    @Override
    public boolean validate() {
        return shouldBank() && tutProgress ==1000;
    }

    @Override
    public int execute() {
        currentTask = "Banking";
        if (Bank.isOpen()) {
            Bank.depositInventory();
            Time.sleepUntil(() -> Inventory.isEmpty(), Random.nextInt(600,1800));
        } else {
            Predicate<SceneObject> bankPred = b -> b.containsAction("Bank");
            SceneObject BANK = SceneObjects.getNearest(bankPred);

            if (!BANK.equals(null)) {
                BANK.interact("Bank");
                Time.sleepUntil(() -> Bank.isOpen(), Random.nextInt(3000, 6000));
            }
        }
        return 0;
    }

    private boolean shouldBank() {
        if (!Inventory.isEmpty() && bankArea.contains(Players.getLocal())) {
            return true;
        } else {
            return false;
        }
    }
}
