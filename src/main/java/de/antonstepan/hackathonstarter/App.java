package de.antonstepan.hackathonstarter;

import static java.lang.Integer.parseInt;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.ListUtils;
import org.apache.commons.math3.util.Pair;
import org.assertj.core.groups.Tuple;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;

import de.antonstepan.helper.FileUtils;

public class App {

  public static String[] INPUTS = new String[] { "mother_of_all_warehouses.in", "redundancy.in", "busy_day.in"};

  public static void main(String[] args) {

    for (String input : INPUTS) {
      FileUtils fileUtils = new FileUtils();
      ImmutableList<String> lines = fileUtils.readFile(input);
      
      App app = new App();
      app.start(lines, input);
    }
  }

  private void start(ImmutableList<String> lines, String filename) {
    String[] paraOfSimu = lines.get(0)
        .split(" ");
    Simulation simuation = new Simulation(paraOfSimu);

    int productTypesCount = Integer.parseInt(lines.get(1));
    String[] productTypes = lines.get(2)
        .split(" ");

    if (productTypesCount != productTypes.length) {
      throw new IllegalArgumentException(
          String.format("Invalid product types count: %s and %s", productTypesCount, productTypes));
    }

    for (int i = 0; i < productTypes.length; i++) {
      ProductType type = new ProductType(i, Integer.parseInt(productTypes[i]));
      simuation.getTypes()
          .add(type);
    }

    int warehouseCount = Integer.parseInt(lines.get(3));
    // Zeile 4 bis Zeile warehouseCount * 2
    int i = 4;
    int wId = 0;
    for (; i < 4 + warehouseCount * 2; i++) {
      String[] loc = lines.get(i)
          .split(" ");
      Warehouse warehouse = new Warehouse(wId, Integer.parseInt(loc[0]), Integer.parseInt(loc[1]));
      wId++;
      i++;
      String[] stockSizes = lines.get(i)
          .split(" ");
      for (String string : stockSizes) {
        warehouse.getStock()
            .add(Integer.parseInt(string));
      }
      simuation.addWarehouse(warehouse);
    }
    int customerOrders = Integer.parseInt(lines.get(i));
    i++;
    int endLine = i + customerOrders * 3;
    int orderId = 0;
    for (; i < endLine; i++) {
      String[] delivery = lines.get(i)
          .split(" ");
      i++;
      int orderedItems = Integer.parseInt(lines.get(i));
      i++;
      String[] itemsString = lines.get(i)
          .split(" ");
      Location deliveryLoc = new Location(parseInt(delivery[0]), parseInt(delivery[1]));
      List<ProductType> items = parseItem(simuation, itemsString);
      if (orderedItems != items.size()) {
        throw new IllegalArgumentException(String.format("Items are not valid: %s, %s", orderedItems, items.size()));
      }
      Order order = new Order(orderId, deliveryLoc, items);
      simuation.addOrder(order);
      orderId++;
    }

    System.out.println("Starting Simulation: " + filename);
    simuation.prepare();

    for (int currentMove = 0; currentMove < simuation.getMaxMoves(); currentMove++) {
      simuation.makeMove(currentMove);
    }
    System.out.println(filename + " >> " + simuation.getCommands().size() + "\n");
    simuation.writeResult(filename);
  }

  private List<ProductType> parseItem(Simulation simulation, String[] items) {
    ArrayList<ProductType> result = new ArrayList<>();

    for (int i = 0; i < items.length; i++) {
      result.add(simulation.getTypes()
          .get(parseInt((items[i]))));
    }

    return result;
  }

}
