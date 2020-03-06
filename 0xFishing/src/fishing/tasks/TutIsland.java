package fishing.tasks;

import org.rspeer.runetek.adapter.Interactable;
import org.rspeer.runetek.adapter.component.InterfaceComponent;
import org.rspeer.runetek.adapter.component.Item;
import org.rspeer.runetek.adapter.scene.Npc;
import org.rspeer.runetek.adapter.scene.SceneObject;
import org.rspeer.runetek.api.Game;
import org.rspeer.runetek.api.Varps;
import org.rspeer.runetek.api.commons.Time;
import org.rspeer.runetek.api.commons.math.Random;
import org.rspeer.runetek.api.component.Dialog;
import org.rspeer.runetek.api.component.InterfaceOptions;
import org.rspeer.runetek.api.component.Interfaces;
import org.rspeer.runetek.api.component.ItemTables;
import org.rspeer.runetek.api.component.chatter.BefriendedPlayers;
import org.rspeer.runetek.api.component.tab.Combat;
import org.rspeer.runetek.api.component.tab.Inventory;
import org.rspeer.runetek.api.component.tab.Tab;
import org.rspeer.runetek.api.component.tab.Tabs;
import org.rspeer.runetek.api.input.Keyboard;
import org.rspeer.runetek.api.movement.Movement;
import org.rspeer.runetek.api.movement.position.Area;
import org.rspeer.runetek.api.movement.position.Position;
import org.rspeer.runetek.api.scene.Npcs;
import org.rspeer.runetek.api.scene.Players;
import org.rspeer.runetek.api.scene.SceneObjects;
import org.rspeer.script.task.Task;
import org.rspeer.ui.Log;

import java.util.HashSet;
import java.util.Set;
import java.util.function.Predicate;

import static fishing.ZeroxFishing.MULE_NAME;
import static fishing.ZeroxFishing.V_TUTISLAND;
import static fishing.ZeroxFishing.tutProgress;

public class TutIsland extends Task {

    private boolean runToggled = false;
    private static boolean inventoryReady = false;

    @Override
    public boolean validate() {
        return tutProgress != 1000;
    }

