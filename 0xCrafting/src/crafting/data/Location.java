package crafting.data;

import org.rspeer.runetek.api.movement.position.Area;

public enum Location {

    EDGEVILLE(Area.rectangular(3098, 3499, 3091, 3488),Area.rectangular(3105, 3501, 3110, 3496));

    Area bankArea;
    Area furnaceArea;

    Location(Area bankArea, Area furnaceArea) {
        this.bankArea = bankArea;
        this.furnaceArea = furnaceArea;
    }

    public static Location getLocation(String location) {
        return Location.valueOf(location);
    }

    public Area getBankArea() {
        return bankArea;
    }

    public void setBankArea(Area bankArea) {
        this.bankArea = bankArea;
    }

    public Area getFurnaceArea() {
        return furnaceArea;
    }

    public void setFurnaceArea(Area furnaceArea) {
        this.furnaceArea = furnaceArea;
    }
}

