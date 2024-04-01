package az.gurfdev.todoapp.ui.main.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import az.gurfdev.todoapp.databinding.ListItemTodoEntryBinding
import az.gurfdev.todoapp.entities.TodoEntity
import az.gurfdev.todoapp.tools.OnItemClickListener
import az.gurfdev.todoapp.tools.setStrikeThrough


class InnerTodoAdapter(private val listener: OnItemClickListener) :
    RecyclerView.Adapter<InnerTodoAdapter.TodoViewHolder>() {


    object DiffUtilCallback : DiffUtil.ItemCallback<TodoEntity>() {
        override fun areItemsTheSame(oldItem: TodoEntity, newItem: TodoEntity): Boolean {
            return oldItem.title == newItem.title
        }

        override fun areContentsTheSame(oldItem: TodoEntity, newItem: TodoEntity): Boolean {
            return oldItem == newItem
        }
    }

    private val mDiffer = AsyncListDiffer(this, DiffUtilCallback)

    inner class TodoViewHolder(private val binding: ListItemTodoEntryBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(todo: TodoEntity) {
            binding.title.text = todo.title
            binding.todoCheckIc.isChecked = todo.completed
            binding.title.setStrikeThrough(todo.completed)

            binding.todoCheckIc.setOnCheckedChangeListener { _, isChecked ->
                listener.onCheckboxClick(todo, isChecked)
                binding.title.setStrikeThrough(isChecked)
            }
        }

        init {
            binding.root.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    listener.onItemClick(mDiffer.currentList[position])
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TodoViewHolder {
        val view = ListItemTodoEntryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TodoViewHolder(view)
    }

    override fun onBindViewHolder(holder: TodoViewHolder, position: Int) {
        val currentItem = mDiffer.currentList[holder.adapterPosition]
        holder.bind(currentItem)
    }

    override fun getItemCount(): Int {
        return mDiffer.currentList.size
    }

    fun submitList(list: MutableList<TodoEntity>) {
        mDiffer.submitList(list)
    }

}