package edu.duke.ece651.sallysstash.zq24;

import java.util.*;

public class Driver {

    public static void main(String[] args) {

        Scanner in = new Scanner(System.in);
        Player playerA = getPlayer("Player A", "Player B", in);
        Player playerB = getPlayer("Player B", "Player A", in);

        if (!(playerA instanceof ComputerPlayer)) {
            welcomeMessage(playerA.getName(), playerB.getName());
            playerA.getBoard().printBoard();
        }
        placingAllStacks(playerA, in);

        if (!(playerB instanceof ComputerPlayer)) {
            welcomeMessage(playerB.getName(), playerA.getName());
            playerB.getBoard().printBoard();
        }
        placingAllStacks(playerB, in);

        while (true) {
            playerTurn(playerA, playerB, in);
            if (checkStatus(playerA, playerB)) break;

            playerTurn(playerB, playerA, in);
            if (checkStatus(playerA, playerB)) break;
        }
    }

    /**
     * This method allows player as a human or to be played by computer.
     *
     * @param player   player's name
     * @param opponent opponent player's name
     * @param in       produces values scanned from the specified input stream
     * @return either a human player or a computer player.
     */
    private static Player getPlayer(String player, String opponent, Scanner in) {
        System.out.println("----------------------------------------------------------------\n"
                + player + " is a Human or to be played by the computer?\n" +
                "type H, h or human will be played as human. Otherwise will be played by the computer.\n"
                + "----------------------------------------------------------------\n");
        String type = in.nextLine().toLowerCase().trim();
        if (type.equals("h") || type.equals("human")) {
            return new Player(player, opponent);
        } else {
            return new ComputerPlayer(player, opponent);
        }
    }

    /**
     * This method lets player to choose and perform that action in his/her turn.
     *
     * @param you      the player
     * @param opponent the opponent player
     * @param in       produces values scanned from the specified input stream
     */
    private static void playerTurn(Player you, Player opponent, Scanner in) {
        while (true) {
            Action action = actionSelect(you, in);
            if (action == Action.DIG && guessLocation(you, opponent, in))
                break;
            if (action == Action.MOVE && moveStack(you, in))
                break;
            if (action == Action.SCAN && sonarScan(you, opponent, in))
                break;
        }
    }

    /**
     * This method performs the sonar scan operation.
     *
     * @param player   the player
     * @param opponent the opponent player
     * @param in       produces values scanned from the specified input stream
     * @return true if the sonar scan is successfully performed, otherwise return false.
     */
    private static boolean sonarScan(Player player, Player opponent, Scanner in) {
        String center;
        if (!(player instanceof ComputerPlayer)) {
            System.out.println("Please type in a center coordinate for sonar scan");
            center = in.nextLine().trim().toLowerCase();
        } else {
            center = ((ComputerPlayer) player).randomCoordinateGuessSelect().toLowerCase().trim();
        }
        if (!center.matches("[a-t][0-9]")) {
            System.out.println("Center coordinate is not valid, please choose a valid coordinate");
            return false;
        }
        int row = center.charAt(0) - 'a';
        int col = center.charAt(1) - '0';
        ArrayList<int[]> sonarScanArea = calculateSonarScanArea(row, col);
        Map<Character, Integer> report = new HashMap<>();
        for (int[] coordinate : sonarScanArea) {
            try {
                char color = opponent.getBoard().getActualBoard()[coordinate[0]][coordinate[1]].getContent();
                if (color != ' ')
                    report.put(color, report.getOrDefault(color, 0) + 1);
            } catch (IndexOutOfBoundsException e) {
                //System.err.println("current coordinate, row: " + coordinate[0] + " , row: " + coordinate[1]);
            }
        }
        if (!(player instanceof ComputerPlayer))
            System.out.println(scanReport(report));
        player.setScanRemainTimes(player.getScanRemainTimes() - 1);
        if (player instanceof ComputerPlayer)
            System.out.println("----------------------------------------------------------------\n"
                    + player.getName() + " used a special action\n"
                    + "----------------------------------------------------------------\n");
        return true;
    }

