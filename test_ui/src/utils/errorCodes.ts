type errorCodesType = {
  [e: number]: string;
};

export const errorCodes: errorCodesType = {
  200: "File processed successfully!",
  201: "CSV file will be formatting only – tags, blank lines, and sub-totals",
  401: "The combined manufacturer/model column is not in a simple format, system cannot process",
  502: "Cannot Split Pages of PDF",
  504: "Error processing document",
};

export const noChangeErrorCodes = [400, 500, 501, 503, 505, 506];
