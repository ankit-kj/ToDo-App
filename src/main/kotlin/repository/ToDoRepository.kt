package repository

import entities.ToDo
import entities.ToDoDraft

interface ToDoRepository {

    fun getAllToDos() : List<ToDo>

    fun getToDo(id : Int) : ToDo?

    fun addToDo(draft: ToDoDraft) : ToDo // returns the same to do with an associated with it

    fun removeToDo(id : Int) : Boolean // boolean tell whether the remove is successful or not

    fun updateToDo(id : Int , draft : ToDoDraft) : Boolean // tells whether the update is successful or not

}