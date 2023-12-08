package android2.genzelody;

import java.util.ArrayList;

public interface SlidingPanelToggleListener {
    void setTrackLists(ArrayList<Track> tracks, String name, int index);

    void getCurrentTrack(String img, String name, String artist);

    void toggleSlideUP();

}
