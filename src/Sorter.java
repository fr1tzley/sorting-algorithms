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


        while (!solved) {
            noUnmatchedPairs = true;
            for (int i = 0; i < numberList.size() - 1; i++) {
                Anim anim = new Anim();
                Integer int1 = numberList.get(i);
                Integer int2 = numberList.get(i + 1);
                anim.setCompare(int1, int2);

                if (int1 > int2) {
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
        animateList.clear();
        List<Integer> mainList = new ArrayList<>(numberList);
        List<Integer> workList = new ArrayList<>(numberList);
        mergeSortSingleList(mainList, 0, numberList.size() - 1, workList);
        System.out.println(numberList);
    }



    private void mergeSortSingleList(List<Integer> mainList, int start, int end, List<Integer> workList) {
        if (end - start <= 1) {
            return;
        }

        int mid = (start + end) / 2;

        mergeSortSingleList(mainList, start, mid, workList);
        mergeSortSingleList(mainList, mid, end, workList);

        mergeSortMerge(workList, start, mid, end, mainList);
    }

    private void mergeSortMerge(List<Integer> mainList, int start, int mid, int end, List<Integer> workList) {
        int i = start;
        int j = mid;

        for (int k = start; k < end; k++) {
            Anim anim = new Anim();
            anim.setCompare(mainList.get(k), mainList.get(j));
            if (i < mid && (j >= end || mainList.get(i) <= mainList.get(j))) {
                workList.set(k, mainList.get(i));
                i++;
            } else {
                workList.set(k, mainList.get(j));
                anim.setSwap(workList.get(k), mainList.get(j));
                j++;
            }
            System.out.println(workList);
            animateList.add(anim);
        }
    }


//    private List<Integer> mergeSortRecursion(List<Integer> numberList) {
//        if (numberList.size() <= 1) {
//            return numberList;
//        } else {
//
//            List<Integer> left = new ArrayList<>();
//            List<Integer> right = new ArrayList<>();
//
//            for (int i = 0; i <= (numberList.size() / 2) - 1; i++) {
//                left.add(numberList.get(i));
//            }
//
//            for (int i = numberList.size() / 2; i <= numberList.size() - 1; i++ ) {
//                right.add(numberList.get(i));
//            }
//
//            List<Integer> sortedLeft = mergeSortRecursion(left);
//            List<Integer> sortedRight = mergeSortRecursion(right);
//
//            List<Integer> completeList = mergeLists(sortedLeft, sortedRight);
//
//            return completeList;
//        }
//    }
//
//    private List<Integer> mergeLists(List<Integer> left, List<Integer> right) {
//        List<Integer> result = new ArrayList<>();
//        while (!left.isEmpty() || !right.isEmpty()) {
//            if (!left.isEmpty() && !right.isEmpty()) {
//                Anim anim = new Anim();
//                anim.setCompare(right.get(0), left.get(0));
//                anim.setSwap(right.get(0), left.get(0));
//                if (right.get(0) > left.get(0)) {
//                    result.add(left.get(0));
//                    left.remove(0);
//                } else {
//                    result.add(right.get(0));
//                    right.remove(0);
//                }
//                animateList.add(anim);
//            } else if (!right.isEmpty()) {
//                result.add(right.get(0));
//                right.remove(0);
//            } else {
//                result.add(left.get(0));
//                left.remove(0);
//            }
//
//        }
//        return result;
//    }

//    private void addToAnimate(List<Integer> result) {
//
//        int start = numberList.size();
//        int end = -1;
//        for (int i = 0; i < numberList.size() ; i++) {
//            if (result.contains(numberList.get(i))) {
//                start = min(start, i);
//                end = max(end, i);
//            }
//        }
//
//        List<Integer> listToAnimate = new ArrayList<>();
//        for (int i : animateList.get(animateList.size() - 1)) {
//            listToAnimate.add(i);
//        }
//
//        for (int i = 0 ; i <= end - start ; i++) {
//            int resultChoice = i - start;
//            listToAnimate.set(resultChoice, result.get(i));
//        }
//        animateList.add(listToAnimate);
//    }

    public List<Integer> getNumberList() {
        return numberList;
    }

    public List<Anim> getAnimateList() {
        return animateList;
    }
}
