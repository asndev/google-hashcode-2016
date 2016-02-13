package de.antonstepan.hackathonstarter;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Stack;

import org.assertj.core.util.VisibleForTesting;

import de.antonstepan.hackathonstarter.sorter.DistancePlusItemsSorter;
import de.antonstepan.hackathonstarter.sorter.ItemSizeThenDistanceSorter;
import de.antonstepan.helper.FileUtils;

public class Simulation {

  private final int rows;
  private final int columns;
  private final int numberOfDrones;
  private final int maxLoad;
  private final int maxMoves;

  private final List<Command> commands = new ArrayList<>();

  private List<Drone> drones = new ArrayList<>();
  private final List<ProductType> types = new ArrayList<>();
  private final List<Warehouse> warehouses = new ArrayList<>();

  private final List<Order> orders = new ArrayList<>();

  private int currentOrderIndex = 0;

  public Simulation(String[] paraOfSimu) {
    this.rows = Integer.parseInt(paraOfSimu[0]);
    this.columns = Integer.parseInt(paraOfSimu[1]);
    this.numberOfDrones = Integer.parseInt(paraOfSimu[2]);
    this.maxMoves = Integer.parseInt(paraOfSimu[3]);
    this.maxLoad = Integer.parseInt(paraOfSimu[4]);

    for (int i = 0; i < numberOfDrones; i++) {
      drones.add(new Drone(i, maxLoad));
    }

    // System.out.println(
    // String.format("Created Simulation with: [rows: %s, columns: %s, #drones:
    // %s, deadline: %s, maxWeight: %s]",
    // rows, columns, numberOfDrones, getMaxMoves(), maxLoad));
  }

  public void makeMove(int currentMove) {
    for (Drone drone : drones) {
      boolean isFree = drone.isFree(currentMove);
      if (isFree && orders.size() > 0) {
        Order order = findBestOrder(drone);
        makeMove(currentMove, drone, order);
      }
    }
  }

  private Order findBestOrder(Drone drone) {
    orders.sort(new ItemSizeThenDistanceSorter(drone));
    //orders.sort(new DistancePlusItemsSorter(drone));
    return orders.remove(0);
  }
  
  @VisibleForTesting
  public static int getDistance(Location c1, Location c2) {
    int ra = c1.X;
    int ca = c1.Y;
    int rb = c2.X;
    int cb = c2.Y;

    double pow1 = Math.pow(ra - rb, 2);
    double pow2 = Math.pow(ca - cb, 2);
    double unrounded = Math.sqrt(pow1 + pow2);

    return (int) Math.ceil(unrounded);
  }

  private void makeMove(int currentMove, Drone drone, Order order) {
    Warehouse w = findClosestWarehouse(drone.getPosition(), order);
    if (w == null) {
      return;
    }

    List<Command> cmds = drone.load(w, order);
    commands.addAll(cmds);
  }

  private Warehouse findClosestWarehouse(Location position, Order order) {
    Warehouse result = null;

    int bestDist = 100000;

    for (Warehouse warehouse : warehouses) {
      int distance = getDistance(order.getDeliveryLocation(), warehouse.getLocation());
      if (warehouse.isEligable(order) && distance < bestDist) {
        bestDist = distance;
        result = warehouse;
      }
    }

    return result;
  }

  /**
   * @return the types
   */
  public List<ProductType> getTypes() {
    return types;
  }

  public void addWarehouse(Warehouse warehouse) {
    getWarehouses().add(warehouse);
  }

  /**
   * @return the warehouses
   */
  public List<Warehouse> getWarehouses() {
    return warehouses;
  }

  public void addOrder(Order order) {
    getOrders().add(order);
  }

  /**
   * @return the orders
   */
  public List<Order> getOrders() {
    return orders;
  }

  public void printResult() {
    System.out.println(commands.size());
    for (Command command : commands) {
      System.out.println(command);
    }
  }

  /**
   * @return the commands
   */
  public List<Command> getCommands() {
    return commands;
  }

  public void prepare() {
    this.orders.sort(new ItemCountComparator());
  }

  private class WeightComparator implements Comparator<Order> {

    @Override
    public int compare(Order o1, Order o2) {
      int w1 = o1.getWeight();
      int w2 = o2.getWeight();

      if (w1 == w2) {
        return 0;
      } else if (w1 < w2) {
        return -1;
      } else {
        return 1;
      }
    }

  }

  private class ItemCountComparator implements Comparator<Order> {

    @Override
    public int compare(Order o1, Order o2) {
      int size1 = o1.getItems()
          .size();
      int size2 = o2.getItems()
          .size();

      if (size1 == size2) {
        Integer o1W = o1.getWeight();
        Integer o2W = o2.getWeight();
        
        if (o1W == o2W) {
          return 0;
        } else if (o1W < o2W) {
          return -1;
        } else {
          return 1;
        }
      } else if (size1 < size2) {
        return -1;
      } else {
        return 1;
      }
    }

  }

  /**
   * @return the maxMoves
   */
  public int getMaxMoves() {
    return maxMoves;
  }

  public void writeResult(String inputName) {
    List<String> lines = new ArrayList<>();
    lines.add("" + commands.size());
    for (Command cmd : commands) {
      lines.add(cmd.toString());
    }

    FileUtils.writeFile(inputName.replace("in", "out"), lines);
  }

}
