package GE;

import GE.data.PriceCheck;
import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;
import org.json.JSONObject;
import org.rspeer.runetek.api.component.tab.Skill;
import org.rspeer.runetek.api.component.tab.Skills;
import org.rspeer.runetek.api.scene.Players;
import org.rspeer.script.Script;
import org.rspeer.script.ScriptCategory;
import org.rspeer.script.ScriptMeta;

import java.net.URISyntaxException;

@ScriptMeta(name = "0xGE", desc = "Gets GE prices.", category = ScriptCategory.OTHER, developer = "0xRip", version = 0.1)

public class Main extends Script {

    PriceCheck.ItemPrice GOLD_BAR = PriceCheck.getPrice(2357);
    PriceCheck.ItemPrice RUBY = PriceCheck.getPrice(1603);
    PriceCheck.ItemPrice EMERALD = PriceCheck.getPrice(1605);
    PriceCheck.ItemPrice SAPPHIRE = PriceCheck.getPrice(1607);

    PriceCheck.ItemPrice RUBY_RING = PriceCheck.getPrice(1641);
    PriceCheck.ItemPrice RUBY_NECKLACE = PriceCheck.getPrice(1660);

    PriceCheck.ItemPrice EMERALD_RING = PriceCheck.getPrice(1639);
    PriceCheck.ItemPrice EMERALD_NECKLACE = PriceCheck.getPrice(1658);

    PriceCheck.ItemPrice SAPPHIRE_RING = PriceCheck.getPrice(1637);
    PriceCheck.ItemPrice SAPPHIRE_NECKLACE = PriceCheck.getPrice(1656);

    int CostRuby = GOLD_BAR.getOverallAverage() + RUBY.getOverallAverage();
    int CostEmerald = GOLD_BAR.getOverallAverage() + EMERALD.getOverallAverage();
    int CostSapphire = GOLD_BAR.getOverallAverage() + SAPPHIRE.getOverallAverage();

    int ProfitRubyRing = RUBY_RING.getOverallAverage() - CostRuby;
    int ProfitRubyNecklace = RUBY_NECKLACE.getOverallAverage() - CostRuby;

    int ProfitEmeraldRing = EMERALD_RING.getOverallAverage() - CostEmerald;
    int ProfitEmeraldNecklace = EMERALD_NECKLACE.getOverallAverage() - CostEmerald;

    int ProfitSapphireRing = SAPPHIRE_RING.getOverallAverage() - CostSapphire;
    int ProfitSapphireNecklace = SAPPHIRE_NECKLACE.getOverallAverage() - CostSapphire;

    public Socket socket;


    @Override
    public void onStart() {
        /*
        Log.info("Ruby ring: " + ProfitRubyRing);
        Log.info("Ruby necklace: " + ProfitRubyNecklace);
        Log.info("Emerald ring: " + ProfitEmeraldRing);
        Log.info("Emerald necklace: " + ProfitEmeraldNecklace);
        Log.info("Sapphire ring: " + ProfitSapphireRing);
        Log.info("Sapphire necklace: " +ProfitSapphireNecklace);
        */

        try {
            socket = IO.socket("http://localhost:3000");
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

        socket.on(Socket.EVENT_CONNECT, new Emitter.Listener() {

            @Override
            public void call(Object... args) {

            }

        }).on("event", new Emitter.Listener() {

            @Override
            public void call(Object... args) {}

        }).on(Socket.EVENT_DISCONNECT, new Emitter.Listener() {

            @Override
            public void call(Object... args) {}

        });
        socket.connect();

    }

    @Override
    public int loop() {

        //getting user data
        String name = Players.getLocal().getName();
        int strLvl = Skills.getCurrentLevel(Skill.STRENGTH);

        //populating json
        JSONObject obj = new JSONObject();
        obj.put("strength",strLvl);
        obj.put("name", name);

        //sending json
        socket.emit("accupdate",obj);

        return 6000;
    }

    @Override
    public void onStop() {
        socket.close();

    }
}
