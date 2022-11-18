package com.setjy.practiceapp.presentation.ui.topic

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.view.View
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import by.kirich1409.viewbindingdelegate.viewBinding
import com.setjy.practiceapp.R
import com.setjy.practiceapp.ZulipApp
import com.setjy.practiceapp.databinding.FragmentTopicBinding
import com.setjy.practiceapp.presentation.base.mvi.MviView
import com.setjy.practiceapp.presentation.base.mvi.MviViewModel
import com.setjy.practiceapp.presentation.base.mvi.MviViewModelFactory
import com.setjy.practiceapp.presentation.base.recycler.Adapter
import com.setjy.practiceapp.presentation.base.recycler.base.ViewTyped
import com.setjy.practiceapp.presentation.model.MessageUI
import com.setjy.practiceapp.presentation.ui.channels.StreamListFragment
import com.setjy.practiceapp.presentation.ui.topic.bottom_sheet_fragment.BottomSheetFragment
import com.setjy.practiceapp.util.hideKeyboard
import com.setjy.practiceapp.util.plusAssign
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.subjects.PublishSubject
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class TopicFragment : Fragment(R.layout.fragment_topic), MviView<TopicState, TopicEffect> {

    @Inject
    lateinit var mviViewModelFactory: MviViewModelFactory<TopicAction, TopicState, TopicEffect>

    private val viewModel: MviViewModel<TopicAction, TopicState, TopicEffect> by viewModels {
        mviViewModelFactory
    }

    private val binding: FragmentTopicBinding by viewBinding()

    private val holderFactory: TopicHolderFactory = TopicHolderFactory(
        this::onEmojiClick, this::onAddEmojiClick
    )
    private val adapter: Adapter<ViewTyped> = Adapter(holderFactory)

    private val searchSubject: PublishSubject<SearchAction> = PublishSubject.create()

    private var disposable: CompositeDisposable = CompositeDisposable()

    private val bottomSheetFragment by lazy { BottomSheetFragment() }

    private val topicName: String by lazy {
        arguments?.getStringArray(StreamListFragment.STREAM_BUNDLE_KEY)
            ?.get(StreamListFragment.TOPIC_ARRAY_INDEX).orEmpty()
    }
    private val streamName: String by lazy {
        arguments?.getStringArray(StreamListFragment.STREAM_BUNDLE_KEY)
            ?.get(StreamListFragment.STREAM_ARRAY_INDEX).orEmpty()
    }

    override fun onAttach(context: Context) {
        (context.applicationContext as ZulipApp).apply {
            addTopicComponent()
            topicComponent?.inject(this@TopicFragment)
        }
        super.onAttach(context)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(binding) {
            rvListOfMessages.adapter = adapter
            rvListOfMessages.layoutManager =
                LinearLayoutManager(context, RecyclerView.VERTICAL, true)
            tbStream.setNavigationIcon(R.drawable.ic_round_arrow_back_24)
        }

        viewModel.bind(this)
        viewModel.accept(TopicAction.GetNewestMessages(streamName, topicName))
        viewModel.accept(TopicAction.RegisterEventsQueue)
        setStreamAndTopicName()
        initClicks()
        initSearch()
        subscribeToSearchResults()
        setButtonVisibility()
        initScrollListener()
    }

    override fun renderState(state: TopicState) {

        binding.shimmer.isVisible = state.isLoading

        if (state.messages != null) {
            adapter.items = state.messages
        }
    }

    override fun renderEffect(effect: TopicEffect) = when (effect) {
        is TopicEffect.GetEvents -> viewModel.accept(
            TopicAction.GetEvents(
                streamName = streamName,
                topicName = topicName,
                queueId = effect.queueId,
                lastEventId = effect.lastEventId
            )
        )
        is TopicEffect.ShowBottomSheetFragment -> {
            bottomSheetFragment.show(parentFragmentManager, null)
            parentFragmentManager.setFragmentResultListener(
                BottomSheetFragment.REQUEST_KEY, viewLifecycleOwner
            ) { _, bundle ->
                val emojiName = bundle.getString(BottomSheetFragment.BUNDLE_KEY).orEmpty()
                viewModel.accept(TopicAction.AddReaction(effect.messageId, emojiName))
            }
        }
    }

    private fun initScrollListener() {
        binding.rvListOfMessages.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {

                if (!viewModel.currentState.isPaginationLoading && !viewModel.currentState.isPaginationLastPage && dy != 0) {
                    val visibleItemsCount = recyclerView.childCount
                    val totalItemsCount = adapter.itemCount
                    val pastVisibleItem =
                        (recyclerView.layoutManager as LinearLayoutManager).findFirstCompletelyVisibleItemPosition()
                    if (visibleItemsCount + pastVisibleItem + PAGINATION_NUMBER > totalItemsCount) {
                        viewModel.accept(
                            TopicAction.StartPagination(
                                streamName,
                                topicName,
                                (adapter.items[adapter.items.lastIndex] as MessageUI).messageId
                            )
                        )
                    }
                }
                super.onScrolled(recyclerView, dx, dy)
            }
        })
    }

    private fun setStreamAndTopicName() {
        binding.tbStream.title = streamName
        binding.tvTopicName.text = getString(R.string.header_topic, topicName)
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
                    if (searchIterator.isFoundItems.isNotEmpty()) {
                        binding.rvListOfMessages.smoothScrollToPosition(
                            adapter.items.indexOf(searchIterator.currentMessage())
                        )
                    }
                }
                SearchAction.NEXT -> {
                    if (searchIterator.hasNext()) {
                        binding.rvListOfMessages.smoothScrollToPosition(
                            adapter.items.indexOf(searchIterator.nextMessage())
                        )
                    }
                }
                SearchAction.PREV -> {
                    if (searchIterator.hasPrevious()) {
                        binding.rvListOfMessages.smoothScrollToPosition(
                            adapter.items.indexOf(searchIterator.previousMessage())
                        )
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
                etSearch.text.isBlank() -> getString(R.string.topic_type_to_search)
                searchIterator.isFoundItems.isNotEmpty() -> getString(R.string.topic_match_found) + searchIterator.isFoundItems.size
                else -> getString(R.string.topic_match_not_found)
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
        viewModel.accept(TopicAction.ShowBottomSheetFragment(messageId))
    }

    private fun onEmojiClick(messageId: Int, emojiName: String, emojiCode: String) {
        adapter.items = adapter.items.map { item ->
            when {
                item is MessageUI && item.messageId == messageId -> {
                    val mutableReactions = item.reactions.toMutableList()
                    val removed =
                        mutableReactions.removeIf { it.code == emojiCode && it.isSelected }
                    if (removed) {
                        viewModel.accept(TopicAction.DeleteReaction(messageId, emojiName))
                        item
                    } else {
                        viewModel.accept(TopicAction.AddReaction(messageId, emojiName))
                        item
                    }
                }
                else -> item
            }
        }
    }

    private fun setMessageSender() {
        val messageText = binding.etSend.text.toString()
        viewModel.accept(
            TopicAction.SendMessage(
                streamName = streamName,
                topicName = topicName,
                message = messageText
            )
        )
        binding.etSend.text.clear()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        disposable.dispose()
        viewModel.unbind()
    }

    companion object {
        private const val PAGINATION_NUMBER: Int = 4
    }
}