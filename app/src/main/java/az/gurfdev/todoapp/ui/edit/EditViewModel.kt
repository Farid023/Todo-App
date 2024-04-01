package az.gurfdev.todoapp.ui.edit

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import az.gurfdev.todoapp.entities.TodoEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class EditTodoViewModel() : ViewModel() {

    private val db = az.gurfdev.todoapp.MainApplication.database.todoDao()

    private val _selectedCategoryTodoList: MutableLiveData<List<TodoEntity>> = MutableLiveData()
    val selectedCategoryTodoList: LiveData<List<TodoEntity>> get() = _selectedCategoryTodoList

    fun saveTodo(todo: TodoEntity, category: String) {
        viewModelScope.launch {
            db.add(todo)
            getByCategory(category = category)
            _selectedCategoryTodoList.value = _selectedCategoryTodoList.value

        }
    }

    fun getByCategory(category: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val list = db.getByCategory(category)

            _selectedCategoryTodoList.postValue(list)
        }
    }

    fun setCompleted(todo: TodoEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            db.update(todo)
        }
    }


//    fun select(category: TodoCategory) {
//        // _selectedCategoryTodoList.postValue(category)
//    }


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