package com.reece.platform.products.branches.model.entity;

import com.reece.platform.products.branches.model.enums.MileRadiusEnum;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class BranchTest {

    public Branch branch;
    public static final String TEST_BRANCH_ID = "1003";
    public static final String TEST_BRANCH_ID_ALT = "1004";

    @BeforeEach
    public void setup() {
        branch = new Branch();
    }

    @Test
    void isActiveAndAvailable_success_true() {
        branch.setIsActive(true);
        branch.setIsAvailableInStoreFinder(true);
        branch.setIsShoppable(true);
        assertEquals(true, branch.isActiveAndAvailable(true, true));
    }

    @Test
    void isActiveAndAvailable_success_false() {
        branch.setIsActive(true);
        branch.setIsAvailableInStoreFinder(true);
        branch.setIsShoppable(false);
        assertEquals(false, branch.isActiveAndAvailable(true, true));
    }

    @Test
    void isWithinSearchRadius_success_true() {
        branch.setDistance(150f);
        assertEquals(true, branch.isWithinSearchRadius(MileRadiusEnum.MILES_200));
    }

    @Test
    void isWithinSearchRadius_success_false() {
        branch.setDistance(250f);
        assertEquals(false, branch.isWithinSearchRadius(MileRadiusEnum.MILES_200));
    }

    @Test
    void isWithinTerritory_success_true() {
        List<String> mockBranchIds = new ArrayList<>(List.of(TEST_BRANCH_ID));
        branch.setBranchId(TEST_BRANCH_ID);
        assertEquals(true,branch.isInTerritory(mockBranchIds));
    }

    @Test
    void isWithinTerritory_success_false() {
        List<String> mockBranchIds = new ArrayList<>(List.of("TEST_BRANCH_ID"));
        branch.setBranchId(TEST_BRANCH_ID_ALT);
        assertEquals(false, branch.isInTerritory(mockBranchIds));
    }

    @Test
    void isWithinTerritory_missing_false() {
        List<String> mockBranchIds = new ArrayList<>(List.of());
        assertEquals(true, branch.isInTerritory(mockBranchIds));
    }

}
