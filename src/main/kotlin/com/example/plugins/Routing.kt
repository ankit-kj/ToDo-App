package com.example.plugins

import entities.ToDo
import entities.ToDoDraft
import io.ktor.server.routing.*
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.response.*
import io.ktor.server.request.*
import repository.ToDoRepository
import repository.InMemoryToDoRepository
import repository.MySQLToDoRepository

fun Application.configureRouting() {

    routing {

        val repository : ToDoRepository = MySQLToDoRepository()

        get("/") {
            call.respondText("Hello World! making a kotlin todoList")
        }

        get("/todos"){
            call.respond(repository.getAllToDos())
        }

        get("/todos/{id}"){
            val id = call.parameters["id"]?.toIntOrNull();

            if(id == null){
                call.respond(HttpStatusCode.BadRequest , "id must be an integer")
                return@get
            }

            val todo = repository.getToDo(id)
            if(todo == null){
                call.respond(HttpStatusCode.NotFound , "found no todo for the provided ${id}")
            }else{
                call.respond(todo);
            }

            //call.respondText("todoList for for todo Item No. ${id}")
        }

        post("/todos"){
            val toDoDraft = call.receive<ToDoDraft>() // if the input can't be converted then it is automatically handeled by ktor
            val todo = repository.addToDo(toDoDraft)
            call.respond(todo)
        }

        put("/todos/{id}"){
            val todoDraft = call.receive<ToDoDraft>()
            val todoId = call.parameters["id"]?.toIntOrNull()
            if(todoId == null){
                call.respond(HttpStatusCode.BadRequest , "id parameter has to be an integer")
                return@put
            }

            val updated = repository.updateToDo(todoId,todoDraft)

            if(updated) call.respond(HttpStatusCode.OK) // it is possible that there is no todo list with the given id
            else call.respond(HttpStatusCode.NotFound,"found no ToDo with the id ${todoId}")
        }

        delete("/todos/{id}"){
            val todoId = call.parameters["id"]?.toIntOrNull()
            if(todoId == null){
                call.respond(HttpStatusCode.BadRequest , "id parameter has to be an integer")
                return@delete
            }

            val removed = repository.removeToDo(todoId)
            if(removed) call.respond(HttpStatusCode.OK)
            else call.respond(HttpStatusCode.NotFound, "found no ToDo with id ${todoId}")

        }
    }
}
