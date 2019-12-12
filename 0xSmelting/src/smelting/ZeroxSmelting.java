package smelting;

import org.rspeer.runetek.event.listeners.RenderListener;
import org.rspeer.runetek.event.listeners.SkillListener;
import org.rspeer.runetek.event.types.RenderEvent;
import org.rspeer.runetek.event.types.SkillEvent;
import org.rspeer.script.ScriptCategory;
import org.rspeer.script.ScriptMeta;
import org.rspeer.script.task.Task;
import org.rspeer.script.task.TaskScript;
import org.rspeer.ui.Log;
import smelting.data.Item;
import smelting.data.Location;
import smelting.tasks.Banking;
import smelting.tasks.Smelt;
import smelting.tasks.Traverse;

import java.awt.*;
import java.util.concurrent.TimeUnit;

@ScriptMeta(name = "0xSmelting", desc = "Tries to smelt.", category = ScriptCategory.SMITHING, developer = "0xRip", version = 0.1)

public class ZeroxSmelting extends TaskScript implements RenderListener, SkillListener {


    private static final Task[] TASKS = {new Banking(), new Traverse(), new Smelt()};
    public static Item toSmelt = Item.getItem("BRONZE_BAR");
    public static Location location = Location.getLocation("EDGEVILLE");

    private long startTime;
    public int amountSmelted = 0;


    @Override
    public void onStart() {
        startTime = System.currentTimeMillis();
        submit(TASKS);
    }

    @Override
    public void notify(RenderEvent renderEvent) {

        Graphics g = renderEvent.getSource();

        long runningTime = System.currentTimeMillis() - startTime;
        //int berriesPerHour = (int) (Settings.picked / ((System.currentTimeMillis() - startTime) / 3600000.0D));

        Color transparentBlack = new Color(0,0,0, 100);
        Color translucentWhite = new Color(255, 255, 255, 150);

        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_LCD_HRGB);

        g2d.setColor(transparentBlack);
        g2d.fillRoundRect(5, 275, 365, 60, 15, 15);
        g2d.setColor(translucentWhite);
        g2d.drawRoundRect(5, 275, 366, 61, 15, 15);

        g2d.setColor(transparentBlack);
        g2d.fillRoundRect(10, 240, 130, 23, 15, 15);
        g2d.setColor(translucentWhite);
        g2d.drawRoundRect(10, 240, 131, 24, 15, 15);

        //Draw Paint Text
        g2d.setColor(Color.WHITE);
        g2d.drawString("0xSmelting", 20, 257);

        g2d.setColor(Color.WHITE);
        g2d.drawString("Runtime: " + formatTime(runningTime), 20, 300);
        g2d.drawString("Smelted: " + amountSmelted, 20, 323);
        g2d.drawString("Smelted: " + amountSmelted, 20, 323);
        g2d.drawString("Per hour: " + ((amountSmelted / runningTime)*3600000), 165, 323);
    }

    private String formatTime(long r){

        //long days = TimeUnit.MILLISECONDS.toDays(r);
        long hours = TimeUnit.MILLISECONDS.toHours(r);
        long minutes = TimeUnit.MILLISECONDS.toMinutes(r) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(r));
        long seconds = TimeUnit.MILLISECONDS.toSeconds(r) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(r));
        String res = "";

        //Pretty Print the time so it will always be in this format 00:00:00
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
            amountSmelted += 1;
        }
    }
}
