import javafx.util.Pair;

//anim class that can be given a pair of values which are compared, and a pair of values to swap
public class Anim {
    private Pair<Integer, Integer> compare;
    private Pair<Integer, Integer> swap;

    public Anim(){
        compare = null;
        swap = null;
    }


    public void setSwap(int int1, int int2) {
        swap= new Pair<>(int1, int2);
    }

    public void setCompare(int int1, int int2) {
        compare = new Pair<>(int1, int2);
    }

    public Pair<Integer, Integer> getSwap() {
        return swap;
    }

    public Pair<Integer, Integer> getCompare() {
        return compare;
    }

    public boolean hasCompare() {
        return compare != null;
    }

    public boolean hasSwap() {
        return swap != null;
    }

}