    @Override
    public int execute() {
        tutProgress = Varps.get(V_TUTISLAND);
        boolean doDefault = false;
        Predicate<String> defaultAction = a -> true;

        Area mineLocation, combatLadderLocation, magicLocation;
        mineLocation = Area.rectangular(new Position(3082, 9507, 0), new Position(3082, 9507, 0));
        combatLadderLocation = Area.rectangular(new Position(3111, 9525, 0), new Position(3111, 9525, 0));
        magicLocation = Area.rectangular(new Position(3141, 3088, 0), new Position(3141, 3088, 0));

        Log.info("" + tutProgress);

        if (tutProgress > 1) {
            doBasics();
        }

        switch (tutProgress) {
            case 1:
                // Account name and looks
                createCharacter();
                break;
            case 2:
                if (Dialog.isViewingChatOptions()) {
                    Dialog.process("I've played in the past");
                } else {
                    doDefault = true;
                }
                break;
            case 3:
                if (Dialog.isOpen()) {
                    if (Dialog.canContinue()) {
                        Dialog.processContinue();
                    }
                }
                if (Tabs.isOpen(Tab.OPTIONS)) {
                    doDefault = true;
                } else {
                    Tabs.open(Tab.OPTIONS);
                    Time.sleepUntil(() -> Tabs.isOpen(Tab.OPTIONS), Random.nextInt(2000, 2500));
                }
                break;
            case 7:
                if (Tabs.isOpen(Tab.OPTIONS)) {
                    doDefault = true;
                } else {
                    Tabs.open(Tab.OPTIONS);
                    Time.sleepUntil(() -> Tabs.isOpen(Tab.OPTIONS), Random.nextInt(2000, 2500));
                }
                break;
            case 10:
                if (Dialog.isOpen()) {
                    doDefault = true;
                } else {
                    SceneObjects.getNearest("Door").interact("Open");
                }
                break;
            case 20:
                Npcs.getNearest("Survival Expert").interact("Talk-to");
                break;
            case 30:
                if (Dialog.isOpen()) {
                    Dialog.processContinue();
                } else {
                    Tabs.open(Tab.INVENTORY);
                    Time.sleepUntil(() -> Tabs.isOpen(Tab.INVENTORY), Random.nextInt(2000, 2500));
                }
                break;
            case 40:
                if (Inventory.contains(303)) {
                    Npcs.getNearest("Fishing spot").interact("Net");
                }
                break;
            case 50:
                Tabs.open(Tab.SKILLS);
                Time.sleepUntil(() -> Tabs.isOpen(Tab.SKILLS), Random.nextInt(2000, 2500));
                break;
            case 60:
                if (!Dialog.isOpen()) {
                    Npcs.getNearest("Survival Expert").interact("Talk-to");
                } else {
                    Dialog.processContinue();
                }
                break;
            case 70:
                if (!Inventory.contains(1351) && !Inventory.contains(590)) {
                    if (!Dialog.isOpen()) {
                        Npcs.getNearest("Survival Expert").interact("Talk-to");
                    } else {
                        Dialog.processContinue();
                    }
                } else {
                    SceneObject tree = SceneObjects.getNearest("Tree");
                    if (tree != null) {
                        tree.interact("Chop down");
                    }
                }
                break;
            case 80:
                SceneObject fire = SceneObjects.getFirstAt(Players.getLocal().getPosition());
                if (fire == null || !fire.getName().equals("Fire")) {
                    useItemOn("Logs", Inventory.getFirst("Tinderbox"));
                } else {
                    Movement.walkTo(Players.getLocal().getPosition().translate(Random.nextInt(-1, 1), Random.nextInt(-1, 1)));
                    Time.sleep(3000);
                }
                break;
            case 90:
                useItemOn("Raw shrimps", SceneObjects.getNearest("Fire"));
                break;
            case 120:
            case 360:
                SceneObjects.getNearest("Gate").interact("Open");
                break;
            case 130:
                SceneObjects.getNearest("Door").interact("Open");
                break;
            case 140:
            case 330:
            case 270:
            case 240:
            case 220:
            case 370:
            case 410:
                doDefault = true;
                break;
            case 150:
                useItemOn("Pot of flour", Inventory.getFirst("Bucket of water"));
                break;
            case 160:
                useItemOn("Bread dough", SceneObjects.getNearest("Range"));
                break;
            case 170:
                SceneObjects.getNearest(9710).interact("Open");
                break;
            case 200:
                // Toggle run
                if (!runToggled) {
                    if (Movement.isRunEnabled()) {
                        Movement.toggleRun(false);
                    } else {
                        Interfaces.getComponent(160, 22).interact("Toggle Run");
                        runToggled = true;
                    }
                }
                break;
            case 210:
                if (new Position(3085, 3127).distance() > 20) {
                    Movement.walkTo(new Position(3085, 3127));
                } else {
                    SceneObjects.getNearest("Door").interact("Open");
                }
                break;
            case 230:
                if (Dialog.canContinue()) {
                    Dialog.processContinue();
                } else {
                    if (!Tabs.isOpen(Tab.QUEST_LIST)) {
                        Tabs.open(Tab.QUEST_LIST);
                        Time.sleepUntil(() -> Tabs.isOpen(Tab.QUEST_LIST), Random.nextInt(2000, 2500));
                    }
                }
                break;
            case 250:
                if (Dialog.canContinue()) {
                    Dialog.processContinue();
                } else {
                    SceneObjects.getNearest("Ladder").interact("Climb-down");
                }
                break;
            case 260:
                Npc miningInstructor = Npcs.getNearest("Mining Instructor");

                if (miningInstructor == null) {
                    if (!mineLocation.contains(Players.getLocal())) {
                        Movement.walkTo(mineLocation.getCenter());
                    }
                } else {
                    doDefault = true;
                }
                break;
            case 300:
                SceneObjects.getNearest(10080).interact("Mine");
                break;
            case 310:
                SceneObjects.getNearest(10079).interact("Mine");
                break;
            case 320:
                SceneObjects.getNearest("Furnace").interact("Use");
                break;
            case 340:
                if (Dialog.isOpen() && Dialog.canContinue()) {
                    Dialog.processContinue();
                } else {
                    SceneObjects.getNearest("Anvil").interact("Smith");
                    Time.sleepUntil(() -> Interfaces.isOpen(312), Random.nextInt(2000, 2500));
                }
                break;
            case 350:
                if (!Players.getLocal().isAnimating()) {
                    if (Interfaces.isOpen(312)) {
                        Interfaces.getComponent(312, 9).interact("Smith");
                    } else {
                        SceneObjects.getNearest("Anvil").interact("Smith");
                        Time.sleepUntil(() -> Interfaces.isOpen(312), Random.nextInt(2000, 2500));
                    }
                }
                break;
            case 390:
                if (Dialog.canContinue()) {
                    Dialog.processContinue();
                } else {
                    if (!Tabs.isOpen(Tab.EQUIPMENT)) {
                        Tabs.open(Tab.EQUIPMENT);
                        Time.sleepUntil(() -> Tabs.isOpen(Tab.EQUIPMENT), Random.nextInt(2000, 2500));
                    }
                }
                break;
            case 400:
                if (Tabs.isOpen(Tab.EQUIPMENT)) {
                    Interfaces.getComponent(387, 1).interact("Use");
                    Time.sleepUntil(() -> Interfaces.getComponent(84, 0).isVisible(), Random.nextInt(2000, 2500));
                } else {
                    Tabs.open(Tab.EQUIPMENT);
                    Time.sleepUntil(() -> Tabs.isOpen(Tab.EQUIPMENT), Random.nextInt(2000, 2500));
                }
                break;
            case 405:

                Predicate<InterfaceComponent> daggerPred = d -> d.getItemId() == 1205;
                Interfaces.getFirst(daggerPred).interact("Equip");
                //Interfaces.getComponent(85, 0, 8).interact("Equip");
                break;
            case 420:
                if (Dialog.canContinue()) {
                    Dialog.processContinue();
                }
                if (ItemTables.contains(ItemTables.EQUIPMENT, s -> s.equalsIgnoreCase("Bronze sword"))) {
                    Inventory.getFirst("Wooden shield").interact(a -> true);
                } else {
                    Inventory.getFirst("Bronze sword").interact("Wield");
                }
                break;
            case 430:
                if (!Tabs.isOpen(Tab.COMBAT)) {
                    Tabs.open(Tab.COMBAT);
                    Time.sleepUntil(() -> Tabs.isOpen(Tab.COMBAT), Random.nextInt(2000, 2500));
                }
                break;
            case 440:
                SceneObjects.getNearest(so -> so.getName().equalsIgnoreCase("Gate") && so.getY() > 9515).interact("Open");
                break;
            case 450:
            case 460:
                if (Players.getLocal().getTargetIndex() == -1) {
                    Npcs.getNearest(n -> n.getName().equals("Giant rat") && n.getTargetIndex() == -1).interact(a -> true);
                }
                break;
            case 470:
                if (Movement.isInteractable(Npcs.getNearest("Combat Instructor"))) {
                    doDefault = true;
                } else {
                    SceneObjects.getNearest(so -> so.getName().equalsIgnoreCase("Gate") && so.getY() > 9515).interact("Open");
                }
                break;
            case 480:
                if (Dialog.canContinue()) {
                    Dialog.processContinue();
                }
                if (!ItemTables.contains(ItemTables.EQUIPMENT, s -> s.equalsIgnoreCase("Shortbow"))) {
                    Inventory.getFirst("Shortbow").interact(a -> true);
                } else if (!ItemTables.contains(ItemTables.EQUIPMENT, s -> s.equalsIgnoreCase("Bronze arrow"))) {
                    Inventory.getFirst("Bronze arrow").interact(a -> true);
                } else if (Players.getLocal().getTargetIndex() == -1) {
                    if (!Combat.getAttackStyle().getName().equals("Rapid")) {
                        Combat.select(1);
                    }
                    Npcs.getNearest(n -> n.getName().equals("Giant rat") && n.getTargetIndex() == -1).interact(a -> true);
                }
                break;
            case 500:
                if (!combatLadderLocation.contains(Players.getLocal())) {
                    Movement.walkTo(combatLadderLocation.getCenter());
                } else {
                    SceneObjects.getNearest("Ladder").interact("Climb-up");
                }
                break;
            case 510:
                doDefault = true;
                break;
            case 520:
                if (!Dialog.isOpen()) {
                    SceneObjects.getNearest("Poll booth").interact("Use");
                } else {
                    if (Dialog.canContinue()) {
                        Dialog.processContinue();
                    }
                }
                break;
            case 525:
                SceneObjects.getNearest(9721).interact("Open");
                break;
            case 530:
                doDefault = true;
                break;
            case 531:
                if (Dialog.canContinue()) {
                    Dialog.processContinue();
                }
                if (!Tabs.isOpen(Tab.ACCOUNT_MANAGEMENT)) {
                    Tabs.open(Tab.ACCOUNT_MANAGEMENT);
                    Time.sleepUntil(() -> Tabs.isOpen(Tab.ACCOUNT_MANAGEMENT), Random.nextInt(2000, 2500));
                }
                break;
            case 532:
                doDefault = true;
                break;
            case 540:
                if (Dialog.canContinue()) {
                    Dialog.processContinue();
                }
                SceneObjects.getNearest(9722).interact("Open");
                break;
            case 550:
                if (Players.getLocal().getY() > 3116) {
                    Movement.walkTo(new Position(3134, 3116));
                } else if (Movement.isInteractable(new Position(3127, 3106))) {
                    doDefault = true;
                } else {
                    SceneObjects.getNearest(so -> so.distance(new Position(3127, 3106)) < 5 && so.containsAction("Open")).interact("Open");
                }
                break;
            case 560:
                if (Dialog.canContinue()) {
                    Dialog.processContinue();
                }
                if (!Tabs.isOpen(Tab.PRAYER)) {
                    Tabs.open(Tab.PRAYER);
                    Time.sleepUntil(() -> Tabs.isOpen(Tab.PRAYER), Random.nextInt(2000, 2500));
                }
                break;
            case 570:
                doDefault = true;
                break;
            case 580:
                if (Dialog.canContinue()) {
                    Dialog.processContinue();
                }
                if (!Tabs.isOpen(Tab.FRIENDS_LIST)) {
                    Tabs.open(Tab.FRIENDS_LIST);
                    Time.sleepUntil(() -> Tabs.isOpen(Tab.FRIENDS_LIST), Random.nextInt(2000, 2500));
                }
                break;
            case 600:
                doDefault = true;
                break;
            case 610:
                if (Dialog.canContinue()) {
                    Dialog.processContinue();
                }
                SceneObjects.getNearest("Door").interact("Open");
                break;
            case 620:
                Npc magicInstructor = Npcs.getNearest("Magic Instructor");

                if (magicInstructor == null) {
                    if (!magicLocation.contains(Players.getLocal())) {
                        Movement.walkTo(magicLocation.getCenter());
                    }
                } else {
                    doDefault = true;
                }
                break;
            case 630:
                if (!Tabs.isOpen(Tab.MAGIC)) {
                    Tabs.open(Tab.MAGIC);
                    Time.sleepUntil(() -> Tabs.isOpen(Tab.MAGIC), Random.nextInt(2000, 2500));
                }
                break;
            case 640:
                doDefault = true;
                break;
            case 650:
                if (Dialog.canContinue()) {
                    Dialog.processContinue();
                }
                if (Game.getClient().isSpellSelected()) {
                    Npcs.getNearest("Chicken").interact(8);
                } else {
                    Interfaces.getComponent(218, 6).interact("Cast");
                }
                break;
            case 670:
                if (!Dialog.isOpen()) {
                    Npcs.getNearest("Magic Instructor").interact("Talk-to");
                } else {
                    if (Dialog.canContinue()) {
                        Dialog.processContinue();
                    } else {
                        if (Dialog.isViewingChatOptions()) {
                            if (Interfaces.getComponent(219, 1, 0).getText().contains("o you want to go to")) {
                                Dialog.process("Yes");
                            } else {
                                Dialog.process("not planning to do that");
                            }
                        }
                    }
                }
                break;
            case 1000:

                if (InterfaceOptions.getViewMode() != InterfaceOptions.ViewMode.FIXED_MODE) {
                    Interfaces.getComponent(261, 33).interact("Fixed Mode");
                }

                // Do final tasks
                if (BefriendedPlayers.getCount() == 0) {
                    // Add mule as friend for muling control
                    addFriend(MULE_NAME);
                    Time.sleepUntil(() -> BefriendedPlayers.getCount() == 1, Random.nextInt(4000, 4500));

                }
                break;
        }

        if (doDefault) {
            if (Dialog.canContinue()) {
                Dialog.processContinue();
            } else if (!Dialog.isProcessing()) {
                switch (Game.getClient().getHintArrowType()) {
                    case 0:
                        Log.info("no hint arrow");
                        break;
                    case 1:
                        Npcs.getAt(Game.getClient().getHintArrowNpcIndex()).interact(defaultAction);
                        break;
                    case 2:
                        Position hintPos = new Position(Game.getClient().getHintArrowX(), Game.getClient().getHintArrowY(), Players.getLocal().getFloorLevel());
                        Log.info(hintPos.toString());
                        for (SceneObject so : SceneObjects.getAt(hintPos)) {
                            if (so.containsAction(defaultAction)) {
                                so.interact(defaultAction);
                                break;
                            }
                        }
                        break;
                }
            }
        }

        return Random.nextInt(500, 1750);
    }

