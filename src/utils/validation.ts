interface Validation {
  valid: boolean;
}

/**
 * Function: validateQuantity
 * Checks if the quantity provided is a numerical value
 * @param quantity - Quantity to validate
 */
export const validateQuantity: (quantity: string) => Validation = (
  quantity
) => {
  const quantityRegex = new RegExp('^(\\d*(-{0})$)');
  return { valid: quantityRegex.test(quantity) };
};
