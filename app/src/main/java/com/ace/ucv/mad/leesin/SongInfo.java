package com.ace.ucv.mad.leesin;

/**
 * Created by ctotolin on 01-May-17.
 */

public class SongInfo {
    private String name;
    private double loudness;
    private double musicalness;
    private double instrumentalness;
    private double energy;
    private double speechiness;

    public SongInfo(String name, double loudness, double musicalness, double instrumentalness,
                    double energy, double speechiness) {

        this.name = name;
        this.loudness = loudness;
        this.musicalness = musicalness;
        this.instrumentalness = instrumentalness;
        this.energy = energy;
        this.speechiness = speechiness;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getLoudness() {
        return loudness;
    }

    public void setLoudness(double loudness) {
        this.loudness = loudness;
    }

    public double getMusicalness() {
        return musicalness;
    }

    public void setMusicalness(double musicalness) {
        this.musicalness = musicalness;
    }

    public double getInstrumentalness() {
        return instrumentalness;
    }

    public void setInstrumentalness(double instrumentalness) {
        this.instrumentalness = instrumentalness;
    }

    public double getEnergy() {
        return energy;
    }

    public void setEnergy(double energy) {
        this.energy = energy;
    }

    public double getSpeechiness() {
        return speechiness;
    }

    public void setSpeechiness(double speechiness) {
        this.speechiness = speechiness;
    }
}
