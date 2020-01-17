package combat.data;

import org.rspeer.runetek.api.movement.position.Area;
import org.rspeer.runetek.api.movement.position.Position;

public enum Target {
    LUMBRIDGE_CHICKENS(Area.polygonal(
            new Position[] {
                    new Position(3225, 3301, 0),
                    new Position(3225, 3295, 0),
                    new Position(3231, 3295, 0),
                    new Position(3231, 3287, 0),
                    new Position(3237, 3287, 0),
                    new Position(3237, 3290, 0),
                    new Position(3238, 3291, 0),
                    new Position(3238, 3293, 0),
                    new Position(3237, 3294, 0),
                    new Position(3237, 3297, 0),
                    new Position(3238, 3298, 0),
                    new Position(3238, 3300, 0),
                    new Position(3236, 3302, 0),
                    new Position(3226, 3302, 0)
            }
    ), "Chicken","Raw chicken","Cooked chicken",10),
    LUMBRIDGE_COWS(Area.polygonal(
            new Position[] {
                    new Position(3241, 3297, 0),
                    new Position(3265, 3297, 0),
                    new Position(3265, 3255, 0),
                    new Position(3253, 3255, 0),
                    new Position(3253, 3272, 0),
                    new Position(3251, 3274, 0),
                    new Position(3251, 3276, 0),
                    new Position(3249, 3278, 0),
                    new Position(3246, 3278, 0),
                    new Position(3244, 3280, 0),
                    new Position(3244, 3281, 0),
                    new Position(3240, 3285, 0),
                    new Position(3240, 3286, 0),
                    new Position(3241, 3288, 0),
                    new Position(3242, 3290, 0),
                    new Position(3242, 3293, 0),
                    new Position(3240, 3297, 0)
            }
    ), "Cow","Raw beef","Cooked meat", 30),
    LUMBRIDGE_FROGS(Area.rectangular(3187, 3182, 3208, 3165), "Giant frog","","", 50);


    private Area area;
    private String name;
    private String rawmeat;
    private String cookedmeat;
    private int level;

    Target(Area location, String name, String rawmeat,String cookedmeat, int level) {
        this.area = location;
        this.name =  name;
        this.rawmeat = rawmeat;
        this.cookedmeat = cookedmeat;
        this.level = level;
    }

    public static Target getBestTarget(int i) {

        Target temp = null;

        for (Target t : Target.values()) {
            if (t.getLevel() <= i) {
                temp = t;
            } else {
                return temp;
            }
        }

        return temp;
    }


    public Area getArea(){
        return area;
    }

    public String getName() {
        return name;
    }

    public int getLevel() {
        return level;
    }

    public void setArea(Area area) {
        this.area = area;
    }

    public void setName(String mob) {
        this.name = name;
    }

    public void setLevel(int level) {
        this.level = level;
    }


    public String getRawMeat() {
        return rawmeat;
    }

    public void setRawMeat(String rawmeat) {
        this.rawmeat = rawmeat;
    }

    public String getCookedmeat() {
        return cookedmeat;
    }

    public void setCookedMeat(String cookedmeat) {
        this.cookedmeat = cookedmeat;
    }
}
