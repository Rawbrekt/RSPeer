package woodcutting.data;

import org.rspeer.runetek.api.movement.position.Area;

public enum Trees {
    LUMBRDIGE(Area.rectangular(3200, 3231, 3160, 3207),null,"Tree",15);

    private Area TreeArea;
    private Area BankArea;
    private String Treename;
    private int Level;

    Trees(Area TreeArea,Area BankArea, String TreeName, int Level) {
    }
}
