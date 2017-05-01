package com.ace.ucv.mad.leesin;

import com.wrapper.spotify.models.Page;
import com.wrapper.spotify.models.Track;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * Created by ctotolin on 01-May-17.
 */

public class SpotifyUtils {
    static final Map<String, Integer> map = new HashMap<>();

    public static int idFor(String s) {
        Integer id = map.get(s);
        if (id == null)
            map.put(s, id = map.size());
        return id;
    }

    public static SongInfo parseInfo(List<Track> songPage, String name){
        Random rand = new Random(idFor(name));
        SongInfo info = new SongInfo(
                name,
                rand.nextFloat() * 0.9 + 0.1,
                rand.nextFloat() * 0.9 + 0.1,
                rand.nextFloat() * 0.9 + 0.1,
                rand.nextFloat() * 0.9 + 0.1,
                rand.nextFloat() * 0.9 + 0.1
        );

        return info;
    }
}
