package fishing.tasks;

import fishing.data.Fishtype;
import org.rspeer.runetek.adapter.scene.SceneObject;
import org.rspeer.runetek.api.commons.Time;
import org.rspeer.runetek.api.commons.math.Random;
import org.rspeer.runetek.api.component.Bank;
import org.rspeer.runetek.api.component.DepositBox;
import org.rspeer.runetek.api.component.tab.Inventory;
import org.rspeer.runetek.api.scene.Players;
import org.rspeer.runetek.api.scene.SceneObjects;
import org.rspeer.script.task.Task;

import java.util.function.Predicate;
import static fishing.ZeroxFishing.fishtype;
import static fishing.ZeroxFishing.questsFinished;
import static fishing.ZeroxFishing.tutProgress;

public class Banking extends Task {

    @Override
    public boolean validate() {
        return questsFinished() && shouldBank() && tutProgress == 1000;
    }

    @Override
    public int execute() {
        if (Bank.isOpen() || DepositBox.isOpen()) {
            if (Bank.isOpen()) {
                if (fishtype.getBait() == 0) {

                    if (!Inventory.contains(fishtype.getItem())) {
                        if (!Inventory.isEmpty()) {
                            Bank.depositAllExcept(fishtype.getItem());
                            Time.sleepUntil(() -> Inventory.isEmpty(), Random.nextInt(1500,2750));
                        } else{
                            Bank.withdraw(fishtype.getItem(), 1);
                            Time.sleepUntil(() -> Inventory.contains(fishtype.getItem()), Random.nextInt(2000, 3000));
                        }

                    }

                    Bank.depositAllExcept(fishtype.getItem());

                } else {


                    if (!Inventory.contains(fishtype.getItem())) {
                        Bank.withdraw(fishtype.getItem(), 1);
                        Time.sleepUntil(() -> Inventory.contains(fishtype.getItem()), Random.nextInt(2000, 3000));
                    }

                    if (Bank.contains(fishtype.getBait())) {
                        Bank.withdrawAll(fishtype.getBait());
                        Time.sleepUntil(() -> !Bank.contains(fishtype.getBait()), Random.nextInt(2000, 3000));
                    }

                    Bank.depositAllExcept(fishtype.getItem(), fishtype.getBait());
                }
            } else {
                if (fishtype.getBait() == 0) {
                    DepositBox.depositAllExcept(fishtype.getItem());
                    Time.sleepUntil(() -> Inventory.getFreeSlots() == 27, Random.nextInt(150, 1000));
                } else {

                    DepositBox.depositAllExcept(fishtype.getItem(), fishtype.getBait());
                    Time.sleepUntil(() -> Inventory.getFreeSlots() == 26, Random.nextInt(1000,2000));
                    /*Item[] items = DepositBox.getItems();

                    Set<Integer> UniqueIDs = new LinkedHashSet<Integer>();

                    for (Item item : items) {
                        if (!(item.getId() == fishtype.getItem()) && !(item.getId() == fishtype.getBait()) && !(item == null)) {
                            UniqueIDs.add(item.getId());
                        }
                    }


                    for (Integer i : UniqueIDs) {
                        Log.info("Depositing: " + i);
                        DepositBox.depositAll(i);
                        Time.sleep(Random.nextInt(1000, 1500));
                    }*/
                }
            }


        } else {

            if (!fishtype.equals(Fishtype.LOBSTERS) || fishtype.getAltBankingArea().contains(Players.getLocal())) {
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
                    Time.sleepUntil(() -> DepositBox.isOpen(), Random.nextInt(3000, 6000));
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