    private void createCharacter() {
        int genderProbability;

        if (Interfaces.isOpen(558)) {
            if (Interfaces.getComponent(558, 18, 9).getTextColor() != 16750623) {
                if (Interfaces.getComponent(558, 11).getText() == "") {
                    if (Interfaces.getComponent(263, 1, 0).isVisible()) {
                        Interfaces.getComponent(558, 7).interact("Look up name");
                    } else {
                        String username = nameGenerator();
                        Keyboard.sendText(username);
                        Time.sleepUntil(() -> Interfaces.getComponent(162, 45).getText().contains(username), Random.nextInt(2000, 3000));
                        Keyboard.pressEnter();
                    }
                } else {
                    Interfaces.getComponent(558, 15).interact("Set name");
                    Time.sleep(Random.nextInt(2000, 2500));
                }
            } else {
                Interfaces.getComponent(558, 18).interact("Set name");
                Time.sleep(Random.nextInt(2000, 2500));
            }
        } else if (Interfaces.isOpen(269)) {
            if (Interfaces.getComponent(269, 97).getText().contains("design your player")) {
                // Select gender
                genderProbability = Random.nextInt(0, 10);
                if (genderProbability > 5) {
                    // Male
                    Interfaces.getComponent(269, 136).click();
                } else {
                    // Female
                    Interfaces.getComponent(269, 137).click();
                }

                // Clothing
                // Select head
                randomDesignSelection(106);

                // Select jaw
                randomDesignSelection(114);

                // Select torso
                randomDesignSelection(115);

                // Select arms
                randomDesignSelection(109);

                // Select hands
                randomDesignSelection(117);

                // Select legs
                randomDesignSelection(111);

                // Select feet
                randomDesignSelection(112);

                // COLORS
                // Color hair
                randomDesignSelection(105);

                // Color torso
                randomDesignSelection(123);

                // Color legs
                randomDesignSelection(129);

                // Color feet
                randomDesignSelection(130);

                // Color skin
                randomDesignSelection(131);

                Log.info("Design done.");
                Interfaces.getComponent(269, 99).click();
                Time.sleep(3000);
            }
        }
    }

