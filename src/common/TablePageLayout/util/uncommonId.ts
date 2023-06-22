export function uncommonId(id: string | string[]) {
  let formattedId = id;
  if (id.includes('Role')) {
    return (formattedId = 'roleId');
  }
  if (id.includes('Rejected Reason')) {
    return (formattedId = 'rejectionReason');
  }
  if (id.includes('Order #')) {
    return (formattedId = 'orderNumber');
  }

  return formattedId;
}
