package de.antonstepan.hackathonstarter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Order {

  private final Location deliveryLocation;
  private final List<ProductType> inputItems;
  private int id;
  
  private final Map<ProductType, Integer> items = new HashMap<>();

  public Order(int id, Location deliveryLoc, List<ProductType> ig) {
    this.setId(id);
    this.deliveryLocation = deliveryLoc;
    this.inputItems = ig;
    
    for (ProductType productType : inputItems) {
      if (!items.containsKey(productType)) {
        items.put(productType, 0);
      }
      items.put(productType, items.get(productType) + 1);
    }
  }

  @Override
  public String toString() {
    return String.format("Order id %s: [%s], items: %s", id, deliveryLocation, inputItems);
  }

  /**
   * @return the deliveryLocation
   */
  public Location getDeliveryLocation() {
    return deliveryLocation;
  }

  /**
   * @return the items
   */
  public Map<ProductType, Integer> getItems() {
    return items;
  }
  
  public Integer getWeight() {
    return getItems()
    .entrySet()
    .parallelStream()
    .map(e -> e.getKey().getWeight() * e.getValue())
    .reduce(0, (sum, v) -> sum += v);
  }

  /**
   * @return the id
   */
  public int getId() {
    return id;
  }

  /**
   * @param id the id to set
   */
  public void setId(int id) {
    this.id = id;
  }

}
