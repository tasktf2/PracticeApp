package com.setjy.practiceapp.presentation.ui.channels

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter

class PagerAdapter(fragmentManager: FragmentManager, lifecycle: Lifecycle) :
    FragmentStateAdapter(fragmentManager, lifecycle) {
    private val fragments: List<Fragment> = listOf(
//        StreamListFragment.newInstance(Page.SUBSCRIBED),
//        StreamListFragment.newInstance(Page.ALL_STREAMS)
    )

    override fun getItemCount(): Int = fragments.size

    override fun createFragment(position: Int): Fragment = fragments[position]
}

enum class Page {
    SUBSCRIBED,
    ALL_STREAMS
}