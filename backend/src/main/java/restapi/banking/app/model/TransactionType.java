package restapi.banking.app.model;

import lombok.ToString;

@ToString
public enum TransactionType {
    TRANSFER,
    WITHDRAW,
    DEPOSIT
}
