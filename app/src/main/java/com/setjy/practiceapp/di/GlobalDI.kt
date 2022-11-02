package com.setjy.practiceapp.di

import com.setjy.practiceapp.ZulipApp
import com.setjy.practiceapp.data.local.storage.MessageStorage
import com.setjy.practiceapp.data.local.storage.ReactionStorage
import com.setjy.practiceapp.data.local.storage.StreamStorage
import com.setjy.practiceapp.data.local.storage.TopicStorage
import com.setjy.practiceapp.data.model.MessageWithReactionsEntityMapper
import com.setjy.practiceapp.data.model.StreamWithTopicsEntityMapper
import com.setjy.practiceapp.data.model.TopicEntityMapper
import com.setjy.practiceapp.data.remote.NetworkService
import com.setjy.practiceapp.data.remote.api.*
import com.setjy.practiceapp.data.remote.response.MessagesRemoteMapper
import com.setjy.practiceapp.data.repo.*
import com.setjy.practiceapp.domain.model.MessageMapper
import com.setjy.practiceapp.domain.model.ReactionMapper
import com.setjy.practiceapp.domain.model.StreamMapper
import com.setjy.practiceapp.domain.model.UserMapper
import com.setjy.practiceapp.domain.usecase.event.GetEventsUseCase
import com.setjy.practiceapp.domain.usecase.message.GetMessagesOnLaunchUseCase
import com.setjy.practiceapp.domain.usecase.message.GetMessagesOnScrollUseCase
import com.setjy.practiceapp.domain.usecase.message.SendMessageUseCase
import com.setjy.practiceapp.domain.usecase.reaction.AddReactionUseCase
import com.setjy.practiceapp.domain.usecase.reaction.DeleteReactionUseCase
import com.setjy.practiceapp.domain.usecase.stream.GetStreamsUseCase
import com.setjy.practiceapp.domain.usecase.user.GetAllUsersUseCase
import com.setjy.practiceapp.domain.usecase.user.GetOwnUserUseCase
import com.setjy.practiceapp.presentation.view.profile.LoadUserMiddleware
import com.setjy.practiceapp.presentation.view.profile.LoadUserReducer
import com.setjy.practiceapp.presentation.view.profile.LoadUserStore
import com.setjy.practiceapp.presentation.view.profile.State
import retrofit2.Retrofit

class GlobalDI {
    val context = ZulipApp.appContext

    private val retrofit: Retrofit by lazy { NetworkService.retrofit }

    private val streamsApi by lazy { retrofit.create(StreamsApi::class.java) }
    private val topicsApi by lazy { retrofit.create(TopicsApi::class.java) }
    private val usersApi by lazy { retrofit.create(UsersApi::class.java) }
    private val messageApi by lazy { retrofit.create(MessageApi::class.java) }
    private val eventsApi by lazy { retrofit.create(EventsApi::class.java) }
    private val reactionsApi by lazy { retrofit.create(ReactionsApi::class.java) }

    private val topicStorage by lazy {
        TopicStorage.initialize(context)
        TopicStorage.get()
    }
    private val streamStorage by lazy {
        StreamStorage.initialize(context)
        StreamStorage.get()
    }
    private val messageStorage by lazy {
        MessageStorage.initialize(context)
        MessageStorage.get()
    }
    private val reactionStorage by lazy {
        ReactionStorage.initialize(context)
        ReactionStorage.get()
    }


    private val streamMapper by lazy { StreamMapper() }
    private val topicEntityMapper by lazy { TopicEntityMapper() }
    private val userMapper by lazy { UserMapper() }
    private val reactionMapper by lazy { ReactionMapper() }
    private val messageMapper by lazy { MessageMapper(reactionMapper) }
    private val messageWithReactionsEntityMapper by lazy { MessageWithReactionsEntityMapper() }
    private val messagesRemoteMapper by lazy { MessagesRemoteMapper() }
    private val streamWithTopicsEntityMapper by lazy {
        StreamWithTopicsEntityMapper(topicEntityMapper)
    }

    private val topicRepoImpl by lazy { TopicRepoImpl(api = topicsApi) }
    private val streamRepoImpl by lazy {
        StreamRepoImpl(
            api = streamsApi,
            streamStorage = streamStorage,
            topicStorage = topicStorage,
            topicRepoImpl = topicRepoImpl,
            mapper = streamWithTopicsEntityMapper
        )
    }
    private val userRepoImpl by lazy { UserRepoImpl(api = usersApi) }
    private val messageRepoImpl by lazy {
        MessageRepoImpl(
            api = messageApi,
            messageStorage = messageStorage,
            reactionStorage = reactionStorage,
            entityMapper = messageWithReactionsEntityMapper,
            remoteMapper = messagesRemoteMapper
        )
    }
    private val eventRepoImpl by lazy {
        EventRepoImpl(
            api = eventsApi,
            messageRepo = messageRepoImpl,
            messageStorage = messageStorage,
            reactionStorage = reactionStorage,
            messageEntityMapper = messageWithReactionsEntityMapper,
            messageRemoteMapper = messagesRemoteMapper
        )
    }
    private val reactionRepoImpl by lazy { ReactionRepoImpl(api = reactionsApi) }
    val getStreamsUseCase by lazy {
        GetStreamsUseCase(
            repo = streamRepoImpl,
            mapper = streamMapper
        )
    }
    val getAllUsersUseCase by lazy { GetAllUsersUseCase(repo = userRepoImpl, mapper = userMapper) }
    val getMessagesOnScrollUseCase by lazy {
        GetMessagesOnScrollUseCase(
            repo = messageRepoImpl,
            mapper = messageMapper
        )
    }

    val getEventsUseCase by lazy { GetEventsUseCase(repo = eventRepoImpl, mapper = messageMapper) }
    val getMessagesOnLaunchUseCase by lazy {
        GetMessagesOnLaunchUseCase(repo = messageRepoImpl, mapper = messageMapper)
    }
    val addReactionUseCase by lazy { AddReactionUseCase(repo = reactionRepoImpl) }
    val deleteReactionUseCase by lazy { DeleteReactionUseCase(repo = reactionRepoImpl) }
    val sendMessageUseCase by lazy { SendMessageUseCase(repo = messageRepoImpl) }
    val getOwnUserUseCase by lazy { GetOwnUserUseCase(repo = userRepoImpl, mapper = userMapper) }

    private val profileReducer by lazy { LoadUserReducer() }
    private val profileMiddleware by lazy { LoadUserMiddleware(getOwnUserUseCase) }
    private val profileInitialState by lazy { State() }
    val store: LoadUserStore by lazy {
        LoadUserStore(
            reducer = profileReducer,
            middlewares = listOf(profileMiddleware),
            initialState = profileInitialState
        )
    }
}