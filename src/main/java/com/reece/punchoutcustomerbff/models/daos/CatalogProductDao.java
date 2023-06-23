package com.reece.punchoutcustomerbff.models.daos;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.UUID;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * CatalogProduct representation in db.
 */
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "catalog_product", schema = "public")
public class CatalogProductDao {
	@Id
	private UUID id;
	@ManyToOne(cascade = CascadeType.MERGE, fetch = FetchType.LAZY)
	@JoinColumn(name = "id_catalog", referencedColumnName = "id")
	private CatalogDao catalog;
	@ManyToOne(cascade = CascadeType.MERGE, fetch = FetchType.EAGER)
	@JoinColumn(name = "id_product", referencedColumnName = "id")
	private ProductDao product;
	@Column(name = "sell_price")
	private BigDecimal sellPrice;
	@Column(name = "list_price")
	private BigDecimal listPrice;
	@Column(name = "uom")
	private String uom;
	@Column(name = "last_pull_datetime")
	private Timestamp lastPullDatetime;
	@Column(name = "sku_quantity")
	private Integer skuQuantity;
	@Column(name = "part_number")
	private String partNumber;

}
