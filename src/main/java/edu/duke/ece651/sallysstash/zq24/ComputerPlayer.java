package edu.duke.ece651.sallysstash.zq24;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

/**
 * This class represents the computer.
 */
public class ComputerPlayer extends Player {

    public ComputerPlayer(String name, String opponentName) {
        super(name, opponentName);
    }

    /**
     * This method allows a computer player to randomly choose an action.
     *
     * @return a randomly-chose action
     */
    public String randomActionSelect() {
        List<String> actions = Arrays.asList("m", "s", "d");
        Random random = new Random();
        return actions.get(random.nextInt(actions.size()));
    }

    /**
     * This method allows a computer player to randomly choose a coordinate.
     *
     * @return a randomly-chose coordinate.
     */
    public String randomCoordinateGuessSelect() {
        return getCoordinate();
    }

    private String getCoordinate() {
        List<Character> alphabet = Arrays.asList('A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K'
                , 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T');
        List<Integer> number = Arrays.asList(0, 1, 2, 3, 4, 5, 6, 7, 8, 9);
        Random random = new Random();
        char randomChar = alphabet.get(random.nextInt(alphabet.size()));
        int randomInt = number.get(random.nextInt(number.size()));
        return String.valueOf(randomChar) + randomInt;
    }

    /**
     * This method allows a computer player to randomly choose a coordinate and an orientation.
     *
     * @param color color of the stash; depend on the color, the available orientations would
     *              be different.
     * @return a randomly-chose coordinate and an orientation.
     */
    public String randomCoordinatePlacementSelect(String color) {
        List<Character> rectangleOrientation = Arrays.asList('h', 'v');
        List<Character> crazySuperOrientation = Arrays.asList('u', 'd', 'l', 'r');
        Random random = new Random();
        if (color.equals("Green") || color.equals("Purple")) {
            return getCoordinate() + rectangleOrientation
                    .get(random.nextInt(rectangleOrientation.size()));
        } else {
            return getCoordinate() + crazySuperOrientation
                    .get(random.nextInt(crazySuperOrientation.size()));
        }
    }
}