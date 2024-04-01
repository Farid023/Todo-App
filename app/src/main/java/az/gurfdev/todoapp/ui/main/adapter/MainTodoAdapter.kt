package az.gurfdev.todoapp.ui.main.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import az.gurfdev.todoapp.databinding.MainTodoCartBinding
import az.gurfdev.todoapp.entities.CategoryWithItems
import az.gurfdev.todoapp.entities.TodoCategory
import az.gurfdev.todoapp.entities.TodoEntity
import az.gurfdev.todoapp.tools.OnItemClickListener


class MainTodoAdapter(private val listener: OnItemClickListener) :
    RecyclerView.Adapter<MainTodoAdapter.CategoryViewHolder>() {

    object DiffUtilCallback : DiffUtil.ItemCallback<CategoryWithItems>() {
        override fun areItemsTheSame(oldItem: CategoryWithItems, newItem: CategoryWithItems): Boolean {
            return oldItem.category == newItem.category
        }

        override fun areContentsTheSame(oldItem: CategoryWithItems, newItem: CategoryWithItems): Boolean {
            return oldItem == newItem
        }
    }

    private val mDiffer = AsyncListDiffer(this, DiffUtilCallback)

    inner class CategoryViewHolder(private val binding: MainTodoCartBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(categoryWithItems: CategoryWithItems) {
            binding.mainCardCategory.text = categoryWithItems.category.toString()
            binding.mainCardTodoRv.setHasFixedSize(true)
            binding.mainCardTodoRv.layoutManager =
                LinearLayoutManager(binding.root.context, LinearLayoutManager.VERTICAL, false)
            val adapter = InnerTodoAdapter(listener)

            when (categoryWithItems.category) {
                TodoCategory.PERSONAL -> binding.mainTodoCart.setCardBackgroundColor(Color.parseColor("#88C978"))
                TodoCategory.WORK -> binding.mainTodoCart.setCardBackgroundColor(Color.parseColor("#FFEBC8"))
                TodoCategory.FINANCE -> binding.mainTodoCart.setCardBackgroundColor(Color.parseColor("#FFC8E7FF"))
                TodoCategory.OTHER -> binding.mainTodoCart.setCardBackgroundColor(Color.parseColor("#FFFDC8FF"))

            }
            adapter.submitList(categoryWithItems.items.toMutableList())
            binding.mainCardTodoRv.adapter = adapter
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        val view = MainTodoCartBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CategoryViewHolder(view)
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        val currentItem = mDiffer.currentList[holder.adapterPosition]
        holder.bind(currentItem)
    }

    override fun getItemCount(): Int {
        return mDiffer.currentList.size
    }

    fun submitList(list: MutableList<TodoEntity>) {
        val groupedItems = list.groupBy { it.category }.map { (category, items) ->
            CategoryWithItems(category, items)
        }
        mDiffer.submitList(groupedItems)
    }

    fun filter(query: String) {
        val filteredList = if (query.isEmpty()) {
            mDiffer.currentList
        } else {
            mDiffer.currentList.filter { categoryWithItems ->
                categoryWithItems.category.name.contains(query, true) ||
                        categoryWithItems.items.any { it.category.toString().contains(query, true) }
            }
        }
        mDiffer.submitList(filteredList)
    }

}