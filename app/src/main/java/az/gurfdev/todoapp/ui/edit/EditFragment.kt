package az.gurfdev.todoapp.ui.edit

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import az.gurfdev.todoapp.R
import az.gurfdev.todoapp.databinding.FragmentEditBinding
import az.gurfdev.todoapp.entities.TodoCategory
import az.gurfdev.todoapp.entities.TodoEntity
import az.gurfdev.todoapp.tools.OnItemClickListener


class EditFragment : Fragment() {


    private var _binding: FragmentEditBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: EditTodoViewModel
    private var category = TodoCategory.PERSONAL

    private val itemClickListener = object : OnItemClickListener {
        override fun onItemClick(todoEntity: TodoEntity) {

        }

        override fun onNewTodoAdded(todoTitle: String) {
            val todo = TodoEntity(title = todoTitle, completed = false, category = category)
            viewModel.saveTodo(todo, category.toString())
        }

        override fun onCheckboxClick(todoEntity: TodoEntity, isChecked: Boolean) {
            todoEntity.completed = isChecked
            viewModel.setCompleted(todoEntity)
        }
    }

    private val adapter = TodoAdapter(itemClickListener)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = ViewModelProvider(this)[EditTodoViewModel::class.java]
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentEditBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        changeCategory()
        viewModel.getByCategory(category.toString())

        binding.backBtn.setOnClickListener {
            findNavController().popBackStack()
        }

        observeLiveData()

        binding.todoRecyclerView.adapter = adapter
        binding.todoRecyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private val categoryClickListener = View.OnClickListener { view ->
        val categ = binding.categoryLayout
        val categories = listOf(categ.personalBtn, categ.workBtn, categ.financeBtn, categ.otherBtn)
        categories.forEach { it.setBackgroundResource(R.drawable.bg_category_unselected) }
        view.setBackgroundResource(R.drawable.bg_category_selected)

        category = when (view) {
            categ.personalBtn -> TodoCategory.PERSONAL
            categ.workBtn -> TodoCategory.WORK
            categ.financeBtn -> TodoCategory.FINANCE
            categ.otherBtn -> TodoCategory.OTHER
            else -> TodoCategory.PERSONAL
        }

        viewModel.getByCategory(category.toString())

    }


    private fun changeCategory() {
        val categ = binding.categoryLayout
        categ.personalBtn.setOnClickListener(categoryClickListener)
        categ.workBtn.setOnClickListener(categoryClickListener)
        categ.financeBtn.setOnClickListener(categoryClickListener)
        categ.otherBtn.setOnClickListener(categoryClickListener)

    }

    private fun observeLiveData() {
        viewModel.selectedCategoryTodoList.observe(viewLifecycleOwner) { selectedCategoryTodoList ->
            selectedCategoryTodoList.let {
                adapter.submitList(it.toMutableList())
            }
        }
    }

}