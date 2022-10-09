package com.setjy.practiceapp.data.database

import androidx.room.*
import com.setjy.practiceapp.data.network.EmojiRemote
import com.setjy.practiceapp.recycler.items.MessageDB
import com.setjy.practiceapp.recycler.items.StreamItemUI
import com.setjy.practiceapp.recycler.items.TopicItemUI
import com.setjy.practiceapp.recycler.items.UserItemUI

@Dao
interface StreamDao {
    @Query("SELECT * FROM stream")
    fun getAllStreams(): List<StreamItemUI>

    @Query("SELECT * FROM stream WHERE isSubscribed = 1")
    fun getSubscribedStreams(): List<StreamItemUI>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertAllStreams(streams: List<StreamItemUI>)

    @Delete
    fun deleteAllStreams(streams: List<StreamItemUI>)
}

@Dao
interface TopicDao {
    @Query("SELECT * FROM topic WHERE parentId = :streamId")
    fun getTopicsByStreamId(streamId: Int): List<TopicItemUI>

    @Insert
    fun insertTopics(topics: List<TopicItemUI>)
}

@Dao
interface MessagesDao {
    @Query("SELECT * FROM MessageDB WHERE streamName=:streamName AND topicName=:topicName")
    fun getMessages(streamName: String, topicName: String): List<MessageDB>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertMessages(messages: List<MessageDB>)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertMessage(message: MessageDB)

    @Delete
    fun deleteMessages(messages: List<MessageDB>)
}

@Dao
interface ReactionsDao {

    @Query("SELECT * FROM reaction WHERE messageId=:messageId")
    fun getReactionsByMessageId(messageId: Int): List<EmojiRemote>

    @Insert
        (onConflict = OnConflictStrategy.IGNORE)
    fun insertReactions(reactions: List<EmojiRemote>)

    @Insert
        (onConflict = OnConflictStrategy.IGNORE)
    fun insertReaction(reaction: EmojiRemote)

    @Delete
    fun deleteReactions(reactions: List<EmojiRemote>)

    @Delete
    fun deleteReaction(reaction: EmojiRemote)
}

@Dao
interface UsersDao {
    @Query("SELECT * FROM user")
    fun getUser(): List<UserItemUI>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertUser(user: UserItemUI)
}