    /**
     * This method generates the sonar scan report in string.
     *
     * @param report the collected scan information
     * @return sonar scan report in string
     */
    private static String scanReport(Map<Character, Integer> report) {
        StringBuilder sb = new StringBuilder();
        sb.append("----------------------------------------------------------------\n");
        Map<Character, String> stackReport = new HashMap<>();
        stackReport.put('G', "Green");
        stackReport.put('P', "Purple");
        stackReport.put('R', "Red");
        stackReport.put('B', "Blue");
        for (Map.Entry<Character, Integer> entry : report.entrySet()) {
            sb.append(stackReport.get(entry.getKey())).append(" stacks occupy ")
                    .append(entry.getValue());
            if ((entry.getValue() == 1)) {
                sb.append(" square");
            } else {
                sb.append(" squares");
            }
        }
        sb.append("\n----------------------------------------------------------------\n");
        return sb.toString();
    }

    /**
     * This method calculates the coordinates of the scan area.
     *
     * @param row y coordinate of the center
     * @param col x coordinate of the center
     * @return coordinates of the scan area in a list.
     */
    private static ArrayList<int[]> calculateSonarScanArea(int row, int col) {
        ArrayList<int[]> area = new ArrayList<>();
        area.add(new int[]{row - 3, col});
        for (int i = col - 1; i <= col + 1; i++) {
            area.add(new int[]{row - 2, i});
        }
        for (int i = col - 2; i <= col + 2; i++) {
            area.add(new int[]{row - 1, i});
        }
        for (int i = col - 3; i <= col + 3; i++) {
            area.add(new int[]{row, i});
        }
        for (int i = col - 2; i <= col + 2; i++) {
            area.add(new int[]{row + 1, i});
        }
        for (int i = col - 1; i <= col + 1; i++) {
            area.add(new int[]{row + 2, i});
        }
        area.add(new int[]{row + 3, col});
        return area;
    }

    /**
     * This method performs the MOVE operation.
     *
     * @param player the player
     * @param in     produces values scanned from the specified input stream.
     * @return true if MOVE operation is successfully performed.
     */
    private static boolean moveStack(Player player, Scanner in) {
        String command;
        if (!(player instanceof ComputerPlayer)) {
            System.out.println("Which stack do you want to move? Please type any " +
                    "coordinate which is a part of that stack");
            command = in.nextLine().trim().toLowerCase();
        } else {
            command = ((ComputerPlayer) player).randomCoordinateGuessSelect().toLowerCase().trim();
        }
        if (!command.matches("[a-t][0-9]")) {
            System.out.println("Invalid location. Please type a valid location");
            return false;
        }
        String location = (command.charAt(0) - 'a') + " " + command.charAt(1);
        if (!player.getAllStashesInfo().containsKey(location)) {
            if (!(player instanceof ComputerPlayer))
                System.out.println("You did not have stash here!");
            return false;
        }
        Shape shape = player.getAllStashesInfo().get(location);
        String color = shape.color;
        String directionRegex = (color.equals("Green") || color.equals("Purple"))
                ? "[a-t][0-9][vh]" : "[a-t][0-9][urdl]";
        String newLocation;
        if (!(player instanceof ComputerPlayer)) {
            System.out.println("Choose a new location where you want to place this stash");
            newLocation = in.nextLine().trim().toLowerCase();
        } else {
            newLocation = ((ComputerPlayer) player).randomCoordinatePlacementSelect(color).toLowerCase().trim();
        }
        if (!newLocation.matches(directionRegex)) {
            System.out.println("Invalid new location. Please type a new valid location");
            return false;
        }
        Shape newShape = generateRequiredShape(shape.color, newLocation.charAt(0),
                newLocation.charAt(1), newLocation.charAt(2));
        newShape.generateShape();
        if (!newShape.verifyShape()) {
            if (!(player instanceof ComputerPlayer))
                System.out.println("Part of the stash will not be within the display board. Please " +
                        "choose another location");
            return false;
        }
        newShape.encodeCoordinates();
        if (!moveCollisionCheck(shape, newShape, player)) {
            if (!(player instanceof ComputerPlayer))
                System.out.println("This stash will collide with one of your previous stashes. " +
                        "Please choose another location to place your stash");
            return false;
        }
        updatePlayerActualBoardAfterMoving(shape, newShape, player);
        player.setMoveRemainTimes(player.getMoveRemainTimes() - 1);
        if (player instanceof ComputerPlayer)
            System.out.println("----------------------------------------------------------------\n"
                    + player.getName() + " used a special action\n"
                    + "----------------------------------------------------------------\n");
        return true;
    }

