package com.setjy.practiceapp.hw_4.channels

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import by.kirich1409.viewbindingdelegate.viewBinding
import com.google.android.material.tabs.TabLayoutMediator
import com.setjy.practiceapp.R
import com.setjy.practiceapp.databinding.FragmentChannelsBinding

class ChannelsFragment : Fragment() {

    private val binding: FragmentChannelsBinding by viewBinding()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_channels, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val tabs: List<String> = listOf("Subscribed", "All streams")
        val pagerAdapter = PagerAdapter(parentFragmentManager, lifecycle)
        binding.vpStreams.adapter = pagerAdapter
//        pagerAdapter.update(listOf(StreamListFragment(),StreamListFragment()))

        TabLayoutMediator(binding.tlStreams, binding.vpStreams) { tab, position ->
            tab.text = tabs[position]
        }.attach()
    }
}