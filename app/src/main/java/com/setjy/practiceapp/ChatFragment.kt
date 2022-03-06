package com.setjy.practiceapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import by.kirich1409.viewbindingdelegate.viewBinding
import com.setjy.practiceapp.databinding.FragmentChatBinding
import com.setjy.practiceapp.recycler.Adapter
import com.setjy.practiceapp.recycler.ChatHolderFactory
import com.setjy.practiceapp.recycler.base.ViewTyped
import com.setjy.practiceapp.recycler.holders.IncomingMessageUI
import com.setjy.practiceapp.recycler.holders.OutgoingMessageUI
import com.setjy.practiceapp.view.CustomViewGroup

class ChatFragment : Fragment() {

    private val binding: FragmentChatBinding by viewBinding()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_chat, container, false)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val longClickListener =
            { _: View -> Toast.makeText(context, "Imitation", Toast.LENGTH_SHORT).show() }
        val holderFactory = ChatHolderFactory(longClickListener)
        val adapter = Adapter<ViewTyped>(holderFactory)
        binding.rvListOfMessages.adapter = adapter
        binding.rvListOfMessages.layoutManager =
            LinearLayoutManager(context, RecyclerView.VERTICAL, true)
        adapter.items = listOf(
            OutgoingMessageUI(getString(R.string.test_message_text)),
            IncomingMessageUI(R.drawable.ic_launcher_background, "Denis Mashkov", getString(R.string.test_message_text))
        )
    }
}