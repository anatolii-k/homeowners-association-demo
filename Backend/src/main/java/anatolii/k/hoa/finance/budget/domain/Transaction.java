package anatolii.k.hoa.finance.budget.domain;

import anatolii.k.hoa.common.domain.MoneyAmount;

import java.time.LocalDateTime;

public class Transaction {

    public enum Type{
        INCOME,
        EXPENSE
    }

    private Long id;
    private String description;
    private Long categoryId;
    private LocalDateTime date;
    private Type type;
    private MoneyAmount amount;
}
