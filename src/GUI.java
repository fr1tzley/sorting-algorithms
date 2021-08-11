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
    private static JPanel setupPanel;
    private static JComboBox<String> algoBox;
    private static JSlider elementSlider;
    private static Sorter sorter;
    private static JTextField sliderValue;
    private static JComponent valueDisplayChart;
    private static JButton sortButton;
    private static JLabel sortLabel;
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


    public static void main(String[] args) {
        frame = new JFrame("Sorting Algorithm Display Application");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        String[] algorithms = {"Merge Sort", "Quick Sort", "Bubble Sort"};
        algoBox = new JComboBox<>(algorithms);
        algoBox.addActionListener(new BoxListener());

        generateSliderAndField();

        setupPanel = new JPanel(new BorderLayout());
        setupPanel.add(algoBox, BorderLayout.EAST);
        setupPanel.add(sliderValue, BorderLayout.CENTER);
        setupPanel.add(elementSlider, BorderLayout.WEST);

        sorter = new Sorter(10);
        values = sorter.getNumberList();


        valueDisplayChart = new GComponent();

        sortButton = new JButton();
        sortLabel = new JLabel("Sort the graph!");
        sortButton.addActionListener(new SortListener());
        sortButton.add(sortLabel);


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

    private static void generateSliderAndField() {
        elementSlider = new JSlider(JSlider.HORIZONTAL, 0, 40, 10);
        elementSlider.addChangeListener(new sliderListener());

        sliderValue = new JTextField();
        sliderValue.addActionListener(new TextListener());
        PlainDocument fieldText = (PlainDocument) sliderValue.getDocument();
        fieldText.setDocumentFilter(new IntDocFilter());
    }


    private static class sliderListener implements ChangeListener {

        @Override
        public void stateChanged(ChangeEvent e) {
            JSlider slider = (JSlider) e.getSource();
            sorter = new Sorter((int) slider.getValue());
            values = sorter.getNumberList();
            frame.repaint();

            sliderValue.setText(String.valueOf(slider.getValue()));


        }
    }

    private enum Algorithms {
        MERGE, QUICK, BUBBLE;
    }

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
            int width = (int) Math.round((CHART_WIDTH / values.size()) - 10);

            Color specialColor = (swap ? Color.RED : Color.GREEN);

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

    private static class SortListener implements ActionListener {

        Timer timer = new Timer((int) ((40 - values.size()) * 25), this);
        List<Anim> animateList = new ArrayList<>();
        List<Integer> preservedList;




        @Override
        public void actionPerformed(ActionEvent e) {
            if (e.getSource() instanceof javax.swing.Timer) {
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

    private static class IntDocFilter extends DocumentFilter {

        @Override
        public void insertString(FilterBypass fb, int offs, String str, AttributeSet a) throws BadLocationException {
            Document doc = fb.getDocument();
            StringBuilder sb = new StringBuilder();
            sb.append(doc.getText(0, doc.getLength()));
            sb.insert(offs, str);

            if (test(sb.toString())) {
                super.insertString(fb, offs, str, a);
            } else {
                // warn the user and don't allow the insert
            }
        }

        private boolean test(String str) {
            if (str.isEmpty()) {
                return true;
            } else {
                try {
                    int number = parseInt(str);

                    if (number <= 40) {
                        return true;
                    } else {
                        return false;
                    }

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
            } else {
                // warn the user and don't allow the insert
            }

        }

        @Override
        public void remove(FilterBypass fb, int offs, int length)
                throws BadLocationException {
            Document doc = fb.getDocument();
            StringBuilder sb = new StringBuilder();
            sb.append(doc.getText(0, doc.getLength()));
            sb.delete(offs, offs + length);

            if (test(sb.toString())) {
                super.remove(fb, offs, length);
            } else {
                // warn the user and don't allow the insert
            }

        }

    }

}