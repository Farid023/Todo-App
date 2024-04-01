package az.gurfdev.todoapp.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import az.gurfdev.todoapp.entities.TodoEntity


@Database(entities = [TodoEntity::class], version = 1)
abstract class RoomDB : RoomDatabase() {
    abstract fun todoDao(): TodoDao

}