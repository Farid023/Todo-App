package az.gurfdev.todoapp.tools

import az.gurfdev.todoapp.entities.TodoEntity


interface OnItemClickListener {
    fun onItemClick(todoEntity: TodoEntity)
    fun onNewTodoAdded(todoTitle: String)
    fun onCheckboxClick(todoEntity: TodoEntity, isChecked: Boolean)
}