package fishing.data;

import org.rspeer.runetek.api.movement.position.Area;
import org.rspeer.ui.Log;

public enum Fishtype {
        SHRIMPS(Area.rectangular(3269, 3173, 3272, 3161),Area.rectangular(3265, 3150, 3278, 3138),303,"Small Net",20),
        TROUT(Area.rectangular(3269, 3173, 3272, 3161),Area.rectangular(3265, 3150, 3278, 3138),303,"Small Net",40),
        LOBSTERS(Area.rectangular(3269, 3173, 3272, 3161),Area.rectangular(3265, 3150, 3278, 3138),303,"Small Net",99);

        private Area BANKING_AREA;
        private Area FISHING_AREA;
        private int item;
        private String method;
        private int goal;

        Fishtype(Area banking_area, Area fishing_area, int item, String method, int goal) {
                this.BANKING_AREA = banking_area;
                this.FISHING_AREA = fishing_area;
                this.item = item;
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

        public void setGoal(int goal) {
                this.goal = goal;
        }

        public String getMethod() {
                return method;
        }

        public void setMethod(String method) {
                this.method = method;
        }

        public int getItem() {
                return item;
        }

        public void setItem(int item) {
                this.item = item;
        }

        public Area getFISHING_AREA() {
                return FISHING_AREA;
        }

        public void setFISHING_AREA(Area FISHING_AREA) {
                this.FISHING_AREA = FISHING_AREA;
        }

        public Area getBANKING_AREA() {
                return BANKING_AREA;
        }

        public void setBANKING_AREA(Area BANKING_AREA) {
                this.BANKING_AREA = BANKING_AREA;
        }
}
