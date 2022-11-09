package com.setjy.practiceapp.di

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import androidx.room.Room
import com.setjy.practiceapp.ZulipApp
import com.setjy.practiceapp.data.local.db.ZulipDatabase
import com.setjy.practiceapp.data.local.db.dao.*
import com.setjy.practiceapp.data.local.model.MessageWithReactionsEntityMapper
import com.setjy.practiceapp.data.local.model.ReactionsEntityMapper
import com.setjy.practiceapp.data.local.model.StreamWithTopicsEntityMapper
import com.setjy.practiceapp.data.local.model.TopicEntityMapper
import com.setjy.practiceapp.data.local.storage.*
import com.setjy.practiceapp.data.remote.api.*
import com.setjy.practiceapp.data.remote.response.MessagesRemoteMapper
import com.setjy.practiceapp.data.repo.*
import com.setjy.practiceapp.domain.model.MessageMapper
import com.setjy.practiceapp.domain.model.ReactionMapper
import com.setjy.practiceapp.domain.model.StreamMapper
import com.setjy.practiceapp.domain.model.UserMapper
import com.setjy.practiceapp.domain.repo.*
import com.setjy.practiceapp.domain.usecase.event.GetEventsUseCase
import com.setjy.practiceapp.domain.usecase.message.GetMessagesOnScrollUseCase
import com.setjy.practiceapp.domain.usecase.message.GetNewestMessagesUseCase
import com.setjy.practiceapp.domain.usecase.message.SendMessageUseCase
import com.setjy.practiceapp.domain.usecase.reaction.AddReactionUseCase
import com.setjy.practiceapp.domain.usecase.reaction.DeleteReactionUseCase
import com.setjy.practiceapp.domain.usecase.stream.GetStreamsUseCase
import com.setjy.practiceapp.domain.usecase.user.GetAllUsersUseCase
import com.setjy.practiceapp.domain.usecase.user.GetOwnUserUseCase
import io.reactivex.rxjava3.core.Scheduler
import io.reactivex.rxjava3.schedulers.Schedulers
import okhttp3.Credentials
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

@SuppressLint("StaticFieldLeak")
object GlobalDI {

    private const val BASE_URL = "https://setjy.zulipchat.com/api/v1/"

    private const val API_KEY = "GDnSt0MrYpAIiOwt4ILxdznzNcVyTeSC"

    private const val API_KEY_TEST = "71BnlNL4vMzhZYc4Ur6bKXYJklRMY122"

    private const val username = "task.tf2@gmail.com"

    private const val usernameTest = "setjy.work@gmail.com"

    private const val CONNECT_TIMEOUT = 10L

    private const val READ_TIMEOUT = 10L

    private const val WRITE_TIMEOUT = 10L

    private const val DATABASE_NAME = "zulip.db"

    private const val KEY_SHARED_PREFS = "ZULIP_APP_SHARED_PREFS"

    val context = ZulipApp.appContext

    private val sharedPreferences: SharedPreferences by lazy {
        context.getSharedPreferences(KEY_SHARED_PREFS, Context.MODE_PRIVATE)
    }


    private val header by lazy { Credentials.basic(username, API_KEY) }
    //      test profile below
//    private val header by lazy { Credentials.basic(usernameTest, API_KEY_TEST) }

    private val interceptor: Interceptor by lazy {
        Interceptor { chain ->
            val request: Request = chain.request()
            val authenticatedRequest: Request = request.newBuilder()
                .header("Authorization", header)
                .build()
            chain.proceed(authenticatedRequest)
        }
    }

    private val httpLoggingInterceptor by lazy {
        HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
    }

