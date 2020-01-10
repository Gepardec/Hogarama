package com.gepardec.hogarama.rest.v2;

import com.gepardec.hogarama.rest.interceptor.DetermineOwner;
import com.gepardec.hogarama.rest.translator.ActorDtoTranslator;
import com.gepardec.hogarama.rest.v2.dto.ActorDto;
import com.gepardec.hogarama.service.ActorService;

import javax.inject.Inject;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

@DetermineOwner
public class ActorApiImpl implements ActorApi {

    @Inject
    private ActorService service;
    @Inject
    private ActorDtoTranslator translator;

    @Override
    public Response createActor(SecurityContext securityContext, ActorDto actorDto) {
//        translator.from
        return null;
    }
}
