package de.antonstepan.hackathonstarter;

public class Location {
  
  int X;
  int Y;
  
  public Location(int X, int Y) {
    this.X = X;
    this.Y = Y;
  }
  
  @Override
  public String toString() {
    return X + " " + Y;
  }

}
