package com.setjy.practiceapp.di.module.topic

import com.setjy.practiceapp.data.base.EntityMapper
import com.setjy.practiceapp.data.base.RemoteMapper
import com.setjy.practiceapp.data.local.db.dao.EventStorage
import com.setjy.practiceapp.data.local.db.dao.UserStorage
import com.setjy.practiceapp.data.local.model.MessageWithReactionsEntity
import com.setjy.practiceapp.data.local.model.MessageWithReactionsEntityMapper
import com.setjy.practiceapp.data.local.model.ReactionEntity
import com.setjy.practiceapp.data.local.model.ReactionsEntityMapper
import com.setjy.practiceapp.data.local.storage.EventStorageImpl
import com.setjy.practiceapp.data.local.storage.UserStorageImpl
import com.setjy.practiceapp.data.remote.response.EmojiToggleResponse
import com.setjy.practiceapp.data.remote.response.MessagesRemote
import com.setjy.practiceapp.data.remote.response.MessagesRemoteMapper
import com.setjy.practiceapp.data.remote.response.SendEventResponse
import com.setjy.practiceapp.data.repo.EventRepoImpl
import com.setjy.practiceapp.data.repo.MessageRepoImpl
import com.setjy.practiceapp.data.repo.ReactionRepoImpl
import com.setjy.practiceapp.di.scope.TopicScope
import com.setjy.practiceapp.domain.base.DomainMapper
import com.setjy.practiceapp.domain.base.UseCase
import com.setjy.practiceapp.domain.model.MessageMapper
import com.setjy.practiceapp.domain.model.MessageWithReactionsDomain
import com.setjy.practiceapp.domain.model.ReactionDomain
import com.setjy.practiceapp.domain.model.ReactionMapper
import com.setjy.practiceapp.domain.repo.EventRepo
import com.setjy.practiceapp.domain.repo.MessageRepo
import com.setjy.practiceapp.domain.repo.ReactionRepo
import com.setjy.practiceapp.domain.usecase.event.RegisterEventsQueueUseCase
import com.setjy.practiceapp.domain.usecase.message.SendMessageUseCase
import com.setjy.practiceapp.domain.usecase.reaction.AddReactionUseCase
import com.setjy.practiceapp.domain.usecase.reaction.DeleteReactionUseCase
import com.setjy.practiceapp.presentation.base.mvi.Reducer
import com.setjy.practiceapp.presentation.model.EmojiUI
import com.setjy.practiceapp.presentation.model.MessageUI
import com.setjy.practiceapp.presentation.ui.topic.TopicAction
import com.setjy.practiceapp.presentation.ui.topic.TopicEffect
import com.setjy.practiceapp.presentation.ui.topic.TopicReducer
import com.setjy.practiceapp.presentation.ui.topic.TopicState
import dagger.Binds
import dagger.Module
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single

@Module
interface TopicBindModule {

    @Binds
    @TopicScope
    fun bindReactionRepo(reactionRepoImpl: ReactionRepoImpl): ReactionRepo

    @Binds
    @TopicScope
    fun bindMessageRepo(messageRepoImpl: MessageRepoImpl): MessageRepo

    @Binds
    @TopicScope
    fun bindEventRepo(eventRepoImpl: EventRepoImpl): EventRepo

    @Binds
    @TopicScope
    fun bindEventStorage(eventStorageImpl: EventStorageImpl): EventStorage

    @Binds
    @TopicScope
    fun bindUserStorage(userStorageImpl: UserStorageImpl): UserStorage

    @Binds
    @TopicScope
    fun bindMessageRemoteMapper(messagesRemoteMapper: MessagesRemoteMapper): RemoteMapper<MessagesRemote, MessageWithReactionsDomain, MessageWithReactionsEntity>

    @Binds
    @TopicScope
    fun bindMessageWithReactionsEntityMapper(messageWithReactionsEntityMapper: MessageWithReactionsEntityMapper): EntityMapper<MessageWithReactionsEntity, MessageWithReactionsDomain>

    @Binds
    @TopicScope
    fun bindReactionsEntityMapper(reactionsEntityMapper: ReactionsEntityMapper): EntityMapper<ReactionEntity, ReactionDomain>

    @Binds
    @TopicScope
    fun bindReactionMapper(reactionMapper: ReactionMapper): DomainMapper<ReactionDomain, EmojiUI>

    @Binds
    @TopicScope
    fun bindMessageMapper(messageMapper: MessageMapper): DomainMapper<MessageWithReactionsDomain, MessageUI>

    @Binds
    @TopicScope
    fun provideTopicReducer(topicReducer: TopicReducer): Reducer<TopicAction, TopicState, TopicEffect>

    @Binds
    @TopicScope
    fun bindSendMessageUseCase(sendMessageUseCase: SendMessageUseCase): UseCase<SendMessageUseCase.Params, Completable>

    @Binds
    @TopicScope
    fun bindRegisterEventsQueueUseCase(registerEventsQueueUseCase: RegisterEventsQueueUseCase): UseCase<Unit, Observable<SendEventResponse>>

    @Binds
    @TopicScope
    fun bindAddReactionUseCase(addReactionUseCase: AddReactionUseCase): UseCase<AddReactionUseCase.Params, Single<EmojiToggleResponse>>

    @Binds
    @TopicScope
    fun bindDeleteReactionUseCase(deleteReactionUseCase: DeleteReactionUseCase): UseCase<DeleteReactionUseCase.Params, Single<EmojiToggleResponse>>
}