import javafx.util.Pair;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.text.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static java.lang.Integer.parseInt;

public class GUI {
    private static JFrame frame;
    private static JSlider elementSlider;
    private static Sorter sorter;
    private static JTextField sliderValue;
    private static Algorithms currentAlgo = Algorithms.MERGE;

    private static final int CHART_HEIGHT = 400;
    private static final int CHART_WIDTH = 600;
    private static final String MERGE_COMMAND = "Merge Sort";
    private static final String QUICK_COMMAND = "Quick Sort";
    private static final String BUBBLE_COMMAND = "Bubble Sort";

    private static List<Integer> values;
    private static Pair<Integer, Integer> pair;
    private static boolean animating = false;
    private static boolean swap = false;

    //main method
    public static void main(String[] args) {
        frame = new JFrame("Sorting Algorithm Display Application");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        //create selections for JComboBox and add action listener
        String[] algorithms = {"Merge Sort", "Quick Sort", "Bubble Sort"};
        JComboBox<String> algoBox = new JComboBox<>(algorithms);
        algoBox.addActionListener(new BoxListener());

        //method to generate JSlider and text field for changing number of items
        generateSliderAndField();

        //sets up central JPanel
        JPanel setupPanel = new JPanel(new BorderLayout());
        setupPanel.add(algoBox, BorderLayout.EAST);
        setupPanel.add(sliderValue, BorderLayout.CENTER);
        setupPanel.add(elementSlider, BorderLayout.WEST);

        //initializes sorter that will be used in application
        sorter = new Sorter(10);
        values = sorter.getNumberList();

        //creates custom GComponent for graphed display of sorter's values
        JComponent valueDisplayChart = new GComponent();

        //buttons and labels
        JButton sortButton = new JButton();
        JLabel sortLabel = new JLabel("Sort the graph!");
        sortButton.addActionListener(new SortListener());
        sortButton.add(sortLabel);

        //misc. frame setup
        frame.getContentPane().add(setupPanel, BorderLayout.NORTH);
        frame.getContentPane().add(valueDisplayChart, BorderLayout.CENTER);
        frame.getContentPane().add(sortButton, BorderLayout.SOUTH);
        frame.pack();
        frame.setSize(800, 600);
        frame.setVisible(true);
        frame.validate();
        valueDisplayChart.repaint();
        frame.repaint();
    }

    //creates JSlider and JTextField, adding action listenrs to both and a DocumentFilter to the JTextField to ensure
    //only numbers up to 40can be inputted
    private static void generateSliderAndField() {
        elementSlider = new JSlider(JSlider.HORIZONTAL, 0, 40, 10);
        elementSlider.addChangeListener(new sliderListener());

        sliderValue = new JTextField();
        sliderValue.addActionListener(new TextListener());
        PlainDocument fieldText = (PlainDocument) sliderValue.getDocument();
        fieldText.setDocumentFilter(new IntDocFilter());
    }

    //action listener for the JSlider, updates the Sorter whenever the slider's value is changed
    private static class sliderListener implements ChangeListener {

        @Override
        public void stateChanged(ChangeEvent e) {
            JSlider slider = (JSlider) e.getSource();
            sorter = new Sorter(slider.getValue());
            values = sorter.getNumberList();
            frame.repaint();

            sliderValue.setText(String.valueOf(slider.getValue()));


        }
    }

    //enum of algorithms for use in application
    private enum Algorithms {
        MERGE, QUICK, BUBBLE
    }

    //create a basic GComponent class that whose PaintComponent method paints it with a bar graph of all
    //the sorter's values
    private static class GComponent extends JComponent {

        public GComponent() {
            setBorder(BorderFactory.createLineBorder(Color.BLACK));
            super.setPreferredSize(new Dimension(CHART_WIDTH, CHART_HEIGHT));
            repaint();

        }

        @Override
        public Dimension getPreferredSize() {
            return new Dimension(CHART_WIDTH, CHART_HEIGHT);
        }



        @Override
        protected void paintComponent(Graphics g) {

            super.paintComponent(g);
            g.setColor(Color.WHITE);


            Integer biggest = Collections.max(values);

            int interval = CHART_WIDTH / values.size() + 1;
            int width = Math.round((CHART_WIDTH / values.size()) - 10);

            Color specialColor = (swap ? Color.RED : Color.GREEN);
            //basic graph-making. bars are placed at fixed intervals, and their height is generated based on their size
            //relative to the largest int in the list
            for (int i = 0; i < values.size(); i++) {
                int currentValue = values.get(i);
                int height = (int) Math.round((CHART_HEIGHT - 20) * ((double) currentValue / biggest));



                g.setColor(Color.BLACK);
                g.drawRect(interval * (i + 1), CHART_HEIGHT - height, width, height);

                if (animating && (currentValue == pair.getValue() || currentValue == pair.getKey())) {
                    g.setColor(specialColor);
                } else {
                    g.setColor(Color.BLUE);
                }

                g.fillRect(interval * (i + 1), CHART_HEIGHT - height, width, height);
            }
        }
    }

