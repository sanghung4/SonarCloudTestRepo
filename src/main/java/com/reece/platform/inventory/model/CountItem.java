package com.reece.platform.inventory.model;

import com.reece.platform.inventory.dto.EclipseLoadCountDto;
import java.util.Set;
import javax.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "pi_count_location_item")
@Getter
@Setter
@NoArgsConstructor
public class CountItem extends AuditableEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "location_count_id", nullable = false)
    private LocationCount locationCount;

    @Column(name = "item_num")
    private String itemNum;

    @Column(name = "product_num")
    private String productNum;

    @Column(name = "catalog_num")
    private String catalogNum;

    @Column(name = "tag_num")
    private String tagNum;

    @Column(name = "product_desc")
    private String productDesc;

    @Column(name = "image_url")
    private String imageUrl;

    @Column(name = "uom")
    private String uom;

    @Column(name = "most_recent_quantity")
    private Integer mostRecentQuantity;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private CountLocationItemStatus status;

    @Column(name = "sequence")
    private Integer sequence;

    @Column(name = "variance_cost")
    private Double varianceCost;

    @Enumerated(EnumType.STRING)
    @Column(name = "variance_status")
    private VarianceCountItemStatus varianceStatus;

    @OneToMany(mappedBy = "countItem", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<CountItemQuantity> countItemQuantities;

    @Column(name = "control_num")
    private String controlNum;

    public CountItem(EclipseLoadCountDto product) {
        itemNum = product.getProductId();
        productNum = product.getProductId();
        tagNum = product.getProductId();
        productDesc = product.getProductDescription1();
        uom = product.getUnitOfMeasure();
        status = CountLocationItemStatus.UNCOUNTED;
        varianceStatus = VarianceCountItemStatus.NONVARIANCE;
        catalogNum = product.getCatalogNumber();
    }
}
