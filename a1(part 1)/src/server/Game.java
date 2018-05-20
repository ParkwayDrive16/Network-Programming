package server;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class Game {
  private final int MIN_SIZE_GAME = 3;
  private final int MAX_SIZE_GAME = 8;
  private int size;
  private String numbers;
  private int[] posPair;
  //class that represents a game and it's attributes
  public Game() {}
  
  public void generateNumbers() {
    StringBuilder builder = new StringBuilder();
    ArrayList<Integer> list = new ArrayList<Integer>();
    //adding unique digits to the list
    for (int i=0; i<= 9; i++){
      list.add(new Integer(i));
    }
    //shuffle the list
    Collections.shuffle(list);
    //pick first n numbers from the list, where n is the size of the game
    for (int i = 0; i < size; i++){
      builder.append(list.get(i));
    }
    numbers = builder.toString();
  }

  public String getNumbers()
  {
     return this.numbers;
  }
  
  public int[] getPosPair() {
    return this.posPair;
  }
  
  public boolean setSize(int size) {
    //checks if the size given is valid
    if (size >= MIN_SIZE_GAME && size <= MAX_SIZE_GAME){
       this.size = size;
       return true;
    }
    return false;
  }
  

  public void compare(String guess) {
    //create position pair to be returned
    posPair = new int[2];
    
    //convert the strings to list of chars
    List<Character> guessList = guess.chars().mapToObj(e->(char)e).collect(Collectors.toList());
    List<Character> numbersList = numbers.chars().mapToObj(e->(char)e).collect(Collectors.toList());
    //checking how many numbers are in correct position
    for (int i = 0; i < numbersList.size(); i++){
      if (numbersList.get(i) == guessList.get(i)){
        //increment if values are the same
        posPair[0] += 1;
        //remove them from the list
        numbersList.remove(i);
        guessList.remove(i);
        //decrement i to look at the first position in the list again
        i--;
      }
    }
    //check if numbers list contains any of the guess list characters
    for (int i = 0; i < numbersList.size(); i++){
      if(numbersList.contains(guessList.get(i))) {
        //increment if values if it does
        posPair[1] += 1;
      }
      
    }
  }
}
