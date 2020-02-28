package fishlooting.tasks;

import org.rspeer.runetek.adapter.component.InterfaceComponent;
import org.rspeer.runetek.adapter.component.Item;
import org.rspeer.runetek.adapter.scene.Player;
import org.rspeer.runetek.adapter.scene.SceneObject;
import org.rspeer.runetek.api.Worlds;
import org.rspeer.runetek.api.commons.Time;
import org.rspeer.runetek.api.commons.math.Random;
import org.rspeer.runetek.api.component.Bank;
import org.rspeer.runetek.api.component.Trade;
import org.rspeer.runetek.api.component.WorldHopper;
import org.rspeer.runetek.api.component.chatter.BefriendedPlayers;
import org.rspeer.runetek.api.component.tab.Inventory;
import org.rspeer.runetek.api.movement.Movement;
import org.rspeer.runetek.api.movement.position.Position;
import org.rspeer.runetek.api.scene.Players;
import org.rspeer.runetek.api.scene.SceneObjects;
import org.rspeer.script.task.Task;
import org.rspeer.ui.Log;

import java.util.function.Predicate;

import static fishlooting.ZeroxFishlooting.*;


public class Mule extends Task {

    @Override
    public boolean validate() {

        return !muled && BefriendedPlayers.getFirst(MULE_NAME).getWorld() == MULE_WORLD_INT;
    }

    @Override
    public int execute() {
        currentTask = "Muling";

        if (Players.getLocal().getAnimation() == 619) {
            Position currentPos = Players.getLocal().getPosition();
            Movement.walkTo(currentPos.translate(currentPos.getX() + 1, currentPos.getY() + 1));

        } else if (Worlds.getCurrent() != MULE_WORLD_INT) {
            worldHop();
        } else {
            if (!banked) {
                getFish();
            } else {
                tradeMule();
            }
        }
        return Random.nextInt(600,1500);
    }

    private void worldHop() {
        Log.info("Hopping to: " + MULE_WORLD);
        WorldHopper.hopTo(MULE_WORLD_INT);
        Time.sleepUntil(() -> Worlds.getCurrent() == MULE_WORLD_INT, Random.nextInt(10000, 11000));
    }

    private void getFish() {
        if (bankArea.contains(Players.getLocal())) {
            if (Bank.isOpen()) {

                if (!Inventory.isEmpty()) {
                    Bank.depositAllExcept(334,336,330,332);
                    Time.sleep(Random.nextInt(600,1700));
                }

                if (Bank.getWithdrawMode() != Bank.WithdrawMode.NOTE) {
                    Bank.setWithdrawMode(Bank.WithdrawMode.NOTE);
                } else if (Bank.contains(333)) {
                    Bank.withdrawAll(333);
                    Time.sleepUntil(() -> !Bank.contains(333), Random.nextInt(600,1400));
                } else if (Bank.contains(335)) {
                    Bank.withdrawAll(335);
                    Time.sleepUntil(() -> !Bank.contains(335), Random.nextInt(600,1400));
                } else if (Bank.contains(329)) {
                    Bank.withdrawAll(329);
                    Time.sleepUntil(() -> !Bank.contains(329), Random.nextInt(600,1400));
                } else if (Bank.contains(331)) {
                    Bank.withdrawAll(331);
                    Time.sleepUntil(() -> !Bank.contains(331), Random.nextInt(600,1400));
                } else {
                    banked = true;
                }

            } else {
                Predicate<SceneObject> bankPred = b -> b.containsAction("Bank");
                SceneObject BANK = SceneObjects.getNearest(bankPred);

                if (!BANK.equals(null)) {
                    BANK.interact("Bank");
                    Time.sleepUntil(() -> Bank.isOpen(), Random.nextInt(3000, 6000));
                }
            }
        } else {
            Movement.walkToRandomized(bankArea.getCenter());
        }
    }

    private void tradeMule() {
        if (Trade.isOpen()) {
            if (Trade.isOpen(true) && Trade.isWaitingForMe()) {
                Trade.accept();
            } else {
                Item[] inventory = Inventory.getItems();
                if (!(inventory.length == 0)) {
                    for (Item i : inventory) {
                        Trade.offerAll(i.getId());
                        Time.sleep(Random.nextInt(500, 750));
                    }
                } else {
                    Trade.accept();
                }
            }
        } else {
            Player mule = Players.getNearest(MULE_NAME);
            if (!mule.equals(null)) {
                mule.interact("Trade with");
                Time.sleepUntil(() -> Trade.isOpen(), Random.nextInt(7000, 10000));
            }
        }
    }
}
