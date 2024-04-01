package az.gurfdev.todoapp.ui.main


import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import az.gurfdev.todoapp.R
import az.gurfdev.todoapp.databinding.FragmentMainBinding
import az.gurfdev.todoapp.entities.TodoEntity
import az.gurfdev.todoapp.tools.OnItemClickListener
import az.gurfdev.todoapp.tools.hideKeyboard
import az.gurfdev.todoapp.tools.showKeyboard
import az.gurfdev.todoapp.ui.main.adapter.MainTodoAdapter


class MainFragment : Fragment() {
    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding!!

    private val itemClickListener = object : OnItemClickListener {
        override fun onItemClick(todoEntity: TodoEntity) {}
        override fun onNewTodoAdded(todoTitle: String) {}
        override fun onCheckboxClick(todoEntity: TodoEntity, isChecked: Boolean) {
            todoEntity.completed = isChecked
            viewModel.setCompleted(todoEntity)
        }
    }

    private val adapter = MainTodoAdapter(itemClickListener)
    private lateinit var viewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = ViewModelProvider(this)[MainViewModel::class.java]
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentMainBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeLiveData()

        binding.mainRecyclerview.adapter = adapter
        binding.mainRecyclerview.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)

        val toolbarSearchView = binding.searchViewToolbar
        val toolbarSearchBtn = binding.toolbarSearchBtn
        val toolbarCancelButton = binding.cancelToolbar
        val toolbarLogo = binding.toolbarLogo
        val searchEditText = binding.editTextSearch


        searchEditText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {}
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val query = s.toString().trim()
                if (query.isEmpty()) {
                    adapter.filter("")
                    viewModel.getAllData()
                } else {
                    adapter.filter(query)
                }
            }
        })


        toolbarSearchBtn.setOnClickListener {
            searchEditText.showKeyboard()
            toolbarSearchView.visibility = View.VISIBLE
            toolbarLogo.visibility = View.GONE
            toolbarSearchBtn.visibility = View.GONE
        }

        binding.fabMain.setOnClickListener {
            findNavController().navigate(R.id.editFragment)
        }

        toolbarCancelButton.setOnClickListener {
            searchEditText.text?.clear()
            it.hideKeyboard()
            adapter.filter("")
            viewModel.getAllData()
            toolbarSearchView.visibility = View.GONE
            toolbarLogo.visibility = View.VISIBLE
            toolbarSearchBtn.visibility = View.VISIBLE
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


    private fun observeLiveData() {
        viewModel.todoList.observe(viewLifecycleOwner) { todoList ->
            todoList.let {
                adapter.submitList(it.toMutableList())
            }
        }
    }
}