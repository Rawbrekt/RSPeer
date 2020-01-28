package fishing.tasks;

import fishing.data.Fishtype;
import org.rspeer.runetek.adapter.component.Item;
import org.rspeer.runetek.adapter.scene.SceneObject;
import org.rspeer.runetek.api.commons.Time;
import org.rspeer.runetek.api.commons.math.Random;
import org.rspeer.runetek.api.component.Bank;
import org.rspeer.runetek.api.component.DepositBox;
import org.rspeer.runetek.api.component.tab.Inventory;
import org.rspeer.runetek.api.scene.Players;
import org.rspeer.runetek.api.scene.SceneObjects;
import org.rspeer.script.task.Task;
import org.rspeer.ui.Log;

import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.function.Predicate;

import static fishing.ZeroxFishing.fishtype;

public class Banking extends Task {

    @Override
    public boolean validate() {
        return shouldBank();
    }

    @Override
    public int execute() {
        if (Bank.isOpen() || DepositBox.isOpen()) {
            if (Bank.isOpen()) {
                if (fishtype.getBait() == 0) {

                    Bank.depositAllExcept(fishtype.getItem());

                    if (!Inventory.contains(fishtype.getItem())) {
                        Bank.withdraw(fishtype.getItem(), 1);
                        Time.sleepUntil(() -> Inventory.contains(fishtype.getItem()), Random.nextInt(2000, 3000));
                    }

                } else {

                    Bank.depositAllExcept(fishtype.getItem(), fishtype.getBait());

                    if (!Inventory.contains(fishtype.getItem())) {
                        Bank.withdraw(fishtype.getItem(), 1);
                        Time.sleepUntil(() -> Inventory.contains(fishtype.getItem()), Random.nextInt(2000, 3000));
                    }

                    if (!Inventory.contains(fishtype.getBait())) {
                        Bank.withdrawAll(fishtype.getBait());
                        Time.sleepUntil(() -> Inventory.contains(fishtype.getBait()), Random.nextInt(2000, 3000));
                    }
                }
            } else {
                if (fishtype.getBait() == 0 && Inventory.getFreeSlots() == 27) {
                    DepositBox.depositAllExcept(fishtype.getItem());
                    Time.sleepUntil(() -> Inventory.getFreeSlots() == 27, Random.nextInt(150, 1000));
                } else {

                    Item[] items = DepositBox.getItems();

                    Set<Item> set = new LinkedHashSet<Item>(Arrays.asList(items));
                    set.toArray();



                    Item[] Uniques = Arrays.stream(DepositBox.getItems()).distinct().toArray(Item[]::new);
                    Log.severe("uniques: " + Uniques.length);

                    for (Item item : Uniques) {
                        if (!(item.getId() == fishtype.getItem()) && !(item.getId() == fishtype.getBait()) && !(item == null)) {
                            Log.info("Depositing: " + item);
                            //item.interact("Deposit-All");
                            Time.sleep(Random.nextInt(250, 500));
                        }
                    }
                }
            }

        } else {

            if (fishtype.getBankingArea().contains(Players.getLocal()) && !fishtype.equals(Fishtype.LOBSTERS)) {
                Predicate<SceneObject> bankPred = b -> b.containsAction("Bank");
                SceneObject BANK = SceneObjects.getNearest(bankPred);

                if (!BANK.equals(null)) {
                    BANK.interact("Bank");
                    Time.sleepUntil(() -> Bank.isOpen(), Random.nextInt(3000, 6000));
                }
            } else {
                Predicate<SceneObject> bankPred = b -> b.containsAction("Deposit");
                SceneObject BANK = SceneObjects.getNearest(bankPred);

                if (!BANK.equals(null)) {
                    BANK.interact("Deposit");
                    Time.sleepUntil(() -> Bank.isOpen(), Random.nextInt(3000, 6000));
                }
            }

        }

        return Random.nextInt(750, 1500);
    }

    private boolean shouldBank() {

        if (fishtype.getBait() == 0) {
            return ((fishtype.getBankingArea().contains(Players.getLocal()) || fishtype.getAltBankingArea().contains(Players.getLocal())) && (!Inventory.contains(fishtype.getItem()) || Inventory.isFull()));
        } else {
            return ((fishtype.getBankingArea().contains(Players.getLocal()) || fishtype.getAltBankingArea().contains(Players.getLocal())) && (!Inventory.contains(fishtype.getItem()) || !Inventory.contains(fishtype.getBait()) || Inventory.isFull()));
        }

    }
}
