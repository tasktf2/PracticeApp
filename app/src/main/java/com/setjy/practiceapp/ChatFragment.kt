package com.setjy.practiceapp

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
import com.setjy.practiceapp.databinding.FragmentChatBinding
import com.setjy.practiceapp.recycler.Adapter
import com.setjy.practiceapp.recycler.ChatHolderFactory
import com.setjy.practiceapp.recycler.base.ViewTyped
import com.setjy.practiceapp.recycler.bottom_sheet_fragment.BottomSheetFragment
import com.setjy.practiceapp.recycler.items.EmojiUI
import com.setjy.practiceapp.recycler.items.IncomingMessageUI
import com.setjy.practiceapp.recycler.items.OutgoingMessageUI
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.disposables.Disposable
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

    private var disposable: Disposable? = null
    private var searchDisposable: Disposable? = null
    private val publish = PublishSubject.create<Int>() //move to Repo.kt
    private val resultsPublish: Observable<Int> = publish

    private var searchList = listOf<ViewTyped>()
    private var currentSearchList = listOf<ViewTyped>()

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
    }

    private fun initClicks() {
        with(binding) {
            ivSend.setOnClickListener { setMessageSender() }
            val searchMenuItem = tbStream.menu.findItem(R.id.search_button_chat)
            tbStream.setNavigationOnClickListener { //back pressing to hide search group
                if (groupSearch.isVisible) {
                    groupSearch.isVisible = false
                    groupSend.isVisible = true
                    binding.etSearch.text.clear()
                    hideKeyboard()
                    unsubscribeFromSearch()
                }
            }
            ivSearchDelete.setOnClickListener { //delete text in search field
                etSearch.text.clear()
                setDeleteAnimationOnClick(it)
            }
            ivSearchUp.setOnClickListener {
                subscribeToUp()
            }
            ivSearchDown.setOnClickListener {
                subscribeToDown()
            }

            searchMenuItem.setOnMenuItemClickListener { //sets visibility of search group
                groupSearch.isVisible = true
                groupSend.isVisible = false
                initSearchLogic()
                true
            }
        }
    }

    private fun setSearchClicksLogicIfResultsIsNotEmpty(size: Int) {
        if (size != 0) {
            setSearchClicksLogic()
        } else {
            setSearchClicksDisabled()
        }
    }

    private fun setSearchClicksLogic() {
        with(binding) {
            when (searchCounter) {
                currentSearchList.size - 1 -> {
                    ivSearchUp.apply {
                        isClickable = false
                        isEnabled = false
                    }
                    if (searchCounter != 0)
                        ivSearchDown.apply {
                            isClickable = true
                            isEnabled = true
                        }
                }
                -1, 0 -> {
                    ivSearchUp.apply {
                        isClickable = true
                        isEnabled = true
                    }
                    ivSearchDown.apply {
                        isClickable = false
                        isEnabled = false
                    }
                }
                else -> {
                    ivSearchUp.apply {
                        isClickable = true
                        isEnabled = true
                    }
                    ivSearchDown.apply {
                        isClickable = true
                        isEnabled = true
                    }
                }
            }
            val id = when (currentSearchList[searchCounter].viewType) {
                R.layout.item_msg_outgoing -> {
                    (currentSearchList[searchCounter] as OutgoingMessageUI).messageId
                }
                R.layout.item_msg_incoming -> {
                    (currentSearchList[searchCounter] as IncomingMessageUI).messageId
                }
                else -> "error"
            }
            rvListOfMessages.scrollToPosition(adapter.itemCount - id.toInt() - 1)
            /*
            todo callback after scroll and then recolor background (use findViewByPosition?)
            also make scroll to center of screen if possible?
            refactor id to Int?
             */
        }
    }

    private fun initSearchLogic() {
        with(binding) {
            ivSearchUp.isVisible = false //todo refactor to group
            ivSearchDown.isVisible = false
            etSearch.apply {
                searchList = adapter.items
                addTextChangedListener { text ->
                    setVisibilityOfSearchClicks(text)
                    searchDisposable = Observable.create<String> { emitter ->
                        emitter.onNext(text.toString().lowercase().trim())
                    }.debounce(500, TimeUnit.MILLISECONDS)
                        .map { stringText ->
                            searchList.filter {
                                when (it.viewType) {
                                    R.layout.item_msg_outgoing -> {
                                        (it as OutgoingMessageUI).message.lowercase()
                                            .contains(stringText)
                                    }
                                    R.layout.item_msg_incoming -> {
                                        (it as IncomingMessageUI).message.lowercase()
                                            .contains(stringText)
                                    }
                                    else -> return@filter false
                                }
                            }
                        }.subscribe(
                            { list ->
                                tvSearchResults.text = if (text.isNullOrEmpty()) ""
                                else "Найдено совпадений: ${list.size}"
                                currentSearchList = list
                                searchCounter = -1 //start of search
                                setSearchClicksLogicIfResultsIsNotEmpty(list.size)
                            },
                            { error -> Log.d("xxx", "error $error") }
                        )
                    setDeleteAnimation(text)
                }
            }
            disposable = resultsPublish.subscribe {
                setSearchClicksLogic()
            }
        }
    }

    private fun setVisibilityOfSearchClicks(text: Editable?) {
        with(binding) {
            if (text.isNullOrEmpty()) {
                ivSearchUp.isVisible = false
                ivSearchDown.isVisible = false
            } else {
                ivSearchUp.isVisible = true
                ivSearchDown.isVisible = true
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

    private fun dispatchPublish(n: Int) { //move to Repo.kt
        publish.onNext(n)
    }

    private fun subscribeToUp() {
        dispatchPublish(searchCounter++)
    }

    private fun subscribeToDown() {
        dispatchPublish(searchCounter--)
    }

    private fun unsubscribeFromSearch() {
        disposable?.dispose()
        searchDisposable?.dispose()
        binding.tvSearchResults.text = ""
    }

    companion object {
        private var searchCounter: Int = 0
    }
}
