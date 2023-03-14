package com.food.ordering.system.order.service.domain.entity;

import com.food.ordering.system.domain.entity.BaseEntity;
import com.food.ordering.system.domain.valueobject.Money;
import com.food.ordering.system.domain.valueobject.OrderId;
import com.food.ordering.system.order.service.domain.valueobject.OrderItemId;

public class OrderItem extends BaseEntity<OrderItemId> {
	private OrderId orderId;
	private final Product product;
	private final int quantity;
	private final Money price;
	private final Money subTotal;
	
	public OrderItem(Builder builder) {
		super(builder.orderItemId);
		this.product = builder.product;
		this.quantity = builder.quantity;
		this.price = builder.price;
		this.subTotal = builder.subTotal;
	}

//BUSINESS BEHAVIOR ----------------------------------------------------------------------
	//package scope
	void initilizeOrderItem(OrderId orderId, OrderItemId orderItemId) {
		this.setId(orderItemId);
		this.orderId = orderId;
	}
	
	public boolean isPriceValid() {
		return	price.isGreaterThanZero() &&
				price.equals(product.getPrice()) &&
				price.multiply(quantity).equals(subTotal);
	}
	
//GETTERS --------------------------------------------------------------------------------
	public OrderId getOrderId() {
		return orderId;
	}
	public Product getProduct() {
		return product;
	}
	public int getQuantity() {
		return quantity;
	}
	public Money getPrice() {
		return price;
	}
	public Money getSubTotal() {
		return subTotal;
	}
	
//Builder pattern-----------------------------------------------------------------------------
	public static Builder builder() {
		return new Builder();
	}

	public static final class Builder {
		private OrderItemId orderItemId;
		private Product product;
		private int quantity;
		private Money price;
		private Money subTotal;
		
		private Builder() {}
		
		public OrderItem build() {
			return new OrderItem( Builder.this );
		}
		
		public Builder orderItemId(OrderItemId orderItemId) {
			this.orderItemId = orderItemId;
			return this;
		}
		public Builder product(Product product) {
			this.product = product;
			return this;
		}
		public Builder quantity(int quantity) {
			this.quantity = quantity;
			return this;
		}
		public Builder price(Money price) {
			this.price = price;
			return this;
		}
		public Builder subTotal(Money subTotal) {
			this.subTotal = subTotal;
			return this;
		}
	}
}