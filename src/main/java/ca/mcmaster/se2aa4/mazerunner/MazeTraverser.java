package ca.mcmaster.se2aa4.mazerunner;

import java.io.IOException;

public class MazeTraverser {
    private final MazeBuilder mazeBuilder;

    public MazeTraverser(MazeBuilder mazeBuilder) {
        this.mazeBuilder = mazeBuilder;
    }

    public String traverseMaze() throws IOException {
        mazeBuilder.buildMaze();
        int[][] maze = mazeBuilder.getMaze();
        int[] position = mazeBuilder.getEntry();
        StringBuilder path = new StringBuilder();

        int direction = 1;
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
        return mazeBuilder.getMaze()[x][y] == 0;
    }

    public String testPath(String path) throws IOException { // check if current position is not a wall
        return "a";
    }
}
