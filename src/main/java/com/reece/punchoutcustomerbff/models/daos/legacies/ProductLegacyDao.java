package com.reece.punchoutcustomerbff.models.daos.legacies;

import com.reece.punchoutcustomerbff.models.models.Product;
import java.math.BigDecimal;
import java.util.UUID;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
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
@Table(name = "product_legacy", schema = "public")
public class ProductLegacyDao {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private UUID id;
	@Column(name = "id_product")
	private String productId;
	@Column(name = "name")
	private String productName;
	@Column(name = "description")
	private String productDescription;
	@Column(name = "image_full_size")
	private String imageFullSize;
	@Column(name = "price")
	private BigDecimal price;
	@Column(name = "part_number")
	private String partNumber;
	@Column(name = "unit_of_measure")
	private String unitOfMeasure;
	@Column(name = "manufacturer")
	private String manufacturer;
	@Column(name = "category_1_code")
	private String categoryLevel1Code;
	@Column(name = "category_1_name")
	private String categoryLevel1Name;
	@Column(name = "unspsc")
	private int unspsc;
	@Column(name = "image_thumb")
	private String imageThumb;
	@Column(name = "scale_start")
	private int scaleStart;
	@Column(name = "scale_end")
	private int scaleEnd;
	@Column(name = "list_price")
	private BigDecimal listPrice;
	@Column(name = "manufacturer_part_number")
	private String manufacturerPartNumber;
	@Column(name = "category_2_code")
	private String categoryLevel2Code;
	@Column(name = "category_2_name")
	private String categoryLevel2Name;
	@Column(name = "category_3_code")
	private String categoryLevel3Code;
	@Column(name = "category_3_name")
	private String categoryLevel3Name;
	@Column(name = "category_4_code")
	private String categoryLevel4Code;
	@Column(name = "category_4_name")
	private String categoryLevel4Name;
	@Column(name = "category_5_code")
	private String categoryLevel5Code;
	@Column(name = "category_5_name")
	private String categoryLevel5Name;
	@Column(name = "delivery_in_days")
	private int deliveryInDays;
	@Column(name = "buyer_id")
	private String buyerId;

	public static ProductLegacyDao getFromProduct(Product product) {
		return ProductLegacyDao.builder()
				.productId(product.getProductId())
				.productName(product.getProductName())
				.productDescription(product.getProductDescription())
				.imageFullSize(product.getImageFullSize())
				.price(product.getPrice())
				.partNumber(product.getPartNumber())
				.unitOfMeasure(product.getUnitOfMeasure())
				.unitOfMeasure(product.getUnitOfMeasure())
				.manufacturer(product.getManufacturer())
				.categoryLevel1Code(product.getCategoryLevel1Code())
				.categoryLevel1Name(product.getCategoryLevel1Name())
				.unspsc(product.getUnspsc())
				.imageThumb(product.getImageThumb())
				.scaleStart(product.getScaleStart())
				.scaleEnd(product.getScaleEnd())
				.listPrice(product.getListPrice())
				.manufacturerPartNumber(product.getManufacturerPartNumber())
				.categoryLevel2Code(product.getCategoryLevel2Code())
				.categoryLevel2Name(product.getCategoryLevel2Name())
				.categoryLevel3Code(product.getCategoryLevel3Code())
				.categoryLevel3Name(product.getCategoryLevel3Name())
				.categoryLevel4Code(product.getCategoryLevel4Code())
				.categoryLevel4Name(product.getCategoryLevel4Name())
				.categoryLevel5Code(product.getCategoryLevel5Code())
				.categoryLevel5Name(product.getCategoryLevel5Name())
				.deliveryInDays(product.getDeliveryInDays())
				.buyerId(product.getBuyerId()).build();
	}
}
