package fishing.tasks;

import org.rspeer.runetek.adapter.scene.Player;
import org.rspeer.runetek.api.Game;
import org.rspeer.runetek.api.Worlds;
import org.rspeer.runetek.api.commons.StopWatch;
import org.rspeer.runetek.api.commons.Time;
import org.rspeer.runetek.api.component.Dialog;
import org.rspeer.runetek.api.component.Trade;
import org.rspeer.runetek.api.component.WorldHopper;
import org.rspeer.runetek.api.component.tab.Inventory;
import org.rspeer.runetek.api.movement.Movement;
import org.rspeer.runetek.api.movement.position.Position;
import org.rspeer.runetek.api.scene.Players;
import org.rspeer.script.task.Task;
import org.rspeer.ui.Log;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

public class GetStartingGold extends Task {

    public static final String BRONZE_AXE = "Bronze axe";
    public static final String TINDERBOX = "Tinderbox";
    public static final String SMALL_FISHING_NET = "Small fishing net";
    public static final String SHRIMPS = "Shrimps";
    public static final String BRONZE_PICKAXE = "Bronze pickaxe";
    public static final String BRONZE_DAGGER = "Bronze dagger";
    public static final String BRONZE_SWORD = "Bronze sword";
    public static final String WOODEN_SHIELD = "Wooden shield";
    public static final String SHORTBOW = "Shortbow";
    public static final String BRONZE_ARROW = "Bronze arrow";
    public static final String AIR_RUNE = "Air rune";
    public static final String MIND_RUNE = "Mind rune";
    public static final String BUCKET = "Bucket";
    public static final String POT = "Pot";
    public static final String BREAD = "Bread";
    public static final String WATER_RUNE = "Water rune";
    public static final String EARTH_RUNE = "Earth rune";
    public static final String BODY_RUNE = "Body rune";
    public static final String COINS = "Coins";

    public static final String[] TUTORIAL_ISLAND_ITEMS = {
            BRONZE_AXE,
            TINDERBOX,
            SMALL_FISHING_NET,
            SHRIMPS,
            BRONZE_PICKAXE,
            BRONZE_DAGGER,
            BRONZE_SWORD,
            WOODEN_SHIELD,
            SHORTBOW,
            BRONZE_ARROW,
            AIR_RUNE,
            MIND_RUNE,
            BUCKET,
            POT,
            BREAD,
            WATER_RUNE,
            EARTH_RUNE,
            BODY_RUNE
    };

    @Override
    public boolean validate() {
        return hasStartingItems();
    }

    @Override
    public int execute() {

        try {
            sendMessage("Trade:" + Players.getLocal().getName() + ":" + Worlds.getCurrent());
        } catch (IOException e) {
            e.printStackTrace();
        }

        doMule("2147 Emblems", 308, new Position(3234, 3226));

        return 600;
    }

    public static boolean hasStartingItems() {
        return Inventory.containsAll(TUTORIAL_ISLAND_ITEMS) && !Inventory.contains(COINS);
    }

    public static void sendMessage(String MessageToSend) throws IOException {
        //create a socket to be able to communicate with the socket server of the mule
        Socket s = new Socket("localhost", 9876);
        Log.info("Socket created");

        //send a message from the socket to the socket server of the mule
        PrintWriter pr = new PrintWriter(s.getOutputStream());
        pr.println(MessageToSend);
        pr.flush();
    }

    public static void doMule(String muleName, int muleWorld, Position mulePosition) {

        if (Dialog.canContinue()) {
            Dialog.processContinue();
        }

        if (mulePosition.distance() <= 5) {
            if (Worlds.getCurrent() == muleWorld) {
                Player mule = Players.getNearest(muleName);
                if (mule != null) {
                    if (!Trade.isOpen()) {
                        Log.info("I am offering the mule to trade");
                        if (mule.interact("Trade with")) {
                            Time.sleepUntil(Trade::isOpen, 3_000);
                        }
                    }
                    if (Trade.isOpen(false)) {
                        Log.info("The first trade screen is open");
                        if (Trade.hasOtherAccepted()) {
                            Log.info("I am accepting first trade screen");
                            if (Trade.accept()) {
                            }
                        }
                    }
                    if (Trade.isOpen(true)) {
                        Log.info("The second trade screen is open");
                        if (Trade.hasOtherAccepted()) {
                            Log.info("I am accepting second trade screen");
                            if (Trade.accept()) {
                                Time.sleep(5_000);
                            }
                        }
                    }
                }
                if (mule == null) {
                    Log.info("I can't find the mule, I'll wait for him");
                    Time.sleepUntil(() -> Players.getNearest(muleName) != null, 3_000);
                }
            }
            if (Worlds.getCurrent() != muleWorld) {
                Log.info("I am hopping to the mule world");
                if (WorldHopper.hopTo(muleWorld)) {
                    Time.sleepUntil(() -> !Game.isLoggedIn(), 10_000);
                    Time.sleepUntil(Game::isLoggedIn, 10_000);
                }
            }
        }
        if (mulePosition.distance() > 5) {
            Log.info("I am walking to the mule");
            Movement.walkTo(mulePosition);
        }
    }


}
