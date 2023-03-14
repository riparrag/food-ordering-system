
package com.food.ordering.system.domain.valueobject;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;

public class Money {
	public final static Money ZERO = Money.create(BigDecimal.ZERO);
	private final static int ROUNDING_DECIMAL_SCALE = 2;
	
	private final BigDecimal amount;
	
	public Money(BigDecimal amount) {
		this.amount = amount;
	}
	
	public static Money create(BigDecimal amount) {
		return new Money(amount);
	}

	public boolean isGreaterThanZero() {
		return this.getAmount() != null && this.getAmount().compareTo(BigDecimal.ZERO) > 0;
	}
	
	public boolean isGreaterThan(Money money) {
		return this.getAmount() != null && this.getAmount().compareTo(money.getAmount()) > 0;
	}
	
	public Money add(Money money) {
		return	new Money( 
					this.setScale(
						this.getAmount().add(money.getAmount())
					)
				);
	}
	
	public Money subtract(Money money) {
		return	new Money( 
					this.setScale(
						this.getAmount().subtract(money.getAmount())
					)
				);
	}
	
	public Money multiply(Money money) {
		return	new Money( 
					this.setScale(
						this.getAmount().multiply(money.getAmount())
					)
				);
	}
	
	public Money multiply(int quantity) {
		return	new Money( 
					this.setScale(
						this.getAmount().multiply(new BigDecimal(quantity))
					)
				);
	}	
	
	public Money divide(Money money) {
		return	new Money( 
					this.setScale(
						this.getAmount().divide(money.getAmount())
					)
				);
	}
	
	public BigDecimal setScale(BigDecimal input) {
		return input.setScale(ROUNDING_DECIMAL_SCALE, RoundingMode.HALF_EVEN);
	}
	
	public BigDecimal getAmount() {
		return amount;
	}

	@Override
	public int hashCode() {
		return Objects.hash(amount);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Money other = (Money) obj;
		return Objects.equals(amount, other.amount);
	}
}