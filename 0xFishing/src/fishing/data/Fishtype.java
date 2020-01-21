package fishing.data;

import org.rspeer.runetek.api.movement.position.Area;
import org.rspeer.ui.Log;

public enum Fishtype {
        SHRIMPS(Area.rectangular(3269, 3173, 3272, 3161),Area.rectangular(3265, 3150, 3278, 3138),303,0,"Small Net",20),
        TROUT(Area.rectangular(3091, 3499, 3098, 3488),Area.rectangular(3102, 3435, 3111, 3422),309,314,"Lure",40),
        LOBSTERS(Area.rectangular(3269, 3173, 3272, 3161),Area.rectangular(3265, 3150, 3278, 3138),303,0,"Small Net",99);

        private Area BANKING_AREA;
        private Area FISHING_AREA;
        private int item;
        private int bait;
        private String method;
        private int goal;

        Fishtype(Area banking_area, Area fishing_area, int item, int bait,String method, int goal) {
                this.BANKING_AREA = banking_area;
                this.FISHING_AREA = fishing_area;
                this.item = item;
                this.bait = bait;
                this.method = method;
                this.goal = goal;
        }

        public static Fishtype getBestFishType(int i) {
                Fishtype temp = null;

                for (Fishtype t: Fishtype.values()) {
                        if (t.getGoal() < i) {
                                temp = t;
                        } else {
                                Log.info(temp);
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

        public Area getFISHING_AREA() {
                return FISHING_AREA;
        }

        public Area getBANKING_AREA() {
                return BANKING_AREA;
        }

        public int getBait() {
                return bait;
        }
}
