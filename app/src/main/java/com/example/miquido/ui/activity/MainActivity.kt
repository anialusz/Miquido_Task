package com.example.miquido.ui.activity

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.os.Parcelable
import android.view.View
import android.widget.AbsListView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.content.res.AppCompatResources
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.miquido.R
import com.example.miquido.dagger.AppComponent
import com.example.miquido.databinding.ActivityMainBinding
import com.example.miquido.model.ToDoItem
import com.example.miquido.ui.adapter.ToDoListAdapter
import com.example.miquido.viewModel.ToDoListViewModel


class MainActivity : AppCompatActivity() {

    private lateinit var toDoListViewModel: ToDoListViewModel
    private lateinit var binding: ActivityMainBinding

    private var recyclerViewState: Parcelable? = null
    private var isScrolling = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val appComponent = AppComponent.Manager.init()
        toDoListViewModel =
            appComponent.viewModelFactory.create(ToDoListViewModel::class.java)

        var currentToDoList: List<ToDoItem>
        toDoListViewModel.savedToDoList.observe(this) {
            if (it.isNotEmpty()) {
                toDoListViewModel.deactivateOldSnapshotListener()
                toDoListViewModel.provideLifeUpdatesForAllData(it.size.toLong())
            }

            currentToDoList = it
            setListAdapter(currentToDoList)
            binding.rvToDoList.layoutManager?.onRestoreInstanceState(recyclerViewState)
        }

        binding.fabAdd.setOnClickListener {
            startActivity(Intent(this, EditToDoItemActivity::class.java))
        }

        binding.rvToDoList.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
                    isScrolling = true
                }
            }

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val linearLayoutManager = recyclerView.layoutManager as LinearLayoutManager
                val firstVisibleItemPosition: Int =
                    linearLayoutManager.findFirstVisibleItemPosition()
                val visibleItemCount: Int = linearLayoutManager.childCount
                val totalItemCount: Int = linearLayoutManager.itemCount
                if (isScrolling && (firstVisibleItemPosition + visibleItemCount == totalItemCount)) {
                    isScrolling = false
                    recyclerViewState = recyclerView.layoutManager?.onSaveInstanceState()
                    toDoListViewModel.addNextPageToToDoList()
                }
            }
        })
    }

    override fun onResume() {
        super.onResume()
        toDoListViewModel.initializeToDoList()
    }

    private fun setListAdapter(list: List<ToDoItem>) {
        val adapter = ToDoListAdapter(list, { itemOnClick(it) }, { itemOnLongClick(it) })
        binding.rvToDoList.adapter = adapter
        binding.tvEmpty.visibility = View.GONE

        if (list.isEmpty()) {
            binding.tvEmpty.visibility = View.VISIBLE
        }
    }

    private fun itemOnClick(currentToDoItem: ToDoItem) {
        val intent = Intent(this, EditToDoItemActivity()::class.java)
        intent.putExtra("title", currentToDoItem.title)
        intent.putExtra("description", currentToDoItem.description)
        intent.putExtra("icon", currentToDoItem.icon)
        this.startActivity(intent)
    }

    private fun itemOnLongClick(currentToDoItem: ToDoItem): Boolean {
        AlertDialog.Builder(this)
            .setTitle(getString(R.string.delete_entry))
            .setMessage(getString(R.string.delete_confirmation))
            .setPositiveButton(getString(R.string.yes)) { _, _ ->
                toDoListViewModel.removeToDoItem(currentToDoItem.id)
                toDoListViewModel.initializeToDoList()
            }
            .setNegativeButton(getString(R.string.no), null)
            .setIcon(AppCompatResources.getDrawable(this, R.drawable.ic_alert))
            .show()

        return true
    }
}