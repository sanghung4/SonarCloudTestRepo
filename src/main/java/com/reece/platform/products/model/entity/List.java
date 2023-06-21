package com.reece.platform.products.model.entity;

import com.reece.platform.products.model.DTO.CreateListDTO;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import javax.persistence.*;
import lombok.*;
import org.hibernate.annotations.Type;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "lists")
public class List {

    @Id
    @GeneratedValue
    @Type(type = "pg-uuid")
    public UUID id;

    @Column(name = "name")
    public String name;

    @Column(name = "billto_account_id")
    public UUID billToAccountId;

    @OneToMany
    @JoinColumn(name = "list_id")
    @OrderBy("sort_order")
    private Set<ListLineItem> listLineItems;

    public List(CreateListDTO createListDTO) {
        this.name = createListDTO.getName();
        this.billToAccountId = createListDTO.getBillToAccountId();

        var lineItems = createListDTO.getListLineItems();
        if (lineItems != null && lineItems.size() > 0) {
            this.listLineItems =
                createListDTO.getListLineItems().stream().map(ListLineItem::new).collect(Collectors.toSet());
        }
    }

    public List(String name, UUID billToAccountId) {
        this.name = name;
        this.billToAccountId = billToAccountId;
    }
}