    private val okHttpClient by lazy {
        OkHttpClient.Builder()
            .connectTimeout(CONNECT_TIMEOUT, TimeUnit.SECONDS)
            .readTimeout(READ_TIMEOUT, TimeUnit.SECONDS)
            .writeTimeout(WRITE_TIMEOUT, TimeUnit.SECONDS)
            .addInterceptor(interceptor)
            .addNetworkInterceptor(httpLoggingInterceptor)
            .build()
    }

    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .client(okHttpClient)
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
            .build()
    }

    private val database: ZulipDatabase by lazy {
        Room.databaseBuilder(
            context.applicationContext,
            ZulipDatabase::class.java,
            DATABASE_NAME
        ).build()
    }

    private val streamsApi: StreamsApi by lazy { retrofit.create(StreamsApi::class.java) }
    private val topicsApi: TopicsApi by lazy { retrofit.create(TopicsApi::class.java) }
    private val usersApi: UsersApi by lazy { retrofit.create(UsersApi::class.java) }
    private val messageApi: MessageApi by lazy { retrofit.create(MessageApi::class.java) }
    private val eventsApi: EventsApi by lazy { retrofit.create(EventsApi::class.java) }

    private val reactionsApi: ReactionsApi by lazy { retrofit.create(ReactionsApi::class.java) }
    private val topicDao: TopicDao by lazy { database.topicDao() }
    private val streamDao: StreamDao by lazy { database.streamDao() }
    private val messageDao: MessageDao by lazy { database.messageDao() }
    private val reactionDao: ReactionDao by lazy { database.reactionDao() }

    private val topicStorage: TopicStorage by lazy { TopicStorage(topicDao) }
    private val streamStorage: StreamStorage by lazy { StreamStorage(streamDao) }
    private val messageStorage: MessageStorage by lazy { MessageStorage(messageDao) }
    private val reactionStorage: ReactionStorage by lazy { ReactionStorage(reactionDao) }
    private val userStorage: UserStorage by lazy { UserStorageImpl(sharedPreferences) }


    private val streamMapper: StreamMapper by lazy { StreamMapper() }
    private val topicEntityMapper: TopicEntityMapper by lazy { TopicEntityMapper() }
    private val userMapper: UserMapper by lazy { UserMapper() }
    private val reactionMapper: ReactionMapper by lazy { ReactionMapper(userStorage.getOwnUserId()) }
    private val reactionEntityMapper: ReactionsEntityMapper by lazy { ReactionsEntityMapper() }
    private val messageMapper: MessageMapper by lazy { MessageMapper(reactionMapper) }
    private val messageWithReactionsEntityMapper: MessageWithReactionsEntityMapper by lazy {
        MessageWithReactionsEntityMapper(reactionEntityMapper)
    }
    private val messagesRemoteMapper: MessagesRemoteMapper by lazy {
        MessagesRemoteMapper(userStorage.getOwnUserId())
    }
    private val streamWithTopicsEntityMapper: StreamWithTopicsEntityMapper by lazy {
        StreamWithTopicsEntityMapper(topicEntityMapper)
    }

    private val topicRepo: TopicRepo by lazy { TopicRepoImpl(api = topicsApi) }
    private val streamRepo: StreamRepo by lazy {
        StreamRepoImpl(
            api = streamsApi,
            streamStorage = streamStorage,
            topicStorage = topicStorage,
            topicRepo = topicRepo,
            mapper = streamWithTopicsEntityMapper
        )
    }
    private val userRepo: UserRepo by lazy { UserRepoImpl(api = usersApi) }
    private val messageRepo: MessageRepo by lazy {
        MessageRepoImpl(
            api = messageApi,
            messageStorage = messageStorage,
            reactionStorage = reactionStorage,
            entityMapper = messageWithReactionsEntityMapper,
            remoteMapper = messagesRemoteMapper
        )
    }
    private val eventRepo: EventRepo by lazy {
        EventRepoImpl(
            api = eventsApi,
            messageRepo = messageRepo,
            messageStorage = messageStorage,
            reactionStorage = reactionStorage,
            messageEntityMapper = messageWithReactionsEntityMapper,
            messageRemoteMapper = messagesRemoteMapper,
            ownUserId = userStorage.getOwnUserId()
        )
    }

    private val scheduler: Scheduler = Schedulers.io()

    private val reactionRepo: ReactionRepo by lazy { ReactionRepoImpl(api = reactionsApi) }
    val getStreamsUseCase: GetStreamsUseCase by lazy {
        GetStreamsUseCase(repo = streamRepo, mapper = streamMapper, scheduler = scheduler)
    }
    val getAllUsersUseCase: GetAllUsersUseCase by lazy {
        GetAllUsersUseCase(repo = userRepo, mapper = userMapper, scheduler = scheduler)
    }
    val getMessagesOnScrollUseCase: GetMessagesOnScrollUseCase by lazy {
        GetMessagesOnScrollUseCase(
            repo = messageRepo,
            mapper = messageMapper,
            scheduler = scheduler
        )
    }

    val getEventsUseCase: GetEventsUseCase by lazy {
        GetEventsUseCase(repo = eventRepo, mapper = messageMapper, scheduler = scheduler)
    }
    val getNewestMessagesUseCase: GetNewestMessagesUseCase by lazy {
        GetNewestMessagesUseCase(repo = messageRepo, mapper = messageMapper, scheduler = scheduler)
    }
    val addReactionUseCase: AddReactionUseCase by lazy {
        AddReactionUseCase(repo = reactionRepo, scheduler = scheduler)
    }
    val deleteReactionUseCase: DeleteReactionUseCase by lazy {
        DeleteReactionUseCase(repo = reactionRepo, scheduler = scheduler)
    }
    val sendMessageUseCase: SendMessageUseCase by lazy {
        SendMessageUseCase(repo = messageRepo, scheduler = scheduler)
    }
    val getOwnUserUseCase: GetOwnUserUseCase by lazy {
        GetOwnUserUseCase(repo = userRepo, mapper = userMapper, scheduler = scheduler)
    }
}