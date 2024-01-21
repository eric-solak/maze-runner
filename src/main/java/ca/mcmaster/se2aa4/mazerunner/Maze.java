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
        while (position[1] < maze[1].length - 1) {
            char nextMove = rightHand(position, direction);

            switch(nextMove) {
                case 'F': // Move forward
                    switch (direction) {
                        case 0: position[0]--; break; // NORTH
                        case 1: position[1]++; break; // EAST
                        case 2: position[0]++; break; // SOUTH
                        case 3: position[1]--; break; // WEST
                    }
                    break;
                case 'Z': // Turn right AND THEN move forward
                    direction = (direction + 1) % 4;
                    switch (direction) {
                        case 0: position[0]--; break; // NORTH
                        case 1: position[1]++; break; // EAST
                        case 2: position[0]++; break; // SOUTH
                        case 3: position[1]--; break; // WEST
                    }
                    break;
                case 'X': // Turn left AND THEN move forward
                    direction = (direction + 3) % 4;
                    switch (direction) {
                        case 0: position[0]--; break; // NORTH
                        case 1: position[1]++; break; // EAST
                        case 2: position[0]++; break; // SOUTH
                        case 3: position[1]--; break; // WEST
                    }
                    break;
                case 'R': // Update direction
                    direction = (direction + 1) % 4; break; // Right turn
                case 'L': // Update direction
                    direction = (direction + 3) % 4; break; // Left turn

            }

            // Append the updated position to the output
            if (nextMove == 'Z') {
                path.append("RF");
            }
            else if (nextMove == 'X') {
                path.append("LF"); // Append the updated position to the output
            }
            else {
                path.append(nextMove);
            }

        }

        return path.toString();
    }

    private char rightHand(int[] position, int direction) throws IOException {
        int x,y; // Coordinates of the next position

        switch (direction) { // Gather data of the cell to the right
            case 0: x = position[0]; y = position[1] + 1; break; // NORTH
            case 1: x = position[0] + 1; y = position[1]; break; // EAST
            case 2: x = position[0]; y = position[1] - 1; break; // SOUTH
            case 3: x = position[0] - 1; y = position[1]; break; // WEST
            default:
                x = -1; y = -1;
        }
        if (isMoveValid(x,y)) { // Check if turning right leads to an open cell, if so, return 'Z': Turn right, move forward
            return 'Z';
        }

        switch (direction) { // Check if moving forward leads to an open cell
            case 0: x = position[0] - 1; y = position[1]; break; // NORTH
            case 1: x = position[0]; y = position[1] + 1; break; // EAST
            case 2: x = position[0] + 1; y = position[1]; break; // SOUTH
            case 3: x = position[0]; y = position[1] - 1; break; // WEST
        }
        if (isMoveValid(x,y)) { // Check if turning right leads to an open cell, if so, return 'Z': Turn right, move forward
            return 'F';
        }

        switch (direction) { // Check if moving left leads to an open cell, if so, return 'X': Turn left, move forward
            case 0: x = position[0]; y = position[1] - 1; break; // NORTH
            case 1: x = position[0] - 1; y = position[1]; break; // EAST
            case 2: x = position[0]; y = position[1] + 1; break; // SOUTH
            case 3: x = position[0] + 1; y = position[1]; break; // WEST
        }

        if (isMoveValid(x,y)) {
            return 'X'; // If move is not valid, turn right
        }

        return 'R'; // No other moves are valid, turn right
    }

    private boolean isMoveValid(int x, int y) {
        // '0' represents PASS, '1' represents WALL
            // True if the position is PASS, False otherwise.
        return maze[x][y] == 0;
    }

    public String testPath(String path) throws IOException { // check if current position is not a wall
        return "a";
    }


}
