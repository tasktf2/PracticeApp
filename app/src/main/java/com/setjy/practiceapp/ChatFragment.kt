package com.setjy.practiceapp

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.children
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
import com.setjy.practiceapp.recycler.holders.IncomingMessageUI
import com.setjy.practiceapp.recycler.holders.OutgoingMessageUI
import com.setjy.practiceapp.recycler.holders.OutgoingMessageViewHolder.Companion.DEFAULT_USER_ID
import com.setjy.practiceapp.recycler.holders.TimeUI
import com.setjy.practiceapp.recycler.reactions.EmojiNCS
import com.setjy.practiceapp.recycler.reactions.Reaction
import com.setjy.practiceapp.recycler.reactions.emojiSetNCS
import com.setjy.practiceapp.util.getEmojiByUnicode
import com.setjy.practiceapp.view.CustomViewGroup
import com.setjy.practiceapp.view.EmojiView

class ChatFragment : Fragment() {

    private val binding: FragmentChatBinding by viewBinding()

    private val tempListOfMessages: MutableList<ViewTyped> = mutableListOf()
    private var messageId: String = ""
    private var emojiCodeFromBSF: String? = ""
    private val longClick: (View) -> Unit = { v: View ->
        BottomSheetFragment().show(
            parentFragmentManager,
            "bottom_sheet_fragment from longClick"
        )
        messageId = (v as CustomViewGroup).binding.messageId.text.toString()
        parentFragmentManager.setFragmentResultListener(
            BottomSheetFragment.REQUEST_KEY,
            viewLifecycleOwner
        ) { key, bundle ->
            emojiCodeFromBSF = bundle.getString(BottomSheetFragment.BUNDLE_KEY)
            addEmojiView(v, messageId, emojiCodeFromBSF)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_chat, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val holderFactory = ChatHolderFactory(longClick)
        val adapter = Adapter<ViewTyped>(holderFactory)
        binding.rvListOfMessages.adapter = adapter
        binding.rvListOfMessages.layoutManager =
            LinearLayoutManager(context, RecyclerView.VERTICAL, true)
        setButtonVisibility()
        loadMessages()
        adapter.items = tempListOfMessages
        adapter.setMessageSender()
    }

    private fun loadMessages() {
        tempListOfMessages.addAll(
            listOf(
                TimeUI(System.currentTimeMillis()),
                OutgoingMessageUI(text = getString(R.string.test_message_text)),
                IncomingMessageUI(
                    "1",
                    R.drawable.ic_launcher_background,
                    "Denis Mashkov",
                    getString(R.string.test_message_text)
                )

            ).asReversed()
        )
    }

    private fun Adapter<ViewTyped>.setMessageSender() {
        binding.ivSend.setOnClickListener {
            val message = binding.etSend.text.toString()
            tempListOfMessages.add(0, OutgoingMessageUI(text = message))
            items = tempListOfMessages
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

    private fun addEmojiView(
        incomeMessage: CustomViewGroup,
        messageId: String,
        emojiCodeFromBSF: String?
    ) {
        with(incomeMessage.binding) {
            val currentEmojiNCS =
                emojiSetNCS.find { it.code == emojiCodeFromBSF }!!
            if (flexbox.children.any {
                    (it as EmojiView).emojiUnicode == getEmojiByUnicode(
                        emojiCodeFromBSF!!
                    )
                }) {
                val emojiView: EmojiView =
                    flexbox.children.find {
                        (it as EmojiView).emojiUnicode == getEmojiByUnicode(
                            emojiCodeFromBSF!!
                        )
                    } as EmojiView
                toggleReactionInList(incomeMessage, messageId, currentEmojiNCS, emojiView)
            } else {
                flexbox.addView(EmojiView(requireContext()).apply {

                    addReactionInList(messageId, currentEmojiNCS)
                    emojiUnicode = currentEmojiNCS.getCodeString()
                    emojiCounter = getEmojiCounter(messageId, currentEmojiNCS)
                    flagIsSelected = true
                    Log.d(
                        "xxx",
                        "view:${emojiUnicode}, code:${getEmojiByUnicode(emojiCodeFromBSF!!)}"
                    )
                    setOnClickListener {
                        toggleReactionInList(incomeMessage, messageId, currentEmojiNCS, this)
                    }
                })

                togglePlusButton(incomeMessage)
            }
        }
    }

    private fun addReactionInList(messageId: String, currentEmojiNCS: EmojiNCS) {

        val currentReactionMap =
            listOfReactions.find { it.message_id == messageId }!!.mapOfReactions

        when {
            listOfReactions.isNullOrEmpty() -> {
                listOfReactions.add(
                    Reaction(
                        messageId, mutableMapOf(
                            currentEmojiNCS to mutableListOf(DEFAULT_USER_ID)
                        )
                    )
                )
            }
            !currentReactionMap.containsKey(currentEmojiNCS) -> {
                currentReactionMap[currentEmojiNCS] = mutableListOf(DEFAULT_USER_ID)
            }
            else -> {
                currentReactionMap[currentEmojiNCS]?.add(DEFAULT_USER_ID)
            }
        }

    }

    private fun getEmojiCounter(messageId: String, currentEmojiNCS: EmojiNCS): Int {

        return listOfReactions.find {
            it.message_id == messageId
        }?.mapOfReactions?.get(currentEmojiNCS)?.size!!
    }

    private fun toggleReactionInList(
        incomeMessage: CustomViewGroup,
        messageId: String,
        currentEmojiNCS: EmojiNCS,
        emojiView: EmojiView
    ) {
        val currentUsersReactionList = listOfReactions.find { it.message_id == messageId }
            ?.mapOfReactions?.get(currentEmojiNCS)
        if (emojiView.flagIsSelected && currentUsersReactionList
                ?.contains(DEFAULT_USER_ID) == true
        ) {

            currentUsersReactionList.removeIf { it == DEFAULT_USER_ID }
            emojiView.apply {
                flagIsSelected = false
                emojiCounter = currentUsersReactionList.size
                invalidate()
            }
            if (currentUsersReactionList.isEmpty()) {
                incomeMessage.binding.flexbox.apply {
                    removeView(emojiView)
                    togglePlusButton(incomeMessage)
                }
            }

        } else {
            addReactionInList(messageId, currentEmojiNCS)
            if (currentUsersReactionList != null) {
                emojiView.apply {
                    emojiCounter = currentUsersReactionList.size
                    emojiView.flagIsSelected = true
                    invalidate()
                }
            }

        }
    }

    private fun togglePlusButton(incomeMessage: CustomViewGroup) {
        var messageId: String
        var emojiCodeFromBSF: String?
        val click = { _: View ->
            BottomSheetFragment().show(
                parentFragmentManager,
                "bottom_sheet_fragment from plusButton"
            )
            messageId = incomeMessage.binding.messageId.text.toString()
            parentFragmentManager.setFragmentResultListener(
                BottomSheetFragment.REQUEST_KEY,
                viewLifecycleOwner
            ) { key, bundle ->
                emojiCodeFromBSF = bundle.getString(BottomSheetFragment.BUNDLE_KEY)
                Log.d("xxx", "$emojiCodeFromBSF")
                addEmojiView(incomeMessage, messageId, emojiCodeFromBSF)
            }
        }
        with(incomeMessage.binding.flexbox) {
            val plusButton = children.find { (it as EmojiView).emojiUnicode == "+" }
            when {
                !children.any { it == plusButton } -> {
                    addView(EmojiView(incomeMessage.context).apply {
                        emojiUnicode = "+"
                        setOnClickListener(click)
                    })
                }
                children.contains(plusButton) && children.elementAt(childCount - 1) != plusButton -> {
                    removeView(plusButton)
                    addView(EmojiView(incomeMessage.context).apply {
                        emojiUnicode = "+"
                        setOnClickListener(click)
                    })
                }
                children.all { (it as EmojiView).emojiUnicode == "+" } -> {
                    removeAllViews()
                }
            }
        }
    }

    companion object {
        val listOfReactions: MutableList<Reaction> = mutableListOf(
            Reaction(
                "1",
                mutableMapOf(EmojiNCS("grinning", "1f600") to mutableListOf("5", "1", "8", "13"))
            )
        )
    }
}
