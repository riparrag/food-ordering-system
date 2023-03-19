package com.food.ordering.system.order.service.domain.entity;

import java.util.List;

import com.food.ordering.system.domain.entity.AggregateRoot;
import com.food.ordering.system.domain.valueobject.RestaurantId;

public class Restaurant extends AggregateRoot<RestaurantId> {

	private final List<Product> products;
	private boolean isActive;
	
	private Restaurant(Builder builder) {
		super(builder.restaurantId);
		this.products = builder.products;
		this.isActive = builder.isActive;
	}

	public List<Product> getProducts() {
		return products;
	}

	public boolean isActive() {
		return isActive;
	}

	public static Builder builder() {
		return new Builder();
	}
	
	public static final class Builder {
		private RestaurantId restaurantId;
		private List<Product> products;
		private boolean isActive;
		
		private Builder() {}
		
		public Restaurant build() {
			return new Restaurant(this);
		}
		
		public Builder restaurantId(RestaurantId restaurantId) {
			this.restaurantId = restaurantId;
			return this;
		}
		
		public Builder products(List<Product> products) {
			this.products = products;
			return this;
		}
		
		public Builder active(boolean isActive) {
			this.isActive = isActive;
			return this;
		}
	}
}