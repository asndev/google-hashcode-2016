package de.antonstepan.hackathonstarter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

public class Warehouse {

  private final int id;
  private final Location location;
  private final List<Integer> stock = new ArrayList<>();

  public Warehouse(int id, int x, int y) {
    this.id = id;
    this.location = new Location(x, y);
  }

  public Warehouse(int id, Location location) {
    this.id = id;
    this.location = location;
  }
  
  public boolean isEligable(Order order) {
    boolean result = true;
    Set<Entry<ProductType,Integer>> entrySet = order.getItems().entrySet();
    for (Entry<ProductType, Integer> entry : entrySet) {
      if (stock.get(entry.getKey().getId()) < entry.getValue()) {
        result = false;
        break;
      }
    }
    return result;
  }

  public int getCountForProductType(ProductType type) {
    return getStock().get(type.getId());
  }
  
  public boolean typeAvailable(ProductType type) {
    return getStock().get(type.getId()) == 0;
  }

  public void decreaseProductType(ProductType type, int count) {
    int index = type.getId();
    if (getStock().get(index) == 0) {
      throw new IllegalArgumentException(
          String.format("Trying to remove and none available. Type: %s, Stock: %s", type, getStock().get(type.getId())));
    }
    
    if (getStock().get(index) < count) {
      throw new IllegalArgumentException(
          String.format("Trying to remove and not enough available. Type: %s, Stock: %s", type, getStock().get(type.getId())));
    }
    
    getStock().set(index, getStock().get(index) - count);
  }

  /**
   * @return the stock
   */
  public List<Integer> getStock() {
    return stock;
  }
  
  @Override
  public String toString() {
    return String.format("Warehouse [%s] and Stock: %s", getLocation(), getStock());
  }

  /**
   * @return the location
   */
  public Location getLocation() {
    return location;
  }

  /**
   * @return the id
   */
  public int getId() {
    return id;
  }

}
