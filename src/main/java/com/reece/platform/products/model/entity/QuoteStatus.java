package com.reece.platform.products.model.entity;

import com.reece.platform.products.model.QuoteStatusEnum;
import java.util.UUID;
import javax.persistence.*;
import lombok.*;
import org.hibernate.annotations.Type;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "quote_status")
public class QuoteStatus {

    @Id
    @Column(name = "id")
    private String quoteId;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private QuoteStatusEnum quoteStatus;
}
