package fishing.tasks;

import org.rspeer.runetek.adapter.component.InterfaceComponent;
import org.rspeer.runetek.adapter.scene.Npc;
import org.rspeer.runetek.adapter.scene.SceneObject;
import org.rspeer.runetek.api.Game;
import org.rspeer.runetek.api.Varps;
import org.rspeer.runetek.api.commons.Time;
import org.rspeer.runetek.api.commons.math.Random;
import org.rspeer.runetek.api.component.Bank;
import org.rspeer.runetek.api.component.Dialog;
import org.rspeer.runetek.api.component.tab.Inventory;
import org.rspeer.runetek.api.movement.Movement;
import org.rspeer.runetek.api.movement.position.Area;
import org.rspeer.runetek.api.movement.position.Position;
import org.rspeer.runetek.api.scene.Npcs;
import org.rspeer.runetek.api.scene.Players;
import org.rspeer.runetek.api.scene.SceneObjects;
import org.rspeer.script.task.Task;
import org.rspeer.ui.Log;

import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Predicate;

import static fishing.ZeroxFishing.*;
import static fishing.tasks.GetStartingGold.COINS;
import static fishing.tasks.GetStartingGold.TUTORIAL_ISLAND_ITEMS;

public class Quests extends Task {

    boolean hasStartersGold;

    private Area LUMBRIDGE_BANK = Area.rectangular(3210, 3215, 3207, 3220, 2);
    private Area COOK = Area.rectangular(3205, 3217, 3212, 3212);
    private Area FARMER = Area.rectangular(3184, 3278, 3192, 3270);
    private Area ROMEO = Area.rectangular(3203, 3438, 3226, 3409);
    private Area JULIET = Area.rectangular(3148, 3442, 3168, 3423, 1);
    private Area BUILDING_JULIET = Area.rectangular(3156, 3436, 3173, 3427);
    private Area FATHER_LAWRENCE = Area.rectangular(3252, 3488, 3259, 3478);
    private Area APOTHECARY = Area.rectangular(3192, 3406, 3198, 3402);
    private Area VARROCK_EAST_BANK = Area.rectangular(3250, 3424, 3257, 3418);
    private Position DUNGEON = new Position(9741, 8526);

    int itemsCook[] = {1933, 1927, 1944};
    int itemsSheep = 1759;
    int itemsRomeo = 753;
    private int toggleNextRun = 20;

    @Override
    public boolean validate() {
        if (Inventory.containsAll(TUTORIAL_ISLAND_ITEMS) && Inventory.contains(COINS)) {
            hasStartersGold = true;
        }
        return Game.isLoggedIn() && !Game.isLoadingRegion() && !questsFinished() && tutProgress == 1000 && hasStartersGold;
    }

