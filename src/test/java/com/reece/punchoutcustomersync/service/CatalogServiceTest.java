package com.reece.punchoutcustomersync.service;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.nullValue;
import static org.mockito.Mockito.when;

import com.reece.punchoutcustomerbff.dto.CatalogDto;
import com.reece.punchoutcustomerbff.models.daos.CatalogDao;
import com.reece.punchoutcustomerbff.models.repositories.CatalogRepository;
import com.reece.punchoutcustomerbff.util.TestUtils;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class CatalogServiceTest {

  @Mock
  private CatalogRepository catalogRepository;

  @InjectMocks
  private CatalogService subject;

  @Test
  public void testRetrieveCatalogWithMappingsWhenNoMatches() {
    // given: The ID of the catalog
    String catalogId = "6a946dc1-e0ef-4fae-8dcc-9ed24b89e3aa";

    // and: that there are no matches
    when(catalogRepository.retrieveWithMappings(UUID.fromString(catalogId)))
        .thenReturn(new ArrayList<>());

    // when: we call to get the catalog
    CatalogDto result = subject.retrieveCatalogWithMappings(catalogId);

    // then: the result is null
    assertThat(result, nullValue());
  }

  @Test
  public void testRetrieveCatalogWithMappings() {
    // given: an existing catalog
    CatalogDao catalog = TestUtils.generateCatalogWithMappings();
    String catalogId = catalog.getId().toString();

    when(catalogRepository.retrieveWithMappings(catalog.getId()))
        .thenReturn(List.of(catalog));

    // when: we call to get the catalog
    CatalogDto result = subject.retrieveCatalogWithMappings(catalogId);

    // then:
    TestUtils.assertDefaultCatalogDto(result);
    assertThat(result.getMappings().size(), equalTo(1));
    TestUtils.assertDefaultMapping(result.getMappings().get(0));

  }
}
