import org.rspeer.runetek.adapter.component.Item;
import org.rspeer.runetek.adapter.scene.SceneObject;
import org.rspeer.runetek.api.commons.Time;
import org.rspeer.runetek.api.commons.math.Random;
import org.rspeer.runetek.api.component.Bank;
import org.rspeer.runetek.api.component.Production;
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

        @ScriptMeta(name = "0xPastry", desc = "Tries to make pastry dough.", category = ScriptCategory.MONEY_MAKING, developer = "0xRip", version = 0.1)

        public class Main extends Script implements RenderListener {

            Area GRAND_EXCHANGE = Area.rectangular(3151, 3502, 3181, 3475);
            int BUCKET = 1929;
            int POT = 1933;
            private long anim = 0;
            int DOUGH;
            private long startTime;

            @Override
            public void onStart(){
                startTime = System.currentTimeMillis();
                DOUGH = 0;
            }

            @Override
            public int loop() {
                if (GRAND_EXCHANGE.contains(Players.getLocal())) {

                    if (Inventory.contains(BUCKET) && Inventory.contains(POT)) {
                        if (Production.isOpen()) {
                            if (!Production.getAmount().equals(Production.Amount.ALL)) {
                                Log.info("Setting amount to all");
                                Production.initiate(Production.Amount.ALL);
                            } else {
                                Production.initiate("Pastry dough");
                                Time.sleepUntil(() -> !Inventory.contains(BUCKET), Random.nextInt(15000,20000));
                            }
                        } else {
                            Item bucket = Inventory.getLast(BUCKET);
                            Inventory.use(x -> x.getId() == POT, bucket);
                        }
                    } else {
                        if (Bank.isOpen()) {

                            if (!Bank.contains(BUCKET, POT)) {
                                return -1;
                            }

                            if (!Inventory.contains(BUCKET) || !Inventory.contains(POT)) {
                                if (!Inventory.isEmpty()) {
                                    DOUGH += 9;
                                    Bank.depositAllExcept(BUCKET, POT);
                                    Time.sleepUntil(() -> Inventory.isEmpty(), Random.nextInt(1500, 2500));
                                }

                                if (Inventory.getCount(BUCKET) < 9) {
                                    Bank.withdraw(BUCKET, 9 - Inventory.getCount(BUCKET));
                                    Time.sleepUntil(() -> Inventory.getCount(BUCKET) == 9, Random.nextInt(3000, 6000));
                                }

                                if (Inventory.getCount(POT) < 9) {
                                    Bank.withdraw(POT, 9 - Inventory.getCount(POT));
                                    Time.sleepUntil(() -> Inventory.getCount(POT) == 9, Random.nextInt(3000, 6000));
                                }
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

                return Random.nextInt(600,1200);
            }

            @Override
            public void onStop() {
                long runningTime = System.currentTimeMillis() - startTime;
                Log.info("It took me " + formatTime(runningTime) + " to make " + DOUGH + " pastry dough");
            }

            @Override
            public void notify(RenderEvent renderEvent) {

                Graphics g = renderEvent.getSource();

                long runningTime = System.currentTimeMillis() - startTime;

                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_LCD_HRGB);

                g2d.setColor(Color.WHITE);
                g2d.drawString("Runtime: " + formatTime(runningTime), 20, 20);
                g2d.drawString("Dough: " + DOUGH, 20, 40);
    }

    private String formatTime(long r){


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
