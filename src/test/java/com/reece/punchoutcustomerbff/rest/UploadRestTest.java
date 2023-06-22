package com.reece.punchoutcustomerbff.rest;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.reece.punchoutcustomerbff.dto.ResultDto;
import com.reece.punchoutcustomerbff.dto.UploadInputDto;
import com.reece.punchoutcustomerbff.dto.UploadOutputDto;
import com.reece.punchoutcustomerbff.service.UploadService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class UploadRestTest {

  @Mock
  private UploadService uploadService;

  @InjectMocks
  private UploadRest subject;

  @Test
  public void testUpload() {
    // given
    UploadInputDto input = new UploadInputDto();
    String customerId = "0e3a4ecc-6cfa-4821-8d80-b85a28f27dfa";

    // and
    UploadOutputDto mockedResult = new UploadOutputDto();
    when(uploadService.upload(input, customerId)).thenReturn(mockedResult);

    // when
    UploadOutputDto result = subject.upload(input, customerId);

    // then
    assertThat(result, equalTo(mockedResult));
  }

  @Test
  public void testDelete() {
    // given
    String uploadId = "0e3a4ecc-6cfa-4821-8d80-b85a28f27dfa";

    // when
    ResultDto result = subject.delete(uploadId);

    // then:
    verify(uploadService).deleteUpload(uploadId);

    // and
    assertThat(result.isSuccess(), equalTo(true));
  }
}
