package com.automationintesting.it.checks;

import com.automationintesting.it.payloads.Room;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.params.aggregator.ArgumentsAccessor;
import org.junit.jupiter.params.aggregator.ArgumentsAggregationException;
import org.junit.jupiter.params.aggregator.ArgumentsAggregator;

public class RoomAggregator implements ArgumentsAggregator {
    @Override
    public Object aggregateArguments(
        ArgumentsAccessor argumentsAccessor,
        ParameterContext parameterContext
    ) throws ArgumentsAggregationException {
        String roomName = argumentsAccessor.getString(0);
        String roomType = argumentsAccessor.getString(1);
        int roomPrice = argumentsAccessor.getInteger(2);
        String roomDescription = argumentsAccessor.getString(3);
        String[] roomFeatures = argumentsAccessor.getString(4).split("\\|");
        String roomImageUrl = argumentsAccessor.getString(5);

        Room room = new Room();
        room.setRoomName(roomName);
        room.setType(roomType);
        room.setRoomPrice(roomPrice);
        room.setDescription(roomDescription);
        room.setFeatures(roomFeatures);
        room.setImage(roomImageUrl);

        return room;
    }
}
