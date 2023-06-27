package com.reece.specialpricing.utilities;

import com.reece.specialpricing.model.SpecialPriceSuggestion;
import com.reece.specialpricing.model.UploadPriceChangesResult;
import com.reece.specialpricing.model.pojo.SpecialPriceChangeRequest;
import com.reece.specialpricing.postgres.SpecialPrice;
import com.reece.specialpricing.postgres.User;
import com.reece.specialpricing.postgres.UserBranch;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class TestCommon {
    public static final SpecialPriceSuggestion validSuggestion = new SpecialPriceSuggestion(
            "change1",
            "change1",
            "DFLT",
            "change1",
            1000.00,
            100.00,
            "Rate Card",
            "change1",
            "change1"
    );

    public static final SpecialPriceSuggestion invalidSuggestion = new SpecialPriceSuggestion(
            "change2",
            "change2",
            "DFLT",
            "change2",
            2000.00,
            200.00,
            "change2",
            "change2",
            "change2"
    );

    public static final List<SpecialPriceSuggestion> listOfSuggestions = List.of(
            new SpecialPriceSuggestion(
                    "customer1",
                    "change",
                    "DFLT",
                    "branch1",
                    1000.00,
                    100.00,
                    "Rate card",
                    "change",
                    "change"
            ),
            new SpecialPriceSuggestion(
                    "customer2",
                    "change",
                    "DFLT",
                    "branch1",
                    1000.00,
                    100.00,
                    "Rate card",
                    "change",
                    "change"
            ),
            new SpecialPriceSuggestion(
                    "customer2",
                    "change",
                    "DFLT",
                    "branch1",
                    1000.00,
                    100.00,
                    "Rate card",
                    "change",
                    "change"
            ),
            new SpecialPriceSuggestion(
                    "customer2",
                    "change",
                    "DFLT",
                    "branch2",
                    1000.00,
                    100.00,
                    "Rate card",
                    "change",
                    "change"
            )
    );

    public static final SpecialPriceChangeRequest emptyChangeRequest = new SpecialPriceChangeRequest();
    public static final SpecialPriceChangeRequest validChangeRequest = new SpecialPriceChangeRequest(
            List.of( new SpecialPriceSuggestion(
                    "valid",
                    "valid",
                    "DFLT",
                    "valid",
                    1111.11,
                    111.11,
                    "Rate Card",
                    "valid",
                    "valid"
            )),
            List.of( new SpecialPriceSuggestion(
                            "valid",
                            "valid",
                            "DFLT",
                            "valid",
                            1111.11,
                            111.11,
                            "Rate Card",
                            "valid",
                            "valid"
            ))
    );

    public static final SpecialPriceChangeRequest validCreateRequest = new SpecialPriceChangeRequest(
           Collections.emptyList(),
            List.of( new SpecialPriceSuggestion(
                    "valid",
                    "valid",
                    "DFLT",
                    "valid",
                    1111.11,
                    111.11,
                    "Rate Card",
                    "valid",
                    "valid"
            ))
    );

    public static final SpecialPriceChangeRequest invalidProductIdCreateRequest = new SpecialPriceChangeRequest(
            Collections.emptyList(),
            List.of( new SpecialPriceSuggestion(
                    "invalidProductId",
                    "     ",
                    "DFLT",
                    "invalid",
                    2222.22,
                    222.22,
                    "Rate Card",
                    "invalidProductId",
                    "invalidProductId"
            ))
    );

    public static final SpecialPriceChangeRequest invalidProductIdChangeRequest = new SpecialPriceChangeRequest(
            List.of( new SpecialPriceSuggestion(
                    "invalidProductId",
                    "     ",
                    "DFLT",
                    "invalid",
                    2222.22,
                    222.22,
                    "Rate Card",
                    "invalidProductId",
                    "invalidProductId"
            )),
            List.of( new SpecialPriceSuggestion(
                    "invalidProductId",
                    "     ",
                    "DFLT",
                    "invalid",
                    2222.22,
                    222.22,
                    "Rate Card",
                    "invalidProductId",
                    "invalidProductId"
            ))
    );

    public static final SpecialPriceChangeRequest invalidPriceCategoryChangeRequest = new SpecialPriceChangeRequest(
            List.of(new SpecialPriceSuggestion(
                    "invalidPriceCategory",
                    "invalidPriceCategory",
                    "DFLT",
                    "invalidPriceCategory",
                    3333.33,
                    333.33,
                    "Rate bard",
                    "invalidPriceCategory",
                    "invalidPriceCategory"
            )),
            List.of(new SpecialPriceSuggestion(
                    "invalidPriceCategory",
                    "invalidPriceCategory",
                    "DFLT",
                    "invalidPriceCategory",
                    3333.33,
                    333.33,
                    "Rate bard",
                    "invalidPriceCategory",
                    "invalidPriceCategory"
            ))
    );


    public static final UploadPriceChangesResult emptyUploadResponse =
            new UploadPriceChangesResult(new ArrayList<>(), new ArrayList<>(),new ArrayList<>(), new ArrayList<>());
    public static final UploadPriceChangesResult allSuccessfulUploadResponse = new UploadPriceChangesResult(
            List.of(
                    "/file/path/allSuccessful.csv"
            ),
            new ArrayList<>(),
            new ArrayList<>(),
            new ArrayList<>()
    );
    public static final UploadPriceChangesResult someSuccessSomeFailedUploadResponse = new UploadPriceChangesResult(
            List.of(
                    "/file/path/someSuccessful.csv"
            ),
            List.of(
                    "/file/path/someSuccessful.csv"
            ),
            List.of(
                    new SpecialPriceSuggestion(
                            "failed2",
                            "failed2",
                            "DFLT",
                            "failed2",
                            4444.44,
                            444.44,
                            "failed2",
                            "failed2",
                            "failed2"
                    )
            ),
            List.of(
                    new SpecialPriceSuggestion(
                            "failed2",
                            "failed2",
                            "DFLT",
                            "failed2",
                            4444.44,
                            444.44,
                            "failed2",
                            "failed2",
                            "failed2"
                    )
            )
    );
    public static final UploadPriceChangesResult allFailuresUploadResponse = new UploadPriceChangesResult(
            new ArrayList<>(),
            new ArrayList<>(),
            List.of(
                    new SpecialPriceSuggestion(
                            "failed3",
                            "failed3",
                            "DFLT",
                            "failed3",
                            5555.55,
                            555.55,
                            "failed3",
                            "failed3",
                            "failed3"
                    )
            ),
            List.of(
                    new SpecialPriceSuggestion(
                            "failed3",
                            "failed3",
                            "DFLT",
                            "failed3",
                            5555.55,
                            555.55,
                            "failed3",
                            "failed3",
                            "failed3"
                    )
            )

    );

    public static final List<SpecialPrice> MOCK_CUSTOMER_ID_SPECIAL_PRICE_SEARCH_RESULT = List.of(
            new SpecialPrice(
                    "ProductId1",
                    "CustomerId1",
                    "Branch1",
                    "Customer 1",
                    "https://media.istockphoto.com/vectors/pipe-fittings-vector-icon-vector-id578287918?s=612x612",
                    "PipeCo1",
                    "An Awesome Pipe",
                    "PipeCo1-ManuNum1",
                    "Billy Bob",
                    "Yellow4",
                    100,
                    58.76,
                    61.56,
                    62.00,
                    59.89,
                    60.00,
                    60.25,
                    65.32,
                    "territory1"
            ),
            new SpecialPrice(
                    "ProductId2",
                    "CustomerId1",
                    "Branch1",
                    "Customer 1",
                    "https://media.istockphoto.com/vectors/pipe-fittings-vector-icon-vector-id578287918?s=612x612",
                    "PipeCo1",
                    "Yet Another Awesome Pipe",
                    "PipeCo1-ManuNum2",
                    "Billy Bob",
                    "Yellow4",
                    10,
                    5.76,
                    6.56,
                    6.00,
                    5.89,
                    6.00,
                    6.25,
                    6.32,
                    "territory1"
            ),
            new SpecialPrice(
                    "ProductId3",
                    "CustomerId1",
                    "Branch2",
                    "Customer 1",
                    "https://media.istockphoto.com/vectors/pipe-fittings-vector-icon-vector-id578287918?s=612x612",
                    "PipeCo1",
                    "A Slightly Less Awesome Pipe",
                    "PipeCo1-ManuNum255",
                    "Bobby Bill",
                    "Maroon10",
                    50000,
                    0.76,
                    0.56,
                    0.50,
                    .89,
                    .40,
                    .25,
                    .32,
                    "territory1"
            )
    );

    public static final List<SpecialPrice> MOCK_PRODUCT_ID_SPECIAL_PRICE_SEARCH_RESULT = List.of(
            new SpecialPrice(
                    "ProductId1",
                    "CustomerId1",
                    "Branch1",
                    "Customer 1",
                    "https://media.istockphoto.com/vectors/pipe-fittings-vector-icon-vector-id578287918?s=612x612",
                    "PipeCo1",
                    "An Awesome Pipe",
                    "PipeCo1-ManuNum1",
                    "Billy Bob",
                    "Yellow4",
                    100,
                    58.76,
                    61.56,
                    62.00,
                    59.89,
                    60.00,
                    60.25,
                    65.32,
                    "territory1"
            ),
            new SpecialPrice(
                    "ProductId1",
                    "CustomerId2",
                    "Branch1",
                    "Customer 2",
                    "https://media.istockphoto.com/vectors/pipe-fittings-vector-icon-vector-id578287918?s=612x612",
                    "PipeCo1",
                    "An Awesome Pipe",
                    "PipeCo1-ManuNum1",
                    "Billy Bob",
                    "Yellow4",
                    10,
                    5.76,
                    6.56,
                    6.00,
                    5.89,
                    6.00,
                    6.25,
                    6.32,
                    "territory1"
            ),
            new SpecialPrice(
                    "ProductId1",
                    "CustomerId3",
                    "Branch2",
                    "Customer 3",
                    "https://media.istockphoto.com/vectors/pipe-fittings-vector-icon-vector-id578287918?s=612x612",
                    "PipeCo1",
                    "An Awesome Pipe",
                    "PipeCo1-ManuNum1",
                    "Bobby Bill",
                    "Maroon10",
                    50000,
                    0.76,
                    0.56,
                    0.50,
                    0.56,
                    .89,
                    .25,
                    .32,
                    "territory1"
            )
    );

    public static final List<SpecialPrice> MOCK_PRODUCT_ID_AND_CUSTOMER_ID_SPECIAL_PRICE_SEARCH_RESULT = List.of(
            new SpecialPrice(
                    "ProductId1",
                    "CustomerId1",
                    "Branch1",
                    "Customer 1",
                    "https://media.istockphoto.com/vectors/pipe-fittings-vector-icon-vector-id578287918?s=612x612",
                    "PipeCo1",
                    "An Awesome Pipe",
                    "PipeCo1-ManuNum1",
                    "Billy Bob",
                    "Yellow4",
                    100,
                    58.76,
                    61.56,
                    62.00,
                    59.89,
                    60.00,
                    60.25,
                    65.32,
                    "territory1"
            ),
            new SpecialPrice(
                    "ProductId1",
                    "CustomerId1",
                    "Branch2",
                    "Customer 1",
                    "https://media.istockphoto.com/vectors/pipe-fittings-vector-icon-vector-id578287918?s=612x612",
                    "PipeCo1",
                    "An Awesome Pipe",
                    "PipeCo1-ManuNum1",
                    "Bobby Bill",
                    "Blue2",
                    100,
                    58.76,
                    61.56,
                    62.00,
                    59.89,
                    60.00,
                    60.25,
                    65.32,
                    "territory1"
            )
    );

    public static final Optional<User> USER_TEST_1 = Optional.of(
      new User("1000","malvarez",
              List.of(
                      new UserBranch("1000", "1039")
              ))
      );
}