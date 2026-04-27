package cz.uhk.timetable.gui;

import cz.uhk.timetable.model.Activity;
import cz.uhk.timetable.model.LocationTimetable;

import javax.swing.*;
import java.awt.*;
import java.time.LocalTime;
import java.util.List;

public class TimetableGridPanel extends JPanel {

    private static final String[] DAYS = {"Pondělí", "Úterý", "Středa", "Čtvrtek", "Pátek"};
    private static final LocalTime START = LocalTime.of(7, 0);
    private static final LocalTime END   = LocalTime.of(21, 0);

    private static final int HEADER_WIDTH  = 80;  // šířka sloupce s dny
    private static final int HEADER_HEIGHT = 30;  // výška řádku s časem
    private static final int ROW_HEIGHT    = 60;  // výška jednoho dne
    private static final int PIXELS_PER_MINUTE = 3; // kolik px = 1 minuta

    private final LocationTimetable timetable;

    public TimetableGridPanel(LocationTimetable timetable) {
        this.timetable = timetable;

        int totalMinutes = (int) java.time.Duration.between(START, END).toMinutes();
        int width  = HEADER_WIDTH + totalMinutes * PIXELS_PER_MINUTE;
        int height = HEADER_HEIGHT + DAYS.length * ROW_HEIGHT;
        setPreferredSize(new Dimension(width, height));
        setBackground(Color.WHITE);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        drawGrid(g2);
        drawActivities(g2);
    }

    private void drawGrid(Graphics2D g) {
        int totalMinutes = (int) java.time.Duration.between(START, END).toMinutes();

        // --- Časová osa nahoře (každá hodina) ---
        g.setColor(Color.DARK_GRAY);
        g.setFont(new Font("SansSerif", Font.PLAIN, 10));
        for (int min = 0; min <= totalMinutes; min += 60) {
            int x = HEADER_WIDTH + min * PIXELS_PER_MINUTE;
            LocalTime t = START.plusMinutes(min);
            g.drawString(t.toString(), x + 2, HEADER_HEIGHT - 5);
            g.setColor(new Color(200, 200, 200));
            g.drawLine(x, HEADER_HEIGHT, x, HEADER_HEIGHT + DAYS.length * ROW_HEIGHT);
            g.setColor(Color.DARK_GRAY);
        }

        // --- Řádky dnů ---
        for (int i = 0; i < DAYS.length; i++) {
            int y = HEADER_HEIGHT + i * ROW_HEIGHT;
            // název dne
            g.setColor(new Color(240, 240, 240));
            g.fillRect(0, y, HEADER_WIDTH, ROW_HEIGHT);
            g.setColor(Color.DARK_GRAY);
            g.setFont(new Font("SansSerif", Font.BOLD, 12));
            g.drawString(DAYS[i], 5, y + ROW_HEIGHT / 2 + 5);
            // oddělovací čára
            g.setColor(new Color(180, 180, 180));
            g.drawLine(0, y, getWidth(), y);
        }

        // spodní čára
        g.drawLine(0, HEADER_HEIGHT + DAYS.length * ROW_HEIGHT,
                getWidth(), HEADER_HEIGHT + DAYS.length * ROW_HEIGHT);
    }

    private void drawActivities(Graphics2D g) {
        List<Activity> activities = timetable.getActivities();

        Color[] COLORS = {
                new Color(100, 149, 237),  // modrá
                new Color(144, 238, 144),  // zelená
                new Color(255, 179, 71),   // oranžová
                new Color(221, 160, 221),  // fialová
                new Color(255, 105, 97),   // červená
        };
        int colorIdx = 0;

        for (Activity act : activities) {
            int dayIndex = dayIndex(act.getDay());
            if (dayIndex < 0) continue;

            LocalTime from = act.getStartTime();
            LocalTime to   = act.getEndTime();
            if (from == null || to == null) continue;

            int startMin = (int) java.time.Duration.between(START, from).toMinutes();
            int endMin   = (int) java.time.Duration.between(START, to).toMinutes();

            int x = HEADER_WIDTH + startMin * PIXELS_PER_MINUTE;
            int y = HEADER_HEIGHT + dayIndex * ROW_HEIGHT + 4;
            int w = (endMin - startMin) * PIXELS_PER_MINUTE;
            int h = ROW_HEIGHT - 8;

            // blok aktivity
            Color c = COLORS[colorIdx % COLORS.length];
            colorIdx++;
            g.setColor(c);
            g.fillRoundRect(x, y, w, h, 8, 8);
            g.setColor(c.darker());
            g.drawRoundRect(x, y, w, h, 8, 8);

            // text uvnitř bloku
            g.setColor(Color.WHITE);
            g.setFont(new Font("SansSerif", Font.BOLD, 11));
            drawClippedString(g, act.getCode(), x + 4, y + 16, w - 8);
            g.setFont(new Font("SansSerif", Font.PLAIN, 10));
            drawClippedString(g, act.getTeacher(), x + 4, y + 30, w - 8);
        }
    }

    /** Ořízne text pokud se nevejde do šířky w */
    private void drawClippedString(Graphics2D g, String text, int x, int y, int maxWidth) {
        if (text == null) return;
        FontMetrics fm = g.getFontMetrics();
        while (text.length() > 1 && fm.stringWidth(text + "…") > maxWidth) {
            text = text.substring(0, text.length() - 1);
        }
        g.drawString(text.length() < fm.stringWidth(text) ? text : text, x, y);
    }

    private int dayIndex(String day) {
        if (day == null) return -1;
        switch (day.trim().toLowerCase()) {
            case "pondělí": case "po": case "mon": return 0;
            case "úterý":   case "ut": case "tue": return 1;
            case "středa":  case "st": case "wed": return 2;
            case "čtvrtek": case "ct": case "thu": return 3;
            case "pátek":   case "pa": case "fri": return 4;
            default: return -1;
        }
    }
}
