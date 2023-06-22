package com.reece.punchoutcustomerbff.mapper;

import com.reece.punchoutcustomerbff.dto.ProductDto;
import com.reece.punchoutcustomerbff.models.daos.ProductDao;
import com.reece.punchoutcustomerbff.util.TestUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class ProductMapperTest {

    @BeforeEach
    public void before() {
        new ProductMapper();
    }

    @Test
    public void whenToDTOsOk() {
        ProductDao inputs = TestUtils.getProductDao();
        ProductDto result = ProductMapper.toDTO(inputs);
        TestUtils.assertProductToDto(inputs, result);
    }

    @Test
    public void whenToDTOsWithNullOk() {
        ProductDto result = ProductMapper.toDTO(null);
        Assertions.assertNull(result);
    }
}
