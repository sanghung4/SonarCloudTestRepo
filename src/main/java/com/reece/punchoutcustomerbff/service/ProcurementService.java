package com.reece.punchoutcustomerbff.service;

import com.reece.punchoutcustomerbff.dto.ProcurementSystemListDto;
import com.reece.punchoutcustomerbff.mapper.ProcurementSystemMapper;
import com.reece.punchoutcustomerbff.models.daos.ProcurementSystemDao;
import com.reece.punchoutcustomerbff.models.repositories.ProcurementSystemRepository;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * General service for dealing with the procurement systems.
 * @author john.valentino
 */
@Service
@Slf4j
public class ProcurementService {

  @Autowired
  private ProcurementSystemRepository procurementSystemRepository;

  public ProcurementSystemListDto retrieve() {
    ProcurementSystemListDto result = new ProcurementSystemListDto();

    List<ProcurementSystemDao> inputs = procurementSystemRepository.findAll();
    result.setProcurementSystems(ProcurementSystemMapper.toDTOs(inputs));

    return result;
  }

}
