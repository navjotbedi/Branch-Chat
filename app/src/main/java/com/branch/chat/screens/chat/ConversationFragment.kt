package com.branch.chat.screens.chat

import android.os.Bundle
import android.view.View
import android.view.inputmethod.EditorInfo
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.DividerItemDecoration
import com.branch.chat.R
import com.branch.chat.adapters.MessageAdapter
import com.branch.chat.data.repository.MessageRepository
import com.branch.chat.databinding.FragmentConversationBinding
import com.branch.chat.network.STATUS
import com.branch.chat.utils.Utils
import com.branch.chat.viewmodels.ConversationViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_conversation.*
import kotlinx.android.synthetic.main.view_progress_overlay.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@ExperimentalCoroutinesApi
@AndroidEntryPoint
class ConversationFragment : Fragment(R.layout.fragment_conversation) {

    @Inject
    lateinit var messageRepository: MessageRepository
    @Inject
    lateinit var utils: Utils
    private val viewModel: ConversationViewModel by viewModels()
    private val args: ConversationFragmentArgs by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        FragmentConversationBinding.bind(view).apply {
            conversationViewModel = viewModel
            listView.addItemDecoration(
                DividerItemDecoration(
                    context,
                    DividerItemDecoration.VERTICAL
                )
            )

            val adapter = MessageAdapter(false)
            listView.adapter = adapter
            subscribeUi(adapter)
        }
    }

    private fun subscribeUi(adapter: MessageAdapter) {
        lifecycleScope.launch {
            messageRepository.getConversation(args.threadId).collect {
                adapter.submitList(it)
            }
        }

        viewModel.apply {
            loadingStatus.observe(viewLifecycleOwner, { status ->
                when (status) {
                    is STATUS.LOADING -> {
                        showLoading(true)
                    }
                    is STATUS.FAILED -> {
                        utils.showToast(status.message)
                        showLoading(false)
                    }
                    is STATUS.SUCCESS -> {
                        showLoading(false)
                        messageEditText.text = null
                    }
                }
            })

            messageEditText.setOnEditorActionListener { _, actionId, _ ->
                if (actionId == EditorInfo.IME_ACTION_SEND) {
                    sendMessage(args.threadId)
                    true
                } else {
                    false
                }
            }
        }
    }

    private fun showLoading(show: Boolean) {
        progressOverlay.visibility = if (show) View.VISIBLE else View.GONE
    }

}