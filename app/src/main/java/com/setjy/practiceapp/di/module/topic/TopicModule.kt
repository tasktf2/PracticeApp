package com.setjy.practiceapp.di.module.topic

import com.setjy.practiceapp.data.local.db.ZulipDatabase
import com.setjy.practiceapp.data.local.db.dao.UserStorage
import com.setjy.practiceapp.data.remote.api.EventsApi
import com.setjy.practiceapp.data.remote.api.MessageApi
import com.setjy.practiceapp.data.remote.api.ReactionsApi
import com.setjy.practiceapp.di.scope.TopicScope
import com.setjy.practiceapp.domain.base.UseCase
import com.setjy.practiceapp.domain.usecase.event.GetEventsUseCase
import com.setjy.practiceapp.domain.usecase.message.GetNewestMessagesUseCase
import com.setjy.practiceapp.domain.usecase.message.PaginationUseCase
import com.setjy.practiceapp.presentation.base.mvi.Middleware
import com.setjy.practiceapp.presentation.base.mvi.Reducer
import com.setjy.practiceapp.presentation.model.MessageUI
import com.setjy.practiceapp.presentation.ui.topic.TopicAction
import com.setjy.practiceapp.presentation.ui.topic.TopicEffect
import com.setjy.practiceapp.presentation.ui.topic.TopicReducer
import com.setjy.practiceapp.presentation.ui.topic.TopicState
import com.setjy.practiceapp.presentation.ui.topic.middleware.*
import dagger.Module
import dagger.Provides
import dagger.multibindings.ElementsIntoSet
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.core.Observable
import retrofit2.Retrofit
import javax.inject.Named

@Module
class TopicModule {

    @Provides
    @TopicScope
    fun provideMessageDao(zulipDatabase: ZulipDatabase) = zulipDatabase.messageDao()

    @Provides
    @TopicScope
    fun provideReactionDao(zulipDatabase: ZulipDatabase) = zulipDatabase.reactionDao()

    @Provides
    @TopicScope
    fun provideEventsApi(retrofit: Retrofit): EventsApi = retrofit.create(EventsApi::class.java)

    @Provides
    @TopicScope
    fun provideMessageApi(retrofit: Retrofit): MessageApi = retrofit.create(MessageApi::class.java)

    @Provides
    @TopicScope
    fun provideReactionsApi(retrofit: Retrofit): ReactionsApi =
        retrofit.create(ReactionsApi::class.java)

    @Provides
    @TopicScope
    @Named(NAMED_USER_ID)
    fun provideOwnUserId(userStorage: UserStorage): Int = userStorage.getOwnUserId()

    @Provides
    @TopicScope
    fun providePaginationUseCase(paginationUseCase: PaginationUseCase): UseCase<PaginationUseCase.Params, Observable<List<MessageUI>>> =
        paginationUseCase

    @Provides
    @TopicScope
    fun provideGetNewestMessagesUseCase(getNewestMessagesUseCase: GetNewestMessagesUseCase): UseCase<GetNewestMessagesUseCase.Params, Flowable<List<MessageUI>>> =
        getNewestMessagesUseCase

    @Provides
    @TopicScope
    fun provideGetEventsUseCase(getEventsUseCase: GetEventsUseCase): UseCase<GetEventsUseCase.Params, Observable<Pair<List<MessageUI>, Int>>> =
        getEventsUseCase

    @Provides
    @TopicScope
    @ElementsIntoSet
    fun provideTopicMiddlewares(
        addReactionMiddleware: AddReactionMiddleware,
        deleteReactionMiddleware: DeleteReactionMiddleware,
        getEventsMiddleware: GetEventsMiddleware,
        getNewestMessagesMiddleware: GetNewestMessagesMiddleware,
        paginationMiddleware: PaginationMiddleware,
        registerEventsQueueMiddleware: RegisterEventsQueueMiddleware,
        sendMessageMiddleware: SendMessageMiddleware
    ): Set<Middleware<TopicState, TopicAction>> = setOf(
        addReactionMiddleware,
        deleteReactionMiddleware,
        getEventsMiddleware,
        getNewestMessagesMiddleware,
        paginationMiddleware,
        registerEventsQueueMiddleware,
        sendMessageMiddleware
    )

    @Provides
    fun provideTopicInitialState(): TopicState = TopicState()

    companion object {
        const val NAMED_USER_ID: String = "ownUserId"
    }
}