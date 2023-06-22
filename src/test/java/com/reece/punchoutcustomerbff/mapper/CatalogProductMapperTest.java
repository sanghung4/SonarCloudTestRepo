package com.reece.punchoutcustomerbff.mapper;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

import com.reece.punchoutcustomerbff.dto.CatalogProductDto;
import com.reece.punchoutcustomerbff.models.daos.CatalogProductDao;
import com.reece.punchoutcustomerbff.util.DateUtil;
import java.math.BigDecimal;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import org.junit.jupiter.api.Test;

public class CatalogProductMapperTest {

  @Test
  public void testToDTOs() {
    // given
    new CatalogProductMapper();

    CatalogProductDao input = CatalogProductDao.builder()
        .id(UUID.fromString("39a92e3b-1db9-44fc-b058-621ba4293343"))
        .lastPullDatetime(DateUtil.toTimestamp("2023-05-17T05:00:00.000+0000"))
        .uom("uom-alpha")
        .sellPrice(new BigDecimal(0))
        .skuQuantity(1)
        .listPrice(new BigDecimal(1))
        .partNumber("part-number-alpha")
        .build();
    Set<CatalogProductDao> inputs = Set.of(input);

    // when
    List<CatalogProductDto> results = CatalogProductMapper.toDTOs(inputs);

    // then
    assertThat(results.size(), equalTo(1));

    // and
    CatalogProductDto result = results.get(0);
    assertThat(result.getId().toString(), equalTo("39a92e3b-1db9-44fc-b058-621ba4293343"));
    assertThat(result.getLastPullDatetime(), equalTo("2023-05-17T05:00:00.000+0000"));
    assertThat(result.getUom(), equalTo("uom-alpha"));
    assertThat(result.getSellPrice(), equalTo(new BigDecimal(0)));
    assertThat(result.getSkuQuantity(), equalTo(1));
    assertThat(result.getListPrice(), equalTo(new BigDecimal(1)));
    assertThat(result.getPartNumber(), equalTo("part-number-alpha"));

  }

}
