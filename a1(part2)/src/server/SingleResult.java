package server;

public class SingleResult {
  
  public int numOfGuesses;
  public boolean isCorrect;
  public String playerName;
  
  public SingleResult(int numOfGuesses, boolean isCorrect, String playerName) {
    this.numOfGuesses = numOfGuesses;
    this.isCorrect = isCorrect;
    this.playerName = playerName;
  }

  public int getNumOfGuesses() {
    return numOfGuesses;
  }

  public boolean isCorrect() {
    return isCorrect;
  }

  public String getPlayerName() {
    return playerName;
  }
  

}
