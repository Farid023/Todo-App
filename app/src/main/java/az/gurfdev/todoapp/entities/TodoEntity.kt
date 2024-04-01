package az.gurfdev.todoapp.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity("todo")
data class TodoEntity(
    @PrimaryKey(true) val uid: Int = 0,
    val title: String,
    var completed: Boolean,
    val category: TodoCategory
)