    /**
     * This method lets player choose one action to perform.
     *
     * @param player player
     * @return the action that player wants to perform.
     */
    private static Action actionSelect(Player player, Scanner in) {
        if (!(player instanceof ComputerPlayer)) {
            player.getBoard().printAllBoard(player.getName(), player.getOpponentName());
            System.out.println("----------------------------------------------------------------");
            System.out.println("Possible actions for " + player.getName() + ": \n");
            System.out.println("D Dig in a square");
            System.out.println("M Move a stack to another square (" + player.getMoveRemainTimes()
                    + " remaining)");
            System.out.println("S Sonar scan (" + player.getScanRemainTimes() + " remaining)\n");
            System.out.println(player.getName() + ", what would you like to do?");
            System.out.println("----------------------------------------------------------------\n");
            while (true) {
                String command = in.nextLine().trim().toLowerCase();
                Action action = actionSelectionValidation(command, player);
                if (action != Action.INVALID) {
                    return action;
                }
            }
        } else {
            while (true) {
                Action action = actionSelectionValidation(((ComputerPlayer) player).randomActionSelect(), player);
                if (action != Action.INVALID) {
                    return action;
                }
            }
        }
    }

    /**
     * This method verifies if the input is a valid action command (Dig, Move or Scan)
     *
     * @param command input action command
     * @param player  the player
     * @return the corresponding Action Enum.
     */
    private static Action actionSelectionValidation(String command, Player player) {
        if (command.matches("[m]")) {
            if (player.getMoveRemainTimes() <= 0) {
                if (!(player instanceof ComputerPlayer))
                    System.out.println("You have already used up all your move opportunities");
                return Action.INVALID;
            } else {
                return Action.MOVE;
            }
        } else if (command.matches("[s]")) {
            if (player.getScanRemainTimes() <= 0) {
                if (!(player instanceof ComputerPlayer))
                    System.out.println("You have already used up all your scan opportunities");
                return Action.INVALID;
            } else {
                return Action.SCAN;
            }
        } else {
            if (!command.matches("[d]")) {
                System.out.println("Action command is not valid. Please type in a valid action command");
                return Action.INVALID;
            } else {
                return Action.DIG;
            }
        }
    }

    /**
     * This method checks if one of the player wins the game.
     *
     * @param playerA the first player
     * @param playerB the second player
     * @return true if one of hte player wins the game, otherwise return false.
     */
    private static boolean checkStatus(Player playerA, Player playerB) {
        if (playerA.getCoordinatesOfAllShapesInString().isEmpty()) {
            System.out.println("Player B Win!!!");
            return true;
        }

        if (playerB.getCoordinatesOfAllShapesInString().isEmpty()) {
            System.out.println("Player A Win!!!");
            return true;
        }
        return false;
    }

    /**
     * This method allows player to place all the available stashes on the board.
     *
     * @param player the player
     * @param in     produces values scanned from the specified input stream.
     */
    private static void placingAllStacks(Player player, Scanner in) {
        placingStack("Green", "first", player, in);
        placingStack("Green", "second", player, in);
        placingStack("Purple", "first", player, in);
        placingStack("Purple", "Second", player, in);
        placingStack("Purple", "Third", player, in);
        placingStack("Red", "first", player, in);
        placingStack("Red", "Second", player, in);
        placingStack("Red", "Third", player, in);
        placingStack("Blue", "first", player, in);
        placingStack("Blue", "second", player, in);
    }

    /**
     * The method prints out the welcome message when game starts
     *
     * @param playerOne name of the first player.
     * @param playerTwo name of the second player.
     */
    private static void welcomeMessage(String playerOne, String playerTwo) {
        System.out.println("----------------------------------------------------------------------------------");
        System.out.println(playerOne + ", you are going place Sally’s stash on the board. Make sure " +
                playerTwo + " isn’t\n" +
                "looking! For Green and Purple stacks, type the coordinate of the upper left side of the stash,\n" +
                "followed by either H (for horizontal) or V (for vertical). For example, M4H would\n" +
                "place a stack horizontally starting at M4 and going to the right. For Red and Purple stacks, \n" +
                "they have four available orientations to choose - Up (U), Right (R), Down (D), and left (L).\n" +
                "You have\n" +
                "2 Green stacks that are 1x2\n" +
                "3 Purple stacks that are 1x3\n" +
                "3 Red \"Superstacks\"\n" +
                "2 Blue \"Crazystacks\"");
        System.out.println("----------------------------------------------------------------------------------\n");
    }

