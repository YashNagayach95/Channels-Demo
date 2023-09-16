package com.example.channelsdemo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.view.isVisible
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.channelsdemo.databinding.ActivityMainBinding
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var todoAdapter: TodoAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupRecyclerView()

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.CREATED) {
                binding.progressBar.isVisible = true
                val response = try {
                    RetrofitInstance.api.getTodos()
                } catch (io: IOException) {
                    io.printStackTrace()
                    binding.progressBar.isVisible = false
                    return@repeatOnLifecycle
                } catch (e: HttpException) {
                    e.message()
                    binding.progressBar.isVisible = false
                    return@repeatOnLifecycle
                }

                if (response.isSuccessful && response.body() != null) {
                    todoAdapter.todos = response.body()!!
                } else {
                    println(response.message())
                }
                binding.progressBar.isVisible = false
            }
        }
    }

    private fun setupRecyclerView() {
        binding.rvTodos.apply {
            todoAdapter = TodoAdapter()
            adapter = todoAdapter
            layoutManager = LinearLayoutManager(this@MainActivity)
        }
    }

}