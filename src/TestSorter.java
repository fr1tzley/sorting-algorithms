import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class TestSorter {

    Sorter sorter;

    @BeforeEach
    public void setup() {
        sorter = new Sorter(5);
    }

    @Test
    public void testRandom() {
        assertEquals(sorter.getNumberList().size(), 5);
        sorter.bubbleSort();
        sorter.quickSort();
        sorter.mergeSort();
    }
}
