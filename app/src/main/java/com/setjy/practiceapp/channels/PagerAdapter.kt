package com.setjy.practiceapp.channels

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter

class PagerAdapter(fragmentManager: FragmentManager, lifecycle: Lifecycle) :
    FragmentStateAdapter(fragmentManager, lifecycle) {
    private val fragments: MutableList<Fragment> = mutableListOf(
        StreamListFragment().newInstance(Page.SUBSCRIBED),
        StreamListFragment().newInstance(Page.ALL_STREAMS)
    )

    override fun getItemCount(): Int = fragments.size

    override fun createFragment(position: Int): Fragment = fragments[position]

    fun update(newFragments: List<Fragment>) {
        this.fragments.clear()
        this.fragments.addAll(newFragments)
        notifyDataSetChanged()
    }
}

enum class Page(val subscribed: Boolean) {
    SUBSCRIBED(true),
    ALL_STREAMS(false)
}