    /**
     * This method is for placing one of the stack on the board.
     *
     * @param color  color of the stash.
     * @param number the number of the stash. (e.g: first, second, third ...)
     * @param player the player
     * @param in     produces values scanned from the specified input stream.
     */
    private static void placingStack(String color, String number, Player player, Scanner in) {
        if (!(player instanceof ComputerPlayer))
            System.out.println("----------------------------------------------------------------------------------\n"
                    + player.getName() + ", where do you want to place the " + number + " " + color + " stack?\n"
                    + "----------------------------------------------------------------------------------\n");
        boolean valid = false;
        while (!valid) {
            String command;
            if (!(player instanceof ComputerPlayer)) {
                command = in.nextLine().toLowerCase().trim();
            } else {
                command = ((ComputerPlayer) player).randomCoordinatePlacementSelect(color).toLowerCase().trim();
            }
            String directionRegex = (color.equals("Green") || color.equals("Purple"))
                    ? "[a-t][0-9][vh]" : "[a-t][0-9][urdl]";
            if (!command.matches(directionRegex)) {
                System.out.println("command format is not correct. Please enter a valid command");
                continue;
            }
            Shape shape = generateRequiredShape(color, command.charAt(0), command.charAt(1), command.charAt(2));
            shape.generateShape();
            if (!shape.verifyShape()) {
                if (!(player instanceof ComputerPlayer))
                    System.out.println("Part of the stash will not be within the display board. Please" +
                            " choose another location");
                continue;
            }
            shape.encodeCoordinates();
            if (!collisionCheck(player, shape)) {
                if (!(player instanceof ComputerPlayer))
                    System.out.println("This stash will collide with one of your previous stashes." +
                            " Please choose another location to place your stash");
                continue;
            }
            player.getAllStashes().add(shape);
            updatePlayerActualBoard(player, shape);
            valid = true;
            if (!(player instanceof ComputerPlayer))
                player.getBoard().printBoard();
        }
    }

    /**
     * This method is for checking if there is any collision after moving a stash to
     * a new location.
     *
     * @param oldShape shape that consists of all the old coordinates.
     * @param newShape shape that consists of all the new coordinates.
     * @param player   the player
     * @return true if no collision would happen, otherwise return false.
     */
    private static boolean moveCollisionCheck(Shape oldShape, Shape newShape, Player player) {
        Map<String, Shape> newShapeInfo = new HashMap<>();
        for (String coordinate : newShape.getShapeInString()) {
            if (player.getAllStashesInfo().containsKey(coordinate) &&
                    !oldShape.getShapeInString().contains(coordinate)) {
                return false;
            } else {
                newShapeInfo.put(coordinate, newShape);
            }
        }

        for (String coordinate : oldShape.getShapeInString()) {
            player.getAllStashesInfo().remove(coordinate);
        }

        for (int i = 0; i < oldShape.getShapeInString().size(); i++) {
            String oldShapeCoordinate = oldShape.getShapeInString().get(i);
            if (player.getCoordinatesOfAllShapesInString().remove(oldShapeCoordinate)) {
                player.getCoordinatesOfAllShapesInString().add(newShape.getShapeInString().get(i));
            }
        }
        player.getAllStashesInfo().putAll(newShapeInfo);
        return true;
    }

    /**
     * This method updates the player's own board if the move operation would not
     * cause the collision.
     *
     * @param oldShape shape that consists of all the old coordinates.
     * @param newShape shape that consists of all the new coordinates.
     * @param player   the player
     */
    private static void updatePlayerActualBoardAfterMoving(Shape oldShape, Shape newShape, Player player) {
        int numOfCoordinates = oldShape.shape.size();
        char[] boardMark = new char[numOfCoordinates];
        for (int i = 0; i < numOfCoordinates; i++) {
            boardMark[i] = player.getBoard()
                    .getActualBoard()[oldShape.shape.get(i)[0]][oldShape.shape.get(i)[1]].getContent();
            player.getBoard()
                    .getActualBoard()[oldShape.shape.get(i)[0]][oldShape.shape.get(i)[1]].setContent(' ');
        }
        for (int i = 0; i < boardMark.length; i++) {
            player.getBoard()
                    .getActualBoard()[newShape.shape.get(i)[0]][newShape.shape.get(i)[1]].setContent(boardMark[i]);
        }
    }

