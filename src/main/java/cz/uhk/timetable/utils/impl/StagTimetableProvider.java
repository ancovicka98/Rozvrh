package cz.uhk.timetable.utils.impl;

import com.google.gson.Gson;
import cz.uhk.timetable.model.LocationTimetable;
import cz.uhk.timetable.utils.TimetableProvider;

import java.io.InputStreamReader;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

public class StagTimetableProvider implements TimetableProvider {

    @Override
    public LocationTimetable read(String building, String room) {


        //1. pripravit URL pro nacteni ze STAGu
        var url = "https://stag-demo.uhk.cz/ws/services/rest2/rozvrhy/getRozvrhByMistnost?" +
                "semestr=%25&budova=%s&mistnost=%s&outputFormat=JSON"
                        .formatted(building, room);

        //2. pripojit se k serveru
        try {
            URL server = new URL(url);
            //3. vyrobit instanci Gson parseru
            Gson gson = new Gson();
            //4. nacist data rozvrhu pomoci parseru
            gson.fromJson(
                    new InputStreamReader(server.openStream()),
                    LocationTimetable.class
            );//znakovy stream

        } catch (MalformedURLException ex)
        {
            ex.printStackTrace();
            throw new RuntimeException(ex);

        } catch(IOException e){
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        return null;
    }
}
