package GE.data;

public class GoldJewellery {

    public String JewelleryName;
    public int Profit;

    public GoldJewellery(String JewelleryName, int Profit) {
        this.JewelleryName = JewelleryName;
        this.Profit = Profit;
    }

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

    int ProfitRubyRing = RUBY_RING.getOverallAverage() - GOLD_BAR.getOverallAverage() - RUBY.getOverallAverage();
    int ProfitRubyNecklace = RUBY_NECKLACE.getOverallAverage() - GOLD_BAR.getOverallAverage() - RUBY.getOverallAverage();

    int ProfitEmeraldRing = EMERALD_RING.getOverallAverage() - GOLD_BAR.getOverallAverage() - EMERALD.getOverallAverage();
    int ProfitEmeraldNecklace = EMERALD_NECKLACE.getOverallAverage() - GOLD_BAR.getOverallAverage() - EMERALD.getOverallAverage();

    int ProfitSapphireRing = SAPPHIRE_RING.getOverallAverage() - GOLD_BAR.getOverallAverage() - SAPPHIRE.getOverallAverage();
    int ProfitSapphireNecklace = SAPPHIRE_NECKLACE.getOverallAverage() - GOLD_BAR.getOverallAverage() - SAPPHIRE.getOverallAverage();


    GoldJewellery GJRR = new GoldJewellery("Ruby Ring", ProfitRubyRing);
    GoldJewellery GJRN = new GoldJewellery("Ruby Necklace", ProfitRubyNecklace);

    GoldJewellery GJER = new GoldJewellery("Emerald Ring", ProfitEmeraldRing);
    GoldJewellery GJEN = new GoldJewellery("Emerald Necklace", ProfitEmeraldNecklace);

    GoldJewellery GJSR = new GoldJewellery("Sapphire Ring", ProfitSapphireRing);
    GoldJewellery GJSN = new GoldJewellery("Sapphire Necklace", ProfitSapphireNecklace);

    GoldJewellery arrGJ[] = new GoldJewellery[3];

}
