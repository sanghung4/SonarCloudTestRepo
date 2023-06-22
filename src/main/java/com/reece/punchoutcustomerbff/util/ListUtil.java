package com.reece.punchoutcustomerbff.util;

import java.util.ArrayList;
import java.util.List;

/**
 * Utility for dealing with common list operations.
 * @author john.valentino
 */
public class ListUtil {

  public static <T> List<List<T>> splitInto(List<T> inputs, int size, Class<T> type) {
    List<List<T>> outputs = new ArrayList<>();

    List<T> currentList = new ArrayList<>();
    outputs.add(currentList);

    for (T input : inputs) {
      if (currentList.size() == size) {
        currentList = new ArrayList<>();
        outputs.add(currentList);
      }

      currentList.add(input);
    }

    return outputs;
  }

}
