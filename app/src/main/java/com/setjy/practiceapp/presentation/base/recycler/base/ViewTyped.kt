package com.setjy.practiceapp.presentation.base.recycler.base

interface ViewTyped {

    val uid: Int
        get() = error("provide uid for viewType: $this")
}