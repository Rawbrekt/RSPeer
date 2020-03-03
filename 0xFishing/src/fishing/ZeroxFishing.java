package fishing;

import fishing.data.Fishtype;
import fishing.tasks.*;
import org.rspeer.runetek.api.Varps;
import org.rspeer.runetek.api.commons.StopWatch;
import org.rspeer.runetek.api.component.tab.Skill;
import org.rspeer.runetek.api.component.tab.Skills;
import org.rspeer.runetek.event.listeners.ChatMessageListener;
import org.rspeer.runetek.event.listeners.RenderListener;
import org.rspeer.runetek.event.types.ChatMessageEvent;
import org.rspeer.runetek.event.types.RenderEvent;
import org.rspeer.script.ScriptCategory;
import org.rspeer.script.ScriptMeta;
import org.rspeer.script.task.Task;
import org.rspeer.script.task.TaskScript;
import org.rspeer.ui.Log;

import java.awt.*;
import java.util.concurrent.TimeUnit;

@ScriptMeta(name = "0xFishing50", desc = "Tries to fish to 50.", category = ScriptCategory.FISHING, developer = "0xRip", version = 0.1)

public class ZeroxFishing extends TaskScript implements ChatMessageListener, RenderListener {

    public static boolean muled = false;
    public static boolean banked = false;
    public static String MULE_NAME = "2147 Emblems";
    public static String MULE_WORLD = "World 308";
    public static int MULE_WORLD_INT = 308;
    public static final int V_TUTISLAND = 281;
    public static final int V_COOKS_ASSISTANT = 29;
    public static final int V_SHEEP_SHEARER = 179;
    public static final int V_ROMEO_AND_JULLIET = 144;
    public static int tutProgress;
    public static int cookProgress;
    public static int sheepProgress;
    public static int romeoProgress;
    private long startTime;
    StopWatch stopWatch;


    public static Fishtype fishtype = Fishtype.SHRIMPS;

    private static final Task[] TASKS = {new TutIsland(), new GetStartingGold(), new Quests(), new Mule(), new Banking(), new Traverse(), new Fish()};

    @Override
    public void onStart() {

        int fishlvl = Skills.getCurrentLevel(Skill.FISHING);
        fishtype = Fishtype.getBestFishType(fishlvl);

        startTime = System.currentTimeMillis();
        stopWatch = StopWatch.start();

        tutProgress = Varps.get(V_TUTISLAND);
        cookProgress = Varps.get(V_COOKS_ASSISTANT);
        sheepProgress = Varps.get(V_SHEEP_SHEARER);
        romeoProgress = Varps.get(V_ROMEO_AND_JULLIET);

        submit(TASKS);

    }

    @Override
    public void onStop() {
        Log.info(Skills.getCurrentLevel(Skill.FISHING));
        long runningTime = System.currentTimeMillis() - startTime;
        Log.info(formatTime(runningTime));
    }

    @Override
    public void notify(ChatMessageEvent msg) {
        if (msg.getMessage().contains("Accepted trade")) {
            muled = true;
            Log.info("trade done");
            this.setStopping(true);
        }

        if (msg.getMessage().contains("has logged o")) {
            muled = false;
            Log.info("mule logged out");
        }
    }

    public static boolean questsFinished() {
        if (cookProgress == 2 && sheepProgress == 21 && romeoProgress == 100) {
            return true;
        } else {
            return false;
        }
    }

    private String formatTime(long r) {

        //long days = TimeUnit.MILLISECONDS.toDays(r);
        long hours = TimeUnit.MILLISECONDS.toHours(r);
        long minutes = TimeUnit.MILLISECONDS.toMinutes(r) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(r));
        long seconds = TimeUnit.MILLISECONDS.toSeconds(r) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(r));
        String res = "";

        if (hours < 10) {
            res = res + "0" + hours + ":";
        } else {
            res = res + hours + ":";
        }
        if (minutes < 10) {
            res = res + "0" + minutes + ":";
        } else {
            res = res + minutes + ":";
        }
        if (seconds < 10) {
            res = res + "0" + seconds;
        } else {
            res = res + seconds;
        }

        return res;
    }

    @Override
    public void notify(RenderEvent e) {
        //runtime in seconds.
        long seconds = stopWatch.getElapsed().getSeconds();
        if (seconds != 0) {
            long amountOfLobsCaught = (Skills.getExperience(Skill.FISHING) - 37250) / 90;
            long fishingLevel = Skills.getLevel(Skill.FISHING);
            long lobsterPrice = 123;
            long profit = amountOfLobsCaught * lobsterPrice;
            int lobsPerHour = (int) (amountOfLobsCaught * (3600 / seconds));
            int profitPerHour = (int) (lobsPerHour * lobsterPrice);
            //Initialize our graphics
            Graphics g = e.getSource();
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            int y = 35;
            int x = 10;
            g2.setColor(Color.white);
            g2.drawString("Time running: " + stopWatch.toElapsedString(), x, y);
            g2.drawString("Fishing level: " + String.valueOf(fishingLevel), x, y + 20);
            g2.drawString("Lobs caught: " + String.valueOf(amountOfLobsCaught), x, y + 40);
            g2.drawString("Profit: " + String.valueOf(profit), x, y + 60);
            g2.drawString("Lobs per hour: " + String.valueOf(lobsPerHour), x, y + 80);
            g2.drawString("Profit per hour: " + String.valueOf(profitPerHour), x, y + 100);
        }
    }
}
