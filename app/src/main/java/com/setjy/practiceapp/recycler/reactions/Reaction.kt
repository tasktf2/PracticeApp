package com.setjy.practiceapp.recycler.reactions

class Reaction(
    val message_id: String,
    val mapOfReactions: MutableMap<EmojiNCS, MutableList<String>>
)
