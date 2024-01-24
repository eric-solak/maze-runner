package ca.mcmaster.se2aa4.mazerunner;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;

public class MazeBuilder {
    private static int[][] maze;
    private final String filePath;

    public MazeBuilder(String filePath) {
        this.filePath = filePath;
    }

    public void buildMaze() throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(filePath));
        int rows = (int) reader.lines().count(); // Count the number of lines in the file

        reader = new BufferedReader(new FileReader(filePath)); // Reopen the file to read it again
        String firstLine = reader.readLine();
        int cols = firstLine.length();
        maze = new int[rows][cols]; // Works for files that contain only square mazes

        for (int i = 0; i < cols; i++) { // Process the first line
            if (firstLine.charAt(i) == '#') { // wall represents "1"
                maze[0][i] = 1;
            } else if (firstLine.charAt(i) == ' ') { // pass represents "0"
                maze[0][i] = 0;
            }
        }
        String line;
        int j = 0;
        while ((line = reader.readLine()) != null) { // Process the remaining lines
            int idx;
            for (idx = 0; idx < line.length(); idx++) {
                if (line.charAt(idx) == '#') { // wall represents "1"
                    maze[j+1][idx] = 1;
                } else if (line.charAt(idx) == ' ') { // pass represents "0"
                    maze[j+1][idx] = 0;
                }
            }
            j++;
        }
    }

    private int[] findEntry(int[][] maze) {
        int[] entrance  = new int[2];
        for (int i = 0; i < maze[0].length; i++) {
            if (maze[i][0] == 0) {
                entrance[0] = i;
            }
        }

        return entrance;
    }

    public int[][] getMaze() {
        return maze;
    }

    public int[] getEntry() {
        return findEntry(maze);
    }
}
