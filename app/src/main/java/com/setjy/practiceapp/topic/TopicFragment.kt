package com.setjy.practiceapp.topic

import android.os.Bundle
import android.text.Editable
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import by.kirich1409.viewbindingdelegate.viewBinding
import com.setjy.practiceapp.R
import com.setjy.practiceapp.channels.StreamListFragment
import com.setjy.practiceapp.data.Data
import com.setjy.practiceapp.databinding.FragmentTopicBinding
import com.setjy.practiceapp.recycler.Adapter
import com.setjy.practiceapp.recycler.TopicHolderFactory
import com.setjy.practiceapp.recycler.base.ViewTyped
import com.setjy.practiceapp.recycler.bottom_sheet_fragment.BottomSheetFragment
import com.setjy.practiceapp.recycler.items.MessageUI
import com.setjy.practiceapp.util.hideKeyboard
import com.setjy.practiceapp.util.plusAssign
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers
import io.reactivex.rxjava3.subjects.PublishSubject
import java.util.concurrent.TimeUnit

class TopicFragment : Fragment(R.layout.fragment_topic) {

    private val binding: FragmentTopicBinding by viewBinding()

    private val holderFactory: TopicHolderFactory = TopicHolderFactory(
        this::onEmojiClick, this::onAddEmojiClick
    )
    private val adapter: Adapter<ViewTyped> = Adapter(holderFactory)

    private val searchSubject: PublishSubject<SearchAction> = PublishSubject.create()

    private var disposable: CompositeDisposable = CompositeDisposable()

    private val topicName: String by lazy {
        arguments?.getStringArray(StreamListFragment.STREAM_BUNDLE_KEY)
            ?.get(StreamListFragment.TOPIC_ARRAY_INDEX).orEmpty()
    }
    private val streamName: String by lazy {
        arguments?.getStringArray(StreamListFragment.STREAM_BUNDLE_KEY)
            ?.get(StreamListFragment.STREAM_ARRAY_INDEX).orEmpty()
    }

    private var isLoading: Boolean = false

