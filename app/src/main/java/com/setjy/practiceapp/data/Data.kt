package com.setjy.practiceapp.data

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import com.google.gson.Gson
import com.setjy.practiceapp.data.network.*
import com.setjy.practiceapp.profile.UserOwnItemUI
import com.setjy.practiceapp.profile.UserStatus
import com.setjy.practiceapp.recycler.base.ViewTyped
import com.setjy.practiceapp.recycler.items.*
import com.setjy.practiceapp.util.getMessageTimeStampMillis
import com.setjy.practiceapp.util.getMessageTimeStampSeconds
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.Schedulers
import java.time.Instant


object Data {

    private var userOwnUI: UserOwnItemUI? = null

    private var listOfStreams: List<StreamItemUI> = mutableListOf()

    private var listOfSubscribedStreams: List<StreamItemUI> = mutableListOf()

    private var listOfUsers: List<UserItemUI> = mutableListOf()

    private var tempMessageDatabase: MutableMap<String, MutableList<ViewTyped>> = mutableMapOf()

    private var lastEventId = 0

    private var queueId = ""

    private const val EVENT_OPERATION_ADD = "add"

    private const val EVENT_MESSAGE = "message"

    private const val EVENT_REACTION = "reaction"

    private const val ANCHOR_NEWEST = "newest"

    private const val NARROW_STREAM = "stream"

    private const val NARROW_TOPIC = "topic"

    fun getStreamsAndTopics(subscribedOnly: Boolean): Single<List<StreamItemUI>> =
        when {
            subscribedOnly && listOfSubscribedStreams.isEmpty() ->
                NetworkService.zulipService.getSubscribedStreams()
                    .flatMap {
                        Observable.fromIterable(it.subscriptions)
                            .flatMapSingle { streamRemote ->
                                Single.zip(Single.just(streamRemote), getTopic(streamRemote))
                                { stream, topics ->
                                    streamsToPresentation(stream, topics, subscribedOnly)
                                }
                            }.toList()
                    }.doAfterSuccess { listOfSubscribedStreams = it }
            subscribedOnly -> Single.just(listOfSubscribedStreams.onEach { it.isExpanded = false })
            !subscribedOnly && listOfStreams.isEmpty() -> NetworkService.zulipService.getAllStreams()
                .flatMap {
                    Observable.fromIterable(it.streams)
                        .flatMapSingle { streamRemote ->
                            Single.zip(Single.just(streamRemote), getTopic(streamRemote))
                            { stream, topics ->
                                streamsToPresentation(stream, topics, subscribedOnly)
                            }
                        }.toList()
                }.doAfterSuccess { listOfStreams = it }
            else -> Single.just(listOfStreams.onEach { it.isExpanded = false })
        }

    private fun getTopic(stream: StreamsRemote) =
        NetworkService.zulipService.getTopicsById(stream.streamId).map { it.topics }

    private fun streamsToPresentation(
        remoteStream: StreamsRemote,
        remoteTopics: List<TopicsRemote>,
        subscribedOnly: Boolean
    ) =
        StreamItemUI(
            streamId = remoteStream.streamId,
            streamName = remoteStream.streamName,
            isSubscribed = subscribedOnly,
            listOfTopics = remoteTopics.map {
                TopicItemUI(
                    topicId = it.topicId,
                    topicName = it.topicName,
                    parent = remoteStream.streamName
                )
            })

    @RequiresApi(Build.VERSION_CODES.O)
    fun getOwnUser(): Single<UserOwnItemUI> =
        if (userOwnUI == null) {
            NetworkService.zulipService.getOwnUser()
                .flatMap {
                    Single.zip(Single.just(it), getUserStatus(it.userId), ::ownUserToPresentation)
                }.doAfterSuccess { userOwnUI = it }
        } else {
            Single.just(userOwnUI!!)
        }

    private fun ownUserToPresentation(
        userOwnResponse: UserOwnResponse,
        status: String
    ) = with(userOwnResponse) {
        UserOwnItemUI(
            userId = userId,
            userFullName = fullName,
            avatarUrl = avatarUrl,
            timezone = userTimeZone,
            status = when (status) {
                UserStatus.ACTIVE.name.lowercase() -> UserStatus.ACTIVE
                UserStatus.IDLE.name.lowercase() -> UserStatus.IDLE
                else -> UserStatus.OFFLINE
            }
        )
    }

    private fun getUserStatus(userId: Int): Single<String> =
        NetworkService.zulipService.getUserStatus(userId).subscribeOn(Schedulers.io())
            .map { it.presence.statusAndTimestamp.status }

    fun getAllUsers(): Single<List<UserItemUI>> =
        if (listOfUsers.isEmpty()) {
            NetworkService.zulipService.getAllUsers()
                .flatMap { usersResponse ->
                    Observable.fromIterable(usersResponse.members)
                        .flatMapSingle { userRemote ->
                            Single.zip(
                                Single.just(userRemote),
                                getUserStatus(userRemote.userId),
                                ::usersToPresentation
                            )
                        }.toList()
                }.doAfterSuccess { listOfUsers = it }
        } else {
            Single.just(listOfUsers)
        }

    private fun usersToPresentation(userRemote: UsersRemote, userStatus: String) =
        with(userRemote) {
            UserItemUI(
                userId = userId,
                fullName = fullName,
                userEmail = userEmail,
                avatarUrl = avatarUrl,
                status = when (userStatus) {
                    UserStatus.ACTIVE.name.lowercase() -> UserStatus.ACTIVE
                    UserStatus.IDLE.name.lowercase() -> UserStatus.IDLE
                    else -> UserStatus.OFFLINE
                }
            )
        }

    @RequiresApi(Build.VERSION_CODES.O)
    fun getMessages(streamName: String, topicName: String): Single<List<ViewTyped>> =
        if (tempMessageDatabase[streamName + topicName].isNullOrEmpty()) {
            NetworkService.zulipService.getMessages(
                anchor = ANCHOR_NEWEST,
                numBefore = 15,
                numAfter = 0,
                narrow = Gson().toJson( //todo find how to fix it
                    listOf(
                        Narrow(NARROW_STREAM, streamName),
                        Narrow(NARROW_TOPIC, topicName)
                    )
                )
            ).map { response ->
                response.messages.map { message ->
                    with(message) {
                        when (senderId) {
                            getUserOwnId() -> {
                                OutgoingMessageUI(
                                    messageId = messageId,
                                    userId = getUserOwnId(),
                                    message = content,
                                    timestamp = getMessageTimeStampSeconds(timestamp),
                                    reactions = getListOfEmojiUI(this)
                                )
                            }
                            else -> {
                                IncomingMessageUI(
                                    messageId = messageId,
                                    userId = senderId,
                                    username = senderFullName,
                                    message = content,
                                    avatarUrl = avatarUrl,
                                    timestamp = getMessageTimeStampSeconds(timestamp),
                                    reactions = getListOfEmojiUI(this)
                                )
                            }
                        }
                    }
                }.asReversed()
            }.doAfterSuccess { tempMessageDatabase[streamName + topicName] = it.toMutableList() }
        } else {
            Single.just(tempMessageDatabase[streamName + topicName]!!)
        }

    private fun getUserOwnId(): Int = userOwnUI!!.userId

    private fun getListOfEmojiUI(messagesRemote: MessagesRemote): List<EmojiUI> =
        messagesRemote.reactions.map { response ->
            with(response) {
                EmojiRemote(
                    code = emojiCode,
                    name = emojiName,
                    userId = userId
                )
            }
        }.map { emojiRemote ->
            with(emojiRemote) {
                EmojiUI(
                    emojiName = name,
                    code = code,
                    isSelected = userId == getUserOwnId()
                )
            }
        }

    @RequiresApi(Build.VERSION_CODES.O)
    fun sendMessage(
        streamName: String,
        topicName: String,
        message: String
    ): Single<List<ViewTyped>> =
        NetworkService.zulipService.sendMessage(streamName, topicName, message)
            .flatMap {
                Single.zip(
                    Single.just(it),
                    getMessages(streamName, topicName)
                ) { response, list -> messagesToPresentation(response, list, message) }
            }.doAfterSuccess { tempMessageDatabase[streamName + topicName] = it.toMutableList() }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun messagesToPresentation(
        response: MessageSendResponse,
        messages: List<ViewTyped>,
        message: String
    ): List<ViewTyped> = listOf(
        OutgoingMessageUI(
            userId = getUserOwnId(),
            message = message,
            messageId = response.messageId,
            reactions = listOf(),
            timestamp = getMessageTimeStampMillis(
                Instant.now().toEpochMilli()
            )
        )
    ) + messages

