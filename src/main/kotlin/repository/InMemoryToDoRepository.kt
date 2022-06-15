package repository

import entities.ToDo
import entities.ToDoDraft

class InMemoryToDoRepository : ToDoRepository {
    private val todos = mutableListOf<ToDo>()

    override fun getAllToDos(): List<ToDo> {
        return todos;
    }

    override fun getToDo(id: Int): ToDo? {
        return todos.firstOrNull(){it.id == id}
    }

    override fun addToDo(draft: ToDoDraft): ToDo {

        val id = todos.size + 1
        val title = draft.title
        val done = draft.done

        val todo = ToDo(id,title,done)
        todos.add(todo)
        return todo
    }

    override fun removeToDo(id: Int): Boolean {
        return todos.removeIf { it.id == id }
    }

    override fun updateToDo(id: Int, draft: ToDoDraft): Boolean {
        val todo = todos.firstOrNull() {it.id == id}
        if(todo == null) return false

        todo.title = draft.title
        todo.done = draft.done
        return true
    }

}