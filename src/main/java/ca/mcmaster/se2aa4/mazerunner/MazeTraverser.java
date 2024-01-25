package ca.mcmaster.se2aa4.mazerunner;

import java.io.IOException;

/**
 * MazeTraverser provides methods for traversing a maze based on algorithms
 * and user-inputted paths.
 */
public class MazeTraverser implements MazeTraversal {
    private final MazeBuilder mazeBuilder;
    private String testPath = "";
    private boolean firstTestComplete = false;
    private enum Direction { NORTH, EAST, SOUTH, WEST }

    public MazeTraverser(MazeBuilder mazeBuilder) {
        this.mazeBuilder = mazeBuilder;
    }

    /**
     * Traverses the maze based on a maze algorithm or based on the user-inputted path
     * @return The path from start to finish, or result of traversal (correct path, incorrect path)
     */
    public String traverseMaze() throws IOException {
        mazeBuilder.buildMaze();
        StringBuilder path = new StringBuilder();
        int[][] maze = mazeBuilder.getMaze();
        int[] position;
        Direction direction;

        if (!firstTestComplete) {
            position = mazeBuilder.getEntryWest();
            direction = Direction.EAST;
        } else {
            position = mazeBuilder.getEntryEast();
            direction = Direction.WEST;
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
                    moveForward(position, direction);
                    break;
                case 'Z': // Turn right AND THEN move forward
                    direction = turnRight(direction);
                    moveForward(position, direction);
                    break;
                case 'X': // Turn left AND THEN move forward
                    direction = turnLeft(direction);
                    moveForward(position, direction);
                    break;
                case 'R': // Right turn
                    direction = turnRight(direction);
                    break;
                case 'L': // Left turn
                    direction = turnLeft(direction);
                    break;
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

        return toFactorial(path.toString());
    }

    private Direction turnLeft(Direction current) {
        return Direction.values()[(current.ordinal() + 3) % 4];
    }

    private Direction turnRight(Direction current) {
        return Direction.values()[(current.ordinal() + 1) % 4];
    }

    private void moveForward(int[] position, Direction direction) {
        switch (direction) {
            case NORTH: position[0]--; break;
            case EAST: position[1]++; break;
            case SOUTH: position[0]++; break;
            case WEST: position[1]--; break;
        }
    }

    /**
     * Finds a path through the maze by following the right wall
     * @param position The current position
     * @param direction The current direction
     * @return The next valid move
     */
    private char rightHand(int[] position, Direction direction) {
        int x,y; // Coordinates of the next position

        switch (direction) { // Gather data of the cell to the right
            case NORTH: x = position[0]; y = position[1] + 1; break;
            case EAST: x = position[0] + 1; y = position[1]; break;
            case SOUTH: x = position[0]; y = position[1] - 1; break;
            case WEST: x = position[0] - 1; y = position[1]; break;
            default:
                x = -1; y = -1;
        }

        if (isMoveValid(x,y)) { // 'Z': Turn right, move forward
            return 'Z';
        }

        switch (direction) { // Gather data of the cell forward
            case NORTH: x = position[0] - 1; y = position[1]; break;
            case EAST: x = position[0]; y = position[1] + 1; break;
            case SOUTH: x = position[0] + 1; y = position[1]; break;
            case WEST: x = position[0]; y = position[1] - 1; break;
        }
        if (isMoveValid(x,y)) {
            return 'F';
        }

        switch (direction) { // Gather data of the cell to the left
            case NORTH: x = position[0]; y = position[1] - 1; break;
            case EAST: x = position[0] - 1; y = position[1]; break;
            case SOUTH: x = position[0]; y = position[1] + 1; break;
            case WEST: x = position[0] + 1; y = position[1]; break;
        }
        if (isMoveValid(x,y)) { // 'X': Turn left, move forward
            return 'X';
        }

        return 'R'; // No other moves are valid, turn right
    }


    private boolean isMoveValid(int x, int y) {
        return mazeBuilder.getMaze()[x][y] == 0;
    } // '0' represents PASS, '1' represents WALL

    /**
     * Tests a user-inputted path
     * First traverses from the west side of the maze to east, then vise-versa
     * @param path User inputted path
     * @return Result of traversal (correct path, incorrect path)
     */
    public String testPath(String path) throws IOException {
        String newPath = toCanonical(path); // Change formatting of path if needed (3F = FFF)
        if (newPath.equals("incorrect path")) {
            return "incorrect path";
        }

        testPath = newPath;
        if (traverseMaze().equals("correct path"))
            return "correct path";

        firstTestComplete = true;
        testPath = newPath;
        if (traverseMaze().equals("correct path"))
            return "correct path";

        return "incorrect path";
    }

    /**
     * Changes the formatting of text to a canonical form
     * e.g. 11F = FFF...FF (11 instances of F), 3F = FFF
     * @param path The original path
     * @return The path in a canonical format
     */
    private String toCanonical(String path) throws StringIndexOutOfBoundsException {
        StringBuilder newPath = new StringBuilder();
        int i = 0;

        while (i < path.length()) {
            char current = path.charAt(i);

            // If the character is a digit, take that value into account
                // then check for future digits then convert to a canonical format
            if (Character.isDigit(current)) {
                StringBuilder value = new StringBuilder();

                while (Character.isDigit(path.charAt(i))) {
                    current = path.charAt(i);
                    value.append(current);
                    i++;
                }

                String stringValue = value.toString();
                int multiplier = Integer.parseInt(stringValue);
                String output = String.valueOf(path.charAt(i));
                output = output.repeat(multiplier);
                newPath.append(output);

            } else if (current == 'F' || current == 'R' || current == 'L') {
                newPath.append(current);
            } else if (!Character.isWhitespace(current)) { // incorrect inputs are not accepted
                return "incorrect path";
            }

            i++;
        }

        return newPath.toString();
    }

    /**
     * Changes the formatting of text to a factorized form
     * e.g. FFF...FF (11 instances of F) = 11F , FFF = 3F
     * @param path The original path
     * @return The path in a factorized format
     */
    private String toFactorial(String path) {
        StringBuilder newPath = new StringBuilder();
        int i = 0;
        char next = 0;

        while (i < path.length() - 1) {
            int multiplier = 1;
            next = path.charAt(i + 1);
            char current = path.charAt(i);

            while (current == next && i < path.length() - 2) {
                i++;
                multiplier+=1;
                next = path.charAt(i+1);
            }

            String output = String.valueOf(current);

            // If multiplier < 2, append the original characters
            // since 2F = FF (lengths are equal), 1F = F (factorized form is larger)
            if (multiplier < 2) {
                newPath.append(output.repeat(multiplier));
            } else {
                newPath.append(multiplier);
                newPath.append(output);
            }
            newPath.append(" ");
            i++;
        }

        newPath.append(next);
        return newPath.toString();
    }
}