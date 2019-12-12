package smelting.data;

public enum Item {

    BRONZE_BAR(436, 438, 2349, 14, 14);

    int Mat1;
    int Mat2;
    int FinishedProduct;
    int Amount1;
    int Amount2;


    Item(int Mat1, int Mat2, int FinishedProduct, int Amount1, int Amount2) {
        this.Mat1 = Mat1;
        this.Mat2 = Mat2;
        this.FinishedProduct = FinishedProduct;
        this.Amount1 = Amount1;
        this.Amount2 = Amount2;
    }

    public static Item getItem(String toCraft) {
        return Item.valueOf(toCraft);
    }

    public int getMat1() {
        return Mat1;
    }

    public void setMat1(int mat1) {
        Mat1 = mat1;
    }

    public int getMat2() {
        return Mat2;
    }

    public void setMat2(int mat2) {
        Mat2 = mat2;
    }

    public int getFinishedProduct() {
        return FinishedProduct;
    }

    public void setFinishedProduct(int finishedProduct) {
        FinishedProduct = finishedProduct;
    }

    public int getAmount1() {
        return Amount1;
    }

    public void setAmount1(int amount1) {
        Amount1 = amount1;
    }

    public int getAmount2() {
        return Amount2;
    }

    public void setAmount2(int amount2) {
        Amount2 = amount2;
    }


}
