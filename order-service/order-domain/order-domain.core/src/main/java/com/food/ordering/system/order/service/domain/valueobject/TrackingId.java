package com.food.ordering.system.order.service.domain.valueobject;

import java.util.UUID;

import com.food.ordering.system.domain.valueobject.BaseId;

public class TrackingId extends BaseId<UUID> {

	protected TrackingId(UUID value) {
		super(value);
	}

	public static TrackingId create() {
		return new TrackingId(UUID.randomUUID());
	}
}