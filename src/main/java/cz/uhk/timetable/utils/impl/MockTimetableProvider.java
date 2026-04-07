package cz.uhk.timetable.utils.impl;

import cz.uhk.timetable.model.Activity;
import cz.uhk.timetable.model.LocationTimetable;
import cz.uhk.timetable.utils.TimetableProvider;

import java.time.LocalTime;
import java.util.List;

/**
 * falesna trida provider se vzorovymi daty
 */

public class MockTimetableProvider implements TimetableProvider {

    @Override
    public LocationTimetable read(String building, String room) {
        var tt = new LocationTimetable(building, room);
        var activities = List.of(
                new Activity("PRO1", "Programovani1", "Utery", LocalTime.of(12,25), LocalTime.of(13,55), "Kozel"),
                new Activity("PRO1", "Programovani1", "Utery", LocalTime.of(12,25), LocalTime.of(13,55), "Kozel"),
                new Activity("PRO1", "Programovani1", "Utery", LocalTime.of(12,25), LocalTime.of(13,55), "Kozel")
        );
        return null;
    }
}
