package com.abdullah.smartcampus.resource;

import com.abdullah.smartcampus.model.Sensor;
import com.abdullah.smartcampus.model.SensorReading;
import com.abdullah.smartcampus.store.DataStore;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import com.abdullah.smartcampus.exception.SensorUnavailableException;

@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class SensorReadingResource {

    private final String sensorId;

    public SensorReadingResource(String sensorId) {
        this.sensorId = sensorId;
    }

    @GET
    public Response getReadingHistory() {
        Sensor sensor = DataStore.sensors.get(sensorId);

        if (sensor == null) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("Sensor not found.")
                    .build();
        }

        List<SensorReading> readings = DataStore.readings.get(sensorId);

        if (readings == null) {
            readings = new ArrayList<>();
        }

        return Response.ok(readings).build();
    }

    @POST
    public Response addReading(SensorReading reading) {
        Sensor sensor = DataStore.sensors.get(sensorId);

        if (sensor == null) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("Sensor not found.")
                    .build();
        }

        if ("MAINTENANCE".equalsIgnoreCase(sensor.getStatus())) {
            throw new SensorUnavailableException("Sensor is in maintenance mode and cannot accept readings.");
        }
        if (reading == null || reading.getId() == null || reading.getId().trim().isEmpty()) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("Reading ID is required.")
                    .build();
        }

        List<SensorReading> readings = DataStore.readings.get(sensorId);

        if (readings == null) {
            readings = new ArrayList<>();
            DataStore.readings.put(sensorId, readings);
        }

        readings.add(reading);
        sensor.setCurrentValue(reading.getValue());

        return Response.created(URI.create("/sensors/" + sensorId + "/readings/" + reading.getId()))
                .entity(reading)
                .build();
    }
}