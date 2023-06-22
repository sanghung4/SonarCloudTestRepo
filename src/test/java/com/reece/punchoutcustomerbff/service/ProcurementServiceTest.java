package com.reece.punchoutcustomerbff.service;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.Mockito.when;

import com.reece.punchoutcustomerbff.dto.ProcurementSystemListDto;
import com.reece.punchoutcustomerbff.models.daos.ProcurementSystemDao;
import com.reece.punchoutcustomerbff.models.repositories.ProcurementSystemRepository;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class ProcurementServiceTest {

  @Mock
  private ProcurementSystemRepository procurementSystemRepository;

  @InjectMocks
  private ProcurementService subject;

  @Test
  public void testRetrieve() {
    // given
    List<ProcurementSystemDao> inputs = List.of(ProcurementSystemDao.builder().name("alpha").build());
    when(procurementSystemRepository.findAll()).thenReturn(inputs);

    // when
    ProcurementSystemListDto result = subject.retrieve();

    // then
    assertThat(result.getProcurementSystems().size(), equalTo(1));
    assertThat(result.getProcurementSystems().get(0).getName(), equalTo("alpha"));
  }

}
