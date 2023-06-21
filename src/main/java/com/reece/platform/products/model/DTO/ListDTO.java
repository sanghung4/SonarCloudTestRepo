package com.reece.platform.products.model.DTO;

import com.reece.platform.products.model.entity.ListLineItem;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ListDTO {

    private UUID id;
    private String name;
    private UUID billToAccountId;
    private List<ListLineItemDTO> listLineItems;

    private int listLineItemsSize;

    public ListDTO(com.reece.platform.products.model.entity.List list) {
        this.id = list.getId();
        this.name = list.getName();
        this.billToAccountId = list.getBillToAccountId();
        if (list.getListLineItems() != null) {
            this.listLineItems =
                new ArrayList<>(list.getListLineItems())
                    .stream()
                    .map(ListLineItemDTO::new)
                    .collect(Collectors.toList());
        }
    }

    public static ListDTO createWithoutLineItems(com.reece.platform.products.model.entity.List list) {
        final ListDTO listDTO = new ListDTO();
        listDTO.setId(list.getId());
        listDTO.setName(list.getName());
        listDTO.setBillToAccountId(list.getBillToAccountId());
        listDTO.setListLineItems(new ArrayList<>());
        listDTO.setListLineItemsSize(list.getListLineItems().size());

        return listDTO;
    }
}
