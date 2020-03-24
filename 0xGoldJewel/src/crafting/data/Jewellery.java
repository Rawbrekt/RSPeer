package crafting.data;

public enum Jewellery {
    RING(1635,2357,0,1592,5),
    NECKLACE(1654,2357,0,1597,6),
    AMULET(1673,2357,0,1595,8),
    SAPPHIRE_RING(1637,2357,1607,1592,20),
    SAPPHIRE_NECKLACE(1656,2357,1607,1597,22),
    SAPPHIRE_AMULET(1675,2357,1607,1595,24);

    private int finishedProduct;
    private int rawMaterial;
    private int gem;
    private int mould;
    private int level;

    Jewellery(int finishedProduct, int rawMaterial, int gem, int mould, int level) {
        this.finishedProduct = finishedProduct;
        this.rawMaterial = rawMaterial;
        this.gem = gem;
        this.mould = mould;
        this.level = level;
    }

    public int getFinishedProduct() {
        return finishedProduct;
    }

    public int getRawMaterial() {
        return rawMaterial;
    }

    public int getMould() {
        return mould;
    }

    public int getGem() {
        return gem;
    }

    public int getLevel() {
        return level;
    }
}