    private void randomDesignSelection(int component) {
        for (int i = 0; i < 15; i++) {
            Interfaces.getComponent(269, component).click();
            Time.sleep(20);
            if (i == Random.nextInt(0, 15)) {
                break;
            }
        }
    }

    private void useItemOn(String itemName, Interactable target) {
        if (Inventory.isItemSelected()) {
            if (target.interact("Use")) {
                Time.sleepUntil(() -> Varps.get(V_TUTISLAND) != tutProgress, 30 * 1000);
            }
        } else {
            Inventory.getFirst(itemName).interact("Use");
        }
    }

    public static void addFriend(String name) {
        // Open friendslist if not open
        if (!Tabs.isOpen(Tab.FRIENDS_LIST)) {
            Tabs.open(Tab.FRIENDS_LIST);
            Time.sleepUntil(() -> Tabs.isOpen(Tab.FRIENDS_LIST), Random.nextInt(2000, 2500));
        }

        // Add mule as friend
        Interfaces.getComponent(429, 14).interact("Add Friend");
        Time.sleepUntil(() -> Interfaces.getComponent(162, 45).getText().contains("*"), Random.nextInt(2000, 2500));
        Keyboard.sendText(name);
        Time.sleepUntil(() -> Interfaces.getComponent(162, 45).getText().contains(MULE_NAME), Random.nextInt(2000, 2500));
        Keyboard.pressEnter();
    }

