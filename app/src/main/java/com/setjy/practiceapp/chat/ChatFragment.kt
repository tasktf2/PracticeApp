package com.setjy.practiceapp.chat

import android.app.Activity
import android.os.Bundle
import android.text.Editable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import by.kirich1409.viewbindingdelegate.viewBinding
import com.setjy.practiceapp.Data
import com.setjy.practiceapp.R
import com.setjy.practiceapp.databinding.FragmentChatBinding
import com.setjy.practiceapp.recycler.Adapter
import com.setjy.practiceapp.recycler.ChatHolderFactory
import com.setjy.practiceapp.recycler.base.ViewTyped
import com.setjy.practiceapp.recycler.bottom_sheet_fragment.BottomSheetFragment
import com.setjy.practiceapp.recycler.items.EmojiUI
import com.setjy.practiceapp.recycler.items.IncomingMessageUI
import com.setjy.practiceapp.recycler.items.OutgoingMessageUI
import com.setjy.practiceapp.util.plusAssign
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers
import io.reactivex.rxjava3.subjects.PublishSubject
import java.util.concurrent.TimeUnit

class ChatFragment : Fragment() {

    private val binding: FragmentChatBinding by viewBinding()

    private val holderFactory: ChatHolderFactory = ChatHolderFactory(
        this::onEmojiClick,
        this::onAddEmojiClick
    )
    private val adapter: Adapter<ViewTyped> = Adapter(holderFactory)

