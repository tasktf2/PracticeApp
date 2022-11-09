package com.setjy.practiceapp.presentation.ui.channels

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import by.kirich1409.viewbindingdelegate.viewBinding
import com.google.android.material.tabs.TabLayoutMediator
import com.setjy.practiceapp.R
import com.setjy.practiceapp.databinding.FragmentChannelsBinding

class ChannelsFragment : Fragment(R.layout.fragment_channels) {

    private val binding: FragmentChannelsBinding by viewBinding()

    private val tabs: List<String> by lazy { listOf(getString(R.string.tab_subscribed), getString(R.string.tab_all_streams)) }

    private val tabLayoutMediator by lazy {
        TabLayoutMediator(binding.tlStreams, binding.vpStreams) { tab, position ->
            tab.text = tabs[position]
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val pagerAdapter = PagerAdapter(parentFragmentManager, lifecycle)
        binding.vpStreams.adapter = pagerAdapter
        tabLayoutMediator.attach()
        initQuerySender()
    }

    private fun initQuerySender() {
        binding.ivSearch.setOnClickListener {
            val query = binding.etSearch.text?.toString()?.trim().orEmpty()
            parentFragmentManager.setFragmentResult(
                QUERY_REQUEST_KEY,
                bundleOf(QUERY_BUNDLE_KEY to query)
            )
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        tabLayoutMediator.detach()
    }

    companion object {
        const val QUERY_BUNDLE_KEY: String = "QUERY_BUNDLE_KEY"
        const val QUERY_REQUEST_KEY: String = "QUERY_REQUEST_KEY"
    }
}