package com.abdullah.smartcampus.mapper;

import com.abdullah.smartcampus.exception.SensorUnavailableException;
import java.util.LinkedHashMap;
import java.util.Map;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class SensorUnavailableExceptionMapper implements ExceptionMapper<SensorUnavailableException> {

    @Override
    public Response toResponse(SensorUnavailableException ex) {
        Map<String, Object> error = new LinkedHashMap<>();
        error.put("error", "SensorUnavailableException");
        error.put("message", ex.getMessage());
        error.put("status", 403);

        return Response.status(Response.Status.FORBIDDEN)
                .type(MediaType.APPLICATION_JSON)
                .entity(error)
                .build();
    }
}