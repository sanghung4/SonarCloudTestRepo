package com.reece.specialpricing.utilities;

import com.reece.specialpricing.model.DynamicSortable;
import com.reece.specialpricing.postgres.SpecialPrice;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class SortingUtils {
    public static List<Object> dynamicSort(List<DynamicSortable> sortableList, String direction, String by){

        if(by.equals("currentPrice") || by.equals("standardCost") || by.equals("typicalPrice") || by.equals("rateCardPrice") ||
        by.equals("recommendedPrice"))
        {
            List<SpecialPrice> specialPrice= sortableList.stream().map(element ->(SpecialPrice) element).collect(Collectors.toList());

            if(direction.equals("asc"))
            {
                return specialPrice.stream().sorted(Comparator.comparingDouble(o1-> o1.getComparisonFieldForPrice(by))).collect(Collectors.toList());
            }
            else
            {
                return specialPrice.stream().sorted((p1,p2)->Double.compare(p2.getComparisonFieldForPrice(by),p1.getComparisonFieldForPrice(by))).collect(Collectors.toList());
            }
        }

        return sortableList.stream().sorted(
                (o1, o2) -> {
                    if (direction.equals("asc")) {
                        return o1.getComparisonField(by).compareTo(o2.getComparisonField(by));
                    } else{
                        return o2.getComparisonField(by).compareTo(o1.getComparisonField(by));
                    }
                }).collect(Collectors.toList());
    }
}