    fun getTimeZone(): String = userOwnUI!!.timezone

    fun addReaction(messageId: Int, emojiName: String): Single<EmojiToggleResponse> =
        NetworkService.zulipService.addReaction(messageId, emojiName)

    fun deleteReaction(messageId: Int, emojiName: String): Single<EmojiToggleResponse> =
        NetworkService.zulipService.deleteReaction(messageId, emojiName)

    fun registerEventQueue(): Single<SendEventResponse> =
        NetworkService.zulipService.registerEventQueue()
            .doAfterSuccess {
                lastEventId = it.lastEventId
                queueId = it.queueId
                Log.d("xxx", "register success: $it")
            }

    @RequiresApi(Build.VERSION_CODES.O)
    fun getMessagesFromEventsQueue(
        streamName: String,
        topicName: String
    ): Observable<List<ViewTyped>> = NetworkService.zulipService.getEventsQueue(
        queueId,
        lastEventId
    )
        .flatMap { response ->
            if (response.events.isEmpty()) {
                Observable.empty()
            } else {
                lastEventId = response.events.last().eventId
                Observable.zip(
                    Observable.just(response.events),
                    Observable.fromSingle(getMessages(streamName, topicName)),
                    ::messagesFromEventsQueueToPresentation
                )
            }
        }
        .map { eventsAndMessages -> reactionsFromEventsQueueToPresentation(eventsAndMessages) }
        .doAfterNext { tempMessageDatabase[streamName + topicName] = it.toMutableList() }
        .doOnError { e -> Log.d("xxx", "get error, resume next?: (messages) $e") }
        .retry()

    private fun reactionsFromEventsQueueToPresentation(
        eventsAndMessages: Pair<List<GetEventRemote>, List<ViewTyped>>
    ): List<ViewTyped> =
        eventsAndMessages.first.flatMap { event ->
            eventsAndMessages.second.map { message ->
                when {
                    message is IncomingMessageUI && message.messageId == event.messageId -> {
                        if (event.operation == EVENT_OPERATION_ADD) {
                            message.copy(
                                reactions = message.reactions + listOf(
                                    EmojiUI(
                                        emojiName = event.emojiName,
                                        code = event.emojiCode,
                                        isSelected = getUserOwnId() == event.userId
                                    )
                                )
                            )
                        } else {
                            if (event.userId == getUserOwnId()) {
                                message.copy(reactions = message.reactions.filterNot { emoji ->
                                    emoji.code == event.emojiCode && emoji.isSelected
                                })
                            } else {
                                val reactions = message.reactions.toMutableList()
                                reactions.remove(reactions.find { emoji -> emoji.code == event.emojiCode && !emoji.isSelected })
                                message.copy(reactions = reactions)
                            }
                        }
                    }
                    message is OutgoingMessageUI && message.messageId == event.messageId -> {
                        if (event.operation == EVENT_OPERATION_ADD) {
                            message.copy(
                                reactions = message.reactions + listOf(
                                    EmojiUI(
                                        emojiName = event.emojiName,
                                        code = event.emojiCode,
                                        isSelected = getUserOwnId() == event.userId
                                    )
                                )
                            )
                        } else {
                            if (event.userId == getUserOwnId()) {
                                message.copy(reactions = message.reactions.filterNot { emoji ->
                                    emoji.code == event.emojiCode && emoji.isSelected
                                })
                            } else {
                                val reactions = message.reactions.toMutableList()
                                reactions.remove(reactions.find { emoji -> emoji.code == event.emojiCode && !emoji.isSelected })
                                message.copy(reactions = reactions)
                            }
                        }
                    }
                    else -> {
                        message
                    }
                }
            }
        }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun messagesFromEventsQueueToPresentation(
        eventsRemote: List<GetEventRemote>,
        databaseMessages: List<ViewTyped>
    ): Pair<List<GetEventRemote>, List<ViewTyped>> = Pair(eventsRemote
//        .filter { it.type== EVENT_REACTION }
        ,
        eventsRemote.filter { it.type == EVENT_MESSAGE }
            .filterNot { eventRemote ->
                tempMessageDatabase[eventRemote.message.streamName + eventRemote.message.topicName]
                    ?.find { item -> item is OutgoingMessageUI && item.messageId == eventRemote.message.messageId } != null
            }
            .map { event ->
                with(event.message) {
                    when (senderId) { //todo можно засунуть в метод, чтобы не было повторений
                        getUserOwnId() -> {
                            OutgoingMessageUI(
                                messageId = messageId,
                                userId = getUserOwnId(),
                                message = content,
                                timestamp = getMessageTimeStampSeconds(timestamp),
                            )
                        }
                        else -> {
                            IncomingMessageUI(
                                messageId = messageId,
                                userId = senderId,
                                username = senderFullName,
                                message = content,
                                avatarUrl = avatarUrl,
                                timestamp = getMessageTimeStampSeconds(timestamp),
                            )
                        }
                    }
                }
            }.asReversed() + databaseMessages
    )

