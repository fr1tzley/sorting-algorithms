import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class Sorter {

    protected List<Integer> numberList;

    public Sorter(Integer elementCount) {
        numberList = makeList(elementCount);
    }

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

        while (!solved) {
            noUnmatchedPairs = true;
            for (int i = 0; i < numberList.size() - 1; i++) {
                Integer int1 = numberList.get(i);
                Integer int2 = numberList.get(i + 1);

                if (int1 > int2) {
                    noUnmatchedPairs = false;
                    Collections.swap(numberList, i, i + 1);
                }
            }
            if (noUnmatchedPairs) {
                solved = true;
            }
        }
        System.out.println(numberList);

    }

    public void quickSort() {
       numberList = quickSortRecursion(numberList);
       System.out.println(numberList);
    }

    private List<Integer> quickSortRecursion(List<Integer> numberList) {

        if (numberList.size() <= 1) {
            return numberList;
        } else {
            Integer pivot = numberList.get((numberList.size() / 2) - 1);
            numberList.remove((numberList.size() / 2) - 1);
            List<Integer> lesserList = new ArrayList<>();
            List<Integer> greaterList = new ArrayList<>();

            for (Integer i : numberList) {
                if (i > pivot) {
                    greaterList.add(i);
                } else {
                    lesserList.add(i);
                }
            }

            List<Integer> sortedLesser = quickSortRecursion(lesserList);
            List<Integer> sortedGreater = quickSortRecursion(greaterList);

            sortedLesser.add(pivot);

            for (Integer i : sortedGreater) {
                sortedLesser.add(i);
            }
            return sortedLesser;
        }
    }

    public void mergeSort() {
        numberList = mergeSortRecursion(numberList);
        System.out.println(numberList);
    }

    private List<Integer> mergeSortRecursion(List<Integer> numberList) {
        if (numberList.size() <= 1) {
            return numberList;
        } else {

            List<Integer> left = new ArrayList<>();
            List<Integer> right = new ArrayList<>();

            for (int i = 0; i <= (numberList.size() / 2) - 1; i++) {
                left.add(numberList.get(i));
            }

            for (int i = numberList.size() / 2; i <= numberList.size() - 1; i++ ) {
                right.add(numberList.get(i));
            }

            List<Integer> sortedLeft = mergeSortRecursion(left);
            List<Integer> sortedRight = mergeSortRecursion(right);

            List<Integer> completeList = mergeLists(left, right);
            return completeList;
        }
    }

    private List<Integer> mergeLists(List<Integer> left, List<Integer> right) {
        List<Integer> result = new ArrayList<>();
        while (!left.isEmpty() || !right.isEmpty()) {
            if (!left.isEmpty() && !right.isEmpty()) {
                if (right.get(0) > left.get(0)) {
                    result.add(left.get(0));
                    left.remove(0);
                } else {
                    result.add(right.get(0));
                    right.remove(0);
                }
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


    public List<Integer> getNumberList() {
        return numberList;
    }
}
