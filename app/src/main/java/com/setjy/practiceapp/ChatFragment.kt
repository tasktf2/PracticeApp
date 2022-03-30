package com.setjy.practiceapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import by.kirich1409.viewbindingdelegate.viewBinding
import com.setjy.practiceapp.databinding.FragmentChatBinding
import com.setjy.practiceapp.recycler.Adapter
import com.setjy.practiceapp.recycler.ChatHolderFactory
import com.setjy.practiceapp.recycler.base.ViewTyped
import com.setjy.practiceapp.recycler.bottom_sheet_fragment.BottomSheetFragment
import com.setjy.practiceapp.recycler.items.EmojiUI
import com.setjy.practiceapp.recycler.items.IncomingMessageUI
import com.setjy.practiceapp.recycler.items.OutgoingMessageUI

class ChatFragment : Fragment() {

    private val binding: FragmentChatBinding by viewBinding()

    private val holderFactory: ChatHolderFactory = ChatHolderFactory(
        this::onEmojiClick,
        this::onAddEmojiClick
    )
    private val adapter: Adapter<ViewTyped> = Adapter(holderFactory)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_chat, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.rvListOfMessages.adapter = adapter
        binding.rvListOfMessages.layoutManager =
            LinearLayoutManager(context, RecyclerView.VERTICAL, true)
        adapter.items = Data.getMessages()
        adapter.setMessageSender()
        setButtonVisibility()
    }

    private fun onAddEmojiClick(messageId: String) {
        BottomSheetFragment().show(parentFragmentManager, null)
        parentFragmentManager.setFragmentResultListener(
            BottomSheetFragment.REQUEST_KEY,
            viewLifecycleOwner
        ) { _, bundle ->
            val emojiCode = bundle.getString(BottomSheetFragment.BUNDLE_KEY).orEmpty()
            addReactionToMessage(messageId, emojiCode)
        }
    }

    private fun onEmojiClick(messageId: String, emojiCode: String) {
        adapter.items = adapter.items.map { item ->
            when {
                item is IncomingMessageUI && item.messageId == messageId -> {
                    val mutableReactions = item.reactions.toMutableList()
                    val removed =
                        mutableReactions.removeIf { it.code == emojiCode && it.isSelected }
                    if (removed) {
                        item.copy(reactions = mutableReactions)
                    } else {
                        item.copy(
                            reactions = mutableReactions + listOf(
                                EmojiUI(
                                    code = emojiCode,
                                    isSelected = true
                                )
                            )
                        )
                    }
                }
                item is OutgoingMessageUI -> item
                else -> item
            }
        }
    }

    private fun addReactionToMessage(messageId: String, emojiCode: String) {
        adapter.items = adapter.items.map { item ->
            when {
                item is IncomingMessageUI && item.messageId == messageId -> {
                    if (item.reactions.all { !(it.code == emojiCode && it.isSelected) }) {
                        item.copy(
                            reactions = item.reactions + listOf(
                                EmojiUI(
                                    code = emojiCode,
                                    isSelected = true
                                )
                            )
                        )
                    } else {
                        item.copy(
                            reactions = item.reactions.filterNot { it.code == emojiCode && it.isSelected }
                        )
                    }
                }
                item is OutgoingMessageUI -> item
                else -> item
            }
        }
    }

    private fun Adapter<ViewTyped>.setMessageSender() {
        binding.ivSend.setOnClickListener {
            val message = binding.etSend.text.toString()
            items = listOf(OutgoingMessageUI(text = message)) + items
            binding.etSend.text.clear()
        }
    }

    private fun setButtonVisibility() {
        with(binding) {
            etSend.addTextChangedListener { word ->
                if (word.isNullOrBlank()) {
                    ivAdd.visibility = View.VISIBLE
                    ivSend.visibility = View.INVISIBLE
                } else {
                    ivAdd.visibility = View.INVISIBLE
                    ivSend.visibility = View.VISIBLE
                }
            }
        }
    }
}