    private var isLastPage: Boolean = false

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(binding) {
            rvListOfMessages.adapter = adapter
            rvListOfMessages.layoutManager =
                LinearLayoutManager(context, RecyclerView.VERTICAL, true)
            tbStream.setNavigationIcon(R.drawable.ic_round_arrow_back_24)
        }
        setStreamAndTopicName()
        initClicks()
        initSearch()
        subscribeToSearchResults()
        setButtonVisibility()
        subscribeToEvents(streamName, topicName)
        getMessagesOnLaunch()
        initScrollListener()
    }

    private fun initScrollListener() {
        binding.rvListOfMessages.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {

                if (!isLoading && !isLastPage && dy != 0) {
                    val visibleItemsCount = recyclerView.childCount
                    val totalItemsCount = adapter.itemCount
                    val pastVisibleItem =
                        (recyclerView.layoutManager as LinearLayoutManager).findFirstCompletelyVisibleItemPosition()
                    if (visibleItemsCount + pastVisibleItem + PAGINATION_NUMBER > totalItemsCount) {
                        isLoading = true
                        loadOlderMessages((adapter.items[adapter.items.lastIndex] as MessageUI).messageId)
                    }
                }
                super.onScrolled(recyclerView, dx, dy)
            }
        })
    }

    private fun loadOlderMessages(anchor: Int) {
        disposable += Data.getMessages(streamName, topicName, anchor.toString())
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doAfterNext { isLoading = false }
            .subscribe({ nwMessages ->
                if (nwMessages.size < 20) {
                    isLastPage = true
                }
                adapter.items =
                    adapter.items + (nwMessages as List<MessageUI>).filterNot { it.messageId == anchor }
            }, {
                isLoading = false
                Log.d("xxx", "load older messages error " + it.stackTraceToString())
            })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        disposable.dispose()
    }

    private fun subscribeToEvents(streamName: String, topicName: String) {
        disposable += Data.getMessagesFromEventsQueue(streamName, topicName)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doAfterNext { binding.rvListOfMessages.smoothScrollToPosition(0) }
            .subscribe(
                { messages ->
                    adapter.items = messages
                    subscribeToEvents(streamName, topicName)
                },
                { e -> Log.d("xxx", "get error: $e") })
    }

    private fun setStreamAndTopicName() {
        binding.tbStream.title = streamName
        binding.tvTopicName.text = "Topic: $topicName"
    }

    private fun getMessagesOnLaunch() {
        disposable += Data.getMessagesOnLaunch(streamName, topicName)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe { showLoading() }
            .doFinally { hideLoading() }
            .subscribe({ messages ->
                adapter.items = messages
                if (messages.isNotEmpty()) {
                    hideLoading()
                    binding.rvListOfMessages.smoothScrollToPosition(0)
                }
            },
                { e -> Log.d("xxx", "launch mes:${e.printStackTrace()} ") })
    }


    private fun showLoading() {
        binding.shimmer.apply {
            isVisible = true
            showShimmer(true)
        }
    }

    private fun hideLoading() {
        binding.shimmer.apply {
            stopShimmer()
        }.isVisible = false
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
                            is MessageUI -> it.copy(isFound = false)
                            else -> it
                        }
                    }
                } else {
                    adapter.items = adapter.items.map { item ->
                        when (item) {
                            is MessageUI -> {
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
        disposable += searchSubject.debounce(
            1000,
            TimeUnit.MILLISECONDS,
            AndroidSchedulers.mainThread()
        ).subscribe { action ->
            when (action) {
                SearchAction.START -> {
                    searchIterator.apply {
                        resetIndex()
                        setItems(adapter.items)
                        totalFound()
                    }
                    if (searchIterator.isFoundItems.isNotEmpty()) { //todo fix (messageId)
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
                                is MessageUI -> it.copy(isFound = false)
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
            onDeleteDisappearAnimation(binding.ivSearchDelete)
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

    private fun onAddEmojiClick(messageId: Int) {
        BottomSheetFragment().show(parentFragmentManager, null)
        parentFragmentManager.setFragmentResultListener(
            BottomSheetFragment.REQUEST_KEY, viewLifecycleOwner
        ) { _, bundle ->
            val emojiName = bundle.getString(BottomSheetFragment.BUNDLE_KEY).orEmpty()
            addReaction(messageId, emojiName)
        }
    }

    private fun onEmojiClick(messageId: Int, emojiName: String, emojiCode: String) {
        adapter.items = adapter.items.map { item ->
            when {
                item is MessageUI && item.messageId == messageId -> {
                    val mutableReactions = item.reactions.toMutableList()
                    val removed =
                        mutableReactions.removeIf { it.code == emojiCode && it.isSelected }
                    if (removed) {
                        deleteReaction(messageId, emojiName)
                        item
                    } else {
                        addReaction(messageId, emojiName)
                        item
                    }
                }
                else -> item
            }
        }
    }

    private fun deleteReaction(messageId: Int, emojiName: String) {
        disposable += Data.deleteReaction(messageId, emojiName)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ s -> Log.d("emoji_send", s.toString()) },
                { e -> Log.d("emoji_send", e.toString()) })

    }

    private fun addReaction(messageId: Int, emojiName: String) {
        disposable += Data.addReaction(messageId, emojiName)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ s -> Log.d("emoji_send", s.toString()) },
                { e -> Log.d("emoji_send", e.toString()) })
    }

    private fun setMessageSender() {
        val messageText = binding.etSend.text.toString()
        sendMessage(messageText)
        binding.etSend.text.clear()
    }

    private fun sendMessage(messageText: String) {
        disposable += Data.sendMessage(streamName, topicName, messageText)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({}, {
                Toast.makeText(context, "Error sending message!", Toast.LENGTH_SHORT).show()
                Log.d("message_send", it.toString())
            })
    }

    companion object {
        private const val PAGINATION_NUMBER: Int = 4
    }
}
