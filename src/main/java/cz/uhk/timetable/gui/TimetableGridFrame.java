package cz.uhk.timetable.gui;

import cz.uhk.timetable.model.LocationTimetable;
import javax.swing.*;

public class TimetableGridFrame extends JFrame {

    public TimetableGridFrame(LocationTimetable timetable) {
        super("Rozvrh – přehled");
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        add(new JScrollPane(new TimetableGridPanel(timetable)));
        pack();
        setLocationRelativeTo(null);
    }
}