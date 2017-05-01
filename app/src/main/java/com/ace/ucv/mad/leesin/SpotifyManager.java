package com.ace.ucv.mad.leesin;

import com.wrapper.spotify.Api;
import com.wrapper.spotify.methods.TrackSearchRequest;
import com.wrapper.spotify.models.Page;
import com.wrapper.spotify.models.Track;

/**
 * Created by ctotolin on 01-May-17.
 */

public class SpotifyManager {
    private final String clientId = "1558f82001374593b4146feb191b0d0f";
    private final String clientSecret = "973993ea96c44e7bb74b0c0844a35be1";
    private final String redirectURI = "http://google.ro";

    private Api spotifyAPI;

    public SpotifyManager(){
        this.spotifyAPI = Api.builder()
                .clientId(clientId)
                .clientSecret(clientSecret)
                .redirectURI(redirectURI)
                .build();
    }

    public SongInfo getSongInfo(String name){
        final TrackSearchRequest request = spotifyAPI.searchTracks(name).build();

        try {
            final Page<Track> trackSearchResult = request.get();
            return SpotifyUtils.parseInfo(trackSearchResult.getItems(), name);
        } catch (Exception e) {
            return SpotifyUtils.parseInfo(null, name);
        }
    }
}
