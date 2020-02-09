package fishing.tasks;

import fishing.data.Fishtype;
import org.rspeer.runetek.adapter.component.InterfaceComponent;
import org.rspeer.runetek.adapter.component.Item;
import org.rspeer.runetek.adapter.scene.Npc;
import org.rspeer.runetek.adapter.scene.SceneObject;
import org.rspeer.runetek.api.commons.Time;
import org.rspeer.runetek.api.commons.math.Random;
import org.rspeer.runetek.api.component.Dialog;
import org.rspeer.runetek.api.component.tab.Inventory;
import org.rspeer.runetek.api.movement.Movement;
import org.rspeer.runetek.api.movement.position.Area;
import org.rspeer.runetek.api.movement.position.Position;
import org.rspeer.runetek.api.scene.Npcs;
import org.rspeer.runetek.api.scene.Players;
import org.rspeer.runetek.api.scene.SceneObjects;
import org.rspeer.script.task.Task;

import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Predicate;
import static fishing.ZeroxFishing.fishtype;
import static fishing.ZeroxFishing.questsFinished;
import static fishing.ZeroxFishing.tutProgress;

public class Traverse extends Task {

    private Area MUSA_POINT = Area.rectangular(2913, 3182, 2964, 3142);
    private Area MUSA_DOCKS = Area.rectangular(2963, 3144, 2944, 3161);
    private Area PORT_SARIM = Area.rectangular(3024, 3226, 3030, 3210);
    private Area KARAMJA_BOAT = Area.rectangular(2952, 3143, 2963, 3139, 1);
    private Area PORT_SARIM_BOAT = Area.rectangular(3032, 3224, 3036, 3213, 1);


    private int toggleNextRun = 20;

    @Override
    public boolean validate() {
        return questsFinished() && (traverseToBank() || traverseToFish()) && tutProgress == 1000;
    }

    @Override
    public int execute() {
        if (fishtype.isPowerfish() && fishtype.getFishingArea().contains(Players.getLocal())) {
            Item[] Items = Inventory.getItems();
            for (Item item : Items) {
                if (!(item.getId() == fishtype.getItem()) && !(item.getId() == fishtype.getBait())) {
                    item.interact("Drop");
                    Time.sleep(Random.nextInt(120, 300));
                }
            }
        } else {
            checkRunEnergy();

            if (KARAMJA_BOAT.contains(Players.getLocal()) || PORT_SARIM_BOAT.contains(Players.getLocal())) {
                Predicate<SceneObject> plankPred = p -> p.getName().contains("Gangplank");
                SceneObject plank = SceneObjects.getNearest(plankPred);

                if (plank != null) {
                    plank.interact("Cross");
                }
            }
            if (fishtype.equals(Fishtype.LOBSTERS)) {
                if (traverseToBank()) {
                    if (MUSA_POINT.contains(Players.getLocal())) {
                        if (MUSA_DOCKS.contains(Players.getLocal())) {
                            if (Dialog.isOpen()) {
                                if (Dialog.canContinue()) {
                                    Dialog.processContinue();
                                } else {
                                    solveDialog("Yes please", "Can I journey on this ship?", "Search away, I have nothing to hide.", "Ok.");
                                }

                            } else {
                                Predicate<Npc> SeamanPred = s -> s.getName().contains("Customs officer");
                                Npc Seaman = Npcs.getNearest(SeamanPred);
                                if (Seaman != null) {
                                    Seaman.interact("Pay-fare");
                                }
                            }

                        } else {
                            Movement.setWalkFlag(new Position(2955, 3146, 0));
                            //Movement.walkToRandomized(MUSA_DOCKS.getCenter());
                        }
                    } else {
                        if (Inventory.contains(fishtype.getItem()) && Inventory.contains(fishtype.getBait())) {
                            Movement.walkToRandomized(fishtype.getBankingArea().getCenter());
                        } else {
                            Movement.walkToRandomized(fishtype.getAltBankingArea().getCenter());
                        }
                    }

                } else {
                    if (Inventory.getCount(true, fishtype.getBait()) > 60) {
                        if (!MUSA_POINT.contains(Players.getLocal())) {
                            if (PORT_SARIM.contains(Players.getLocal())) {
                                if (Dialog.isOpen()) {
                                    if (Dialog.canContinue()) {
                                        Dialog.processContinue();
                                    } else {
                                        Dialog.process("Yes please");
                                    }

                                } else {

                                    Predicate<Npc> SeamanPred = s -> s.getName().contains("Seaman");
                                    Npc Seaman = Npcs.getNearest(SeamanPred);
                                    if (Seaman != null) {
                                        Seaman.interact("Pay-fare");
                                    }

                                }
                            } else {
                                Movement.walkToRandomized(PORT_SARIM.getCenter());
                            }
                        } else {
                            Movement.setWalkFlag(new Position(2924, 3178, 0));
                            //Movement.walkToRandomized(fishtype.getFishingArea().getCenter());
                        }
                    }
                }

            } else {
                Movement.walkToRandomized(traverseToBank() ? fishtype.getBankingArea().getCenter() : fishtype.getFishingArea().getCenter());
            }
        }


        return Random.nextInt(0, 1500);
    }

    private void checkRunEnergy() {
        if (Movement.getRunEnergy() > toggleNextRun && !Movement.isRunEnabled()) {
            Movement.toggleRun(true);
            toggleNextRun = ThreadLocalRandom.current().nextInt(20, 30 + 1);
        }
    }

    private boolean traverseToBank() {
        if (fishtype.getBait() == 0) {
            return !fishtype.getBankingArea().contains(Players.getLocal()) && (Inventory.isFull() || !Inventory.contains(fishtype.getItem()));
        } else {
            return !fishtype.getBankingArea().contains(Players.getLocal()) && (Inventory.isFull() || !Inventory.contains(fishtype.getItem()) || !Inventory.contains(fishtype.getBait()));
        }
    }

    private boolean traverseToFish() {
        if (fishtype.getBait() == 0) {
            return !fishtype.getFishingArea().contains(Players.getLocal()) && Inventory.contains(fishtype.getItem());
        } else {
            return !fishtype.getFishingArea().contains(Players.getLocal()) && Inventory.contains(fishtype.getItem()) && Inventory.contains(fishtype.getBait());
        }
    }

    private boolean solveDialog(String... options) {

        if (Dialog.canContinue()) {
            return Dialog.processContinue() && solveDialog(options);
        }

        for (String option : options) {
            for (InterfaceComponent chatOption : Dialog.getChatOptions()) {
                if (chatOption.getText().equals(option)) {
                    return Dialog.process(option) && solveDialog(options);
                }
            }
        }

        return false;
    }


}
