package com.food.ordering.system.order.service.domain.entity;

import java.util.List;
import java.util.stream.Collectors;

import com.food.ordering.system.domain.entity.AggregateRoot;
import com.food.ordering.system.domain.valueobject.CustomerId;
import com.food.ordering.system.domain.valueobject.Money;
import com.food.ordering.system.domain.valueobject.OrderId;
import com.food.ordering.system.domain.valueobject.OrderStatus;
import com.food.ordering.system.domain.valueobject.RestaurantId;
import com.food.ordering.system.order.service.domain.exception.OrderDomainException;
import com.food.ordering.system.order.service.domain.valueobject.OrderItemId;
import com.food.ordering.system.order.service.domain.valueobject.StreetAddress;
import com.food.ordering.system.order.service.domain.valueobject.TrackingId;

public class Order extends AggregateRoot<OrderId> {
	private final CustomerId customerId;
	private final RestaurantId restaurantId;
	private final StreetAddress deliveryAddress;
	private final Money price;
	private final List<OrderItem> items;
	
	//son son final porque iran cambiado durante la logica de negocio
	private TrackingId trackingId;
	private OrderStatus orderStatus;
	private List<String> failureMessage;
	
	private Order(Builder builder) {
		super( builder.orderId );
		this.customerId = builder.customerId;
		this.restaurantId = builder.restaurantId;
		this.deliveryAddress = builder.deliveryAddress;
		this.price = builder.price;
		this.items = builder.items;
		this.trackingId = builder.trackingId;
		this.orderStatus = builder.orderStatus;
		this.failureMessage = builder.failureMessage;
	}

//BUSINESS BEHAVIOR--------------------------------------------------------------------
	public void initializeOrder() {
		this.setId( OrderId.create() );
		this.trackingId = TrackingId.create();
		this.orderStatus = OrderStatus.PENDING;
		this.initializeOrderItems();
	}
	
	private void initializeOrderItems() {
		long itemId = 1;
		for (OrderItem orderItem : this.items) {
			orderItem.initilizeOrderItem(this.getId(), OrderItemId.create(itemId++));
		}
	}

	public void validateOrder() {
		this.validateInitialOrder();
		this.validateTotalPrice();
		this.validateItemsPrice();
	}

	//valida que no tengan ningun valor en status y id antes de la inicializacion
	private void validateInitialOrder() {
		if (orderStatus != null || this.getId() != null) {
			throw new OrderDomainException("Order is not in correct state for initalization");
		}
	}

	private void validateTotalPrice() {
		if (price == null || !price.isGreaterThanZero()) {
			throw new OrderDomainException("Total price must be greater than zero");
		}
	}

	private void validateItemsPrice() {
		Money totalItemsPrice = items.stream().map(orderItem -> {
			validateItemPrice( orderItem );
			return orderItem.getSubTotal();
		}).reduce(Money.ZERO, Money::add);
		
		if (!price.equals(totalItemsPrice)) {
			throw new OrderDomainException("Total price: "+price.getAmount()+ " is not equal to toal price items: "+totalItemsPrice.getAmount());
		}
	}

	private void validateItemPrice(OrderItem orderItem) {
		if ( !orderItem.isPriceValid() ) {
			throw new OrderDomainException("order item price is not valid");
		}
	}
	
	public void pay() {
		if (!OrderStatus.PENDING.equals(orderStatus)) {
			throw new OrderDomainException("Order must be in pending state to pay");
		}
		orderStatus = OrderStatus.PAID;
	}
	
	public void approve() {
		if (!OrderStatus.PAID.equals(orderStatus)) {
			throw new OrderDomainException("Order must be paid for approval");
		}
		orderStatus = OrderStatus.APPROVED;
	}

	public void initCancel(List<String> failureMessages) {
		if (!OrderStatus.PAID.equals(orderStatus)) {
			throw new OrderDomainException("Order must be paid for cancelling");
		}
		orderStatus = OrderStatus.CANCELLING;
		this.updateFailureMessage( failureMessages );
	}

	public void cancel(List<String> failureMessages) {
		if (!(OrderStatus.CANCELLING.equals(orderStatus) || OrderStatus.PENDING.equals(orderStatus))) {
			throw new OrderDomainException("Order must be cancelling or pending por cancel");
		}
		orderStatus = OrderStatus.CANCELLED;
		this.updateFailureMessage( failureMessages );
	}

	private void updateFailureMessage(List<String> failureMessages) {
		if (this.failureMessage == null) {
			this.failureMessage = failureMessages;
		}
		else {
			this.failureMessage.addAll(
				failureMessages.stream().filter( fm -> !fm.isEmpty() && !fm.isBlank() )
							   .collect(Collectors.toList())
			);
		}
	}
	
//GETTERS -----------------------------------------------------------------------------
	public CustomerId getCustomerId() {
		return customerId;
	}
	public RestaurantId getRestaurantId() {
		return restaurantId;
	}
	public StreetAddress getDeliveryAddress() {
		return deliveryAddress;
	}
	public Money getPrice() {
		return price;
	}
	public List<OrderItem> getItems() {
		return items;
	}
	public TrackingId getTrackingId() {
		return trackingId;
	}
	public OrderStatus getOrderStatus() {
		return orderStatus;
	}
	public List<String> getFailureMessage() {
		return failureMessage;
	}
	
//Builder Pattern ---------------------------------------------------------------------------
	public static Builder builder() {
		return new Builder();
	}
	
	public static class Builder {
		private OrderId orderId;
		private CustomerId customerId;
		private RestaurantId restaurantId;
		private StreetAddress deliveryAddress;
		private Money price;
		private List<OrderItem> items;
		private TrackingId trackingId;
		private OrderStatus orderStatus;
		private List<String> failureMessage;
		
		private Builder() {};
		
		public Order build() {
			return new Order(this);
		}
		
		public Builder orderId(OrderId orderId) {
			this.orderId = orderId;
			return this;
		}
		public Builder customerId(CustomerId customerId) {
			this.customerId = customerId;
			return this;
		}
		public Builder restaurantId(RestaurantId restaurantId) {
			this.restaurantId = restaurantId;
			return this;
		}
		public Builder deliveryAddress(StreetAddress deliveryAddress) {
			this.deliveryAddress = deliveryAddress;
			return this;
		}
		public Builder price(Money price) {
			this.price = price;
			return this;
		}
		public Builder items(List<OrderItem> items) {
			this.items = items;
			return this;
		}
		public Builder trackingId(TrackingId trackingId) {
			this.trackingId = trackingId;
			return this;
		}
		public Builder orderStatus(OrderStatus orderStatus) {
			this.orderStatus = orderStatus;
			return this;
		}
		public Builder failureMessage(List<String> failureMessage) {
			this.failureMessage = failureMessage;
			return this;
		}
	}
}