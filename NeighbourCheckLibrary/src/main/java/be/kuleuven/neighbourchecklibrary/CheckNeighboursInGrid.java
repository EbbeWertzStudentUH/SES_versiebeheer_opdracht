package be.kuleuven.neighbourchecklibrary;


import be.kuleuven.neighbourchecklibrary.exceptions.GridSizeNotMatchException;

import java.util.ArrayList;
import java.util.HashMap;


public class CheckNeighboursInGrid {

    /**
     * This method takes a 1D Iterable and an element in the array and gives back an iterable containing the indexes of all neighbours with the same value as the specified element
     *@return - Returns a 1D Iterable of ints, the Integers represent the indexes of all neighbours with the same value as the specified element on index 'indexToCheck'.
     *@param grid - This is a 1D Iterable containing all elements of the grid. The elements are integers.
     *@param width - Specifies the width of the grid.
     *@param height - Specifies the height of the grid (extra for checking if 1D grid is complete given the specified width)
     *@param indexToCheck - Specifies the index of the element which neighbours that need to be checked
     */
    public static Iterable<Integer> getSameNeighboursIds(Iterable<Integer> grid ,int width, int height, int indexToCheck){
        //(behalve de filter func) geen private helper functies want die zouden de grid size nodig hebben,
        // die ik pas weet na het eind van de while.
        //als ik hiervoor een aparte loop maak of de iterable omvorm naar array is het minder efficient.
        //hashmap: key=index, value=value
        final HashMap<Integer, Integer> neighbours = new HashMap<>();
        int currentIndex = 0;
        int valueOnIndexToCheck = 0;
        //y neighbour = index +- width
        //x neighbour = index +- 1
        //iterator() kan niet tijdens loop ge-called want dat reset de interne cursor
        for (int currentValue : grid) {

            if (indexToCheck == currentIndex) {
                valueOnIndexToCheck = currentValue;
            }
            for (byte direction : new byte[]{-1, 1}) {
                boolean xNeighbour = currentIndex == indexToCheck + direction;
                boolean yNeighbour = currentIndex == indexToCheck + direction * width;
                boolean notWrapping = currentIndex % width == indexToCheck % width + direction;
                if (yNeighbour || (xNeighbour && notWrapping)) {
                    neighbours.put(currentIndex, currentValue);
                }
            }
            currentIndex++;
        }
        final int size = currentIndex;
        //error als dimenties niet kloppen
        if(size != width*height){   //current index = size van de grid na de while loop klaar is
            throw new GridSizeNotMatchException(width, height, size);
        }
        //verwijder indexen zonder zelfde value of buiten grid size:
        return filterOnEqualValue(neighbours, valueOnIndexToCheck);
    }

    private static ArrayList<Integer> filterOnEqualValue(HashMap<Integer, Integer> neighbours, int valueOnIndexToCheck) {
        ArrayList<Integer> result = new ArrayList<>();
        //return niet keyset van hashmap want hashmap kan tijdens for niet ge-modified.
        //Anders moeten 2 loops -> minder efficient.
        for(HashMap.Entry<Integer, Integer> entry : neighbours.entrySet()){
            if(entry.getValue().equals(valueOnIndexToCheck)){
                //equals ipv == omdat Integer een wrapper class is en dus een eigen ref heeft
                result.add(entry.getKey());
            }
        }
        return result;
    }


}