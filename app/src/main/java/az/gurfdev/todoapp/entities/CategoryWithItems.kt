package az.gurfdev.todoapp.entities


data class CategoryWithItems(val category: TodoCategory, val items: List<TodoEntity>)