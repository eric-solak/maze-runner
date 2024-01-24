package ca.mcmaster.se2aa4.mazerunner;

import java.io.IOException;

public class MazeTraverser {
    private final MazeBuilder mazeBuilder;
    private String testPath = "";
    private boolean firstTestComplete = false;

    public MazeTraverser(MazeBuilder mazeBuilder) {
        this.mazeBuilder = mazeBuilder;
    }

    // Traverses the maze based on a maze algorithm or
        // traverses based on the users inputted path
    public String traverseMaze() throws IOException {
        mazeBuilder.buildMaze();
        StringBuilder path = new StringBuilder();
        int[][] maze = mazeBuilder.getMaze();
        int[] position;
        int direction;
        if (!firstTestComplete) {
            position = mazeBuilder.getEntryWest();
            direction = 1; // Face east
        }
        else {
            position = mazeBuilder.getEntryEast();
            direction = 3; // Face west
        }

        boolean testPathTrue = false;
        while ((position[1] < maze[0].length - 1 && !firstTestComplete) || (position[1] > 0 && firstTestComplete)) {
            char nextMove;

            // If testPath is not empty, use moves from testPath
                // otherwise use moves from rightHand
            if (!testPath.isEmpty() || testPathTrue) {
                if (testPath.isEmpty()) { // If the user-inputted path stops short of the exit
                    return "incorrect path";
                }
                testPathTrue = true;
                nextMove = testPath.charAt(0);
                testPath = testPath.substring(1);
            } else {
                nextMove = rightHand(position, direction);
            }

            switch (nextMove) {
                case 'F': // Move forward
                    moveForward(position, direction); break;
                case 'Z': // Turn right AND THEN move forward
                    direction = (direction + 1) % 4; moveForward(position, direction); break;
                case 'X': // Turn left AND THEN move forward
                    direction = (direction + 3) % 4; moveForward(position, direction); break;
                case 'R': // Update direction
                    direction = (direction + 1) % 4; break; // Right turn
                case 'L': // Update direction
                    direction = (direction + 3) % 4; break; // Left turn
            }

            if (!testPathTrue) { // Append the updated position if the user is finding a path
                if (nextMove == 'Z') {
                    path.append("RF");
                } else if (nextMove == 'X') {
                    path.append("LF");
                } else {
                    path.append(nextMove);
                }
            } else {
                if (!isMoveValid(position[0], position[1])) {
                    return "incorrect path";
                }
            }

        } // end of "while" loop

        if (testPathTrue) {
            return "correct path";
        }

        return path.toString();
    }

    // Helper method to move forward based on the current direction
    private void moveForward(int[] position, int direction) {
        switch (direction) {
            case 0: position[0]--; break; // NORTH
            case 1: position[1]++; break; // EAST
            case 2: position[0]++; break; // SOUTH
            case 3: position[1]--; break; // WEST
        }
    }

    // rightHand maze traversal algorithm
        // Finds a path through the maze by following the right wall
    private char rightHand(int[] position, int direction) {
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

        switch (direction) { // Gather data of the cell forward
            case 0: x = position[0] - 1; y = position[1]; break; // NORTH
            case 1: x = position[0]; y = position[1] + 1; break; // EAST
            case 2: x = position[0] + 1; y = position[1]; break; // SOUTH
            case 3: x = position[0]; y = position[1] - 1; break; // WEST
        }
        if (isMoveValid(x,y)) { // Check if turning right leads to an open cell, if so, return 'Z': Turn right, move forward
            return 'F';
        }

        switch (direction) { // Gather data of the cell to the left
            case 0: x = position[0]; y = position[1] - 1; break; // NORTH
            case 1: x = position[0] - 1; y = position[1]; break; // EAST
            case 2: x = position[0]; y = position[1] + 1; break; // SOUTH
            case 3: x = position[0] + 1; y = position[1]; break; // WEST
        }
        if (isMoveValid(x,y)) { // Check if moving left leads to an open cell, if so, return 'X': Turn left, move forward
            return 'X';
        }

        return 'R'; // No other moves are valid, turn right
    }

    // Returns true if the position is PASS, False otherwise.
        // '0' represents PASS, '1' represents WALL
    private boolean isMoveValid(int x, int y) {
        return mazeBuilder.getMaze()[x][y] == 0;
    }

    // Tests the paths which the user enters
        // First traverses from the west side of the maze to east
        // then traverses from east to west
    public String testPath(String path) throws IOException {
        String newPath = toCanonical(path); // Change formatting of path if needed (3F = FFF)
        testPath = newPath;

        if (traverseMaze().equals("correct path"))
            return "correct path";

        firstTestComplete = true;
        testPath = newPath;
        if (traverseMaze().equals("correct path"))
            return "correct path";

        return "incorrect path";
    }

    // Changes the formatting of text to a canonical form
        // e.g. 11F = FFF...FF (11 instances of F), 3F = FFF
    private String toCanonical(String path) throws StringIndexOutOfBoundsException {
        StringBuilder newPath = new StringBuilder();
        int i = 0;

        while (i < path.length()) {
            char current = path.charAt(i);

            // If the character is a digit, take that value into account
                // check for future digits (and multiply to get the correct value)
                // then convert to a canonical format
            if (Character.isDigit(current)) {
                StringBuilder value = new StringBuilder();
                while (Character.isDigit(path.charAt(i))) {
                    value.append(path.charAt(i));
                    i++;
                }
                int multiplier = Integer.parseInt(value.toString());
                newPath.append(String.valueOf(path.charAt(i)).repeat(multiplier));

            // Else if there is no digit, append the character to the newPath
                // The "else if" allows for improper characters to be ignored
            } else if (current == 'F' || current == 'R' || current == 'L') {
                newPath.append(current);
            }
            i++;
        }
        return newPath.toString();
    }

}