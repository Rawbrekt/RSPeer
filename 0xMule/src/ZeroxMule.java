import org.rspeer.runetek.adapter.component.Item;
import org.rspeer.runetek.adapter.scene.Player;
import org.rspeer.runetek.api.commons.math.Random;
import org.rspeer.runetek.api.component.Trade;
import org.rspeer.runetek.api.scene.Players;
import org.rspeer.runetek.event.listeners.ChatMessageListener;
import org.rspeer.runetek.event.types.ChatMessageEvent;
import org.rspeer.runetek.event.types.ChatMessageType;
import org.rspeer.script.Script;
import org.rspeer.script.ScriptCategory;
import org.rspeer.script.ScriptMeta;
import org.rspeer.ui.Log;

@ScriptMeta(name = "0xMule", desc = "Tries to mule", category = ScriptCategory.MONEY_MAKING, developer = "0xRip", version = 0.1)

public class ZeroxMule extends Script implements ChatMessageListener {

    private String traderName = "";
    private int tradesFinished = 0;
    private String items = "";

    @Override
    public int loop() {

        if (tradesFinished == 2 ) {
            return -1;
        }

        if (Trade.isOpen()) {
            if (Trade.isOpen(true) && Trade.isWaitingForMe()) {
                Trade.accept();
            } else {
                if (Trade.hasOtherAccepted()) {

                    Item[] tradeItems = Trade.getTheirItems();
                    for (Item item : tradeItems) {
                        items += item.getStackSize() + " " + item.getName() + " ";
                    }
                    Log.info(traderName + " traded me: " + items);

                    Trade.accept();
                }
            }
        } else {
            Player trader = Players.getNearest(traderName);
            if (trader != null) {
                Players.getNearest(traderName).interact("Trade with");
            }
        }
        return Random.nextInt(1500,2500);
    }

    @Override
    public void notify(ChatMessageEvent msg) {
        if (!Trade.isOpen() && msg.getType().equals(ChatMessageType.TRADE)) {
            traderName = msg.getSource();
        }

        if(msg.getMessage().contains("Accepted trade")) {
            Log.info("trade done");
            tradesFinished += 1;
            traderName = "";
            items = "";
        }

    }
}