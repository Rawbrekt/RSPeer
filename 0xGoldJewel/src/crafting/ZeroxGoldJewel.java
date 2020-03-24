package crafting;

import crafting.data.GUI;
import crafting.data.Jewellery;
import crafting.tasks.Banking;
import crafting.tasks.Craft;
import crafting.tasks.Traverse;
import org.rspeer.runetek.api.movement.position.Area;
import org.rspeer.runetek.event.listeners.RenderListener;
import org.rspeer.runetek.event.listeners.SkillListener;
import org.rspeer.runetek.event.types.RenderEvent;
import org.rspeer.runetek.event.types.SkillEvent;
import org.rspeer.script.ScriptCategory;
import org.rspeer.script.ScriptMeta;
import org.rspeer.script.task.Task;
import org.rspeer.script.task.TaskScript;

import java.awt.*;
import java.util.concurrent.TimeUnit;


@ScriptMeta(name = "0xGoldJewel", desc = "Crafts gold jewelry.", category = ScriptCategory.CRAFTING, developer = "0xRip", version = 0.1)

public class ZeroxGoldJewel extends TaskScript implements RenderListener, SkillListener {

    public static Jewellery toCraft;
    private long startTime;
    public static String currentTask;
    public long amountCrafted = 0;
    public static Area bankArea = Area.rectangular(3091, 3498, 3098, 3488);
    public static Area furnaceArea = Area.rectangular(3105, 3501, 3110, 3496);

    private static final Task[] TASKS = {new Banking(), new Traverse(), new Craft()};


    @Override
    public void onStart() {
        startTime = System.currentTimeMillis();
        new GUI().setVisible(true);
        submit(TASKS);
    }

    @Override
    public void notify(RenderEvent renderEvent) {

        Graphics g = renderEvent.getSource();
        long runningTime = System.currentTimeMillis() - startTime;
        Graphics2D g2d = (Graphics2D) g;

        g2d.setColor(Color.WHITE);
        g2d.drawString("Runtime: " + formatTime(runningTime), 20, 40);
        g2d.drawString("Crafted: " + amountCrafted, 20, 60);
        g2d.drawString("Per hour: " + amountCrafted*3600000/(runningTime+1), 20, 80);
        g2d.drawString("Task: " + currentTask, 20, 100);
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
    public void notify(SkillEvent skillEvent) {
        if (skillEvent.getType() == 0) {
            amountCrafted += 1;
        }
    }
}
