package anatolii.k.hoa.common.domain;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;

public final class MoneyAmount {

    public static MoneyAmount of(BigDecimal amount) {
        return new MoneyAmount(amount);
    }

    public static MoneyAmount of(String amount) {
        return new MoneyAmount(new BigDecimal(amount));
    }

    public static MoneyAmount of(double amount) {
        return new MoneyAmount(BigDecimal.valueOf(amount));
    }

    public static MoneyAmount zero() {
        return new MoneyAmount(BigDecimal.ZERO);
    }

    public MoneyAmount add(MoneyAmount other) {
        return new MoneyAmount(this.amount.add(other.amount));
    }

    public MoneyAmount subtract(MoneyAmount other) {
        return new MoneyAmount(this.amount.subtract(other.amount));
    }

    public MoneyAmount multiply(BigDecimal multiplier) {
        return new MoneyAmount(this.amount.multiply(multiplier));
    }

    public MoneyAmount divide(BigDecimal divisor) {
        return new MoneyAmount(this.amount.divide(divisor, RoundingMode.HALF_EVEN));
    }

    public boolean isGreaterThan(MoneyAmount other) {
        return this.amount.compareTo(other.amount) > 0;
    }

    public boolean isLessThan(MoneyAmount other) {
        return this.amount.compareTo(other.amount) < 0;
    }

    public boolean isZero() {
        return this.amount.compareTo(BigDecimal.ZERO) == 0;
    }

    public boolean isNegative() {
        return this.amount.compareTo(BigDecimal.ZERO) < 0;
    }

    public boolean isPositive() {
        return this.amount.compareTo(BigDecimal.ZERO) > 0;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        MoneyAmount that = (MoneyAmount) o;
        return Objects.equals(amount, that.amount);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(amount);
    }

    @Override
    public String toString() {
        return amount.toString();
    }

    public BigDecimal getAmount() {
        return amount;
    }

    private final BigDecimal amount;
    private static final int DEFAULT_SCALE = 2;

    private MoneyAmount(BigDecimal amount) {
        this.amount = amount.setScale(DEFAULT_SCALE, RoundingMode.HALF_EVEN);
    }
}