package ca.mcmaster.se2aa4.mazerunner;

import java.io.IOException;

public interface MazeBuild {
    void buildMaze() throws IOException;
    int[][] getMaze();
    int[] getEntryWest();
    int[] getEntryEast();

}
