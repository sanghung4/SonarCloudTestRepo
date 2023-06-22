package com.reece.punchoutcustomerbff.models.daos;

import java.sql.Timestamp;
import java.util.Set;
import java.util.UUID;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * Product representation in db.
 */
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
@ToString
@Entity
@Table(name = "product", schema = "public")
public class ProductDao {
	@Id
	private UUID id;
	@Column(name = "name")
	private String name;
	@Column(name = "description")
	private String description;
	@Column(name = "image_full_size")
	private String imageFullSize;
	@Column(name = "part_number")
	private String partNumber;
	@Column(name = "manufacturer")
	private String manufacturer;
	@Column(name = "category_1_name")
	private String categoryLevel1Name;
	@Column(name = "category_2_name")
	private String categoryLevel2Name;
	@Column(name = "category_3_name")
	private String categoryLevel3Name;
	@Column(name = "unspsc")
	private String unspsc;
	@Column(name = "image_thumb")
	private String imageThumb;
	@Column(name = "manufacturer_part_number")
	private String manufacturerPartNumber;
	@Column(name = "delivery_in_days")
	private int deliveryInDays;
	@Column(name = "max_sync_datetime")
	private Timestamp maxSyncDatetime;
	@OneToMany(mappedBy="product", fetch = FetchType.LAZY)
	@EqualsAndHashCode.Exclude
	@ToString.Exclude
	private Set<CatalogProductDao> mappings;
}