    public String nameGenerator() {

        final String lexicon = "ABCDEFGHIJKLMNOPQRSTUVWXYZ12345674890";
        final java.util.Random rand = new java.util.Random();
        final Set<String> identifiers = new HashSet<String>();

        StringBuilder builder = new StringBuilder();
        while(builder.toString().length() == 0) {
            int length = rand.nextInt(5)+5;
            for(int i = 0; i < length; i++) {
                builder.append(lexicon.charAt(rand.nextInt(lexicon.length())));
            }
            if(identifiers.contains(builder.toString())) {
                builder = new StringBuilder();
            }
        }
        return builder.toString();
    }

    public static void handleICantReachThatScreen() {
        InterfaceComponent iCantReachThat = Interfaces.getComponent(162, 44);
        InterfaceComponent clickToContinue = Interfaces.getComponent(162, 45);
        if (iCantReachThat.isVisible()) {
            if (clickToContinue.isVisible()) {
                Game.getClient().fireScriptEvent(299, 1, 1);
            }
        }
    }

    public static void doDialog() {
        if (Dialog.canContinue()) {
            Dialog.processContinue();
        }
    }

    public static void toggleRun() {
        if (!Movement.isRunEnabled()) {
            if (Movement.getRunEnergy() > 20) {
                Movement.toggleRun(true);
            }
        }
    }

    public static void doBasics() {
        handleICantReachThatScreen();
        doDialog();
        toggleRun();
    }
}
