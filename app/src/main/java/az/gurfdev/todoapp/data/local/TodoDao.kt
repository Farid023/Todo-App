package az.gurfdev.todoapp.data.local

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Update
import androidx.room.Upsert
import az.gurfdev.todoapp.entities.TodoEntity


@Dao
interface TodoDao {


    @Query("SELECT * FROM todo")
    suspend fun getAllTodo(): List<TodoEntity>


//    @Insert(onConflict = OnConflictStrategy.REPLACE)
//    suspend fun addTodo(todo: TodoEntity)


//    @Delete
//    suspend fun deleteCart(product: TodoEntity)

    @Query("SELECT * FROM todo ")
    suspend fun getAll(): List<TodoEntity>


    @Query("SELECT * FROM todo WHERE category = :category ")
    fun getByCategory(category: String): List<TodoEntity>

    // Add or update the existing user data
    @Upsert
    suspend fun add(todo: TodoEntity)

    @Update
    suspend fun update(todo: TodoEntity)

//    @Delete
//    suspend fun delete(user: TodoEntity)


}