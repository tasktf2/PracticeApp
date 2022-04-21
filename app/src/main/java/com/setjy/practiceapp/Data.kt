package com.setjy.practiceapp

import com.setjy.practiceapp.recycler.base.ViewTyped
import com.setjy.practiceapp.recycler.items.EmojiUI
import com.setjy.practiceapp.recycler.items.IncomingMessageUI
import com.setjy.practiceapp.recycler.items.OutgoingMessageUI
import com.setjy.practiceapp.recycler.items.TimeUI

const val DEFAULT_USER_ID: String = "1"

const val DEFAULT_INCOMING_USER_ID: String = "2"

data class EmojiRemote(
    val code: String,
    val userId: String
)

object Data {

    private val listOfRemoteReactions = listOf(
        EmojiRemote("1f600", DEFAULT_USER_ID),
        EmojiRemote("1f600", "3"),
        EmojiRemote("1f600", "2"),

        EmojiRemote("1f603", DEFAULT_USER_ID),
        EmojiRemote("1f603", "3"),

        EmojiRemote("1f604", "4"),
        EmojiRemote("1f601", "5"),
        EmojiRemote("1f606", "6"),
        EmojiRemote("1f605", DEFAULT_USER_ID)
    )
    private val listOfReactions = listOfRemoteReactions.map {
        EmojiUI(
            code = it.code,
            isSelected = it.userId == DEFAULT_USER_ID
        )
    }

    fun getMessages(): List<ViewTyped> =
        listOf(
            TimeUI(System.currentTimeMillis()),
            OutgoingMessageUI(
                message = "getString(R.string.test_message_text)",
                messageId = "2",
                reactions = listOfReactions
            ),
            IncomingMessageUI(
                messageId = "1",
                avatar = R.drawable.ic_launcher_background,
                username = "Denis Mashkov",
                message = "getString(R.string.test_message_text)",
                reactions = listOfReactions
            )

        ).asReversed()

