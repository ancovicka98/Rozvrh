package cz.uhk.timetable.utils.impl;

import cz.uhk.timetable.model.Activity;
import cz.uhk.timetable.model.LocationTimetable;
import cz.uhk.timetable.utils.TimetableProvider;

import java.time.LocalTime;
import java.util.List;

/**
 * falesna trida provider se vzorovymi daty pro testovani
 */

public class MockTimetableProvider implements TimetableProvider {

    @Override
    public LocationTimetable read(String building, String room) {
        var tt = new LocationTimetable(building, room);
        var activities = List.of(
                new Activity("PRO1", "Programovani1", "Utery", LocalTime.of(12,25), LocalTime.of(13,55), "Kozel"),
                new Activity("ZMI2", "Základy matematiky pro informatiky", "Ctvrtek", LocalTime.of(8,15), LocalTime.of(9,50), "Medková"),
                new Activity("DIMA", "Diskretní matematika", "Streda", LocalTime.of(11,35), LocalTime.of(13,50), "Ševčíková "),
                new Activity("OA2", "Odborná angličtina", "Streda", LocalTime.of(8,15), LocalTime.of(9,50), "Součková"),
                new Activity("OS1A", "Operační systémy", "Streda", LocalTime.of(16,35), LocalTime.of(17,55), "Almer"),
                new Activity("OPCM", "Obrana proti černé magii", "Utery", LocalTime.of(0,0), LocalTime.of(1,55), "Snape"),
                new Activity("FZ", "Fanstastická zvířata", "Patek", LocalTime.of(12,25), LocalTime.of(13,0), "Hagrid")
        );

        tt.setActivities(activities);
        return tt;
    }
}
