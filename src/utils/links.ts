export const links = {
  posh: {
    mainShop: '/search?filters=brand%7CPOSH+PLUMBING',
    shopBathroom: '/search?filters=brand%7CPOSH+PLUMBING&categories=Bath',
    shopKitchen:
      '/search?filters=brand%7CPOSH+PLUMBING&categories=Kitchen%2C%20Bar%20%26%20Laundry',
    youTubeEn: 'https://www.youtube.com/watch?v=UnhlUc7o_uo',
    youTubeEs: 'https://www.youtube.com/watch?v=gLBtKHS0MPw',
    shopProducts: [
      '/search?filters=brand%7CPOSH+PLUMBING&categories=Kitchen%2C+Bar+%26+Laundry&categories=Faucets',
      '/search?filters=brand%7CPOSH+PLUMBING&categories=Bath&categories=Shower+%26+Bath+Faucets&categories=Shower+Faucets',
      '/search?filters=brand%7CPOSH+PLUMBING&categories=Bath&categories=Shower+%26+Bath+Faucets&categories=Shower+Faucets',
      '/search?filters=brand%7CPOSH+PLUMBING&categories=Bath&categories=Sink+Faucets&categories=Widespread',
      '/search?filters=brand%7CPOSH+PLUMBING&categories=Bath&categories=Sink+Faucets&categories=Single+Hole',
      '/search?filters=brand%7CPOSH+PLUMBING&categories=Bath&categories=Shower+%26+Bath+Faucets&categories=Bathtub+Faucets'
    ],
    brochure: '/files/posh_brochure.pdf'
  },
  careers: 'https://morsco.wd5.myworkdayjobs.com/Reece_Careers',
  footer: {
    doNotSell: '/do-not-sell-my-info',
    privacyPolicy: '/privacy-policy',
    termsOfAccess: '/terms-of-access',
    termsOfSale: '/terms-of-sale'
  },
  bigDog: {
    seeFullRange: '/search?filters=brand%7CALLPROFESSIONAL+MFG+CO+LTD',
    featuredItems: [
      '/product/MSC-1585329',
      '/product/MSC-1585382',
      '/product/MSC-1585442',
      '/product/MSC-1585403',
      '/product/MSC-1585395'
    ]
  }
} as const;
