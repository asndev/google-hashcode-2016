package de.antonstepan.hackathonstarter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import java.util.Set;
import java.util.Stack;

public class Drone {

  private final int id;
  private final int maxWeight;

  private int freeAtTurn = -1;

  private Location position = new Location(0, 0);

  private final Map<ProductType, Integer> items = new HashMap<>();
  public static int skipCount = 0;

  private Stack<LoadCmd> loadCommands = new Stack<>();
  private Stack<DeliveryCmd> deliverCommands = new Stack<>();

  public Drone(int id, int weight) {
    this.id = id;
    this.maxWeight = weight;
  }

  public boolean isFree(int turn) {
    return turn >= freeAtTurn;
  }

  public boolean canLoad(Order order) {
    return order.getWeight() + getCurrentWeight() <= getMaxWeight();
  }

  public int getCurrentWeight() {
    return getItems().entrySet()
        .parallelStream()
        .map(e -> e.getKey()
            .getWeight() * e.getValue())
        .reduce(0, (sum, v) -> sum += v);
  }

  /**
   * @return the weight
   */
  public int getMaxWeight() {
    return maxWeight;
  }

  /**
   * @return the id
   */
  public int getId() {
    return id;
  }

  /**
   * @return the items
   */
  public Map<ProductType, Integer> getItems() {
    return items;
  }

  /**
   * @return the position
   */
  public Location getPosition() {
    return position;
  }

  /**
   * @param position
   *          the position to set
   */
  public void setPosition(Location position) {
    this.position = position;
  }

  /**
   * @return the freeAtTurn
   */
  public int getFreeAtTurn() {
    return freeAtTurn;
  }

  /**
   * @param freeAtTurn
   *          the freeAtTurn to set
   */
  public void setFreeAtTurn(int freeAtTurn) {
    this.freeAtTurn = freeAtTurn;
  }

  public List<Command> load(Warehouse w, Order order) {
    List<Command> commands = new ArrayList<>();
    Set<Entry<ProductType, Integer>> items = order.getItems()
        .entrySet();

    for (Entry<ProductType, Integer> item : items) {

      int maxCount = item.getValue();

      if (maxCount * item.getKey()
          .getWeight() <= getMaxWeight()) {
        LoadCmd loadCmd = doLoadOperation(maxCount, w, item);
        commands.add(loadCmd);
        DeliveryCmd deliveryCmd = doDeliveryOperation(maxCount, w, order, item);
        commands.add(deliveryCmd);
      } else {
        int count = 1;
        int stack = item.getValue();
        while (stack != 0) {
          while ((count + 1) <= stack && (count + 1) * item.getKey()
              .getWeight() <= getMaxWeight()) {
            count++;
            stack--;
          }
          LoadCmd loadCmd = doLoadOperation(count, w, item);
          commands.add(loadCmd);
          DeliveryCmd deliveryCmd = doDeliveryOperation(count, w, order, item);
          commands.add(deliveryCmd);
          stack--;
        }
      }

      setPosition(order.getDeliveryLocation());
    }

    return commands;
  }

  private DeliveryCmd doDeliveryOperation(int count, Warehouse w, Order order, Entry<ProductType, Integer> item) {
    DeliveryCmd deliveryCmd = new DeliveryCmd(getId(), order.getId(), item.getKey()
        .getId(), count);
    int distance2 = Simulation.getDistance(w.getLocation(), order.getDeliveryLocation());
    setFreeAtTurn(getFreeAtTurn() + distance2 + 1);

    return deliveryCmd;
  }

  private LoadCmd doLoadOperation(int count, Warehouse w, Entry<ProductType, Integer> item) {
    LoadCmd loadCmd = new LoadCmd(getId(), w.getId(), item.getKey()
        .getId(), count);
    int distance = Simulation.getDistance(getPosition(), w.getLocation());
    setFreeAtTurn(getFreeAtTurn() + distance + 1);
    w.decreaseProductType(item.getKey(), count);

    return loadCmd;
  }

}
