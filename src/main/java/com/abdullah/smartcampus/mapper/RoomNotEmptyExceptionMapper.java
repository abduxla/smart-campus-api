package com.abdullah.smartcampus.mapper;

import com.abdullah.smartcampus.exception.RoomNotEmptyException;
import java.util.LinkedHashMap;
import java.util.Map;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class RoomNotEmptyExceptionMapper implements ExceptionMapper<RoomNotEmptyException> {

    @Override
    public Response toResponse(RoomNotEmptyException ex) {
        Map<String, Object> error = new LinkedHashMap<>();
        error.put("error", "RoomNotEmptyException");
        error.put("message", ex.getMessage());
        error.put("status", 409);

        return Response.status(Response.Status.CONFLICT)
                .type(MediaType.APPLICATION_JSON)
                .entity(error)
                .build();
    }
}