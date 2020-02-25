package crafting.data;

public enum Jewellery {
    RING(1635,2357,1592,5),
    NECKLACE(1654,2357,1597,6),
    AMULET(1673,2357,1595,8);

    private int finishedProduct;
    private int rawMaterial;
    private int mould;
    private int level;

    Jewellery(int finishedProduct, int rawMaterial, int mould, int level) {
        this.finishedProduct = finishedProduct;
        this.rawMaterial = rawMaterial;
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

    public int getLevel() {
        return level;
    }
}
