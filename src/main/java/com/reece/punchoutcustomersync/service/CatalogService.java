package com.reece.punchoutcustomersync.service;

import com.reece.punchoutcustomerbff.dto.CatalogDto;
import com.reece.punchoutcustomerbff.mapper.CatalogMapper;
import com.reece.punchoutcustomerbff.models.daos.CatalogDao;
import com.reece.punchoutcustomerbff.models.repositories.CatalogRepository;

import java.util.List;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Used for generate interactions with the catalog.
 * @author john.valentino
 */
@Service
@Slf4j
public class CatalogService {

  @Autowired
  private CatalogRepository catalogRepository;

  /**
   * Retrieves the catalog with its product mappings if it exists, otherwise returns null.
   * @param catalogId The ID of the catalog.
   * @return CatalogDto The matching catalog.
   */
  public CatalogDto retrieveCatalogWithMappings(String catalogId) {
    List<CatalogDao> catalogs = catalogRepository.retrieveWithMappings(UUID.fromString(catalogId));

    if (catalogs.size() == 0) {
      return null;
    }

    CatalogDto output = CatalogMapper.toDTO(catalogs.get(0));
    return output;
  }
}
