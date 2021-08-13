import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class Sorter {

    protected List<Integer> numberList;

    private List<Anim> animateList;





    public Sorter(Integer elementCount) {

        numberList = makeList(elementCount);
        animateList = new ArrayList<>();
    }

    //make a list of n length randomly populated with items up to n in size
    private List<Integer> makeList(Integer elementCount) {
        List<Integer> numberList = new ArrayList<>();
        Random rand = new Random();

        for (int i = 1; i <= elementCount; i++) {
            if (numberList.size() == 0) {
                numberList.add(i);
            } else {
                numberList.add(rand.nextInt(numberList.size()), i);
            }
        }

        return numberList;
    }

    public void bubbleSort() {
        boolean noUnmatchedPairs;
        boolean solved = false;
        animateList.clear();

        //loop through list while it's unsolved
        while (!solved) {
            noUnmatchedPairs = true;
            //compares each item in list with all others
            for (int i = 0; i < numberList.size() - 1; i++) {
                Anim anim = new Anim();
                Integer int1 = numberList.get(i);
                Integer int2 = numberList.get(i + 1);
                anim.setCompare(int1, int2);

                if (int1 > int2) {
                    //if any item is larger than an item after it, set the variable to false so the sorting continues
                    noUnmatchedPairs = false;
                    anim.setSwap(int1, int2);
                    Collections.swap(numberList, i, i + 1);

                }
                animateList.add(anim);
            }
            if (noUnmatchedPairs) {
                solved = true;
            }
        }
        System.out.println(numberList);
    }

    public void quickSort() {
        animateList.clear();
        numberList = quickSortRecursion(numberList);
       System.out.println(numberList);
    }



    private List<Integer> quickSortRecursion(List<Integer> numberList) {

        if (numberList.size() <= 1) {
            //base case
            return numberList;
        } else {
            //pick item in middle of list and remove from list
            Integer pivot = numberList.get((numberList.size() / 2) - 1);
            numberList.remove((numberList.size() / 2) - 1);

            List<Integer> lesserList = new ArrayList<>();
            List<Integer> greaterList = new ArrayList<>();

            //populate lesser and greater list with values lesser and greater than pivot
            for (Integer i : numberList) {
                if (i > pivot) {
                    greaterList.add(i);
                } else {
                    lesserList.add(i);
                }
            }

            //sort recursively
            List<Integer> sortedLesser = quickSortRecursion(lesserList);
            List<Integer> sortedGreater = quickSortRecursion(greaterList);


            //merge lists
            sortedLesser.add(pivot);

            for (Integer i : sortedGreater) {
                sortedLesser.add(i);
            }
            return sortedLesser;
        }
    }

    public void mergeSort() {
        animateList.clear();
        numberList = mergeSortRecursion(numberList);
        System.out.println(numberList);
    }

    private List<Integer> mergeSortRecursion(List<Integer> numberList) {
        if (numberList.size() <= 1) {
            //base case
            return numberList;
        } else {

            List<Integer> left = new ArrayList<>();
            List<Integer> right = new ArrayList<>();

            //add left half of list to left list
            for (int i = 0; i <= (numberList.size() / 2) - 1; i++) {
                left.add(numberList.get(i));
            }

            //add right half to right list
            for (int i = numberList.size() / 2; i <= numberList.size() - 1; i++ ) {
                right.add(numberList.get(i));
            }

            //sort sublists recursively
            List<Integer> sortedLeft = mergeSortRecursion(left);
            List<Integer> sortedRight = mergeSortRecursion(right);

            //merge parallel lists
            List<Integer> completeList = mergeLists(sortedLeft, sortedRight);

            return completeList;
        }
    }

    private List<Integer> mergeLists(List<Integer> left, List<Integer> right) {
        List<Integer> result = new ArrayList<>();
        while (!left.isEmpty() || !right.isEmpty()) {
            if (!left.isEmpty() && !right.isEmpty()) {
                //add items from left and right in order of smallness
                if (right.get(0) > left.get(0)) {
                    result.add(left.get(0));
                    left.remove(0);
                } else {
                    result.add(right.get(0));
                    right.remove(0);
                }
                //if one array is empty, add the rest of the other
            } else if (!right.isEmpty()) {
                result.add(right.get(0));
                right.remove(0);
            } else {
                result.add(left.get(0));
                left.remove(0);
            }

        }
        return result;
    }

    //getters
    public List<Integer> getNumberList() {
        return numberList;
    }

    public List<Anim> getAnimateList() {
        return animateList;
    }
}
