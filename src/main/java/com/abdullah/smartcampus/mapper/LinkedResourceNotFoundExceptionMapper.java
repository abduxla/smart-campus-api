package com.abdullah.smartcampus.mapper;

import com.abdullah.smartcampus.exception.LinkedResourceNotFoundException;
import java.util.LinkedHashMap;
import java.util.Map;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class LinkedResourceNotFoundExceptionMapper implements ExceptionMapper<LinkedResourceNotFoundException> {

    @Override
    public Response toResponse(LinkedResourceNotFoundException ex) {
        Map<String, Object> error = new LinkedHashMap<>();
        error.put("error", "LinkedResourceNotFoundException");
        error.put("message", ex.getMessage());
        error.put("status", 400);

        return Response.status(Response.Status.BAD_REQUEST)
                .type(MediaType.APPLICATION_JSON)
                .entity(error)
                .build();
    }
}