    //action listener for text field
    private static class TextListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            JTextField field = (JTextField) e.getSource();
            if (!field.getText().isEmpty()) {
                int value = parseInt(field.getText());
                elementSlider.setValue(value);
            }
        }
    }

    //action listener to handle the sorting of the graph. handles both sorting and animating.
    //listens for the sort button as well as events from a timer it creates after the sorting is complete
    private static class SortListener implements ActionListener {
        //create a timer with a delay that decreases as the size of values grows
        Timer timer = new Timer((40 - values.size()) * 15, this);
        List<Anim> animateList = new ArrayList<>();
        List<Integer> preservedList;



        //handles events from sorting and timer. if the input comes from the sort button, the list will be sorted,
        //and the sorter class will create a list of anims. After that, the timer's delay is updated and the list of
        //anims is looped through at a rate of one anim per timer event until the sorting process is reproduced.
        @Override
        public void actionPerformed(ActionEvent e) {
            if (e.getSource() instanceof javax.swing.Timer) {
                //stop timer once animatelist is fully looped through
                if (animateList.size() <= 0) {
                    timer.stop();
                    animating = false;
                    values = sorter.getNumberList();
                    frame.repaint();
                } else {
                    animating = true;
                    doRepaint(animateList.get(0));
                    animateList.remove(0);
                }
            } else {
                animateList.clear();
                timer.setDelay((int) ((40 - values.size()) * 2.5));
                preservedList = new ArrayList<>(sorter.getNumberList());


                if (currentAlgo == Algorithms.BUBBLE) {
                    sorter.bubbleSort();
                } else if (currentAlgo == Algorithms.QUICK) {
                    sorter.quickSort();
                } else {
                    sorter.mergeSort();
                }

                //for some reason, once the algorithm finishes the values become the sorter's sorted list
                //so running anim literally unsorts the list from being perfectly sorted
                //am i going to fix this? no, i'll just preserve the initial list as a variable LOL
                values = preservedList;
                this.animateList = sorter.getAnimateList();
                timer.start();
            }
        }

        //repaints the GComponent according to the current anim
        private void doRepaint(Anim anim) {
            if (anim.hasCompare()) {
                swap = false;
                pair = anim.getCompare();
                frame.repaint();
                if (anim.hasSwap()) {
                    swap = true;
                    pair = anim.getSwap();
                    frame.repaint();

                    int i1 = values.indexOf(pair.getKey());
                    int i2 = values.indexOf(pair.getValue());
                    Collections.swap(values, i1, i2);
                    frame.repaint();
                }
            }
        }

    }

    //sets the current algorithm according to input to the JComboBox
    private static class BoxListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            JComboBox<String> box = (JComboBox<String>) e.getSource();
            String value = (String) box.getSelectedItem();

            if (value.equals(MERGE_COMMAND)) {
                currentAlgo = Algorithms.MERGE;
            } else if (value.equals(QUICK_COMMAND)) {
                currentAlgo = Algorithms.QUICK;
            } else {
                currentAlgo = Algorithms.BUBBLE;
            }
        }
    }

    //document filter for JTextField
    private static class IntDocFilter extends DocumentFilter {

        @Override
        public void insertString(FilterBypass fb, int offs, String str, AttributeSet a) throws BadLocationException {
            Document doc = fb.getDocument();
            StringBuilder sb = new StringBuilder();
            sb.append(doc.getText(0, doc.getLength()));
            sb.insert(offs, str);

            if (test(sb.toString())) {
                super.insertString(fb, offs, str, a);
            }
        }

        //tests string to ensure it's either empty or a number between 1 and 40
        private boolean test(String str) {
            if (str.isEmpty()) {
                return true;
            } else {
                try {
                    int number = parseInt(str);

                    return number <= 40;

                } catch (NumberFormatException nfe) {
                    return false;
                }
            }
        }

        @Override
        public void replace(FilterBypass fb, int offs, int length, String text,
                            AttributeSet a) throws BadLocationException {

            Document doc = fb.getDocument();
            StringBuilder sb = new StringBuilder();
            sb.append(doc.getText(0, doc.getLength()));
            sb.replace(offs, offs + length, text);

            if (test(sb.toString())) {
                super.replace(fb, offs, length, text, a);
            }

        }

        //filters out bad input
        @Override
        public void remove(FilterBypass fb, int offs, int length)
                throws BadLocationException {
            Document doc = fb.getDocument();
            StringBuilder sb = new StringBuilder();
            sb.append(doc.getText(0, doc.getLength()));
            sb.delete(offs, offs + length);

            if (test(sb.toString())) {
                super.remove(fb, offs, length);
            }

        }

    }

}