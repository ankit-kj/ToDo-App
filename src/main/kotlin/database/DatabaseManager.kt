package database

import entities.ToDo
import entities.ToDoDraft
import org.ktorm.database.Database
import org.ktorm.dsl.delete
import org.ktorm.dsl.eq
import org.ktorm.dsl.insertAndGenerateKey
import org.ktorm.dsl.update
import org.ktorm.entity.firstOrNull
import org.ktorm.entity.sequenceOf
import org.ktorm.entity.toList

class DatabaseManager {
    private val hostname =  "127.0.0.1"
    private val databaseName = "schema_for_todo_app"
    private val username = "root"
    private val password = "ankit@porter"

    private val ktormDatabase : Database

    init{
        val jdbcUrl = "jdbc:mysql://$hostname:3306/$databaseName?user=$username&password=$password&useSSL=false"
        ktormDatabase = Database.connect (jdbcUrl)
    }

    fun getAllToDos (): List<DBTodoEntity> {
        return ktormDatabase.sequenceOf(DBTodoTable).toList()
    }

    fun getToDo(id : Int) : DBTodoEntity? {
        return ktormDatabase.sequenceOf(DBTodoTable).firstOrNull(){it.id eq id}
    }

    fun addToDo(draft: ToDoDraft): ToDo {

        val insertedId =ktormDatabase.insertAndGenerateKey(DBTodoTable){
            set(DBTodoTable.title , draft.title)
            set(DBTodoTable.done , draft.done)
        } as Int

        return ToDo(insertedId, draft.title , draft.done)
    }

    fun removeToDo(id: Int): Boolean {
        val removedRows = ktormDatabase.delete(DBTodoTable){
            it.id eq id
        }
        return removedRows > 0
    }

    fun updateToDo(id: Int, draft: ToDoDraft): Boolean {

        val updatedRows = ktormDatabase.update(DBTodoTable){
            set(DBTodoTable.title , draft.title)
            set(DBTodoTable.done , draft.done)
            where{
                it.id eq id
            }
        }
        return updatedRows > 0
    }
}