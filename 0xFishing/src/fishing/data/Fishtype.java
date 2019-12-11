package fishing.data;

import org.rspeer.runetek.api.movement.position.Area;

public enum Fishtype {
        AL_KHARID_SOUTH(Area.rectangular(3278, 3140, 3267, 3151, 0),Area.rectangular(3269, 3164, 3272, 3170, 0),"Small fishing net","Small Net",20);

        private Area BANKING_AREA;
        private Area FISHING_AREA;
        private String item;
        private String method;
        private int goal;

        Fishtype(Area banking_area, Area fishing_area, String item, String method, int goal) {
                this.BANKING_AREA = banking_area;
                this. FISHING_AREA = fishing_area;
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

        public String getItem() {
                return item;
        }

        public void setItem(String item) {
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
