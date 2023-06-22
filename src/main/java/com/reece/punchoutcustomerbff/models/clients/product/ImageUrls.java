package com.reece.punchoutcustomerbff.models.clients.product;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * the urls for different size of images.
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class ImageUrls {
  private String thumb;
  private String small;
  private String medium;
  private String large;
}
