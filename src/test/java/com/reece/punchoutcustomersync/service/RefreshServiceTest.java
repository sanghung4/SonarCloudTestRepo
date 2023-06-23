package com.reece.punchoutcustomersync.service;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.Mockito.when;

import com.reece.punchoutcustomerbff.dto.ResultDto;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class RefreshServiceTest {

  @Mock
  private EntityManager entityManager;

  @InjectMocks
  private RefreshService subject;

  @Mock
  private Query query;

  @Test
  public void testRefresh() {
    // given
    when(entityManager.createNativeQuery(Mockito.anyString())).thenReturn(query);
    when(query.executeUpdate()).thenReturn(0);

    // when
    ResultDto result = subject.refresh();

    // then
    assertThat(result.isSuccess(), equalTo(true));
  }

}
