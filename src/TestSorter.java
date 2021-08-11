import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class TestSorter {

    Sorter sorter;

    @BeforeEach
    public void setup() {
        sorter = new Sorter(5);
    }

    @Test
    public void testRandom() {

        //sorter.bubbleSort();
        //sorter.quickSort();
        sorter.mergeSort();
    }
}
