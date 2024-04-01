package az.gurfdev.todoapp.ui.main





import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import az.gurfdev.todoapp.MainApplication
import az.gurfdev.todoapp.entities.TodoEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class MainViewModel(
    // private val todoRepo: TodoRepository
) : ViewModel() {

    private val db = az.gurfdev.todoapp.MainApplication.database.todoDao()

    private val _todoList: MutableLiveData<List<TodoEntity>> = MutableLiveData()
    val todoList: LiveData<List<TodoEntity>> get() = _todoList


    init {
        getAllData()
    }

    fun getAllData(){
        viewModelScope.launch(Dispatchers.IO){
            val list = db.getAll()
            _todoList.postValue(list)
        }
    }

    fun setCompleted(todo: TodoEntity){
        viewModelScope.launch(Dispatchers.IO) {
            db.update(todo)
        }
    }


//    init {
//        getUserData()
//    }
//
//    private fun getUserData() {
//        viewModelScope.launch {
//          ..  _state.value = dao.getAll()
//        }
//    }
//
//    fun insertData(user: User) {
//        viewModelScope.launch {
//            dao.insert(user)
//            getUserData()
//        }
//    }
//
//    fun deleteData(user: User) {
//        viewModelScope.launch {
//            dao.delete(user)
//            getUserData()
//        }
//    }

}