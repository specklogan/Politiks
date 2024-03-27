package goose.politik.util.landUtil;

public abstract class TickableLand extends Land{
    //this is a generic tickable land, which is loaded from the database on each event
    private int productionValue;

    public int getProductionValue() {
        return productionValue;
    }

    public void setProductionValue(int productionValue) {
        this.productionValue = productionValue;
    }

    public abstract void onTickEvent();

    public static void runTickables() {

    }
}
