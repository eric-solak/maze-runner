package ca.mcmaster.se2aa4.mazerunner;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

/**
 * MazeBuilder constructs a maze from a file and provides methods
 * to access information about the maze
 */
public class MazeBuilder implements MazeBuild {
    private static int[][] maze;
    private final String filePath;

    public MazeBuilder(String filePath) {
        this.filePath = filePath;
    }

    /**
     * Copy's the maze contents from the file to a 2D array
     */
    public void buildMaze() throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(filePath));
        int rows = (int) reader.lines().count(); // Count the number of lines in the file

        reader = new BufferedReader(new FileReader(filePath)); // Reopen the file to read it again
        String firstLine = reader.readLine();
        int cols = firstLine.length();
        maze = new int[rows][cols];

        // Process the first line
        for (int i = 0; i < cols; i++) {
            if (firstLine.charAt(i) == '#') {
                maze[0][i] = 1; // Wall
            } else if (firstLine.charAt(i) == ' ') {
                maze[0][i] = 0; // Pass
            }
        }

        // Process the remaining lines
        String line;
        int j = 0;
        while ((line = reader.readLine()) != null) {
            int idx;
            for (idx = 0; idx < line.length(); idx++) {
                if (line.charAt(idx) == '#') {
                    maze[j+1][idx] = 1;
                } else if (line.charAt(idx) == ' ') {
                    maze[j+1][idx] = 0;
                }
            }
            j++;
        }
    }

    /**
     * Finds the entrance of the maze (on the west side) based on the first index of each row.
     * @param maze Maze in 2D array
     * @return The index of the entrance on the west side
     */
    private int[] findEntryWest(int[][] maze) {
        int[] entrance  = new int[2];
        for (int i = 0; i < maze.length; i++) {
            if (maze[i][0] == 0) {
                entrance[0] = i;
            }
        }

        return entrance;
    }

    /**
     * Finds the entrance of the maze (on the east side) based on the last index of each row.
     * @param maze Maze in 2D array
     * @return The index of the entrance on the east side
     */
    private int[] findEntryEast(int[][] maze) {
        int[] entrance  = new int[2];
        for (int i = 0; i < maze.length; i++) {
            if (maze[i][maze[0].length - 1] == 0) {
                entrance[0] = i;
                entrance[1] = maze[0].length - 1;
            }
        }

        return entrance;
    }
    public int[][] getMaze() {
        return maze;
    }

    public int[] getEntryWest() {
        return findEntryWest(maze);
    }

    public int[] getEntryEast() {
        return findEntryEast(maze);
    }

}
