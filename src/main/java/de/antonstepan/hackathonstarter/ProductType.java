package de.antonstepan.hackathonstarter;

public class ProductType {

  private final int weight;
  private final int id;

  public ProductType(int id, int weight) {
    this.id = id;
    this.weight = weight;
  }

  /**
   * @return the weight
   */
  public int getWeight() {
    return weight;
  }

  /**
   * @return the id
   */
  public int getId() {
    return id;
  }

  @Override
  public String toString() {
    return "ProductType: [ id:" + id + ", weight: " + weight + "]";
  }

}