    /**
     * This method is used when placing stashes; it checks if stash would collide with existing stashes.
     *
     * @param player the player
     * @param shape  a new stash
     * @return true if no collision would happen; otherwise return false
     */
    private static boolean collisionCheck(Player player, Shape shape) {
        Set<String> addShape = new HashSet<>();
        Map<String, Shape> addShapeInfo = new HashMap<>();
        for (String coordinate : shape.getShapeInString()) {
            if (player.getCoordinatesOfAllShapesInString().contains(coordinate)) {
                return false;
            } else {
                addShape.add(coordinate);
                addShapeInfo.put(coordinate, shape);
            }
        }
        player.getCoordinatesOfAllShapesInString().addAll(addShape);
        player.getAllStashesInfo().putAll(addShapeInfo);
        return true;
    }

    /**
     * This method updates the player's own board after the generated shape being verified
     * and being checked no collision would happen.
     *
     * @param player the player
     * @param shape  the generated shape
     */
    private static void updatePlayerActualBoard(Player player, Shape shape) {
        for (int[] coordinate : shape.shape) {
            int row = coordinate[0];
            int col = coordinate[1];
            player.getBoard().getActualBoard()[row][col].setContent(shape.color.charAt(0));
        }
    }

    /**
     * This method generates the required-shape stash.
     *
     * @param color     color of the stash.
     * @param row       y coordinate
     * @param col       x coordinate
     * @param direction orientation of the stash
     * @return the required-shape stash
     */
    private static Shape generateRequiredShape(String color, char row, char col, char direction) {
        if (color.equals("Green")) {
            return new Rectangle(row - 'a', col - '0', color, direction, 1, 2);
        } else if (color.equals("Purple")) {
            return new Rectangle(row - 'a', col - '0', color, direction, 1, 3);
        } else if (color.equals("Red")) {
            return new SuperStack(row - 'a', col - '0', color, direction);
        } else {
            return new CrazyStack(row - 'a', col - '0', color, direction);
        }
    }

    /**
     * This method allows player to guess the stash location of opponent player (Dig operation).
     *
     * @param you      the player
     * @param opponent the opponent player
     * @param in       produces values scanned from the specified input stream
     * @return true if Dig operation is successfully completed. Otherwise, return false.
     */
    private static boolean guessLocation(Player you, Player opponent, Scanner in) {
        String command;
        if (!(you instanceof ComputerPlayer)) {
            //you.getBoard().printAllBoard(you.getName(), opponent.getName());
            System.out.println("Now take a guess!");
            command = in.nextLine().toLowerCase().trim();
        } else {
            command = ((ComputerPlayer) you).randomCoordinateGuessSelect().toLowerCase().trim();
        }
        if (!command.matches("[a-t][0-9]")) {
            System.out.println("command format is not correct. Please enter a valid command");
            return false;
        }
        char row = command.charAt(0);
        char col = command.charAt(1);
        String coordinate = (row - 'a') + " " + col;
        if (!opponent.getAllStashesInfo().containsKey(coordinate)) {
            updateYourHitMissBoard(you, row - 'a', col - '0');
            return true;
        }
        opponent.getCoordinatesOfAllShapesInString().remove(coordinate);
        System.out.println("----------------------------------------------------------------\n"
                + you.getName() + " found a stack at " + command + "!\n"
                + "----------------------------------------------------------------\n");
        Shape shape = opponent.getAllStashesInfo().get(coordinate);
        updateYourHitMissBoard(you, row - 'a', col - '0', shape.color.charAt(0));
        updateOpponentActualBoard(opponent, row - 'a', col - '0');
        return true;
    }

    /**
     * This method updates player's hit/miss board when he/she find opponent player's
     * a part of stashes.
     *
     * @param player the player
     * @param row    y coordinate
     * @param col    x coordinate
     * @param color  stash's color
     */
    private static void updateYourHitMissBoard(Player player, int row, int col, char color) {
        player.getBoard().getHitMissBoard()[row][col].setContent(color);
    }

    /**
     * This method updates player's hit/miss board when he/she failed to find
     * opponent's stash.
     *
     * @param player the player
     * @param row    y coordinate
     * @param col    x coordinate
     */
    private static void updateYourHitMissBoard(Player player, int row, int col) {
        System.out.println("----------------------------------------------------------------\n"
                + player.getName() + " missed!\n"
                + "----------------------------------------------------------------\n");
        player.getBoard().getHitMissBoard()[row][col].setContent('X');
    }

    /**
     * This method updates opponent's own board when his/her own stash is found by
     * the current player.
     *
     * @param opponent the opponent player.
     * @param row      the y coordinate
     * @param col      the x coordinate
     */
    private static void updateOpponentActualBoard(Player opponent, int row, int col) {
        opponent.getBoard().getActualBoard()[row][col].setContent('*');
    }
}