package az.gurfdev.todoapp.ui.edit

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import az.gurfdev.todoapp.databinding.ListItemTodoEntryBinding
import az.gurfdev.todoapp.databinding.ListItemTodoNewBinding
import az.gurfdev.todoapp.entities.TodoEntity
import az.gurfdev.todoapp.tools.OnItemClickListener
import az.gurfdev.todoapp.tools.onDone
import az.gurfdev.todoapp.tools.setStrikeThrough

class TodoAdapter(private val listener: OnItemClickListener) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        private const val VIEW_TYPE_ITEM = 1
        private const val VIEW_TYPE_ADD = 2
    }

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

    inner class AddNewTodoHolder(private val binding: ListItemTodoNewBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind() {
            binding.editText.onDone {
                val newTodoTitle = binding.editText.text.toString()
                if (newTodoTitle.isNotEmpty()) {
                    listener.onNewTodoAdded(newTodoTitle)
                    binding.editText.text.clear()
                }
            }
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == VIEW_TYPE_ITEM) {
            val view =
                ListItemTodoEntryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            TodoViewHolder(view)
        } else {
            val view = ListItemTodoNewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            AddNewTodoHolder(view)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is TodoViewHolder) {
            val currentItem = mDiffer.currentList[holder.adapterPosition]
            holder.bind(currentItem)
        } else if (holder is AddNewTodoHolder) {
            holder.bind()
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (position == mDiffer.currentList.size) {
            VIEW_TYPE_ADD
        } else {
            VIEW_TYPE_ITEM
        }
    }

    override fun getItemCount(): Int {
        return mDiffer.currentList.size + 1
    }

    fun submitList(list: MutableList<TodoEntity>) {
        mDiffer.submitList(list)
    }


}