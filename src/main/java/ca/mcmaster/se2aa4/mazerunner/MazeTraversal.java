package ca.mcmaster.se2aa4.mazerunner;

import java.io.IOException;

public interface  MazeTraversal {
    String traverseMaze() throws IOException;
    String testPath(String path) throws IOException;

}
