package com.reece.platform.products.model.entity;

import java.time.LocalDateTime;
import java.util.UUID;
import javax.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "search_index_metadata")
public class SearchIndexMetadata {

    @Id
    @GeneratedValue
    private UUID id;

    @Column(name = "last_update_time")
    private LocalDateTime lastUpdateTime;
}
