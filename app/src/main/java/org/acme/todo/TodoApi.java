package org.acme.todo;

import org.jboss.resteasy.reactive.RestPath;

import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;

@Path("/api/todo")
public class TodoApi {

  @Inject
  private TodoRepository todoRepository;

  @GET
  @Produces(MediaType.APPLICATION_JSON)
  public Response getTodo() {
    return Response.ok(todoRepository.findAll().list()).build();
  }

  @POST
  @Transactional
  @Produces(MediaType.APPLICATION_JSON)
  public Response createTodo(Todo newTodo) {
    todoRepository.persist(newTodo);
    return Response.ok(newTodo).build();
  }

  @PUT
  @Path("/{id}")
  @Transactional
  @Produces(MediaType.APPLICATION_JSON)
  public Response updateTodo(@RestPath Long id, Todo updatedTodo) {
    Todo dbTodo = null;
    if (id != null) {
      dbTodo = todoRepository.findById(id);
      if (dbTodo != null) {
        dbTodo.setTitle(updatedTodo.getTitle());
        dbTodo.setContent(updatedTodo.getContent());
        dbTodo.setDone(updatedTodo.isDone());
        todoRepository.persist(dbTodo);
        return Response.ok(dbTodo).build();
      }
    }

    return Response.status(Status.NOT_FOUND).build();
  }

  @DELETE
  @Path("/{id}")
  @Transactional
  @Produces(MediaType.APPLICATION_JSON)
  public Response deleteTodo(@RestPath Long id) {

    Boolean result = false;

    if (id != null) {
      result = todoRepository.deleteById(id);
    }

    return Response.ok(result).build();
  }

}
