package com.reece.specialpricing.model.pojo;

import com.reece.specialpricing.model.SpecialPriceSuggestion;
import com.reece.specialpricing.model.exception.TypedException;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.List;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class SpecialPriceChangeRequestTests {
    @Mock
    private SpecialPriceSuggestion suggestion1;

    @Mock
    private SpecialPriceSuggestion suggestion2;

    @BeforeEach
    public void setUp(){
        reset(suggestion1, suggestion2);
    }

    @Test
    public void validate_shouldPassArrayPath(){
        var request = new SpecialPriceChangeRequest(List.of(suggestion1, suggestion2), List.of(suggestion1, suggestion2));
        var result = request.validate();
        verify(suggestion1, times(1)).validate("priceChangeSuggestions[0]");
        verify(suggestion2, times(1)).validate("priceChangeSuggestions[1]");
        assert result.size() == 0;
    }

    @Test
    public void validate_shouldReturnErrorsPassedBack(){
        when(suggestion1.validate(anyString())).thenReturn(new TypedException("some error"));
        when(suggestion2.validate(anyString())).thenReturn(new TypedException("some other error"));
        var request = new SpecialPriceChangeRequest(List.of(suggestion1, suggestion2), List.of(suggestion1, suggestion2));
        var result = request.validate();
        verify(suggestion1, times(1)).validate("priceChangeSuggestions[0]");
        verify(suggestion2, times(1)).validate("priceChangeSuggestions[1]");
        assert result.size() == 2;
        assert result.get(0).getMessage().equals("some error");
        assert result.get(1).getMessage().equals("some other error");
    }

    @Test
    public void cleanUserInputData_shouldInvokeCleanOnAllChildren(){
        var request = new SpecialPriceChangeRequest(List.of(suggestion1, suggestion2), List.of(suggestion1, suggestion2));
        request.cleanUserInputData();
        verify(suggestion1, times(2)).cleanUserInputData();
        verify(suggestion2, times(2)).cleanUserInputData();
    }
}
