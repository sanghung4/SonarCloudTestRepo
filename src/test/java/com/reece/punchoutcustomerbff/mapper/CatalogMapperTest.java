package com.reece.punchoutcustomerbff.mapper;

import com.reece.punchoutcustomerbff.dto.CatalogDto;
import com.reece.punchoutcustomerbff.models.daos.CatalogDao;
import com.reece.punchoutcustomerbff.util.TestUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class CatalogMapperTest {

  @BeforeEach
  public void before() {
    new CatalogMapper();
  }

  @Test
  public void testToDTO() {
    // given
    CatalogDao input = TestUtils.generateCatalog();

    // when
    CatalogDto output = CatalogMapper.toDTO(input);

    // then
    TestUtils.assertDefaultCatalogDto(output);

  }

}
