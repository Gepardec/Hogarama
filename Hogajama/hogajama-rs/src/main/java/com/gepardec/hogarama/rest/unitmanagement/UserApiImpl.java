package com.gepardec.hogarama.rest.unitmanagement;

import com.gepardec.hogarama.rest.unitmanagement.dto.UserDto;
import org.apache.http.HttpStatus;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

public class UserApiImpl implements UserApi {

    @Override
    public Response getUser(SecurityContext securityContext) {
        // provide temporary dummy response for frontend
        UserDto userDto = new UserDto();
        userDto.setName("Jamal Dummy User");
        userDto.setGivenName("Jamal");
        userDto.setFamilyName("Gepard");
        userDto.setEmail("jamal@gepard.com");
        return new BaseResponse<>(userDto, HttpStatus.SC_OK).createRestResponse();
    }
}
