package cz.uhk.timetable.gui;

import cz.uhk.timetable.model.LocationTimetable;
import cz.uhk.timetable.utils.TimetableProvider;
import cz.uhk.timetable.utils.impl.StagTimetableProvider;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import java.awt.*;

public class TimetableFrame extends JFrame {
    private LocationTimetable timetable;
    private TimetableProvider provider = new StagTimetableProvider();
    private JTable tabTimetable;

    private JComboBox<String> cbBuilding;
    private JComboBox<String> cbRoom;
    private JButton btnLoad;
    private JButton btnShowGrid;

    public TimetableFrame() {
        super("FIM Rozvrhy");

        setDefaultCloseOperation(EXIT_ON_CLOSE);

        initGui();


    }

    private void initGui() { //tabulka

        cbBuilding = new JComboBox<>(new String[]{"J", "H", "V", "A", "S"});
        cbRoom = new JComboBox<>(new String[]{"J22", "J23", "J24", "H01", "H02"});
        cbRoom.setEditable(true);

        btnLoad    = new JButton("Načíst rozvrh");
        btnShowGrid = new JButton("Zobrazit přehled");
        btnShowGrid.setEnabled(false);

        JPanel topPanel = new JPanel(new GridLayout(2, 1));

        JPanel row1 = new JPanel(new FlowLayout(FlowLayout.LEFT));
        row1.add(new JLabel("Budova:"));
        row1.add(cbBuilding);
        row1.add(new JLabel("Místnost:"));
        row1.add(cbRoom);
        row1.add(btnLoad);

        JPanel row2 = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        row2.add(btnShowGrid);

        topPanel.add(row1);
        topPanel.add(row2);

        add(topPanel, BorderLayout.NORTH);

        tabTimetable = new JTable();
        tabTimetable.setAutoCreateRowSorter(true);
        add(new JScrollPane(tabTimetable), BorderLayout.CENTER);

        btnLoad.addActionListener(e -> loadData());
        btnShowGrid.addActionListener(e ->
                new TimetableGridFrame(timetable).setVisible(true)
        );

        setSize(1100, 400);
        setLocationRelativeTo(null);

    }

    private void loadData() {
        String building = (String) cbBuilding.getSelectedItem();
        String room     = (String) cbRoom.getSelectedItem();

        timetable = provider.read(building, room);

        tabTimetable.setModel(new TimetableModel());
        tabTimetable.setAutoCreateRowSorter(true);
        btnShowGrid.setEnabled(true);
    }



    class TimetableModel extends AbstractTableModel { //vnitrni trida

        @Override //getter pro pojmenovani kolonek
        public String getColumnName(int column) {
            switch (column) {
                case 0: return "Zkratka";
                case 1: return "Název";
                case 2: return "Den";
                case 3: return "Od:";
                case 4: return "Do:";
                case 5: return "Učitel";
            }
            return "";

        }



        @Override
        public int getRowCount() {
            return timetable.getActivities().size(); //prispusobi se poctu aktivit
        }

        @Override
        public int getColumnCount() {
            return 6;
        }

        @Override
        public Object getValueAt(int rowIndex, int columnIndex) {
            var act = timetable.getActivities().get(rowIndex);
            switch (columnIndex) {
                case 0: return act.getCode();
                case 1: return act.getName();
                case 2: return act.getDay();
                case 3: return act.getStartTime();
                case 4: return act.getEndTime();
                case 5: return act.getTeacher();
            }
            return null;
        }
    }
}