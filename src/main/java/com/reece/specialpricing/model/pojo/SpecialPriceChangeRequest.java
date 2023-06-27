package com.reece.specialpricing.model.pojo;

import com.reece.specialpricing.model.SpecialPriceSuggestion;
import com.reece.specialpricing.model.exception.TypedException;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SpecialPriceChangeRequest {
    @NotNull(message = "Invalid parameter: 'priceChangeSuggestions' is null, which is not valid")
    @Valid
    private List<SpecialPriceSuggestion> priceChangeSuggestions;

    @NotNull(message = "Invalid parameter: 'priceCreateSuggestions' is null, which is not valid")
    @Valid
    private List<SpecialPriceSuggestion> priceCreateSuggestions;

    public List<TypedException> validate(){
        AtomicInteger index = new AtomicInteger();
        return priceChangeSuggestions
                .stream()
                .map(change -> change.validate(String.format("priceChangeSuggestions[%d]", index.getAndIncrement())))
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    public void cleanUserInputData(){
        priceChangeSuggestions.forEach(SpecialPriceSuggestion::cleanUserInputData);
        priceCreateSuggestions.forEach(SpecialPriceSuggestion::cleanUserInputData);
    }
}