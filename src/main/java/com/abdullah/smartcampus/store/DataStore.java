package com.abdullah.smartcampus.store;

import com.abdullah.smartcampus.model.Room;
import com.abdullah.smartcampus.model.Sensor;
import com.abdullah.smartcampus.model.SensorReading;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DataStore {
    public static final Map<String, Room> rooms = new HashMap<>();
    public static final Map<String, Sensor> sensors = new HashMap<>();
    public static final Map<String, List<SensorReading>> readings = new HashMap<>();

    static {
        Room room1 = new Room("LIB-301", "Library Quiet Study", 40);
        Room room2 = new Room("ENG-102", "Engineering Lab", 30);

        rooms.put(room1.getId(), room1);
        rooms.put(room2.getId(), room2);
    }

    private DataStore() {
    }
}