    val emojiUISet = listOf(
        // Smileys & Emotion
        EmojiUI("1f600"),
        EmojiUI("1f603"),
        EmojiUI("1f604"),
        EmojiUI("1f601"),
        EmojiUI("1f606"),
        EmojiUI("1f605"),
        EmojiUI("1f923"),
        EmojiUI("1f602"),
        EmojiUI("1f642"),
        EmojiUI("1f643"),
        EmojiUI("1f609"),
        EmojiUI("1f60a"),
        EmojiUI("1f607"),
        EmojiUI("1f60d"),
        EmojiUI("1f618"),
        EmojiUI("1f617"),
        EmojiUI("263a"),
        EmojiUI("1f61a"),
        EmojiUI("1f619"),
        EmojiUI("1f60b"),
        EmojiUI("1f61b"),
        EmojiUI("1f61c"),
        EmojiUI("1f61d"),
        EmojiUI("1f911"),
        EmojiUI("1f917"),
        EmojiUI("1f914"),
        EmojiUI("1f910"),
        EmojiUI("1f610"),
        EmojiUI("1f611"),
        EmojiUI("1f636"),
        EmojiUI("1f60f"),
        EmojiUI("1f612"),
        EmojiUI("1f644"),
        EmojiUI("1f62c"),
        EmojiUI("1f925"),
        EmojiUI("1f60c"),
        EmojiUI("1f614"),
        EmojiUI("1f62a"),
        EmojiUI("1f924"),
        EmojiUI("1f634"),
        EmojiUI("1f637"),
        EmojiUI("1f912"),
        EmojiUI("1f915"),
        EmojiUI("1f922"),
        EmojiUI("1f927"),
        EmojiUI("1f635"),
        EmojiUI("1f920"),
        EmojiUI("1f60e"),
        EmojiUI("1f913"),
        EmojiUI("1f615"),
        EmojiUI("1f61f"),
        EmojiUI("1f641"),
        EmojiUI("2639"),
        EmojiUI("1f62e"),
        EmojiUI("1f62f"),
        EmojiUI("1f632"),
        EmojiUI("1f633"),
        EmojiUI("1f626"),
        EmojiUI("1f627"),
        EmojiUI("1f628"),
        EmojiUI("1f630"),
        EmojiUI("1f625"),
        EmojiUI("1f622"),
        EmojiUI("1f62d"),
        EmojiUI("1f631"),
        EmojiUI("1f616"),
        EmojiUI("1f623"),
        EmojiUI("1f61e"),
        EmojiUI("1f613"),
        EmojiUI("1f629"),
        EmojiUI("1f62b"),
        EmojiUI("1f624"),
        EmojiUI("1f621"),
        EmojiUI("1f620"),
        EmojiUI("1f608"),
        EmojiUI("1f47f"),
        EmojiUI("1f480"),
        EmojiUI("2620"),
        EmojiUI("1f4a9"),
        EmojiUI("1f921"),
        EmojiUI("1f479"),
        EmojiUI("1f47a"),
        EmojiUI("1f47b"),
        EmojiUI("1f47d"),
        EmojiUI("1f47e"),
        EmojiUI("1f916"),
        EmojiUI("1f63a"),
        EmojiUI("1f638"),
        EmojiUI("1f639"),
        EmojiUI("1f63b"),
        EmojiUI("1f63c"),
        EmojiUI("1f63d"),
        EmojiUI("1f640"),
        EmojiUI("1f63f"),
        EmojiUI("1f63e"),
        EmojiUI("1f648"),
        EmojiUI("1f649"),
        EmojiUI("1f64a"),
        EmojiUI("1f48b"),
        EmojiUI("1f48c"),
        EmojiUI("1f498"),
        EmojiUI("1f49d"),
        EmojiUI("1f496"),
        EmojiUI("1f497"),
        EmojiUI("1f493"),
        EmojiUI("1f49e"),
        EmojiUI("1f495"),
        EmojiUI("1f49f"),
        EmojiUI("2763"),
        EmojiUI("1f494"),
        EmojiUI("2764"),
        EmojiUI("1f49b"),
        EmojiUI("1f49a"),
        EmojiUI("1f499"),
        EmojiUI("1f49c"),
        EmojiUI("1f5a4"),
        EmojiUI("1f4af"),
        EmojiUI("1f4a2"),
        EmojiUI("1f4a5"),
        EmojiUI("1f4ab"),
        EmojiUI("1f4a6"),
        EmojiUI("1f4a8"),
        EmojiUI("1f573"),
        EmojiUI("1f4a3"),
        EmojiUI("1f4ac"),
        EmojiUI("1f5e8"),
        EmojiUI("1f5ef"),
        EmojiUI("1f4ad"),
        EmojiUI("1f4a4"),

// People & Body
        EmojiUI("1f44b"),
        EmojiUI("1f91a"),
        EmojiUI("1f590"),
        EmojiUI("270b"),
        EmojiUI("1f596"),
        EmojiUI("1f44c"),
        EmojiUI("270c"),
        EmojiUI("1f91e"),
        EmojiUI("1f918"),
        EmojiUI("1f919"),
        EmojiUI("1f448"),
        EmojiUI("1f449"),
        EmojiUI("1f446"),
        EmojiUI("1f595"),
        EmojiUI("1f447"),
        EmojiUI("261d"),
        EmojiUI("1f44d"),
        EmojiUI("1f44e"),
        EmojiUI("270a"),
        EmojiUI("1f44a"),
        EmojiUI("1f91b"),
        EmojiUI("1f91c"),
        EmojiUI("1f44f"),
        EmojiUI("1f64c"),
        EmojiUI("1f450"),
        EmojiUI("1f91d"),
        EmojiUI("1f64f"),
        EmojiUI("270d"),
        EmojiUI("1f485"),
        EmojiUI("1f933"),
        EmojiUI("1f4aa"),
        EmojiUI("1f442"),
        EmojiUI("1f443"),
        EmojiUI("1f440"),
        EmojiUI("1f441"),
        EmojiUI("1f445"),
        EmojiUI("1f444"),
        EmojiUI("1f476"),
        EmojiUI("1f466"),
        EmojiUI("1f467"),
        EmojiUI("1f468"),
        EmojiUI("1f469"),
        EmojiUI("1f474"),
        EmojiUI("1f475"),
        EmojiUI("1f64d"),
        EmojiUI("1f64e"),
        EmojiUI("1f645"),
        EmojiUI("1f646"),
        EmojiUI("1f481"),
        EmojiUI("1f64b"),
        EmojiUI("1f647"),
        EmojiUI("1f926"),
        EmojiUI("1f937"),
        EmojiUI("1f46e"),
        EmojiUI("1f575"),
        EmojiUI("1f482"),
        EmojiUI("1f477"),
        EmojiUI("1f934"),
        EmojiUI("1f478"),
        EmojiUI("1f473"),
        EmojiUI("1f472"),
        EmojiUI("1f470"),
        EmojiUI("1f930"),
        EmojiUI("1f47c"),
        EmojiUI("1f385"),
        EmojiUI("1f936"),
        EmojiUI("1f486"),
        EmojiUI("1f487"),
        EmojiUI("1f6b6"),
        EmojiUI("1f3c3"),
        EmojiUI("1f483"),
        EmojiUI("1f57a"),
        EmojiUI("1f574"),
        EmojiUI("1f46f"),
        EmojiUI("1f93a"),
        EmojiUI("1f3c7"),
        EmojiUI("26f7"),
        EmojiUI("1f3c2"),
        EmojiUI("1f3cc"),
        EmojiUI("1f3c4"),
        EmojiUI("1f6a3"),
        EmojiUI("1f3ca"),
        EmojiUI("26f9"),
        EmojiUI("1f3cb"),
        EmojiUI("1f6b4"),
        EmojiUI("1f6b5"),
        EmojiUI("1f938"),
        EmojiUI("1f93c"),
        EmojiUI("1f93d"),
        EmojiUI("1f93e"),
        EmojiUI("1f939"),
        EmojiUI("1f6c0"),
        EmojiUI("1f6cc"),
        EmojiUI("1f46d"),
        EmojiUI("1f46b"),
        EmojiUI("1f46c"),
        EmojiUI("1f46a"),
        EmojiUI("1f5e3"),
        EmojiUI("1f464"),
        EmojiUI("1f465"),
        EmojiUI("1f463"),
        EmojiUI("1f935"),

// Animals & Nature
        EmojiUI("1f435"),
        EmojiUI("1f412"),
        EmojiUI("1f98d"),
        EmojiUI("1f436"),
        EmojiUI("1f415"),
        EmojiUI("1f429"),
        EmojiUI("1f43a"),
        EmojiUI("1f98a"),
        EmojiUI("1f431"),
        EmojiUI("1f408"),
        EmojiUI("1f981"),
        EmojiUI("1f42f"),
        EmojiUI("1f405"),
        EmojiUI("1f406"),
        EmojiUI("1f434"),
        EmojiUI("1f40e"),
        EmojiUI("1f984"),
        EmojiUI("1f98c"),
        EmojiUI("1f42e"),
        EmojiUI("1f402"),
        EmojiUI("1f403"),
        EmojiUI("1f404"),
        EmojiUI("1f437"),
        EmojiUI("1f416"),
        EmojiUI("1f417"),
        EmojiUI("1f43d"),
        EmojiUI("1f40f"),
        EmojiUI("1f411"),
        EmojiUI("1f410"),
        EmojiUI("1f42a"),
        EmojiUI("1f42b"),
        EmojiUI("1f418"),
        EmojiUI("1f98f"),
        EmojiUI("1f42d"),
        EmojiUI("1f401"),
        EmojiUI("1f400"),
        EmojiUI("1f439"),
        EmojiUI("1f430"),
        EmojiUI("1f407"),
        EmojiUI("1f43f"),
        EmojiUI("1f987"),
        EmojiUI("1f43b"),
        EmojiUI("1f428"),
        EmojiUI("1f43c"),
        EmojiUI("1f43e"),
        EmojiUI("1f983"),
        EmojiUI("1f414"),
        EmojiUI("1f413"),
        EmojiUI("1f423"),
        EmojiUI("1f424"),
        EmojiUI("1f425"),
        EmojiUI("1f426"),
        EmojiUI("1f427"),
        EmojiUI("1f54a"),
        EmojiUI("1f985"),
        EmojiUI("1f986"),
        EmojiUI("1f989"),
        EmojiUI("1f438"),
        EmojiUI("1f40a"),
        EmojiUI("1f422"),
        EmojiUI("1f98e"),
        EmojiUI("1f40d"),
        EmojiUI("1f432"),
        EmojiUI("1f409"),
        EmojiUI("1f433"),
        EmojiUI("1f40b"),
        EmojiUI("1f42c"),
        EmojiUI("1f41f"),
        EmojiUI("1f420"),
        EmojiUI("1f421"),
        EmojiUI("1f988"),
        EmojiUI("1f419"),
        EmojiUI("1f41a"),
        EmojiUI("1f40c"),
        EmojiUI("1f98b"),
        EmojiUI("1f41b"),
        EmojiUI("1f41c"),
        EmojiUI("1f41d"),
        EmojiUI("1f577"),
        EmojiUI("1f578"),
        EmojiUI("1f982"),
        EmojiUI("1f490"),
        EmojiUI("1f338"),
        EmojiUI("1f4ae"),
        EmojiUI("1f3f5"),
        EmojiUI("1f339"),
        EmojiUI("1f940"),
        EmojiUI("1f33a"),
        EmojiUI("1f33b"),
        EmojiUI("1f33c"),
        EmojiUI("1f337"),
        EmojiUI("1f331"),
        EmojiUI("1f332"),
        EmojiUI("1f333"),
        EmojiUI("1f334"),
        EmojiUI("1f335"),
        EmojiUI("1f33e"),
        EmojiUI("1f33f"),
        EmojiUI("2618"),
        EmojiUI("1f340"),
        EmojiUI("1f341"),
        EmojiUI("1f342"),
        EmojiUI("1f343"),
        EmojiUI("1f41e"),

// Food & Drink
        EmojiUI("1f347"),
        EmojiUI("1f348"),
        EmojiUI("1f349"),
        EmojiUI("1f34a"),
        EmojiUI("1f34b"),
        EmojiUI("1f34c"),
        EmojiUI("1f34d"),
        EmojiUI("1f34e"),
        EmojiUI("1f34f"),
        EmojiUI("1f350"),
        EmojiUI("1f351"),
        EmojiUI("1f352"),
        EmojiUI("1f353"),
        EmojiUI("1f95d"),
        EmojiUI("1f345"),
        EmojiUI("1f951"),
        EmojiUI("1f346"),
        EmojiUI("1f954"),
        EmojiUI("1f955"),
        EmojiUI("1f33d"),
        EmojiUI("1f336"),
        EmojiUI("1f952"),
        EmojiUI("1f344"),
        EmojiUI("1f95c"),
        EmojiUI("1f330"),
        EmojiUI("1f35e"),
        EmojiUI("1f950"),
        EmojiUI("1f956"),
        EmojiUI("1f95e"),
        EmojiUI("1f9c0"),
        EmojiUI("1f356"),
        EmojiUI("1f357"),
        EmojiUI("1f953"),
        EmojiUI("1f354"),
        EmojiUI("1f35f"),
        EmojiUI("1f355"),
        EmojiUI("1f32d"),
        EmojiUI("1f32e"),
        EmojiUI("1f32f"),
        EmojiUI("1f959"),
        EmojiUI("1f95a"),
        EmojiUI("1f373"),
        EmojiUI("1f958"),
        EmojiUI("1f372"),
        EmojiUI("1f957"),
        EmojiUI("1f37f"),
        EmojiUI("1f371"),
        EmojiUI("1f358"),
        EmojiUI("1f359"),
        EmojiUI("1f35a"),
        EmojiUI("1f35b"),
        EmojiUI("1f35c"),
        EmojiUI("1f35d"),
        EmojiUI("1f360"),
        EmojiUI("1f362"),
        EmojiUI("1f363"),
        EmojiUI("1f364"),
        EmojiUI("1f365"),
        EmojiUI("1f361"),
        EmojiUI("1f980"),
        EmojiUI("1f990"),
        EmojiUI("1f991"),
        EmojiUI("1f366"),
        EmojiUI("1f367"),
        EmojiUI("1f368"),
        EmojiUI("1f369"),
        EmojiUI("1f36a"),
        EmojiUI("1f382"),
        EmojiUI("1f370"),
        EmojiUI("1f36b"),
        EmojiUI("1f36c"),
        EmojiUI("1f36d"),
        EmojiUI("1f36e"),
        EmojiUI("1f36f"),
        EmojiUI("1f37c"),
        EmojiUI("1f95b"),
        EmojiUI("2615"),
        EmojiUI("1f375"),
        EmojiUI("1f376"),
        EmojiUI("1f37e"),
        EmojiUI("1f377"),
        EmojiUI("1f378"),
        EmojiUI("1f379"),
        EmojiUI("1f37a"),
        EmojiUI("1f37b"),
        EmojiUI("1f942"),
        EmojiUI("1f943"),
        EmojiUI("1f37d"),
        EmojiUI("1f374"),
        EmojiUI("1f944"),
        EmojiUI("1f52a"),
        EmojiUI("1f3fa"),

// Activities
        EmojiUI("1f383"),
        EmojiUI("1f384"),
        EmojiUI("1f386"),
        EmojiUI("1f387"),
        EmojiUI("2728"),
        EmojiUI("1f388"),
        EmojiUI("1f389"),
        EmojiUI("1f38a"),
        EmojiUI("1f38b"),
        EmojiUI("1f38d"),
        EmojiUI("1f38e"),
        EmojiUI("1f38f"),
        EmojiUI("1f390"),
        EmojiUI("1f391"),
        EmojiUI("1f380"),
        EmojiUI("1f381"),
        EmojiUI("1f397"),
        EmojiUI("1f39f"),
        EmojiUI("1f3ab"),
        EmojiUI("1f396"),
        EmojiUI("1f3c6"),
        EmojiUI("1f3c5"),
        EmojiUI("1f947"),
        EmojiUI("1f948"),
        EmojiUI("1f949"),
        EmojiUI("26bd"),
        EmojiUI("26be"),
        EmojiUI("1f3c0"),
        EmojiUI("1f3d0"),
        EmojiUI("1f3c8"),
        EmojiUI("1f3c9"),
        EmojiUI("1f3be"),
        EmojiUI("1f3b3"),
        EmojiUI("1f3cf"),
        EmojiUI("1f3d1"),
        EmojiUI("1f3d2"),
        EmojiUI("1f3d3"),
        EmojiUI("1f3f8"),
        EmojiUI("1f94a"),
        EmojiUI("1f94b"),
        EmojiUI("1f945"),
        EmojiUI("26f3"),
        EmojiUI("26f8"),
        EmojiUI("1f3a3"),
        EmojiUI("1f3bd"),
        EmojiUI("1f3bf"),
        EmojiUI("1f3af"),
        EmojiUI("1f3b1"),
        EmojiUI("1f52e"),
        EmojiUI("1f3ae"),
        EmojiUI("1f579"),
        EmojiUI("1f3b0"),
        EmojiUI("1f3b2"),
        EmojiUI("2660"),
        EmojiUI("2665"),
        EmojiUI("2666"),
        EmojiUI("2663"),
        EmojiUI("1f0cf"),
        EmojiUI("1f004"),
        EmojiUI("1f3b4"),
        EmojiUI("1f3ad"),
        EmojiUI("1f5bc"),
        EmojiUI("1f3a8"),

// Travel & Places
        EmojiUI("1f30d"),
        EmojiUI("1f30e"),
        EmojiUI("1f30f"),
        EmojiUI("1f310"),
        EmojiUI("1f5fa"),
        EmojiUI("1f5fe"),
        EmojiUI("1f3d4"),
        EmojiUI("26f0"),
        EmojiUI("1f30b"),
        EmojiUI("1f5fb"),
        EmojiUI("1f3d5"),
        EmojiUI("1f3d6"),
        EmojiUI("1f3dc"),
        EmojiUI("1f3dd"),
        EmojiUI("1f3de"),
        EmojiUI("1f3df"),
        EmojiUI("1f3db"),
        EmojiUI("1f3d7"),
        EmojiUI("1f3d8"),
        EmojiUI("1f3da"),
        EmojiUI("1f3e0"),
        EmojiUI("1f3e1"),
        EmojiUI("1f3e2"),
        EmojiUI("1f3e3"),
        EmojiUI("1f3e4"),
        EmojiUI("1f3e5"),
        EmojiUI("1f3e6"),
        EmojiUI("1f3e8"),
        EmojiUI("1f3e9"),
        EmojiUI("1f3ea"),
        EmojiUI("1f3eb"),
        EmojiUI("1f3ec"),
        EmojiUI("1f3ed"),
        EmojiUI("1f3ef"),
        EmojiUI("1f3f0"),
        EmojiUI("1f492"),
        EmojiUI("1f5fc"),
        EmojiUI("1f5fd"),
        EmojiUI("26ea"),
        EmojiUI("1f54c"),
        EmojiUI("1f54d"),
        EmojiUI("26e9"),
        EmojiUI("1f54b"),
        EmojiUI("26f2"),
        EmojiUI("26fa"),
        EmojiUI("1f301"),
        EmojiUI("1f303"),
        EmojiUI("1f3d9"),
        EmojiUI("1f304"),
        EmojiUI("1f305"),
        EmojiUI("1f306"),
        EmojiUI("1f307"),
        EmojiUI("1f309"),
        EmojiUI("2668"),
        EmojiUI("1f3a0"),
        EmojiUI("1f3a1"),
        EmojiUI("1f3a2"),
        EmojiUI("1f488"),
        EmojiUI("1f3aa"),
        EmojiUI("1f682"),
        EmojiUI("1f683"),
        EmojiUI("1f684"),
        EmojiUI("1f685"),
        EmojiUI("1f686"),
        EmojiUI("1f687"),
        EmojiUI("1f688"),
        EmojiUI("1f689"),
        EmojiUI("1f68a"),
        EmojiUI("1f69d"),
        EmojiUI("1f69e"),
        EmojiUI("1f68b"),
        EmojiUI("1f68c"),
        EmojiUI("1f68d"),
        EmojiUI("1f68e"),
        EmojiUI("1f690"),
        EmojiUI("1f691"),
        EmojiUI("1f692"),
        EmojiUI("1f693"),
        EmojiUI("1f694"),
        EmojiUI("1f695"),
        EmojiUI("1f696"),
        EmojiUI("1f697"),
        EmojiUI("1f698"),
        EmojiUI("1f699"),
        EmojiUI("1f69a"),
        EmojiUI("1f69b"),
        EmojiUI("1f69c"),
        EmojiUI("1f3ce"),
        EmojiUI("1f3cd"),
        EmojiUI("1f6f5"),
        EmojiUI("1f6b2"),
        EmojiUI("1f6f4"),
        EmojiUI("1f68f"),
        EmojiUI("1f6e3"),
        EmojiUI("1f6e4"),
        EmojiUI("1f6e2"),
        EmojiUI("26fd"),
        EmojiUI("1f6a8"),
        EmojiUI("1f6a5"),
        EmojiUI("1f6a6"),
        EmojiUI("1f6d1"),
        EmojiUI("1f6a7"),
        EmojiUI("2693"),
        EmojiUI("26f5"),
        EmojiUI("1f6f6"),
        EmojiUI("1f6a4"),
        EmojiUI("1f6f3"),
        EmojiUI("26f4"),
        EmojiUI("1f6e5"),
        EmojiUI("1f6a2"),
        EmojiUI("2708"),
        EmojiUI("1f6e9"),
        EmojiUI("1f6eb"),
        EmojiUI("1f6ec"),
        EmojiUI("1f4ba"),
        EmojiUI("1f681"),
        EmojiUI("1f69f"),
        EmojiUI("1f6a0"),
        EmojiUI("1f6a1"),
        EmojiUI("1f6f0"),
        EmojiUI("1f680"),
        EmojiUI("1f6ce"),
        EmojiUI("231b"),
        EmojiUI("23f3"),
        EmojiUI("231a"),
        EmojiUI("23f0"),
        EmojiUI("23f1"),
        EmojiUI("23f2"),
        EmojiUI("1f570"),
        EmojiUI("1f557"),
        EmojiUI("1f311"),
        EmojiUI("1f314"),
        EmojiUI("1f315"),
        EmojiUI("1f319"),
        EmojiUI("1f31a"),
        EmojiUI("1f31b"),
        EmojiUI("1f321"),
        EmojiUI("2600"),
        EmojiUI("1f31d"),
        EmojiUI("1f31e"),
        EmojiUI("2b50"),
        EmojiUI("1f31f"),
        EmojiUI("1f320"),
        EmojiUI("1f30c"),
        EmojiUI("2601"),
        EmojiUI("26c5"),
        EmojiUI("26c8"),
        EmojiUI("1f324"),
        EmojiUI("1f325"),
        EmojiUI("1f326"),
        EmojiUI("1f327"),
        EmojiUI("1f328"),
        EmojiUI("1f329"),
        EmojiUI("1f32a"),
        EmojiUI("1f32b"),
        EmojiUI("1f32c"),
        EmojiUI("1f300"),
        EmojiUI("1f308"),
        EmojiUI("1f302"),
        EmojiUI("2602"),
        EmojiUI("2614"),
        EmojiUI("26f1"),
        EmojiUI("26a1"),
        EmojiUI("2744"),
        EmojiUI("2603"),
        EmojiUI("26c4"),
        EmojiUI("2604"),
        EmojiUI("1f525"),
        EmojiUI("1f4a7"),
        EmojiUI("1f30a"),

// Objects
        EmojiUI("1f453"),
        EmojiUI("1f576"),
        EmojiUI("1f454"),
        EmojiUI("1f455"),
        EmojiUI("1f456"),
        EmojiUI("1f457"),
        EmojiUI("1f458"),
        EmojiUI("1f459"),
        EmojiUI("1f45a"),
        EmojiUI("1f45b"),
        EmojiUI("1f45c"),
        EmojiUI("1f45d"),
        EmojiUI("1f6cd"),
        EmojiUI("1f392"),
        EmojiUI("1f45e"),
        EmojiUI("1f45f"),
        EmojiUI("1f460"),
        EmojiUI("1f461"),
        EmojiUI("1f462"),
        EmojiUI("1f451"),
        EmojiUI("1f452"),
        EmojiUI("1f3a9"),
        EmojiUI("1f393"),
        EmojiUI("26d1"),
        EmojiUI("1f4ff"),
        EmojiUI("1f484"),
        EmojiUI("1f48d"),
        EmojiUI("1f48e"),
        EmojiUI("1f507"),
        EmojiUI("1f508"),
        EmojiUI("1f509"),
        EmojiUI("1f50a"),
        EmojiUI("1f4e2"),
        EmojiUI("1f4e3"),
        EmojiUI("1f4ef"),
        EmojiUI("1f514"),
        EmojiUI("1f515"),
        EmojiUI("1f3bc"),
        EmojiUI("1f3b5"),
        EmojiUI("1f3b6"),
        EmojiUI("1f399"),
        EmojiUI("1f39a"),
        EmojiUI("1f39b"),
        EmojiUI("1f3a4"),
        EmojiUI("1f3a7"),
        EmojiUI("1f4fb"),
        EmojiUI("1f3b7"),
        EmojiUI("1f3b8"),
        EmojiUI("1f3b9"),
        EmojiUI("1f3ba"),
        EmojiUI("1f3bb"),
        EmojiUI("1f941"),
        EmojiUI("1f4f1"),
        EmojiUI("1f4f2"),
        EmojiUI("260e"),
        EmojiUI("1f4de"),
        EmojiUI("1f4df"),
        EmojiUI("1f4e0"),
        EmojiUI("1f50b"),
        EmojiUI("1f50c"),
        EmojiUI("1f4bb"),
        EmojiUI("1f5a5"),
        EmojiUI("1f5a8"),
        EmojiUI("2328"),
        EmojiUI("1f5b1"),
        EmojiUI("1f5b2"),
        EmojiUI("1f4bd"),
        EmojiUI("1f4be"),
        EmojiUI("1f4bf"),
        EmojiUI("1f4c0"),
        EmojiUI("1f3a5"),
        EmojiUI("1f39e"),
        EmojiUI("1f4fd"),
        EmojiUI("1f3ac"),
        EmojiUI("1f4fa"),
        EmojiUI("1f4f7"),
        EmojiUI("1f4f8"),
        EmojiUI("1f4f9"),
        EmojiUI("1f4fc"),
        EmojiUI("1f50d"),
        EmojiUI("1f56f"),
        EmojiUI("1f4a1"),
        EmojiUI("1f526"),
        EmojiUI("1f3ee"),
        EmojiUI("1f4d4"),
        EmojiUI("1f4d5"),
        EmojiUI("1f4d6"),
        EmojiUI("1f4d7"),
        EmojiUI("1f4d8"),
        EmojiUI("1f4d9"),
        EmojiUI("1f4da"),
        EmojiUI("1f4d3"),
        EmojiUI("1f4d2"),
        EmojiUI("1f4c3"),
        EmojiUI("1f4dc"),
        EmojiUI("1f4c4"),
        EmojiUI("1f4f0"),
        EmojiUI("1f5de"),
        EmojiUI("1f4d1"),
        EmojiUI("1f516"),
        EmojiUI("1f3f7"),
        EmojiUI("1f4b0"),
        EmojiUI("1f4b4"),
        EmojiUI("1f4b5"),
        EmojiUI("1f4b6"),
        EmojiUI("1f4b7"),
        EmojiUI("1f4b8"),
        EmojiUI("1f4b3"),
        EmojiUI("1f4b9"),
        EmojiUI("2709"),
        EmojiUI("1f4e7"),
        EmojiUI("1f4e8"),
        EmojiUI("1f4e9"),
        EmojiUI("1f4e4"),
        EmojiUI("1f4e5"),
        EmojiUI("1f4e6"),
        EmojiUI("1f4eb"),
        EmojiUI("1f4ea"),
        EmojiUI("1f4ec"),
        EmojiUI("1f4ed"),
        EmojiUI("1f4ee"),
        EmojiUI("1f5f3"),
        EmojiUI("270f"),
        EmojiUI("1f58b"),
        EmojiUI("1f58a"),
        EmojiUI("1f58c"),
        EmojiUI("1f58d"),
        EmojiUI("1f4dd"),
        EmojiUI("1f4bc"),
        EmojiUI("1f4c1"),
        EmojiUI("1f4c2"),
        EmojiUI("1f5c2"),
        EmojiUI("1f4c5"),
        EmojiUI("1f4c6"),
        EmojiUI("1f5d2"),
        EmojiUI("1f4c7"),
        EmojiUI("1f4c8"),
        EmojiUI("1f4c9"),
        EmojiUI("1f4ca"),
        EmojiUI("1f4cb"),
        EmojiUI("1f4cc"),
        EmojiUI("1f4cd"),
        EmojiUI("1f4ce"),
        EmojiUI("1f587"),
        EmojiUI("1f4cf"),
        EmojiUI("1f4d0"),
        EmojiUI("2702"),
        EmojiUI("1f5c3"),
        EmojiUI("1f5c4"),
        EmojiUI("1f5d1"),
        EmojiUI("1f512"),
        EmojiUI("1f513"),
        EmojiUI("1f50f"),
        EmojiUI("1f510"),
        EmojiUI("1f511"),
        EmojiUI("1f5dd"),
        EmojiUI("1f528"),
        EmojiUI("26cf"),
        EmojiUI("2692"),
        EmojiUI("1f6e0"),
        EmojiUI("1f5e1"),
        EmojiUI("2694"),
        EmojiUI("1f52b"),
        EmojiUI("1f3f9"),
        EmojiUI("1f6e1"),
        EmojiUI("1f527"),
        EmojiUI("1f529"),
        EmojiUI("2699"),
        EmojiUI("1f5dc"),
        EmojiUI("2696"),
        EmojiUI("1f517"),
        EmojiUI("26d3"),
        EmojiUI("2697"),
        EmojiUI("1f52c"),
        EmojiUI("1f52d"),
        EmojiUI("1f4e1"),
        EmojiUI("1f489"),
        EmojiUI("1f48a"),
        EmojiUI("1f6aa"),
        EmojiUI("1f6cf"),
        EmojiUI("1f6cb"),
        EmojiUI("1f6bd"),
        EmojiUI("1f6bf"),
        EmojiUI("1f6c1"),
        EmojiUI("1f6d2"),
        EmojiUI("1f6ac"),
        EmojiUI("26b0"),
        EmojiUI("26b1"),
        EmojiUI("1f5ff"),

// Symbols
        EmojiUI("1f3e7"),
        EmojiUI("1f6ae"),
        EmojiUI("1f6b0"),
        EmojiUI("267f"),
        EmojiUI("1f6b9"),
        EmojiUI("1f6ba"),
        EmojiUI("1f6bb"),
        EmojiUI("1f6bc"),
        EmojiUI("1f6be"),
        EmojiUI("1f6c2"),
        EmojiUI("1f6c3"),
        EmojiUI("1f6c4"),
        EmojiUI("1f6c5"),
        EmojiUI("26a0"),
        EmojiUI("1f6b8"),
        EmojiUI("26d4"),
        EmojiUI("1f6ab"),
        EmojiUI("1f6b3"),
        EmojiUI("1f6ad"),
        EmojiUI("1f6af"),
        EmojiUI("1f6b1"),
        EmojiUI("1f6b7"),
        EmojiUI("1f4f5"),
        EmojiUI("1f51e"),
        EmojiUI("2622"),
        EmojiUI("2623"),
        EmojiUI("2b06"),
        EmojiUI("2197"),
        EmojiUI("27a1"),
        EmojiUI("2198"),
        EmojiUI("2b07"),
        EmojiUI("2199"),
        EmojiUI("2b05"),
        EmojiUI("2196"),
        EmojiUI("2195"),
        EmojiUI("2194"),
        EmojiUI("21a9"),
        EmojiUI("21aa"),
        EmojiUI("2934"),
        EmojiUI("2935"),
        EmojiUI("1f503"),
        EmojiUI("1f504"),
        EmojiUI("1f519"),
        EmojiUI("1f51a"),
        EmojiUI("1f51b"),
        EmojiUI("1f51c"),
        EmojiUI("1f51d"),
        EmojiUI("1f6d0"),
        EmojiUI("269b"),
        EmojiUI("1f549"),
        EmojiUI("2721"),
        EmojiUI("2638"),
        EmojiUI("262f"),
        EmojiUI("271d"),
        EmojiUI("2626"),
        EmojiUI("262a"),
        EmojiUI("262e"),
        EmojiUI("1f54e"),
        EmojiUI("2648"),
        EmojiUI("2649"),
        EmojiUI("264a"),
        EmojiUI("264b"),
        EmojiUI("264c"),
        EmojiUI("264d"),
        EmojiUI("264e"),
        EmojiUI("264f"),
        EmojiUI("2650"),
        EmojiUI("2651"),
        EmojiUI("2652"),
        EmojiUI("2653"),
        EmojiUI("26ce"),
        EmojiUI("1f500"),
        EmojiUI("1f501"),
        EmojiUI("1f502"),
        EmojiUI("25b6"),
        EmojiUI("23e9"),
        EmojiUI("23ed"),
        EmojiUI("23ef"),
        EmojiUI("25c0"),
        EmojiUI("23ea"),
        EmojiUI("23ee"),
        EmojiUI("1f53c"),
        EmojiUI("23eb"),
        EmojiUI("1f53d"),
        EmojiUI("23ec"),
        EmojiUI("23f8"),
        EmojiUI("23f9"),
        EmojiUI("23fa"),
        EmojiUI("1f3a6"),
        EmojiUI("1f505"),
        EmojiUI("1f506"),
        EmojiUI("1f4f6"),
        EmojiUI("1f4f3"),
        EmojiUI("1f4f4"),
        EmojiUI("2716"),
        EmojiUI("2795"),
        EmojiUI("2796"),
        EmojiUI("2797"),
        EmojiUI("203c"),
        EmojiUI("2049"),
        EmojiUI("2753"),
        EmojiUI("2754"),
        EmojiUI("2755"),
        EmojiUI("2757"),
        EmojiUI("3030"),
        EmojiUI("1f4b1"),
        EmojiUI("1f4b2"),
        EmojiUI("267b"),
        EmojiUI("269c"),
        EmojiUI("1f531"),
        EmojiUI("1f4db"),
        EmojiUI("1f530"),
        EmojiUI("2b55"),
        EmojiUI("2705"),
        EmojiUI("2611"),
        EmojiUI("2714"),
        EmojiUI("274c"),
        EmojiUI("274e"),
        EmojiUI("27b0"),
        EmojiUI("27bf"),
        EmojiUI("303d"),
        EmojiUI("2733"),
        EmojiUI("2734"),
        EmojiUI("2747"),
        EmojiUI("2122"),
        EmojiUI("0023"),
        EmojiUI("002a"),
        EmojiUI("0030"),
        EmojiUI("0031"),
        EmojiUI("0032"),
        EmojiUI("0033"),
        EmojiUI("0034"),
        EmojiUI("0035"),
        EmojiUI("0036"),
        EmojiUI("0037"),
        EmojiUI("0038"),
        EmojiUI("0039"),
        EmojiUI("1f51f"),
        EmojiUI("1f520"),
        EmojiUI("1f521"),
        EmojiUI("1f522"),
        EmojiUI("1f523"),
        EmojiUI("1f524"),
        EmojiUI("1f170"),
        EmojiUI("1f18e"),
        EmojiUI("1f171"),
        EmojiUI("1f191"),
        EmojiUI("1f192"),
        EmojiUI("1f193"),
        EmojiUI("2139"),
        EmojiUI("1f194"),
        EmojiUI("24c2"),
        EmojiUI("1f195"),
        EmojiUI("1f196"),
        EmojiUI("1f17e"),
        EmojiUI("1f197"),
        EmojiUI("1f17f"),
        EmojiUI("1f198"),
        EmojiUI("1f199"),
        EmojiUI("1f19a"),
        EmojiUI("1f534"),
        EmojiUI("1f535"),
        EmojiUI("26ab"),
        EmojiUI("26aa"),
        EmojiUI("2b1b"),
        EmojiUI("2b1c"),
        EmojiUI("25fc"),
        EmojiUI("25fb"),
        EmojiUI("25fe"),
        EmojiUI("25fd"),
        EmojiUI("25aa"),
        EmojiUI("25ab"),
        EmojiUI("1f536"),
        EmojiUI("1f537"),
        EmojiUI("1f538"),
        EmojiUI("1f539"),
        EmojiUI("1f53a"),
        EmojiUI("1f53b"),
        EmojiUI("1f4a0"),
        EmojiUI("1f518"),
        EmojiUI("1f533"),
        EmojiUI("1f532"),

// Flags
        EmojiUI("1f3c1"),
        EmojiUI("1f6a9"),
        EmojiUI("1f38c"),
        EmojiUI("1f3f4"),
        EmojiUI("1f3f3")
    )


}