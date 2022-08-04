package com.setjy.practiceapp.topic

import android.os.Bundle
import android.text.Editable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import by.kirich1409.viewbindingdelegate.viewBinding
import com.setjy.practiceapp.Data
import com.setjy.practiceapp.R
import com.setjy.practiceapp.channels.StreamListFragment
import com.setjy.practiceapp.databinding.FragmentTopicBinding
import com.setjy.practiceapp.recycler.Adapter
import com.setjy.practiceapp.recycler.TopicHolderFactory
import com.setjy.practiceapp.recycler.base.ViewTyped
import com.setjy.practiceapp.recycler.bottom_sheet_fragment.BottomSheetFragment
import com.setjy.practiceapp.recycler.items.EmojiUI
import com.setjy.practiceapp.recycler.items.IncomingMessageUI
import com.setjy.practiceapp.recycler.items.OutgoingMessageUI
import com.setjy.practiceapp.util.hideKeyboard
import com.setjy.practiceapp.util.plusAssign
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers
import io.reactivex.rxjava3.subjects.PublishSubject
import java.util.concurrent.TimeUnit

class TopicFragment : Fragment() {

    private val binding: FragmentTopicBinding by viewBinding()

    private val holderFactory: TopicHolderFactory = TopicHolderFactory(
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
        return inflater.inflate(R.layout.fragment_topic, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(binding) {
            rvListOfMessages.adapter = adapter
            rvListOfMessages.layoutManager =
                LinearLayoutManager(context, RecyclerView.VERTICAL, true)
            tbStream.setNavigationIcon(R.drawable.ic_round_arrow_back_24)
        }
        setStreamAndTopicName()
        getMessagesFromDatabase()
        initClicks()
        initSearch()
        subscribeToSearchResults()
        setButtonVisibility()
    }

    private fun setStreamAndTopicName() {
        val streamAndTopicNames = arguments?.getStringArray(StreamListFragment.STREAM_BUNDLE_KEY)
        if (streamAndTopicNames != null) {
            binding.tbStream.title = streamAndTopicNames[StreamListFragment.STREAM_ARRAY_INDEX]
            binding.tvTopicName.text =
                "Topic: ${streamAndTopicNames[StreamListFragment.TOPIC_ARRAY_INDEX]}"
        } else {
            Log.d("xxx", "args are null, throw exception later ")
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        disposable.dispose()
    }

    private fun getMessagesFromDatabase() {
        Data.getMessages()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ result -> adapter.items = result },
                { error -> Log.d("xxx", "the error is: $error") })
    }

    private fun initClicks() {
        with(binding) {
            ivSend.setOnClickListener { setMessageSender() }
            val searchMenuItem = tbStream.menu.findItem(R.id.search_button_topic)
            tbStream.setNavigationOnClickListener {
                //back pressing to hide search group
                if (groupSearch.isVisible) {
                    searchSubject.onNext(SearchAction.CANCEL)
                } else {
                    findNavController().navigate(R.id.action_topicFragment_to_channels_fragment)
//                    findNavController().navigateUp()
                }
            }
            ivSearchDelete.setOnClickListener { //delete text in search field
                etSearch.text.clear()
                onDeleteDisappearAnimation(it)
            }
            ivSearchNext.setOnClickListener { searchSubject.onNext(SearchAction.NEXT) }
            ivSearchPrev.setOnClickListener { searchSubject.onNext(SearchAction.PREV) }
            searchMenuItem.setOnMenuItemClickListener { //sets visibility of search group
                groupSearch.isVisible = true
                groupSend.isVisible = false
                toggleNextButton(false)
                togglePrevButton(false)
                true
            }
        }
    }

    private fun initSearch() {
        with(binding) {
            etSearch.addTextChangedListener { text ->
                val query = text?.toString()?.trim().orEmpty()
                if (query.isBlank()) {
                    adapter.items = adapter.items.map {
                        when (it) {
                            is IncomingMessageUI -> it.copy(isFound = false)
                            is OutgoingMessageUI -> it.copy(isFound = false)
                            else -> it
                        }
                    }
                } else {
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
                }
                searchSubject.onNext(SearchAction.START)
                deleteAppearingAnimation(text)
            }
        }
    }

    private fun subscribeToSearchResults() {
        val searchIterator = SearchMessagesIterator()
        disposable += searchSubject
            .debounce(1000, TimeUnit.MILLISECONDS, AndroidSchedulers.mainThread())
            .subscribe { action ->
                when (action) {
                    SearchAction.START -> {
                        searchIterator.apply {
                            resetIndex()
                            setItems(adapter.items)
                            totalFound()
                        }
                        if (searchIterator.isFoundItems.isNotEmpty()) {
                            val messageId =
                                searchIterator.getMessageId(searchIterator.currentIndex())
                            binding.rvListOfMessages.smoothScrollToPosition(adapter.items.lastIndex - messageId)
                        }
                    }
                    SearchAction.NEXT -> {
                        if (searchIterator.hasNext()) {
                            val messageId = searchIterator.getMessageId(searchIterator.nextIndex())
                            binding.rvListOfMessages.smoothScrollToPosition(adapter.items.lastIndex - messageId)
                        }
                    }
                    SearchAction.PREV -> {
                        if (searchIterator.hasPrevious()) {
                            val messageId =
                                searchIterator.getMessageId(searchIterator.previousIndex())
                            binding.rvListOfMessages.smoothScrollToPosition(adapter.items.lastIndex - messageId)
                        }
                    }
                    SearchAction.CANCEL -> {
                        searchIterator.apply {
                            resetIndex()
                            setItems(emptyList())
                        }
                        with(binding) {
                            groupSearch.isVisible = false
                            groupSend.isVisible = true
                            etSearch.text.clear()
                            tvSearchResults.text = ""
                            hideKeyboard()
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
                toggleSearchArrowsClickability(searchIterator)
                setSearchResultsText(searchIterator)
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

    private fun toggleSearchArrowsClickability(searchIterator: SearchMessagesIterator) {
        with(searchIterator) {
            if (isFoundItems.isEmpty()) {
                toggleNextButton(false)
                togglePrevButton(false)
            } else {
                toggleNextButton(hasNext())
                togglePrevButton(hasPrevious())
            }
        }
    }

    private fun setSearchResultsText(searchIterator: SearchMessagesIterator) {
        with(binding) {
            tvSearchResults.text = when {
                etSearch.text.isBlank() -> "Введите для поиска"
                searchIterator.isFoundItems.isNotEmpty() -> "Найдено совпадений: ${searchIterator.isFoundItems.size}"
                else -> "Результатов не найдено..."
            }
        }
    }

    private fun toggleNextButton(toggle: Boolean) {
        binding.ivSearchNext.apply {
            isEnabled = toggle
            isClickable = toggle
        }
    }

    private fun togglePrevButton(toggle: Boolean) {
        binding.ivSearchPrev.apply {
            isEnabled = toggle
            isClickable = toggle
        }
    }

    private fun deleteAppearingAnimation(text: Editable?) {
        if (!binding.ivSearchDelete.isVisible) {
            binding.ivSearchDelete.animate().apply {
                rotation(-90F)
                scaleX(1f)
                scaleY(1f)
            }
        }
        if (!text.isNullOrEmpty()) {
            binding.ivSearchDelete.isVisible = true
        } else {
            binding.ivSearchDelete.callOnClick()
        }
    }

    private fun onDeleteDisappearAnimation(view: View) {
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
                item is OutgoingMessageUI && item.messageId == messageId -> {
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
                reactions = listOf()
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
}
