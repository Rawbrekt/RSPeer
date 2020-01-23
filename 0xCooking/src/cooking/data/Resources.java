package cooking.data;

import org.rspeer.runetek.api.movement.position.Area;

public enum Resources {
    SHRIMP(Area.rectangular(3269, 3173, 3272, 3161),Area.rectangular(3271, 3183, 3275, 3179),317),
    ANCHOVIES(Area.rectangular(3269, 3173, 3272, 3161),Area.rectangular(3271, 3183, 3275, 3179),321);

    private Area BANKING_AREA;
    private Area COOKING_AREA;
    private int item;

    Resources(Area BANKING_AREA, Area COOKING_AREA, int item) {
        this.BANKING_AREA = BANKING_AREA;
        this.COOKING_AREA = COOKING_AREA;
        this.item = item;
    }

    public Area getBANKING_AREA() {
        return BANKING_AREA;
    }

    public Area getCOOKING_AREA() {
        return COOKING_AREA;
    }

    public int getItem() {
        return item;
    }

}
