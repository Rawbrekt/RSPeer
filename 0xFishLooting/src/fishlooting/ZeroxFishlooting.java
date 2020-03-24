package fishlooting;

import fishlooting.tasks.*;
import org.rspeer.RSPeer;
import org.rspeer.runetek.api.ClientSupplier;
import org.rspeer.runetek.api.Varps;
import org.rspeer.runetek.api.movement.position.Area;
import org.rspeer.runetek.event.listeners.ChatMessageListener;
import org.rspeer.runetek.event.listeners.LoginResponseListener;
import org.rspeer.runetek.event.listeners.RenderListener;
import org.rspeer.runetek.event.types.ChatMessageEvent;
import org.rspeer.runetek.event.types.LoginResponseEvent;
import org.rspeer.runetek.event.types.RenderEvent;
import org.rspeer.script.ScriptCategory;
import org.rspeer.script.ScriptMeta;
import org.rspeer.script.task.Task;
import org.rspeer.script.task.TaskScript;
import org.rspeer.ui.Log;

import java.awt.*;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

@ScriptMeta(name = "0xFishLooter", desc = "Tries to loot fish.", category = ScriptCategory.MONEY_MAKING, developer = "0xRip", version = 0.1)

public class ZeroxFishlooting extends TaskScript implements ChatMessageListener, RenderListener, LoginResponseListener {

    public static boolean muled = false;
    public static boolean banked = false;
    public static String MULE_NAME = "71opossum388";
    public static String MULE_WORLD = "World 308";
    public static int MULE_WORLD_INT = 308;
    public static int V_TUTISLAND = 281;
    public static int tutProgress;
    public static String currentTask;
    public static String accountStatus;
    private static Scanner x;
    private static boolean banned = false;
    private static boolean invalidCredentials = false;
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

        if (!banned && !invalidCredentials) {
            String username = ClientSupplier.get().getUsername();
            String status = "new";
            updateStatus(username, status);
        }
    }

    @Override
    public void notify(LoginResponseEvent loginResponseEvent) {
        LoginResponseEvent.Response response = loginResponseEvent.getResponse();
        if (response == LoginResponseEvent.Response.ACCOUNT_DISABLED) {
            String username = ClientSupplier.get().getUsername();
            String status = "banned";
            updateStatus(username, status);
            banned = true;
        }
        if (response == LoginResponseEvent.Response.INVALID_CREDENTIALS) {
            String username = ClientSupplier.get().getUsername();
            String status = "invalid_credentials";
            updateStatus(username, status);
            invalidCredentials = true;
            RSPeer.shutdown();
        }
        if (response == LoginResponseEvent.Response.RUNESCAPE_UPDATE || response == LoginResponseEvent.Response.RUNESCAPE_UPDATE_2) {
            String username = ClientSupplier.get().getUsername();
            String status = "new";
            updateStatus(username, status);
            RSPeer.shutdown();
        }
    }

    @Override
    public void notify(ChatMessageEvent msg) {
        if (msg.getMessage().contains("Accepted trade")) {
            muled = true;
            banked = false;
            Log.info("trade done");
        }

        if (msg.getMessage().contains("has logged o")) {
            muled = false;
            Log.info("mule logged out");
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

    public static void updateStatus(String searchUsername, String newStatus) {

        if (newStatus != accountStatus) {
            String filepath = "C:\\Users\\Riprekt\\Documents\\RSPeer\\account creator\\accounts.csv";
            String username = "";
            String password = "";
            String status = "";
            String tempfile = "temp.txt";
            File oldFile = new File(filepath);
            File newFile = new File(tempfile);


            try {
                FileWriter fw = new FileWriter(tempfile, true);
                BufferedWriter bw = new BufferedWriter(fw);
                PrintWriter pw = new PrintWriter(bw);

                x = new Scanner(new File(filepath));
                x.useDelimiter("[,\n\r]");

                while (x.hasNext()) {
                    username = x.next();
                    password = x.next();
                    status = x.next();

                    if (username.equals(searchUsername)) {
                        pw.print(username + "," + password + "," + newStatus + "\n");
                    } else {
                        pw.print(username + "," + password + "," + status + "\n");
                    }
                }
                x.close();
                pw.flush();
                pw.close();
                oldFile.delete();
                File dump = new File(filepath);
                newFile.renameTo(dump);
                accountStatus = newStatus;
            } catch (Exception e) {
                System.out.println("Error");
            }
        }
    }
}
