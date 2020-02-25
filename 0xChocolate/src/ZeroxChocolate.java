import org.rspeer.runetek.adapter.component.Item;
import org.rspeer.runetek.adapter.scene.SceneObject;
import org.rspeer.runetek.api.commons.Time;
import org.rspeer.runetek.api.commons.math.Random;
import org.rspeer.runetek.api.component.Bank;
import org.rspeer.runetek.api.component.tab.Inventory;
import org.rspeer.runetek.api.movement.Movement;
import org.rspeer.runetek.api.movement.position.Area;
import org.rspeer.runetek.api.scene.Players;
import org.rspeer.runetek.api.scene.SceneObjects;
import org.rspeer.runetek.event.listeners.RenderListener;
import org.rspeer.runetek.event.types.RenderEvent;
import org.rspeer.script.Script;
import org.rspeer.script.ScriptCategory;
import org.rspeer.script.ScriptMeta;
import org.rspeer.ui.Log;

import java.awt.*;
import java.util.concurrent.TimeUnit;
import java.util.function.Predicate;

@ScriptMeta(name = "0xChocolate", desc = "Tries to grind chocolate.", category = ScriptCategory.MONEY_MAKING, developer = "0xRip", version = 0.1)

public class ZeroxChocolate extends Script implements RenderListener {

    Area GRAND_EXCHANGE = Area.rectangular(3151, 3502, 3181, 3475);
    int KNIFE = 946;
    int CHOCOLATE = 1973;
    int CHOCOLATE_DUST = 1975;
    int GROUND;
    private long startTime;

    @Override
    public void onStart(){
        startTime = System.currentTimeMillis();
        GROUND = 0;
    }

    @Override
    public int loop() {

        if (GRAND_EXCHANGE.contains(Players.getLocal())) {
            if (Inventory.contains(KNIFE) && Inventory.contains(CHOCOLATE)) {
                Item chocolate = Inventory.getLast(CHOCOLATE);
                Inventory.use(x -> x.getName().equals("Knife"),chocolate);
            } else {
                if (Bank.isOpen()) {

                    if (!Bank.contains(CHOCOLATE)) {
                        return -1;
                    }

                    if (!Inventory.contains(KNIFE)) {
                        Bank.withdraw(KNIFE,1);
                        Time.sleepUntil(() -> Inventory.contains(KNIFE), Random.nextInt(3000,5000));
                    }

                    if (!Inventory.contains(CHOCOLATE)) {

                        GROUND += Inventory.getCount(CHOCOLATE_DUST);

                        Bank.depositAllExcept(KNIFE,CHOCOLATE);
                        Time.sleepUntil(() -> Inventory.containsOnly(KNIFE), Random.nextInt(3000,5000));

                        Bank.withdrawAll(CHOCOLATE);

                        Time.sleepUntil(() -> Inventory.isFull(), Random.nextInt(3000,5000));
                    }
                } else {
                    Predicate<SceneObject> bankPred = b -> b.containsAction("Bank");
                    SceneObject BANK = SceneObjects.getNearest(bankPred);

                    if (!BANK.equals(null)) {
                        BANK.interact("Bank");
                        Time.sleepUntil(() -> Bank.isOpen(), Random.nextInt(3000, 6000));
                    }
                }
            }
        } else {
            Movement.walkToRandomized(GRAND_EXCHANGE.getCenter());
        }

        return 50;
    }

    @Override
    public void onStop() {
        long runningTime = System.currentTimeMillis() - startTime;
        Log.info("It took me " + formatTime(runningTime) + " to grind " + GROUND + " chocolate bars.");
    }

    @Override
    public void notify(RenderEvent renderEvent) {

        Graphics g = renderEvent.getSource();

        long runningTime = System.currentTimeMillis() - startTime;

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

        g2d.setColor(Color.WHITE);
        g2d.drawString("0xChocolate", 20, 257);

        g2d.setColor(Color.WHITE);
        g2d.drawString("Runtime: " + formatTime(runningTime), 20, 300);
        g2d.drawString("Ground: " + GROUND, 20, 323);
        g2d.drawString("Per hour: " + GROUND*3600000/(runningTime+1), 165, 323);
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
}
