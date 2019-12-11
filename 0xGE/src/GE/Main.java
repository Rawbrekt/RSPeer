package GE;

import GE.data.PriceCheck;
import org.rspeer.script.Script;
import org.rspeer.script.ScriptCategory;
import org.rspeer.script.ScriptMeta;
import org.rspeer.ui.Log;

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


    @Override
    public void onStart() {
        Log.info("Ruby ring: " + ProfitRubyRing);
        Log.info("Ruby necklace: " + ProfitRubyNecklace);
        Log.info("Emerald ring: " + ProfitEmeraldRing);
        Log.info("Emerald necklace: " + ProfitEmeraldNecklace);
        Log.info("Sapphire ring: " + ProfitSapphireRing);
        Log.info("Sapphire necklace: " +ProfitSapphireNecklace);
    }

    @Override
    public int loop() {
        return 0;
    }


    private int findMax(int... vals){
        int max = 0;

        for (int i : vals) {
            if (i > max) {
                max = i;
            }
        }
        return max;
    }
}
