package fishlooting;

import fishlooting.tasks.*;
import org.rspeer.runetek.api.Varps;
import org.rspeer.runetek.api.movement.position.Area;
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

@ScriptMeta(name = "0xFishLooter", desc = "Tries to loot fish.", category = ScriptCategory.MONEY_MAKING, developer = "0xRip", version = 0.1)

public class ZeroxFishlooting extends TaskScript implements ChatMessageListener, RenderListener {

    public static boolean muled = false;
    public static boolean banked = false;
    public static String MULE_NAME = "71opossum388";
    public static String MULE_WORLD = "World 308";
    public static int MULE_WORLD_INT = 308;
    public static int V_TUTISLAND = 281;
    public static int tutProgress;
    public static String currentTask;

    private long startTime;
    public static long lastFish;

    public static Area bankArea = Area.rectangular(3091, 3498, 3098, 3488);
    public static Area lootArea = Area.rectangular(3100, 3439, 3109, 3423);

    private static final Task[] TASKS = {new TutIsland(), new Mule(), new Banking(), new Traverse(), new Collect()};


    @Override
    public void onStart() {

        startTime = System.currentTimeMillis();
        tutProgress = Varps.get(V_TUTISLAND);
        submit(TASKS);

    }

    @Override
    public void onStop() {
        long runningTime = System.currentTimeMillis() - startTime;
        Log.info(formatTime(runningTime));
    }

    @Override
    public void notify(ChatMessageEvent msg) {
        if(msg.getMessage().contains("Accepted trade")) {
            muled = true;
            banked = false;
            Log.info("trade done");
        }

        if (msg.getMessage().contains("has logged o")) {
            muled = false;
            Log.info("mule logged out");
        }

    }

    private String formatTime(long r){

        //long days = TimeUnit.MILLISECONDS.toDays(r);
        long hours = TimeUnit.MILLISECONDS.toHours(r);
        long minutes = TimeUnit.MILLISECONDS.toMinutes(r) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(r));
        long seconds = TimeUnit.MILLISECONDS.toSeconds(r) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(r));
        String res = "";

        if( hours < 10 ){
            res = res + "0" + hours + ":";
        }
        else{
            res = res + hours + ":";
        }
        if(minutes < 10){
            res = res + "0" + minutes + ":";
        }
        else{
            res = res + minutes + ":";
        }
        if(seconds < 10){
            res = res + "0" + seconds;
        }
        else{
            res = res + seconds;
        }

        return res;
    }

    @Override
    public void notify(RenderEvent renderEvent) {

        Graphics g = renderEvent.getSource();
        long runningTime = System.currentTimeMillis() - startTime;
        long fishTime = System.currentTimeMillis() - lastFish;
        Graphics2D g2d = (Graphics2D) g;
        g2d.setColor(Color.WHITE);

        g2d.drawString("Runtime: " + formatTime(runningTime), 20, 40);
        g2d.drawString("Time since last fish: " + formatTime(fishTime), 20, 60);
        g2d.drawString("Task: " + currentTask, 20, 80);
    }
}
