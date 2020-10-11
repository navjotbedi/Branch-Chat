package com.branch.chat.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.branch.chat.data.db.Message
import com.branch.chat.databinding.ListItemMessageBinding
import com.branch.chat.screens.chat.MessageFragmentDirections
import dagger.hilt.android.scopes.FragmentScoped

@FragmentScoped
class MessageAdapter(private val enableItemClick: Boolean = true) :
    ListAdapter<Message, MessageAdapter.MessageViewHolder>(MessageDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageViewHolder {
        return MessageViewHolder(
            ListItemMessageBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ), enableItemClick
        )
    }

    override fun onBindViewHolder(holder: MessageViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class MessageViewHolder(private val binding: ListItemMessageBinding, enableItemClick: Boolean) :
        RecyclerView.ViewHolder(binding.root) {
        init {
            if (enableItemClick) {
                binding.apply {
                    setClickListener { view ->
                        message?.let {
                            val action =
                                MessageFragmentDirections.actionMessageFragmentToConversationFragment(
                                    it.threadId
                                )
                            view.findNavController().navigate(action)
                        }
                    }
                }
            }
        }

        fun bind(item: Message) {
            binding.apply {
                message = item


                executePendingBindings()
            }
        }
    }
}

private class MessageDiffCallback : DiffUtil.ItemCallback<Message>() {

    override fun areItemsTheSame(oldItem: Message, newItem: Message): Boolean {
        return oldItem.messageId == newItem.messageId
    }

    override fun areContentsTheSame(oldItem: Message, newItem: Message): Boolean {
        return oldItem == newItem
    }
}