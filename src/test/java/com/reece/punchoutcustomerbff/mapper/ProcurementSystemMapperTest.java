package com.reece.punchoutcustomerbff.mapper;

import com.reece.punchoutcustomerbff.dto.ProcurementSystemDto;
import com.reece.punchoutcustomerbff.models.daos.ProcurementSystemDao;
import com.reece.punchoutcustomerbff.util.TestUtils;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class ProcurementSystemMapperTest {

    @BeforeEach
    public void before() {
        new ProcurementSystemMapper();
    }

    @Test
    public void whenToDTOsOk() {
        List<ProcurementSystemDao> inputs = TestUtils.getListProcurementSystemDao();
        List<ProcurementSystemDto> result = ProcurementSystemMapper.toDTOs(inputs);
        TestUtils.assertProcurementToDtos(inputs, result);
    }

    @Test
    public void whenToDTOsNOk() {
        List<ProcurementSystemDao> inputs = new ArrayList<>();
        List<ProcurementSystemDto> result = ProcurementSystemMapper.toDTOs(inputs);
        Assertions.assertEquals(inputs.size(), result.size());
    }

    @Test
    public void whenToDTOOk() {
        ProcurementSystemDao input = ProcurementSystemDao.builder().name("procurement.1").build();
        ProcurementSystemDto result = ProcurementSystemMapper.toDTO(input);
        Assertions.assertEquals(input.getName(), result.getName());
    }

    @Test
    public void whenToDTONOk() {
        ProcurementSystemDao input = ProcurementSystemDao.builder().build();
        ProcurementSystemDto result = ProcurementSystemMapper.toDTO(input);
        Assertions.assertEquals(input.getName(), result.getName());
    }
}
