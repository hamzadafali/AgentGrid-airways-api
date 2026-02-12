package io.agentgrid.domain.valueObject;

import io.agentgrid.domain.exception.BusinessException;

import java.math.BigDecimal;
import java.util.Currency;

public record Money(BigDecimal amount, Currency currency) {
    public Money {
        if (amount == null) throw new BusinessException("Money amount is required");
        if (currency == null) throw new BusinessException("Currency is required");
        if (amount.scale() > 2) throw new BusinessException("Money scale must be <= 2");
        if (amount.compareTo(BigDecimal.ZERO) < 0) throw new BusinessException("Money cannot be negative");
    }
    public static Money of(String amount, String currencyCode) {
        return new Money(new BigDecimal(amount), Currency.getInstance(currencyCode));
    }
    public Money add(Money other) {
        if (!currency.equals(other.currency)) throw new BusinessException("Cannot add different currencies");
        return new Money(amount.add(other.amount), currency);
    }
}
