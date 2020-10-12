package com.branch.chat.screens.chat

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DividerItemDecoration
import com.branch.chat.R
import com.branch.chat.adapters.MessageAdapter
import com.branch.chat.data.repository.MessageRepository
import com.branch.chat.network.STATUS
import com.branch.chat.utils.Utils
import com.branch.chat.viewmodels.MessageViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_message.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@ExperimentalCoroutinesApi
@AndroidEntryPoint
class MessageFragment : Fragment(R.layout.fragment_message) {

    private val viewModel: MessageViewModel by viewModels()
    @Inject
    lateinit var messageRepository: MessageRepository
    @Inject
    lateinit var utils: Utils
    private lateinit var adapter: MessageAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        adapter = MessageAdapter()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        listView.addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
        listView.adapter = adapter

        subscribeUi(adapter)
    }

    private fun subscribeUi(adapter: MessageAdapter) {
        lifecycleScope.launch {
            messageRepository.getMessages().collect { adapter.submitList(it) }
        }

        viewModel.apply {
            loadingStatus.observe(viewLifecycleOwner) { status ->
                when (status) {
                    is STATUS.LOADING -> {
                        showLoading(true)
                    }
                    is STATUS.FAILED -> {
                        utils.showToast(status.message)
                        showLoading(false)
                    }
                    is STATUS.SUCCESS-> {
                        showLoading(false)
                    }
                }
            }

            swipeRefresh.setOnRefreshListener { fetchMessages() }
        }
    }

    private fun showLoading(showLoading: Boolean) {
        swipeRefresh.isRefreshing = showLoading
    }

}