    @Override
    public int execute() {
        checkRunEnergy();

        cookProgress = Varps.get(V_COOKS_ASSISTANT);
        sheepProgress = Varps.get(V_SHEEP_SHEARER);
        romeoProgress = Varps.get(V_ROMEO_AND_JULLIET);

        if (cookProgress != 2) {
            if (Dialog.isOpen()) {
                if (Dialog.canContinue()) {
                    Dialog.processContinue();
                } else {
                    solveDialog("What's wrong?", "I'm always happy to help a cook in distress.");
                }
            } else if (Inventory.containsAll(itemsCook)) {
                if (COOK.contains(Players.getLocal())) {

                    Predicate<Npc> cookPred = c -> c.getName().equals("Cook");
                    Npc cook = Npcs.getNearest(cookPred);
                    if (cook != null) {
                        cook.interact("Talk-to");
                    }

                } else {
                    Movement.walkToRandomized(COOK.getCenter());
                }
            } else {
                if (LUMBRIDGE_BANK.contains(Players.getLocal())) {
                    if (Bank.isOpen()) {
                        if (Bank.containsAll(itemsCook)) {
                            for (int i : itemsCook) {
                                Bank.withdraw(i, 1);
                                Time.sleepUntil(() -> Inventory.contains(i), Random.nextInt(1500, 3000));
                            }
                        } else {
                            Log.severe("does not have cooks assistant quest items");
                            Bank.depositInventory();

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
                    Movement.walkToRandomized(LUMBRIDGE_BANK.getCenter());
                }

            }


        } else if (sheepProgress != 21) {
            if (Dialog.isOpen()) {
                if (Dialog.canContinue()) {
                    Dialog.processContinue();
                } else {
                    solveDialog("I'm looking for a quest.", "Yes okay. I can do that.", "Of course!", "I'm something of an expert actually");
                }
            } else if ((Inventory.getCount(itemsSheep) == 20)) {
                if (FARMER.contains(Players.getLocal())) {
                    Predicate<Npc> farmerPred = c -> c.getName().equals("Fred the Farmer");
                    Npc farmer = Npcs.getNearest(farmerPred);
                    if (farmer != null) {
                        farmer.interact("Talk-to");
                    }
                } else {
                    Movement.walkToRandomized(FARMER.getCenter());
                }
            } else {
                if (LUMBRIDGE_BANK.contains(Players.getLocal())) {
                    if (Bank.isOpen()) {
                        if (Bank.contains(itemsSheep)) {
                            int amountToWithdraw = 20 - Inventory.getCount(itemsSheep);
                            Bank.withdraw(itemsSheep, amountToWithdraw);
                            Time.sleepUntil(() -> Inventory.getCount(itemsSheep) == 20, Random.nextInt(3000, 6000));
                        } else {
                            Log.severe("does not have sheep shearer quest items");
                            Bank.depositInventory();

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
                    Movement.walkToRandomized(LUMBRIDGE_BANK.getCenter());
                }

            }


        } else if (romeoProgress != 100) {

            switch (romeoProgress) {
                case 0:
                    if (ROMEO.contains(Players.getLocal())) {
                        if (Dialog.isOpen()) {
                            solveDialog("Perhaps I could help to find her for you?", "Yes, ok, I'll let her know.", "Ok, thanks.");
                        } else {
                            Predicate<Npc> romeoPred = r -> r.getName().equals("Romeo");
                            Npc romeo = Npcs.getNearest(romeoPred);

                            if (romeo != null) {
                                romeo.interact("Talk-to");
                            }
                        }
                    } else {
                        Movement.walkToRandomized(ROMEO.getCenter());
                    }
                    break;
                case 10:
                    if (JULIET.contains(Players.getLocal())) {
                        if (Dialog.isOpen()) {
                            solveDialog("Something else.");
                        } else {
                            Predicate<Npc> julietPred = j -> j.getName().equals("Juliet");
                            Npc juliet = Npcs.getNearest(julietPred);

                            if (!juliet.equals(null)) {
                                juliet.interact("Talk-to");
                            }
                        }
                    } else {
                        walkToJuliet();
                        //Movement.walkToRandomized(JULIET.getCenter());
                    }
                    break;
                case 20:
                    if (ROMEO.contains(Players.getLocal())) {
                        if (Dialog.isOpen()) {
                            solveDialog();
                        } else {
                            Predicate<Npc> romeoPred = r -> r.getName().equals("Romeo");
                            Npc romeo = Npcs.getNearest(romeoPred);

                            if (!romeo.equals(null)) {
                                romeo.interact("Talk-to");
                            }
                        }
                    } else {
                        if (JULIET.contains(Players.getLocal())) {
                            Predicate<SceneObject> stairsPred = s -> s.getName().contains("Staircase");
                            SceneObject stairs = SceneObjects.getNearest(stairsPred);

                            if (stairs != null) {
                                stairs.interact("Climb-down");
                            }
                        } else {
                            Movement.walkToRandomized(ROMEO.getCenter());
                        }
                    }
                    break;
                case 30:
                    if (FATHER_LAWRENCE.contains(Players.getLocal())) {
                        if (Dialog.isOpen()) {
                            solveDialog();
                        } else {
                            Predicate<Npc> lawrencePred = r -> r.getName().equals("Father Lawrence");
                            Npc lawrence = Npcs.getNearest(lawrencePred);

                            if (!lawrence.equals(null)) {
                                lawrence.interact("Talk-to");
                            }
                        }
                    } else {
                        Movement.walkToRandomized(FATHER_LAWRENCE.getCenter());
                    }
                    break;
                case 40:
                case 50:
                    if (Dialog.isOpen()) {
                        solveDialog("Talk about something else.", "Talk about Romeo & Juliet.", "Ok, thanks.", "Something else.");
                    } else if (Inventory.contains(756)) {
                        if (JULIET.contains(Players.getLocal())) {
                            Predicate<Npc> julietPred = j -> j.getName().equals("Juliet");
                            Npc juliet = Npcs.getNearest(julietPred);

                            if (!juliet.equals(null)) {
                                juliet.interact("Talk-to");
                            }
                        } else {
                            walkToJuliet();
                        }
                    } else if (Inventory.contains(itemsRomeo)) {
                        if (APOTHECARY.contains(Players.getLocal())) {
                            Predicate<Npc> apothecaryPred = a -> a.getName().equals("Apothecary");
                            Npc apothecary = Npcs.getNearest(apothecaryPred);

                            if (!apothecary.equals(null)) {
                                apothecary.interact("Talk-to");
                            }

                        } else {
                            Movement.walkToRandomized(APOTHECARY.getCenter());
                        }
                    } else {
                        if (VARROCK_EAST_BANK.contains(Players.getLocal())) {
                            if (Bank.isOpen()) {
                                if (Bank.contains(itemsRomeo)) {
                                    Bank.withdraw(itemsRomeo, 1);
                                    Time.sleepUntil(() -> Inventory.contains(itemsRomeo), Random.nextInt(3000, 6000));
                                } else {
                                    Log.severe("does not have romeo and juliet quest items");
                                    Bank.depositInventory();

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
                            Movement.walkToRandomized(VARROCK_EAST_BANK.getCenter());
                        }
                    }
                    break;
                case 60:

                    if (Dialog.isOpen()) {
                        solveDialog();
                    } else if ((Players.getLocal().getPosition() == DUNGEON)) {

                    } else if (Game.isInCutscene()) {
                        if (Dialog.isOpen()) {
                            solveDialog();
                        }
                    } else if (ROMEO.contains(Players.getLocal())) {

                        Predicate<Npc> romeoPred = r -> r.getName().equals("Romeo");
                        Npc romeo = Npcs.getNearest(romeoPred);

                        if (!romeo.equals(null)) {
                            romeo.interact("Talk-to");
                        }
                    } else {

                        if (JULIET.contains(Players.getLocal())) {
                            Predicate<SceneObject> stairsPred = s -> s.getName().contains("Staircase");
                            SceneObject stairs = SceneObjects.getNearest(stairsPred);

                            if (stairs != null) {
                                stairs.interact("Climb-down");
                            }
                        } else {
                            Movement.walkToRandomized(ROMEO.getCenter());
                        }


                    }
                    break;

            }


        }
        return Random.nextInt(750, 1500);
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

    private void checkRunEnergy() {
        if (Movement.getRunEnergy() > toggleNextRun && !Movement.isRunEnabled()) {
            Movement.toggleRun(true);
            toggleNextRun = ThreadLocalRandom.current().nextInt(20, 30 + 1);
        }
    }

    private void walkToJuliet() {
        if (BUILDING_JULIET.contains(Players.getLocal())) {
            Predicate<SceneObject> stairsPred = s -> s.getName().contains("Staircase");
            SceneObject stairs = SceneObjects.getNearest(stairsPred);

            if (stairs != null) {
                stairs.interact("Climb-up");
            }
        } else {
            Movement.walkToRandomized(BUILDING_JULIET.getCenter());
        }

    }
}
