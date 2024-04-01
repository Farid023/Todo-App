package az.gurfdev.todoapp.ui.edit

import az.gurfdev.todoapp.entities.TodoEntity

sealed class UiState {

    data object Loading : UiState()
    data class Success(val data: List<TodoEntity>) : UiState()
    data class Error(val message: String) : UiState()

}