    private val searchSubject: PublishSubject<SearchAction> = PublishSubject.create()
    private var disposable: CompositeDisposable = CompositeDisposable()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_chat, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(binding) {
            rvListOfMessages.adapter = adapter
            rvListOfMessages.layoutManager =
                LinearLayoutManager(context, RecyclerView.VERTICAL, true)
            tbStream.setNavigationIcon(R.drawable.ic_round_arrow_back_24)
        }
        getMessagesFromDatabase()
        setButtonVisibility()
        initClicks()
        initSearch()
        subscribeToSearchResults()
    }

    private fun initClicks() {
        with(binding) {
            ivSend.setOnClickListener { setMessageSender() }
            val searchMenuItem = tbStream.menu.findItem(R.id.search_button_chat)
            tbStream.setNavigationOnClickListener { //back pressing to hide search group
                searchSubject.onNext(SearchAction.CANCEL)
            }
            ivSearchDelete.setOnClickListener { //delete text in search field
                etSearch.text.clear()
                setDeleteAnimationOnClick(it)
            }
            ivSearchUp.setOnClickListener { searchSubject.onNext(SearchAction.NEXT) }
            ivSearchDown.setOnClickListener { searchSubject.onNext(SearchAction.PREV) }
            searchMenuItem.setOnMenuItemClickListener { //sets visibility of search group
                groupSearch.isVisible = true
                groupSend.isVisible = false
                true
            }
        }
    }

    private fun initSearch() {
        with(binding) {
            ivSearchUp.isVisible = false //todo refactor to group
            ivSearchDown.isVisible = false
            etSearch.addTextChangedListener { text ->
                val query = text?.toString()?.trim().orEmpty()
                adapter.items = adapter.items.map { item ->
                    when (item) {
                        is IncomingMessageUI -> {
                            if (item.message.contains(query, ignoreCase = true)) {
                                item.copy(isFound = true)
                            } else {
                                item
                            }
                        }
                        is OutgoingMessageUI -> {
                            if (item.message.contains(query, ignoreCase = true)) {
                                item.copy(isFound = true)
                            } else {
                                item
                            }
                        }
                        else -> item
                    }
                }
                searchSubject.onNext(SearchAction.START)
                setDeleteAnimation(text)
            }
        }
    }

    private fun subscribeToSearchResults() {
        val searchIterator = SearchMessagesIterator()
        disposable += searchSubject
            .debounce(500, TimeUnit.MILLISECONDS, AndroidSchedulers.mainThread())
            .subscribe { action ->
                when (action) {
                    SearchAction.START -> {
                        searchIterator.resetIndex()
                        searchIterator.setItems(adapter.items)
                        if (searchIterator.hasNext()) {
                            //todo set views to "HAS RESULTS"
                            val nextItemIndex = searchIterator.nextIndex()
                            if (nextItemIndex < adapter.items.size) {
                                binding.rvListOfMessages.smoothScrollToPosition(nextItemIndex)
                            }
                            if (searchIterator.hasNext()) {
                                //todo enable next button
                            } else {
                                //todo disable next\prev buttons
                            }
                        } else {
                            //todo set views to "NO RESULTS"
                        }
                    }
                    SearchAction.NEXT -> {
                        if (searchIterator.hasNext()) {
                            val nextItemIndex = searchIterator.nextIndex()
                            if (nextItemIndex < adapter.items.size) {
                                binding.rvListOfMessages.smoothScrollToPosition(nextItemIndex)
                            }
                        } else {
                            //todo disable next button
                        }
                    }
                    SearchAction.PREV -> {
                        //todo по аналогии
                    }
                    SearchAction.CANCEL -> {
                        searchIterator.resetIndex()
                        searchIterator.setItems(emptyList())
                        with(binding) {
                            groupSearch.isVisible = false
                            groupSend.isVisible = true
                            etSearch.text.clear()
                            hideKeyboard()
                            tvSearchResults.text = ""
                            adapter.items = adapter.items.map {
                                when (it) {
                                    is IncomingMessageUI -> it.copy(isFound = false)
                                    is OutgoingMessageUI -> it.copy(isFound = false)
                                    else -> it
                                }
                            }
                        }
                    }
                }
            }
    }

    private fun getMessagesFromDatabase() {
        Data.getMessages()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ result -> adapter.items = result },
                { error -> Log.d("xxx", "the error is: $error") })
    }

    private fun Fragment.hideKeyboard() {
        val imm = context?.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view?.windowToken, 0)
    }

    private fun setDeleteAnimation(text: Editable?) {
        if (!binding.ivSearchDelete.isVisible) {
            binding.ivSearchDelete.animate().apply {
                rotation(-90F)
                scaleX(1f)
                scaleY(1f)
            }
        }
        if (!text.isNullOrEmpty()) binding.ivSearchDelete.isVisible = true
        if (text.isNullOrEmpty()) binding.ivSearchDelete.callOnClick()
    }

    private fun setDeleteAnimationOnClick(view: View) {
        view.animate().apply {
            rotation(90F)
            scaleX(0.01f)
            scaleY(0.01f)
            duration = 300L
        }.withEndAction { view.isVisible = false }
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

    //todo refactor this
    private fun onEmojiClick(messageId: String, emojiCode: String) {
        adapter.items = adapter.items.map { item ->
            when {
                item is IncomingMessageUI && item.messageId == messageId -> {
                    val mutableReactions = item.reactions?.toMutableList()
                    val removed =
                        mutableReactions?.removeIf { it.code == emojiCode && it.isSelected }
                    if (removed!!) {
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
                item is OutgoingMessageUI && item.messageId == messageId -> {
                    val mutableReactions = item.reactions?.toMutableList()
                    val removed =
                        mutableReactions?.removeIf { it.code == emojiCode && it.isSelected }
                    if (removed!!) {
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
                else -> item
            }
        }
    }

    //todo refactor this
    private fun addReactionToMessage(messageId: String, emojiCode: String) {
        adapter.items = adapter.items.map { item ->
            when {
                item is IncomingMessageUI && item.messageId == messageId -> {
                    when {
                        item.reactions.isNullOrEmpty() -> {
                            item.copy(
                                reactions = listOf(
                                    EmojiUI(
                                        code = emojiCode,
                                        isSelected = true
                                    )
                                )
                            )
                        }
                        item.reactions.all { !(it.code == emojiCode && it.isSelected) } -> {
                            item.copy(
                                reactions = item.reactions + listOf(
                                    EmojiUI(
                                        code = emojiCode,
                                        isSelected = true
                                    )
                                )
                            )
                        }
                        else -> {
                            item.copy(
                                reactions = item.reactions.filterNot { it.code == emojiCode && it.isSelected }
                            )
                        }
                    }
                }
                item is OutgoingMessageUI && item.messageId == messageId -> {
                    when {
                        item.reactions.isNullOrEmpty() -> {
                            item.copy(
                                reactions = listOf(
                                    EmojiUI(
                                        code = emojiCode,
                                        isSelected = true
                                    )
                                )
                            )
                        }
                        item.reactions.all { !(it.code == emojiCode && it.isSelected) } -> {
                            item.copy(
                                reactions = item.reactions + listOf(
                                    EmojiUI(
                                        code = emojiCode,
                                        isSelected = true
                                    )
                                )
                            )
                        }
                        else -> {
                            item.copy(
                                reactions = item.reactions.filterNot { it.code == emojiCode && it.isSelected }
                            )
                        }
                    }
                }
                else -> item
            }
        }
    }

    private fun setMessageSender() {
        val messageText = binding.etSend.text.toString()
        val outgoingMessage = listOf(
            OutgoingMessageUI(
                message = messageText,
                messageId = adapter.itemCount.toString(),
                reactions = null
            )
        )
        Data.saveMessage(outgoingMessage)
        binding.etSend.text.clear()
        Data.getMessages()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ result ->
                adapter.setItemsWithCommitCallback(
                    result
                ) { binding.rvListOfMessages.smoothScrollToPosition(0) }
            },
                { error -> Log.d("xxx", "the error is: $error") })
    }

    private fun setSearchClicksDisabled() {
        with(binding) {
            ivSearchUp.apply {
                isClickable = false
                isEnabled = false
            }
            ivSearchDown.apply {
                isClickable = false
                isEnabled = false
            }
        }
    }

    private fun setButtonVisibility() {
        with(binding) {
            etSend.addTextChangedListener { word ->
                if (word.isNullOrBlank()) {
                    ivAdd.isVisible = true
                    ivSend.isVisible = false
                } else {
                    ivAdd.isVisible = false
                    ivSend.isVisible = true
                }
            }
        }
    }

}