    val emojiUISet = listOf(

// Smileys & Emotion
        EmojiUI("grinning", "1f600"),
        EmojiUI("smiley", "1f603"),
        EmojiUI("big_smile", "1f604"),
        EmojiUI("grinning_face_with_smiling_eyes", "1f601"),
        EmojiUI("laughing", "1f606"),
        EmojiUI("sweat_smile", "1f605"),
        EmojiUI("rolling_on_the_floor_laughing", "1f923"),
        EmojiUI("joy", "1f602"),
        EmojiUI("smile", "1f642"),
        EmojiUI("upside_down", "1f643"),
        EmojiUI("wink", "1f609"),
        EmojiUI("blush", "1f60a"),
        EmojiUI("innocent", "1f607"),
        EmojiUI("heart_eyes", "1f60d"),
        EmojiUI("heart_kiss", "1f618"),
        EmojiUI("kiss", "1f617"),
        EmojiUI("smiling_face", "263a"),
        EmojiUI("kiss_with_blush", "1f61a"),
        EmojiUI("kiss_smiling_eyes", "1f619"),
        EmojiUI("yum", "1f60b"),
        EmojiUI("stuck_out_tongue", "1f61b"),
        EmojiUI("stuck_out_tongue_wink", "1f61c"),
        EmojiUI("stuck_out_tongue_closed_eyes", "1f61d"),
        EmojiUI("money_face", "1f911"),
        EmojiUI("hug", "1f917"),
        EmojiUI("thinking", "1f914"),
        EmojiUI("silence", "1f910"),
        EmojiUI("neutral", "1f610"),
        EmojiUI("expressionless", "1f611"),
        EmojiUI("speechless", "1f636"),
        EmojiUI("smirk", "1f60f"),
        EmojiUI("unamused", "1f612"),
        EmojiUI("rolling_eyes", "1f644"),
        EmojiUI("grimacing", "1f62c"),
        EmojiUI("lying", "1f925"),
        EmojiUI("relieved", "1f60c"),
        EmojiUI("pensive", "1f614"),
        EmojiUI("sleepy", "1f62a"),
        EmojiUI("drooling", "1f924"),
        EmojiUI("sleeping", "1f634"),
        EmojiUI("cant_talk", "1f637"),
        EmojiUI("sick", "1f912"),
        EmojiUI("hurt", "1f915"),
        EmojiUI("nauseated", "1f922"),
        EmojiUI("sneezing", "1f927"),
        EmojiUI("dizzy", "1f635"),
        EmojiUI("cowboy", "1f920"),
        EmojiUI("sunglasses", "1f60e"),
        EmojiUI("nerd", "1f913"),
        EmojiUI("oh_no", "1f615"),
        EmojiUI("worried", "1f61f"),
        EmojiUI("frown", "1f641"),
        EmojiUI("sad", "2639"),
        EmojiUI("open_mouth", "1f62e"),
        EmojiUI("hushed", "1f62f"),
        EmojiUI("astonished", "1f632"),
        EmojiUI("flushed", "1f633"),
        EmojiUI("frowning", "1f626"),
        EmojiUI("anguished", "1f627"),
        EmojiUI("fear", "1f628"),
        EmojiUI("cold_sweat", "1f630"),
        EmojiUI("exhausted", "1f625"),
        EmojiUI("cry", "1f622"),
        EmojiUI("sob", "1f62d"),
        EmojiUI("scream", "1f631"),
        EmojiUI("confounded", "1f616"),
        EmojiUI("persevere", "1f623"),
        EmojiUI("disappointed", "1f61e"),
        EmojiUI("sweat", "1f613"),
        EmojiUI("weary", "1f629"),
        EmojiUI("anguish", "1f62b"),
        EmojiUI("triumph", "1f624"),
        EmojiUI("rage", "1f621"),
        EmojiUI("angry", "1f620"),
        EmojiUI("smiling_devil", "1f608"),
        EmojiUI("devil", "1f47f"),
        EmojiUI("skull", "1f480"),
        EmojiUI("skull_and_crossbones", "2620"),
        EmojiUI("poop", "1f4a9"),
        EmojiUI("clown", "1f921"),
        EmojiUI("ogre", "1f479"),
        EmojiUI("goblin", "1f47a"),
        EmojiUI("ghost", "1f47b"),
        EmojiUI("alien", "1f47d"),
        EmojiUI("space_invader", "1f47e"),
        EmojiUI("robot", "1f916"),
        EmojiUI("smiley_cat", "1f63a"),
        EmojiUI("smile_cat", "1f638"),
        EmojiUI("joy_cat", "1f639"),
        EmojiUI("heart_eyes_cat", "1f63b"),
        EmojiUI("smirk_cat", "1f63c"),
        EmojiUI("kissing_cat", "1f63d"),
        EmojiUI("scream_cat", "1f640"),
        EmojiUI("crying_cat", "1f63f"),
        EmojiUI("angry_cat", "1f63e"),
        EmojiUI("see_no_evil", "1f648"),
        EmojiUI("hear_no_evil", "1f649"),
        EmojiUI("speak_no_evil", "1f64a"),
        EmojiUI("lipstick_kiss", "1f48b"),
        EmojiUI("love_letter", "1f48c"),
        EmojiUI("cupid", "1f498"),
        EmojiUI("gift_heart", "1f49d"),
        EmojiUI("sparkling_heart", "1f496"),
        EmojiUI("heart_pulse", "1f497"),
        EmojiUI("heartbeat", "1f493"),
        EmojiUI("revolving_hearts", "1f49e"),
        EmojiUI("two_hearts", "1f495"),
        EmojiUI("heart_box", "1f49f"),
        EmojiUI("heart_exclamation", "2763"),
        EmojiUI("broken_heart", "1f494"),
        EmojiUI("heart", "2764"),
        EmojiUI("yellow_heart", "1f49b"),
        EmojiUI("green_heart", "1f49a"),
        EmojiUI("blue_heart", "1f499"),
        EmojiUI("purple_heart", "1f49c"),
        EmojiUI("black_heart", "1f5a4"),
        EmojiUI("100", "1f4af"),
        EmojiUI("anger", "1f4a2"),
        EmojiUI("boom", "1f4a5"),
        EmojiUI("seeing_stars", "1f4ab"),
        EmojiUI("sweat_drops", "1f4a6"),
        EmojiUI("dash", "1f4a8"),
        EmojiUI("hole", "1f573"),
        EmojiUI("bomb", "1f4a3"),
        EmojiUI("umm", "1f4ac"),
        EmojiUI("speech_bubble", "1f5e8"),
        EmojiUI("anger_bubble", "1f5ef"),
        EmojiUI("thought", "1f4ad"),
        EmojiUI("zzz", "1f4a4"),

// People & Body
        EmojiUI("wave", "1f44b"),
        EmojiUI("stop", "1f91a"),
        EmojiUI("high_five", "1f590"),
        EmojiUI("hand", "270b"),
        EmojiUI("spock", "1f596"),
        EmojiUI("ok", "1f44c"),
        EmojiUI("peace_sign", "270c"),
        EmojiUI("fingers_crossed", "1f91e"),
        EmojiUI("rock_on", "1f918"),
        EmojiUI("call_me", "1f919"),
        EmojiUI("point_left", "1f448"),
        EmojiUI("point_right", "1f449"),
        EmojiUI("point_up", "1f446"),
        EmojiUI("middle_finger", "1f595"),
        EmojiUI("point_down", "1f447"),
        EmojiUI("wait_one_second", "261d"),
        EmojiUI("+1", "1f44d"),
        EmojiUI("-1", "1f44e"),
        EmojiUI("fist", "270a"),
        EmojiUI("fist_bump", "1f44a"),
        EmojiUI("left_fist", "1f91b"),
        EmojiUI("right_fist", "1f91c"),
        EmojiUI("clap", "1f44f"),
        EmojiUI("raised_hands", "1f64c"),
        EmojiUI("open_hands", "1f450"),
        EmojiUI("handshake", "1f91d"),
        EmojiUI("pray", "1f64f"),
        EmojiUI("writing", "270d"),
        EmojiUI("nail_polish", "1f485"),
        EmojiUI("selfie", "1f933"),
        EmojiUI("muscle", "1f4aa"),
        EmojiUI("ear", "1f442"),
        EmojiUI("nose", "1f443"),
        EmojiUI("eyes", "1f440"),
        EmojiUI("eye", "1f441"),
        EmojiUI("tongue", "1f445"),
        EmojiUI("lips", "1f444"),
        EmojiUI("baby", "1f476"),
        EmojiUI("boy", "1f466"),
        EmojiUI("girl", "1f467"),
        EmojiUI("man", "1f468"),
        EmojiUI("woman", "1f469"),
        EmojiUI("older_man", "1f474"),
        EmojiUI("older_woman", "1f475"),
        EmojiUI("person_frowning", "1f64d"),
        EmojiUI("person_pouting", "1f64e"),
        EmojiUI("no_signal", "1f645"),
        EmojiUI("ok_signal", "1f646"),
        EmojiUI("information_desk_person", "1f481"),
        EmojiUI("raising_hand", "1f64b"),
        EmojiUI("bow", "1f647"),
        EmojiUI("face_palm", "1f926"),
        EmojiUI("shrug", "1f937"),
        EmojiUI("police", "1f46e"),
        EmojiUI("detective", "1f575"),
        EmojiUI("guard", "1f482"),
        EmojiUI("construction_worker", "1f477"),
        EmojiUI("prince", "1f934"),
        EmojiUI("princess", "1f478"),
        EmojiUI("turban", "1f473"),
        EmojiUI("gua_pi_mao", "1f472"),
        EmojiUI("bride", "1f470"),
        EmojiUI("pregnant", "1f930"),
        EmojiUI("angel", "1f47c"),
        EmojiUI("santa", "1f385"),
        EmojiUI("mother_christmas", "1f936"),
        EmojiUI("massage", "1f486"),
        EmojiUI("haircut", "1f487"),
        EmojiUI("walking", "1f6b6"),
        EmojiUI("running", "1f3c3"),
        EmojiUI("dancer", "1f483"),
        EmojiUI("dancing", "1f57a"),
        EmojiUI("levitating", "1f574"),
        EmojiUI("dancers", "1f46f"),
        EmojiUI("fencing", "1f93a"),
        EmojiUI("horse_racing", "1f3c7"),
        EmojiUI("skier", "26f7"),
        EmojiUI("snowboarder", "1f3c2"),
        EmojiUI("golf", "1f3cc"),
        EmojiUI("surf", "1f3c4"),
        EmojiUI("rowboat", "1f6a3"),
        EmojiUI("swim", "1f3ca"),
        EmojiUI("ball", "26f9"),
        EmojiUI("lift", "1f3cb"),
        EmojiUI("cyclist", "1f6b4"),
        EmojiUI("mountain_biker", "1f6b5"),
        EmojiUI("cartwheel", "1f938"),
        EmojiUI("wrestling", "1f93c"),
        EmojiUI("water_polo", "1f93d"),
        EmojiUI("handball", "1f93e"),
        EmojiUI("juggling", "1f939"),
        EmojiUI("bath", "1f6c0"),
        EmojiUI("in_bed", "1f6cc"),
        EmojiUI("two_women_holding_hands", "1f46d"),
        EmojiUI("man_and_woman_holding_hands", "1f46b"),
        EmojiUI("two_men_holding_hands", "1f46c"),
        EmojiUI("family", "1f46a"),
        EmojiUI("speaking_head", "1f5e3"),
        EmojiUI("silhouette", "1f464"),
        EmojiUI("silhouettes", "1f465"),
        EmojiUI("footprints", "1f463"),
        EmojiUI("tuxedo", "1f935"),

// Animals & Nature
        EmojiUI("monkey_face", "1f435"),
        EmojiUI("monkey", "1f412"),
        EmojiUI("gorilla", "1f98d"),
        EmojiUI("puppy", "1f436"),
        EmojiUI("dog", "1f415"),
        EmojiUI("poodle", "1f429"),
        EmojiUI("wolf", "1f43a"),
        EmojiUI("fox", "1f98a"),
        EmojiUI("kitten", "1f431"),
        EmojiUI("cat", "1f408"),
        EmojiUI("lion", "1f981"),
        EmojiUI("tiger_cub", "1f42f"),
        EmojiUI("tiger", "1f405"),
        EmojiUI("leopard", "1f406"),
        EmojiUI("pony", "1f434"),
        EmojiUI("horse", "1f40e"),
        EmojiUI("unicorn", "1f984"),
        EmojiUI("deer", "1f98c"),
        EmojiUI("calf", "1f42e"),
        EmojiUI("ox", "1f402"),
        EmojiUI("water_buffalo", "1f403"),
        EmojiUI("cow", "1f404"),
        EmojiUI("piglet", "1f437"),
        EmojiUI("pig", "1f416"),
        EmojiUI("boar", "1f417"),
        EmojiUI("pig_nose", "1f43d"),
        EmojiUI("ram", "1f40f"),
        EmojiUI("sheep", "1f411"),
        EmojiUI("goat", "1f410"),
        EmojiUI("arabian_camel", "1f42a"),
        EmojiUI("camel", "1f42b"),
        EmojiUI("elephant", "1f418"),
        EmojiUI("rhinoceros", "1f98f"),
        EmojiUI("dormouse", "1f42d"),
        EmojiUI("mouse", "1f401"),
        EmojiUI("rat", "1f400"),
        EmojiUI("hamster", "1f439"),
        EmojiUI("bunny", "1f430"),
        EmojiUI("rabbit", "1f407"),
        EmojiUI("chipmunk", "1f43f"),
        EmojiUI("bat", "1f987"),
        EmojiUI("bear", "1f43b"),
        EmojiUI("koala", "1f428"),
        EmojiUI("panda", "1f43c"),
        EmojiUI("paw_prints", "1f43e"),
        EmojiUI("turkey", "1f983"),
        EmojiUI("chicken", "1f414"),
        EmojiUI("rooster", "1f413"),
        EmojiUI("hatching", "1f423"),
        EmojiUI("chick", "1f424"),
        EmojiUI("new_baby", "1f425"),
        EmojiUI("bird", "1f426"),
        EmojiUI("penguin", "1f427"),
        EmojiUI("dove", "1f54a"),
        EmojiUI("eagle", "1f985"),
        EmojiUI("duck", "1f986"),
        EmojiUI("owl", "1f989"),
        EmojiUI("frog", "1f438"),
        EmojiUI("crocodile", "1f40a"),
        EmojiUI("turtle", "1f422"),
        EmojiUI("lizard", "1f98e"),
        EmojiUI("snake", "1f40d"),
        EmojiUI("dragon_face", "1f432"),
        EmojiUI("dragon", "1f409"),
        EmojiUI("whale", "1f433"),
        EmojiUI("humpback_whale", "1f40b"),
        EmojiUI("dolphin", "1f42c"),
        EmojiUI("fish", "1f41f"),
        EmojiUI("tropical_fish", "1f420"),
        EmojiUI("blowfish", "1f421"),
        EmojiUI("shark", "1f988"),
        EmojiUI("octopus", "1f419"),
        EmojiUI("shell", "1f41a"),
        EmojiUI("snail", "1f40c"),
        EmojiUI("butterfly", "1f98b"),
        EmojiUI("bug", "1f41b"),
        EmojiUI("ant", "1f41c"),
        EmojiUI("bee", "1f41d"),
        EmojiUI("spider", "1f577"),
        EmojiUI("web", "1f578"),
        EmojiUI("scorpion", "1f982"),
        EmojiUI("bouquet", "1f490"),
        EmojiUI("cherry_blossom", "1f338"),
        EmojiUI("white_flower", "1f4ae"),
        EmojiUI("rosette", "1f3f5"),
        EmojiUI("rose", "1f339"),
        EmojiUI("wilted_flower", "1f940"),
        EmojiUI("hibiscus", "1f33a"),
        EmojiUI("sunflower", "1f33b"),
        EmojiUI("blossom", "1f33c"),
        EmojiUI("tulip", "1f337"),
        EmojiUI("seedling", "1f331"),
        EmojiUI("evergreen_tree", "1f332"),
        EmojiUI("tree", "1f333"),
        EmojiUI("palm_tree", "1f334"),
        EmojiUI("cactus", "1f335"),
        EmojiUI("harvest", "1f33e"),
        EmojiUI("herb", "1f33f"),
        EmojiUI("shamrock", "2618"),
        EmojiUI("lucky", "1f340"),
        EmojiUI("maple_leaf", "1f341"),
        EmojiUI("fallen_leaf", "1f342"),
        EmojiUI("leaves", "1f343"),
        EmojiUI("beetle", "1f41e"),

// Food & Drink
        EmojiUI("grapes", "1f347"),
        EmojiUI("melon", "1f348"),
        EmojiUI("watermelon", "1f349"),
        EmojiUI("orange", "1f34a"),
        EmojiUI("lemon", "1f34b"),
        EmojiUI("banana", "1f34c"),
        EmojiUI("pineapple", "1f34d"),
        EmojiUI("apple", "1f34e"),
        EmojiUI("green_apple", "1f34f"),
        EmojiUI("pear", "1f350"),
        EmojiUI("peach", "1f351"),
        EmojiUI("cherries", "1f352"),
        EmojiUI("strawberry", "1f353"),
        EmojiUI("kiwi", "1f95d"),
        EmojiUI("tomato", "1f345"),
        EmojiUI("avocado", "1f951"),
        EmojiUI("eggplant", "1f346"),
        EmojiUI("potato", "1f954"),
        EmojiUI("carrot", "1f955"),
        EmojiUI("corn", "1f33d"),
        EmojiUI("hot_pepper", "1f336"),
        EmojiUI("cucumber", "1f952"),
        EmojiUI("mushroom", "1f344"),
        EmojiUI("peanuts", "1f95c"),
        EmojiUI("chestnut", "1f330"),
        EmojiUI("bread", "1f35e"),
        EmojiUI("croissant", "1f950"),
        EmojiUI("baguette", "1f956"),
        EmojiUI("pancakes", "1f95e"),
        EmojiUI("cheese", "1f9c0"),
        EmojiUI("meat", "1f356"),
        EmojiUI("drumstick", "1f357"),
        EmojiUI("bacon", "1f953"),
        EmojiUI("hamburger", "1f354"),
        EmojiUI("fries", "1f35f"),
        EmojiUI("pizza", "1f355"),
        EmojiUI("hotdog", "1f32d"),
        EmojiUI("taco", "1f32e"),
        EmojiUI("burrito", "1f32f"),
        EmojiUI("doner_kebab", "1f959"),
        EmojiUI("egg", "1f95a"),
        EmojiUI("cooking", "1f373"),
        EmojiUI("paella", "1f958"),
        EmojiUI("food", "1f372"),
        EmojiUI("salad", "1f957"),
        EmojiUI("popcorn", "1f37f"),
        EmojiUI("bento", "1f371"),
        EmojiUI("senbei", "1f358"),
        EmojiUI("onigiri", "1f359"),
        EmojiUI("rice", "1f35a"),
        EmojiUI("curry", "1f35b"),
        EmojiUI("ramen", "1f35c"),
        EmojiUI("spaghetti", "1f35d"),
        EmojiUI("yam", "1f360"),
        EmojiUI("oden", "1f362"),
        EmojiUI("sushi", "1f363"),
        EmojiUI("tempura", "1f364"),
        EmojiUI("naruto", "1f365"),
        EmojiUI("dango", "1f361"),
        EmojiUI("crab", "1f980"),
        EmojiUI("shrimp", "1f990"),
        EmojiUI("squid", "1f991"),
        EmojiUI("soft_serve", "1f366"),
        EmojiUI("shaved_ice", "1f367"),
        EmojiUI("ice_cream", "1f368"),
        EmojiUI("donut", "1f369"),
        EmojiUI("cookie", "1f36a"),
        EmojiUI("birthday", "1f382"),
        EmojiUI("cake", "1f370"),
        EmojiUI("chocolate", "1f36b"),
        EmojiUI("candy", "1f36c"),
        EmojiUI("lollipop", "1f36d"),
        EmojiUI("custard", "1f36e"),
        EmojiUI("honey", "1f36f"),
        EmojiUI("baby_bottle", "1f37c"),
        EmojiUI("milk", "1f95b"),
        EmojiUI("coffee", "2615"),
        EmojiUI("tea", "1f375"),
        EmojiUI("sake", "1f376"),
        EmojiUI("champagne", "1f37e"),
        EmojiUI("wine", "1f377"),
        EmojiUI("cocktail", "1f378"),
        EmojiUI("tropical_drink", "1f379"),
        EmojiUI("beer", "1f37a"),
        EmojiUI("beers", "1f37b"),
        EmojiUI("clink", "1f942"),
        EmojiUI("small_glass", "1f943"),
        EmojiUI("hungry", "1f37d"),
        EmojiUI("fork_and_knife", "1f374"),
        EmojiUI("spoon", "1f944"),
        EmojiUI("knife", "1f52a"),
        EmojiUI("vase", "1f3fa"),

// Activities
        EmojiUI("jack-o-lantern", "1f383"),
        EmojiUI("holiday_tree", "1f384"),
        EmojiUI("fireworks", "1f386"),
        EmojiUI("sparkler", "1f387"),
        EmojiUI("sparkles", "2728"),
        EmojiUI("balloon", "1f388"),
        EmojiUI("tada", "1f389"),
        EmojiUI("confetti", "1f38a"),
        EmojiUI("wish_tree", "1f38b"),
        EmojiUI("bamboo", "1f38d"),
        EmojiUI("dolls", "1f38e"),
        EmojiUI("carp_streamer", "1f38f"),
        EmojiUI("wind_chime", "1f390"),
        EmojiUI("moon_ceremony", "1f391"),
        EmojiUI("ribbon", "1f380"),
        EmojiUI("gift", "1f381"),
        EmojiUI("reminder_ribbon", "1f397"),
        EmojiUI("ticket", "1f39f"),
        EmojiUI("pass", "1f3ab"),
        EmojiUI("military_medal", "1f396"),
        EmojiUI("trophy", "1f3c6"),
        EmojiUI("medal", "1f3c5"),
        EmojiUI("first_place", "1f947"),
        EmojiUI("second_place", "1f948"),
        EmojiUI("third_place", "1f949"),
        EmojiUI("football", "26bd"),
        EmojiUI("baseball", "26be"),
        EmojiUI("basketball", "1f3c0"),
        EmojiUI("volleyball", "1f3d0"),
        EmojiUI("american_football", "1f3c8"),
        EmojiUI("rugby", "1f3c9"),
        EmojiUI("tennis", "1f3be"),
        EmojiUI("strike", "1f3b3"),
        EmojiUI("cricket", "1f3cf"),
        EmojiUI("field_hockey", "1f3d1"),
        EmojiUI("ice_hockey", "1f3d2"),
        EmojiUI("ping_pong", "1f3d3"),
        EmojiUI("badminton", "1f3f8"),
        EmojiUI("boxing_glove", "1f94a"),
        EmojiUI("black_belt", "1f94b"),
        EmojiUI("gooooooooal", "1f945"),
        EmojiUI("hole_in_one", "26f3"),
        EmojiUI("ice_skate", "26f8"),
        EmojiUI("fishing", "1f3a3"),
        EmojiUI("running_shirt", "1f3bd"),
        EmojiUI("ski", "1f3bf"),
        EmojiUI("direct_hit", "1f3af"),
        EmojiUI("billiards", "1f3b1"),
        EmojiUI("crystal_ball", "1f52e"),
        EmojiUI("video_game", "1f3ae"),
        EmojiUI("joystick", "1f579"),
        EmojiUI("slot_machine", "1f3b0"),
        EmojiUI("dice", "1f3b2"),
        EmojiUI("spades", "2660"),
        EmojiUI("hearts", "2665"),
        EmojiUI("diamonds", "2666"),
        EmojiUI("clubs", "2663"),
        EmojiUI("joker", "1f0cf"),
        EmojiUI("mahjong", "1f004"),
        EmojiUI("playing_cards", "1f3b4"),
        EmojiUI("performing_arts", "1f3ad"),
        EmojiUI("picture", "1f5bc"),
        EmojiUI("art", "1f3a8"),

// Travel & Places
        EmojiUI("earth_africa", "1f30d"),
        EmojiUI("earth_americas", "1f30e"),
        EmojiUI("earth_asia", "1f30f"),
        EmojiUI("www", "1f310"),
        EmojiUI("map", "1f5fa"),
        EmojiUI("japan", "1f5fe"),
        EmojiUI("snowy_mountain", "1f3d4"),
        EmojiUI("mountain", "26f0"),
        EmojiUI("volcano", "1f30b"),
        EmojiUI("mount_fuji", "1f5fb"),
        EmojiUI("campsite", "1f3d5"),
        EmojiUI("beach", "1f3d6"),
        EmojiUI("desert", "1f3dc"),
        EmojiUI("island", "1f3dd"),
        EmojiUI("national_park", "1f3de"),
        EmojiUI("stadium", "1f3df"),
        EmojiUI("classical_building", "1f3db"),
        EmojiUI("construction", "1f3d7"),
        EmojiUI("houses", "1f3d8"),
        EmojiUI("derelict_house", "1f3da"),
        EmojiUI("house", "1f3e0"),
        EmojiUI("suburb", "1f3e1"),
        EmojiUI("office", "1f3e2"),
        EmojiUI("japan_post", "1f3e3"),
        EmojiUI("post_office", "1f3e4"),
        EmojiUI("hospital", "1f3e5"),
        EmojiUI("bank", "1f3e6"),
        EmojiUI("hotel", "1f3e8"),
        EmojiUI("love_hotel", "1f3e9"),
        EmojiUI("convenience_store", "1f3ea"),
        EmojiUI("school", "1f3eb"),
        EmojiUI("department_store", "1f3ec"),
        EmojiUI("factory", "1f3ed"),
        EmojiUI("shiro", "1f3ef"),
        EmojiUI("castle", "1f3f0"),
        EmojiUI("wedding", "1f492"),
        EmojiUI("tower", "1f5fc"),
        EmojiUI("statue", "1f5fd"),
        EmojiUI("church", "26ea"),
        EmojiUI("mosque", "1f54c"),
        EmojiUI("synagogue", "1f54d"),
        EmojiUI("shinto_shrine", "26e9"),
        EmojiUI("kaaba", "1f54b"),
        EmojiUI("fountain", "26f2"),
        EmojiUI("tent", "26fa"),
        EmojiUI("foggy", "1f301"),
        EmojiUI("night", "1f303"),
        EmojiUI("city", "1f3d9"),
        EmojiUI("mountain_sunrise", "1f304"),
        EmojiUI("sunrise", "1f305"),
        EmojiUI("sunset", "1f306"),
        EmojiUI("city_sunrise", "1f307"),
        EmojiUI("bridge", "1f309"),
        EmojiUI("hot_springs", "2668"),
        EmojiUI("carousel", "1f3a0"),
        EmojiUI("ferris_wheel", "1f3a1"),
        EmojiUI("roller_coaster", "1f3a2"),
        EmojiUI("barber", "1f488"),
        EmojiUI("circus", "1f3aa"),
        EmojiUI("train", "1f682"),
        EmojiUI("railway_car", "1f683"),
        EmojiUI("high_speed_train", "1f684"),
        EmojiUI("bullet_train", "1f685"),
        EmojiUI("oncoming_train", "1f686"),
        EmojiUI("subway", "1f687"),
        EmojiUI("light_rail", "1f688"),
        EmojiUI("station", "1f689"),
        EmojiUI("oncoming_tram", "1f68a"),
        EmojiUI("monorail", "1f69d"),
        EmojiUI("mountain_railway", "1f69e"),
        EmojiUI("tram", "1f68b"),
        EmojiUI("bus", "1f68c"),
        EmojiUI("oncoming_bus", "1f68d"),
        EmojiUI("trolley", "1f68e"),
        EmojiUI("minibus", "1f690"),
        EmojiUI("ambulance", "1f691"),
        EmojiUI("fire_truck", "1f692"),
        EmojiUI("police_car", "1f693"),
        EmojiUI("oncoming_police_car", "1f694"),
        EmojiUI("taxi", "1f695"),
        EmojiUI("oncoming_taxi", "1f696"),
        EmojiUI("car", "1f697"),
        EmojiUI("oncoming_car", "1f698"),
        EmojiUI("recreational_vehicle", "1f699"),
        EmojiUI("moving_truck", "1f69a"),
        EmojiUI("truck", "1f69b"),
        EmojiUI("tractor", "1f69c"),
        EmojiUI("racecar", "1f3ce"),
        EmojiUI("motorcycle", "1f3cd"),
        EmojiUI("scooter", "1f6f5"),
        EmojiUI("bike", "1f6b2"),
        EmojiUI("kick_scooter", "1f6f4"),
        EmojiUI("bus_stop", "1f68f"),
        EmojiUI("road", "1f6e3"),
        EmojiUI("railway_track", "1f6e4"),
        EmojiUI("oil_drum", "1f6e2"),
        EmojiUI("fuel_pump", "26fd"),
        EmojiUI("siren", "1f6a8"),
        EmojiUI("horizontal_traffic_light", "1f6a5"),
        EmojiUI("traffic_light", "1f6a6"),
        EmojiUI("stop_sign", "1f6d1"),
        EmojiUI("work_in_progress", "1f6a7"),
        EmojiUI("anchor", "2693"),
        EmojiUI("boat", "26f5"),
        EmojiUI("canoe", "1f6f6"),
        EmojiUI("speedboat", "1f6a4"),
        EmojiUI("passenger_ship", "1f6f3"),
        EmojiUI("ferry", "26f4"),
        EmojiUI("motor_boat", "1f6e5"),
        EmojiUI("ship", "1f6a2"),
        EmojiUI("airplane", "2708"),
        EmojiUI("small_airplane", "1f6e9"),
        EmojiUI("take_off", "1f6eb"),
        EmojiUI("landing", "1f6ec"),
        EmojiUI("seat", "1f4ba"),
        EmojiUI("helicopter", "1f681"),
        EmojiUI("suspension_railway", "1f69f"),
        EmojiUI("gondola", "1f6a0"),
        EmojiUI("aerial_tramway", "1f6a1"),
        EmojiUI("satellite", "1f6f0"),
        EmojiUI("rocket", "1f680"),
        EmojiUI("bellhop_bell", "1f6ce"),
        EmojiUI("times_up", "231b"),
        EmojiUI("time_ticking", "23f3"),
        EmojiUI("watch", "231a"),
        EmojiUI("alarm_clock", "23f0"),
        EmojiUI("stopwatch", "23f1"),
        EmojiUI("timer", "23f2"),
        EmojiUI("mantelpiece_clock", "1f570"),
        EmojiUI("time", "1f557"),
        EmojiUI("new_moon", "1f311"),
        EmojiUI("waxing_moon", "1f314"),
        EmojiUI("full_moon", "1f315"),
        EmojiUI("moon", "1f319"),
        EmojiUI("new_moon_face", "1f31a"),
        EmojiUI("goodnight", "1f31b"),
        EmojiUI("temperature", "1f321"),
        EmojiUI("sunny", "2600"),
        EmojiUI("moon_face", "1f31d"),
        EmojiUI("sun_face", "1f31e"),
        EmojiUI("star", "2b50"),
        EmojiUI("glowing_star", "1f31f"),
        EmojiUI("shooting_star", "1f320"),
        EmojiUI("milky_way", "1f30c"),
        EmojiUI("cloud", "2601"),
        EmojiUI("partly_sunny", "26c5"),
        EmojiUI("thunderstorm", "26c8"),
        EmojiUI("mostly_sunny", "1f324"),
        EmojiUI("cloudy", "1f325"),
        EmojiUI("sunshowers", "1f326"),
        EmojiUI("rainy", "1f327"),
        EmojiUI("snowy", "1f328"),
        EmojiUI("lightning", "1f329"),
        EmojiUI("tornado", "1f32a"),
        EmojiUI("fog", "1f32b"),
        EmojiUI("windy", "1f32c"),
        EmojiUI("cyclone", "1f300"),
        EmojiUI("rainbow", "1f308"),
        EmojiUI("closed_umbrella", "1f302"),
        EmojiUI("umbrella", "2602"),
        EmojiUI("umbrella_with_rain", "2614"),
        EmojiUI("beach_umbrella", "26f1"),
        EmojiUI("high_voltage", "26a1"),
        EmojiUI("snowflake", "2744"),
        EmojiUI("snowman", "2603"),
        EmojiUI("frosty", "26c4"),
        EmojiUI("comet", "2604"),
        EmojiUI("fire", "1f525"),
        EmojiUI("drop", "1f4a7"),
        EmojiUI("ocean", "1f30a"),

// Objects
        EmojiUI("glasses", "1f453"),
        EmojiUI("dark_sunglasses", "1f576"),
        EmojiUI("tie", "1f454"),
        EmojiUI("shirt", "1f455"),
        EmojiUI("jeans", "1f456"),
        EmojiUI("dress", "1f457"),
        EmojiUI("kimono", "1f458"),
        EmojiUI("bikini", "1f459"),
        EmojiUI("clothing", "1f45a"),
        EmojiUI("purse", "1f45b"),
        EmojiUI("handbag", "1f45c"),
        EmojiUI("pouch", "1f45d"),
        EmojiUI("shopping_bags", "1f6cd"),
        EmojiUI("backpack", "1f392"),
        EmojiUI("shoe", "1f45e"),
        EmojiUI("athletic_shoe", "1f45f"),
        EmojiUI("high_heels", "1f460"),
        EmojiUI("sandal", "1f461"),
        EmojiUI("boot", "1f462"),
        EmojiUI("crown", "1f451"),
        EmojiUI("hat", "1f452"),
        EmojiUI("top_hat", "1f3a9"),
        EmojiUI("graduate", "1f393"),
        EmojiUI("helmet", "26d1"),
        EmojiUI("prayer_beads", "1f4ff"),
        EmojiUI("lipstick", "1f484"),
        EmojiUI("ring", "1f48d"),
        EmojiUI("gem", "1f48e"),
        EmojiUI("mute", "1f507"),
        EmojiUI("speaker", "1f508"),
        EmojiUI("softer", "1f509"),
        EmojiUI("louder", "1f50a"),
        EmojiUI("loudspeaker", "1f4e2"),
        EmojiUI("megaphone", "1f4e3"),
        EmojiUI("horn", "1f4ef"),
        EmojiUI("notifications", "1f514"),
        EmojiUI("mute_notifications", "1f515"),
        EmojiUI("musical_score", "1f3bc"),
        EmojiUI("music", "1f3b5"),
        EmojiUI("musical_notes", "1f3b6"),
        EmojiUI("studio_microphone", "1f399"),
        EmojiUI("volume", "1f39a"),
        EmojiUI("control_knobs", "1f39b"),
        EmojiUI("microphone", "1f3a4"),
        EmojiUI("headphones", "1f3a7"),
        EmojiUI("radio", "1f4fb"),
        EmojiUI("saxophone", "1f3b7"),
        EmojiUI("guitar", "1f3b8"),
        EmojiUI("piano", "1f3b9"),
        EmojiUI("trumpet", "1f3ba"),
        EmojiUI("violin", "1f3bb"),
        EmojiUI("drum", "1f941"),
        EmojiUI("mobile_phone", "1f4f1"),
        EmojiUI("calling", "1f4f2"),
        EmojiUI("phone", "260e"),
        EmojiUI("landline", "1f4de"),
        EmojiUI("pager", "1f4df"),
        EmojiUI("fax", "1f4e0"),
        EmojiUI("battery", "1f50b"),
        EmojiUI("electric_plug", "1f50c"),
        EmojiUI("computer", "1f4bb"),
        EmojiUI("desktop_computer", "1f5a5"),
        EmojiUI("printer", "1f5a8"),
        EmojiUI("keyboard", "2328"),
        EmojiUI("computer_mouse", "1f5b1"),
        EmojiUI("trackball", "1f5b2"),
        EmojiUI("gold_record", "1f4bd"),
        EmojiUI("floppy_disk", "1f4be"),
        EmojiUI("cd", "1f4bf"),
        EmojiUI("dvd", "1f4c0"),
        EmojiUI("movie_camera", "1f3a5"),
        EmojiUI("film", "1f39e"),
        EmojiUI("projector", "1f4fd"),
        EmojiUI("action", "1f3ac"),
        EmojiUI("tv", "1f4fa"),
        EmojiUI("camera", "1f4f7"),
        EmojiUI("taking_a_picture", "1f4f8"),
        EmojiUI("video_camera", "1f4f9"),
        EmojiUI("vhs", "1f4fc"),
        EmojiUI("search", "1f50d"),
        EmojiUI("candle", "1f56f"),
        EmojiUI("light_bulb", "1f4a1"),
        EmojiUI("flashlight", "1f526"),
        EmojiUI("lantern", "1f3ee"),
        EmojiUI("decorative_notebook", "1f4d4"),
        EmojiUI("red_book", "1f4d5"),
        EmojiUI("book", "1f4d6"),
        EmojiUI("green_book", "1f4d7"),
        EmojiUI("blue_book", "1f4d8"),
        EmojiUI("orange_book", "1f4d9"),
        EmojiUI("books", "1f4da"),
        EmojiUI("notebook", "1f4d3"),
        EmojiUI("ledger", "1f4d2"),
        EmojiUI("receipt", "1f4c3"),
        EmojiUI("scroll", "1f4dc"),
        EmojiUI("document", "1f4c4"),
        EmojiUI("headlines", "1f4f0"),
        EmojiUI("newspaper", "1f5de"),
        EmojiUI("place_holder", "1f4d1"),
        EmojiUI("bookmark", "1f516"),
        EmojiUI("label", "1f3f7"),
        EmojiUI("money", "1f4b0"),
        EmojiUI("yen_banknotes", "1f4b4"),
        EmojiUI("dollar_bills", "1f4b5"),
        EmojiUI("euro_banknotes", "1f4b6"),
        EmojiUI("pound_notes", "1f4b7"),
        EmojiUI("losing_money", "1f4b8"),
        EmojiUI("credit_card", "1f4b3"),
        EmojiUI("stock_market", "1f4b9"),
        EmojiUI("email", "2709"),
        EmojiUI("e-mail", "1f4e7"),
        EmojiUI("mail_received", "1f4e8"),
        EmojiUI("mail_sent", "1f4e9"),
        EmojiUI("outbox", "1f4e4"),
        EmojiUI("inbox", "1f4e5"),
        EmojiUI("package", "1f4e6"),
        EmojiUI("mailbox", "1f4eb"),
        EmojiUI("closed_mailbox", "1f4ea"),
        EmojiUI("unread_mail", "1f4ec"),
        EmojiUI("inbox_zero", "1f4ed"),
        EmojiUI("mail_dropoff", "1f4ee"),
        EmojiUI("ballot_box", "1f5f3"),
        EmojiUI("pencil", "270f"),
        EmojiUI("fountain_pen", "1f58b"),
        EmojiUI("pen", "1f58a"),
        EmojiUI("paintbrush", "1f58c"),
        EmojiUI("crayon", "1f58d"),
        EmojiUI("memo", "1f4dd"),
        EmojiUI("briefcase", "1f4bc"),
        EmojiUI("organize", "1f4c1"),
        EmojiUI("folder", "1f4c2"),
        EmojiUI("sort", "1f5c2"),
        EmojiUI("calendar", "1f4c5"),
        EmojiUI("date", "1f4c6"),
        EmojiUI("spiral_notepad", "1f5d2"),
        EmojiUI("rolodex", "1f4c7"),
        EmojiUI("chart", "1f4c8"),
        EmojiUI("downwards_trend", "1f4c9"),
        EmojiUI("bar_chart", "1f4ca"),
        EmojiUI("clipboard", "1f4cb"),
        EmojiUI("push_pin", "1f4cc"),
        EmojiUI("pin", "1f4cd"),
        EmojiUI("paperclip", "1f4ce"),
        EmojiUI("office_supplies", "1f587"),
        EmojiUI("ruler", "1f4cf"),
        EmojiUI("carpenter_square", "1f4d0"),
        EmojiUI("scissors", "2702"),
        EmojiUI("archive", "1f5c3"),
        EmojiUI("file_cabinet", "1f5c4"),
        EmojiUI("wastebasket", "1f5d1"),
        EmojiUI("locked", "1f512"),
        EmojiUI("unlocked", "1f513"),
        EmojiUI("privacy", "1f50f"),
        EmojiUI("secure", "1f510"),
        EmojiUI("key", "1f511"),
        EmojiUI("secret", "1f5dd"),
        EmojiUI("hammer", "1f528"),
        EmojiUI("mine", "26cf"),
        EmojiUI("at_work", "2692"),
        EmojiUI("working_on_it", "1f6e0"),
        EmojiUI("dagger", "1f5e1"),
        EmojiUI("duel", "2694"),
        EmojiUI("gun", "1f52b"),
        EmojiUI("bow_and_arrow", "1f3f9"),
        EmojiUI("shield", "1f6e1"),
        EmojiUI("fixing", "1f527"),
        EmojiUI("nut_and_bolt", "1f529"),
        EmojiUI("gear", "2699"),
        EmojiUI("compression", "1f5dc"),
        EmojiUI("justice", "2696"),
        EmojiUI("link", "1f517"),
        EmojiUI("chains", "26d3"),
        EmojiUI("alchemy", "2697"),
        EmojiUI("science", "1f52c"),
        EmojiUI("telescope", "1f52d"),
        EmojiUI("satellite_antenna", "1f4e1"),
        EmojiUI("injection", "1f489"),
        EmojiUI("medicine", "1f48a"),
        EmojiUI("door", "1f6aa"),
        EmojiUI("bed", "1f6cf"),
        EmojiUI("living_room", "1f6cb"),
        EmojiUI("toilet", "1f6bd"),
        EmojiUI("shower", "1f6bf"),
        EmojiUI("bathtub", "1f6c1"),
        EmojiUI("shopping_cart", "1f6d2"),
        EmojiUI("smoking", "1f6ac"),
        EmojiUI("coffin", "26b0"),
        EmojiUI("funeral_urn", "26b1"),
        EmojiUI("rock_carving", "1f5ff"),

// Symbols
        EmojiUI("atm", "1f3e7"),
        EmojiUI("put_litter_in_its_place", "1f6ae"),
        EmojiUI("potable_water", "1f6b0"),
        EmojiUI("accessible", "267f"),
        EmojiUI("mens", "1f6b9"),
        EmojiUI("womens", "1f6ba"),
        EmojiUI("restroom", "1f6bb"),
        EmojiUI("baby_change_station", "1f6bc"),
        EmojiUI("wc", "1f6be"),
        EmojiUI("passport_control", "1f6c2"),
        EmojiUI("customs", "1f6c3"),
        EmojiUI("baggage_claim", "1f6c4"),
        EmojiUI("locker", "1f6c5"),
        EmojiUI("warning", "26a0"),
        EmojiUI("children_crossing", "1f6b8"),
        EmojiUI("no_entry", "26d4"),
        EmojiUI("prohibited", "1f6ab"),
        EmojiUI("no_bicycles", "1f6b3"),
        EmojiUI("no_smoking", "1f6ad"),
        EmojiUI("do_not_litter", "1f6af"),
        EmojiUI("non-potable_water", "1f6b1"),
        EmojiUI("no_pedestrians", "1f6b7"),
        EmojiUI("no_phones", "1f4f5"),
        EmojiUI("underage", "1f51e"),
        EmojiUI("radioactive", "2622"),
        EmojiUI("biohazard", "2623"),
        EmojiUI("up", "2b06"),
        EmojiUI("upper_right", "2197"),
        EmojiUI("right", "27a1"),
        EmojiUI("lower_right", "2198"),
        EmojiUI("down", "2b07"),
        EmojiUI("lower_left", "2199"),
        EmojiUI("left", "2b05"),
        EmojiUI("upper_left", "2196"),
        EmojiUI("up_down", "2195"),
        EmojiUI("left_right", "2194"),
        EmojiUI("reply", "21a9"),
        EmojiUI("forward", "21aa"),
        EmojiUI("heading_up", "2934"),
        EmojiUI("heading_down", "2935"),
        EmojiUI("clockwise", "1f503"),
        EmojiUI("counterclockwise", "1f504"),
        EmojiUI("back", "1f519"),
        EmojiUI("end", "1f51a"),
        EmojiUI("on", "1f51b"),
        EmojiUI("soon", "1f51c"),
        EmojiUI("top", "1f51d"),
        EmojiUI("place_of_worship", "1f6d0"),
        EmojiUI("atom", "269b"),
        EmojiUI("om", "1f549"),
        EmojiUI("star_of_david", "2721"),
        EmojiUI("wheel_of_dharma", "2638"),
        EmojiUI("yin_yang", "262f"),
        EmojiUI("cross", "271d"),
        EmojiUI("orthodox_cross", "2626"),
        EmojiUI("star_and_crescent", "262a"),
        EmojiUI("peace", "262e"),
        EmojiUI("menorah", "1f54e"),
        EmojiUI("aries", "2648"),
        EmojiUI("taurus", "2649"),
        EmojiUI("gemini", "264a"),
        EmojiUI("cancer", "264b"),
        EmojiUI("leo", "264c"),
        EmojiUI("virgo", "264d"),
        EmojiUI("libra", "264e"),
        EmojiUI("scorpius", "264f"),
        EmojiUI("sagittarius", "2650"),
        EmojiUI("capricorn", "2651"),
        EmojiUI("aquarius", "2652"),
        EmojiUI("pisces", "2653"),
        EmojiUI("ophiuchus", "26ce"),
        EmojiUI("shuffle", "1f500"),
        EmojiUI("repeat", "1f501"),
        EmojiUI("repeat_one", "1f502"),
        EmojiUI("play", "25b6"),
        EmojiUI("fast_forward", "23e9"),
        EmojiUI("next_track", "23ed"),
        EmojiUI("play_pause", "23ef"),
        EmojiUI("play_reverse", "25c0"),
        EmojiUI("rewind", "23ea"),
        EmojiUI("previous_track", "23ee"),
        EmojiUI("upvote", "1f53c"),
        EmojiUI("double_up", "23eb"),
        EmojiUI("downvote", "1f53d"),
        EmojiUI("double_down", "23ec"),
        EmojiUI("pause", "23f8"),
        EmojiUI("stop_button", "23f9"),
        EmojiUI("record", "23fa"),
        EmojiUI("cinema", "1f3a6"),
        EmojiUI("low_brightness", "1f505"),
        EmojiUI("brightness", "1f506"),
        EmojiUI("cell_reception", "1f4f6"),
        EmojiUI("vibration_mode", "1f4f3"),
        EmojiUI("phone_off", "1f4f4"),
        EmojiUI("multiplication", "2716"),
        EmojiUI("plus", "2795"),
        EmojiUI("minus", "2796"),
        EmojiUI("division", "2797"),
        EmojiUI("bangbang", "203c"),
        EmojiUI("interrobang", "2049"),
        EmojiUI("question", "2753"),
        EmojiUI("grey_question", "2754"),
        EmojiUI("grey_exclamation", "2755"),
        EmojiUI("exclamation", "2757"),
        EmojiUI("wavy_dash", "3030"),
        EmojiUI("exchange", "1f4b1"),
        EmojiUI("dollars", "1f4b2"),
        EmojiUI("recycle", "267b"),
        EmojiUI("fleur_de_lis", "269c"),
        EmojiUI("trident", "1f531"),
        EmojiUI("name_badge", "1f4db"),
        EmojiUI("beginner", "1f530"),
        EmojiUI("circle", "2b55"),
        EmojiUI("check", "2705"),
        EmojiUI("checkbox", "2611"),
        EmojiUI("check_mark", "2714"),
        EmojiUI("cross_mark", "274c"),
        EmojiUI("x", "274e"),
        EmojiUI("loop", "27b0"),
        EmojiUI("double_loop", "27bf"),
        EmojiUI("part_alternation", "303d"),
        EmojiUI("eight_spoked_asterisk", "2733"),
        EmojiUI("eight_pointed_star", "2734"),
        EmojiUI("sparkle", "2747"),
        EmojiUI("tm", "2122"),
        EmojiUI("hash", "0023"),
        EmojiUI("asterisk", "002a"),
        EmojiUI("zero", "0030"),
        EmojiUI("one", "0031"),
        EmojiUI("two", "0032"),
        EmojiUI("three", "0033"),
        EmojiUI("four", "0034"),
        EmojiUI("five", "0035"),
        EmojiUI("six", "0036"),
        EmojiUI("seven", "0037"),
        EmojiUI("eight", "0038"),
        EmojiUI("nine", "0039"),
        EmojiUI("ten", "1f51f"),
        EmojiUI("capital_abcd", "1f520"),
        EmojiUI("abcd", "1f521"),
        EmojiUI("1234", "1f522"),
        EmojiUI("symbols", "1f523"),
        EmojiUI("abc", "1f524"),
        EmojiUI("a", "1f170"),
        EmojiUI("ab", "1f18e"),
        EmojiUI("b", "1f171"),
        EmojiUI("cl", "1f191"),
        EmojiUI("cool", "1f192"),
        EmojiUI("free", "1f193"),
        EmojiUI("info", "2139"),
        EmojiUI("id", "1f194"),
        EmojiUI("metro", "24c2"),
        EmojiUI("new", "1f195"),
        EmojiUI("ng", "1f196"),
        EmojiUI("o", "1f17e"),
        EmojiUI("squared_ok", "1f197"),
        EmojiUI("parking", "1f17f"),
        EmojiUI("sos", "1f198"),
        EmojiUI("squared_up", "1f199"),
        EmojiUI("vs", "1f19a"),
        EmojiUI("red_circle", "1f534"),
        EmojiUI("blue_circle", "1f535"),
        EmojiUI("black_circle", "26ab"),
        EmojiUI("white_circle", "26aa"),
        EmojiUI("black_large_square", "2b1b"),
        EmojiUI("white_large_square", "2b1c"),
        EmojiUI("black_medium_square", "25fc"),
        EmojiUI("white_medium_square", "25fb"),
        EmojiUI("black_medium_small_square", "25fe"),
        EmojiUI("white_medium_small_square", "25fd"),
        EmojiUI("black_small_square", "25aa"),
        EmojiUI("white_small_square", "25ab"),
        EmojiUI("large_orange_diamond", "1f536"),
        EmojiUI("large_blue_diamond", "1f537"),
        EmojiUI("small_orange_diamond", "1f538"),
        EmojiUI("small_blue_diamond", "1f539"),
        EmojiUI("red_triangle_up", "1f53a"),
        EmojiUI("red_triangle_down", "1f53b"),
        EmojiUI("cute", "1f4a0"),
        EmojiUI("radio_button", "1f518"),
        EmojiUI("black_and_white_square", "1f533"),
        EmojiUI("white_and_black_square", "1f532"),

// Flags
        EmojiUI("checkered_flag", "1f3c1"),
        EmojiUI("triangular_flag", "1f6a9"),
        EmojiUI("crossed_flags", "1f38c"),
        EmojiUI("black_flag", "1f3f4"),
        EmojiUI("white_flag", "1f3f3")
    )
}