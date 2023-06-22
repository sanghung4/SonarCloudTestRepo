package com.reece.punchoutcustomerbff.models.models;

import com.reece.punchoutcustomerbff.models.daos.legacies.ProductLegacyDao;
import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Product {
	private String productId;
	private String productName;
	private String productDescription;
	private String imageFullSize;
	private BigDecimal price;
	private String partNumber;
	private String unitOfMeasure;
	private String manufacturer;
	private String categoryLevel1Code;
	private String categoryLevel1Name;
	private int unspsc;
	private String imageThumb;
	private int scaleStart;
	private int scaleEnd;
	private BigDecimal listPrice;
	private String manufacturerPartNumber;
	private String categoryLevel2Code;
	private String categoryLevel2Name;
	private String categoryLevel3Code;
	private String categoryLevel3Name;
	private String categoryLevel4Code;
	private String categoryLevel4Name;
	private String categoryLevel5Code;
	private String categoryLevel5Name;
	private int deliveryInDays;
	private String buyerId;

	public static Product getFromProductDao(ProductLegacyDao productLegacyDao) {
		return Product.builder().productId(productLegacyDao.getProductId())
				.productName(productLegacyDao.getProductName())
				.productDescription(productLegacyDao.getProductDescription())
				.imageFullSize(productLegacyDao.getImageFullSize())
				.price(productLegacyDao.getPrice())
				.partNumber(productLegacyDao.getPartNumber())
				.unitOfMeasure(productLegacyDao.getUnitOfMeasure())
				.manufacturer(productLegacyDao.getManufacturer())
				.categoryLevel1Code(productLegacyDao.getCategoryLevel1Code())
				.categoryLevel1Name(productLegacyDao.getCategoryLevel1Name())
				.unspsc(productLegacyDao.getUnspsc())
				.imageThumb(productLegacyDao.getImageThumb())
				.scaleStart(productLegacyDao.getScaleStart())
				.scaleEnd(productLegacyDao.getScaleEnd())
				.listPrice(productLegacyDao.getListPrice())
				.manufacturerPartNumber(productLegacyDao.getManufacturerPartNumber())
				.categoryLevel2Code(productLegacyDao.getCategoryLevel2Code())
				.categoryLevel2Name(productLegacyDao.getCategoryLevel2Name())
				.categoryLevel3Code(productLegacyDao.getCategoryLevel3Code())
				.categoryLevel3Name(productLegacyDao.getCategoryLevel3Name())
				.categoryLevel4Code(productLegacyDao.getCategoryLevel4Code())
				.categoryLevel4Name(productLegacyDao.getCategoryLevel4Name())
				.categoryLevel5Code(productLegacyDao.getCategoryLevel5Code())
				.categoryLevel5Name(productLegacyDao.getCategoryLevel5Name())
				.deliveryInDays(productLegacyDao.getDeliveryInDays())
				.buyerId(productLegacyDao.getBuyerId()).build();
	}
}
