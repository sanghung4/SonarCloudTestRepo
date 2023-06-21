package com.reece.platform.accounts.model.key;

import java.io.Serializable;
import java.util.UUID;

public class UserAccountKey implements Serializable {
    private UUID userId;
    private UUID accountId;
}
