package com.gepardec.hogarama.ai;

import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Response;

import java.util.List;

@Path("/unitmanagement/chat")
public class ChatResource {

    @Inject
    private ChatService chatService;

    @POST
    @Path("/")
    @Consumes("application/json")
    @Produces("application/json")
    public Response chat(List<Message> messages) {
        Dialog dialog = chatService.chat(messages.toArray(new Message[0]));
        return Response.ok(dialog.getLastMessage()).build();
    }
}