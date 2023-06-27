package com.reece.specialpricing.utilities;

import com.reece.specialpricing.model.DynamicSortable;
import com.reece.specialpricing.model.TestDynamicSortable;
import org.junit.Test;

import java.util.List;

public class SortingUtilsTests {

    @Test
    public void dynamicSort_shouldDynamicallySortByFieldNameAscending(){
        List<DynamicSortable> list = List.of(new TestDynamicSortable("b", "y"), new TestDynamicSortable("a", "z"));

        var sortedList = SortingUtils.dynamicSort(list, "asc", "field1");
        assert sortedList.size() == 2;
        assert sortedList.get(0).equals(list.get(1));
        assert sortedList.get(1).equals(list.get(0));

        sortedList = SortingUtils.dynamicSort(list, "asc", "field2");
        assert sortedList.size() == 2;
        assert sortedList.get(0).equals(list.get(0));
        assert sortedList.get(1).equals(list.get(1));
    }

    @Test
    public void dynamicSort_shouldDynamicallySortByFieldNameDescending(){
        List<DynamicSortable> list = List.of(new TestDynamicSortable("b", "y"), new TestDynamicSortable("a", "z"));

        var sortedList = SortingUtils.dynamicSort(list, "desc", "field1");
        assert sortedList.size() == 2;
        assert sortedList.get(0).equals(list.get(0));
        assert sortedList.get(1).equals(list.get(1));

        sortedList = SortingUtils.dynamicSort(list, "desc", "field2");
        assert sortedList.size() == 2;
        assert sortedList.get(0).equals(list.get(1));
        assert sortedList.get(1).equals(list.get(0));
    }
}
