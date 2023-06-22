package com.reece.punchoutcustomerbff.rest;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.Mockito.when;

import com.reece.punchoutcustomerbff.dto.ProcurementSystemListDto;
import com.reece.punchoutcustomerbff.service.ProcurementService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class ProcurementRestTest {

  @Mock
  private ProcurementService procurementService;

  @InjectMocks
  private ProcurementRest subject;

  @Test
  public void testList() {
    // given
    ProcurementSystemListDto mocked = new ProcurementSystemListDto();
    when(procurementService.retrieve()).thenReturn(mocked);

    // when
    ProcurementSystemListDto result = subject.list();

    // then
    assertThat(result, equalTo(mocked));
  }

}
