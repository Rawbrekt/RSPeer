package fishing.data;

import org.rspeer.runetek.api.movement.position.Area;

public enum Fishtype {
    SHRIMPS(Area.rectangular(3210, 3215, 3207, 3220, 2), Area.rectangular(3210, 3215, 3207, 3220, 2), Area.rectangular(3237, 3163, 3250, 3145), 303, 0, "Net", false, 0),
    TROUT(Area.rectangular(3091, 3499, 3098, 3488),Area.rectangular(3091, 3499, 3098, 3488), Area.rectangular(3100, 3435, 3114, 3422), 309, 314, "Lure", false, 20),
    LOBSTERS(Area.rectangular(3038, 3238, 3052, 3232), Area.rectangular(3094, 3246, 3092, 3240), Area.rectangular(2927, 3181, 2917, 3174), 301, 995, "Cage", false, 40);

    private Area bankingArea;
    private Area altBankingArea;
    private Area fishingArea;
    private int item;
    private int bait;
    private String method;
    private boolean powerfish;
    private int goal;

    Fishtype(Area bankingArea, Area altBankingArea, Area fishingArea, int item, int bait, String method, boolean powerfish, int goal) {
        this.bankingArea = bankingArea;
        this.altBankingArea = altBankingArea;
        this.fishingArea = fishingArea;
        this.item = item;
        this.bait = bait;
        this.method = method;
        this.powerfish = powerfish;
        this.goal = goal;
    }

    public static Fishtype getBestFishType(int i) {
        Fishtype temp = null;
        for (Fishtype f : Fishtype.values()) {
            if (f.getGoal() <= i) {
                temp = f;
            } else {
                return temp;
            }
        }
        return temp;
    }

    public int getGoal() {
        return goal;
    }

    public String getMethod() {
        return method;
    }

    public int getItem() {
        return item;
    }

    public Area getFishingArea() {
        return fishingArea;
    }

    public Area getBankingArea() {
        return bankingArea;
    }

    public int getBait() {
        return bait;
    }

    public Area getAltBankingArea() {
        return altBankingArea;
    }

    public boolean isPowerfish() {
        return powerfish;
    }
}
