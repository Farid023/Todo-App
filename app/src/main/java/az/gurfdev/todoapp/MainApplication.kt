package az.gurfdev.todoapp

import android.app.Application
import androidx.room.Room
import az.gurfdev.todoapp.data.local.RoomDB

class MainApplication: Application() {
    override fun onCreate() {
        super.onCreate()

        database = Room.databaseBuilder(
            applicationContext,
            RoomDB::class.java,
            "todo_database"
        ).build()
    }

    companion object {
        lateinit var database: RoomDB
    }


}