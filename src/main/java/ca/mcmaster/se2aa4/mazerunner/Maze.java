package ca.mcmaster.se2aa4.mazerunner;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;

public class Maze {
    private final String filePath;
    private static int[][] maze;
    enum Cardinal { NORTH, EAST, SOUTH, WEST }
    public Maze(String filePath) {
        this.filePath = filePath;
    }

    public static void buildMaze(String filePath) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(filePath));
        String firstLine = reader.readLine();
        int x = firstLine.length();
        maze = new int[x][x]; // Works for files that contain only square mazes

        for (int i = 0; i < x; i++) { // Process the first line
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
        entrance[0] = 8; entrance[1] = 0;
        return entrance; // Works for only "/examples/small.maz.txt"
    }

    public String traverseMaze() throws IOException {
        buildMaze(filePath);
        StringBuilder path = new StringBuilder();

        int[] position = findEntry(maze);
        int direction = Cardinal.EAST.ordinal();
        boolean atExit = false;
        while (!atExit){
            char nextMove = rightHand(position, direction);
            path.append(rightHand(position, direction)); // Append the next position to the output string

            switch(nextMove) {
                case 'F': // Move positions
                    switch (direction) {
                        case 0: position[0]--; break; // NORTH
                        case 1: position[1]++; break; // EAST
                        case 2: position[0]++; break; // SOUTH
                        case 3: position[1]--; break;// WEST
                    }
                    break;
                case 'L': // Update direction
                    direction = (direction + 3) % 4; break; // Left turn
                case 'R': // Update direction
                    direction = (direction + 1) % 4; break; // Right turn
            }

            if (position[1] == maze[0].length) // If the solver has reached the right-most position (where the exit is)
                atExit = true;
        }

        return path.toString();
    }

    private char rightHand(int[] position, int direction) throws IOException {
        return 'F';

    }

    public String testPath(String path) throws IOException { // check if current position is not a wall
        return "a";
    }


}
