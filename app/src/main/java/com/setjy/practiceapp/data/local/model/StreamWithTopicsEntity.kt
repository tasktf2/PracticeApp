package com.setjy.practiceapp.data.local.model

import androidx.room.Embedded
import androidx.room.Insert
import androidx.room.Relation
import com.setjy.practiceapp.data.base.EntityMapper
import com.setjy.practiceapp.data.base.ModelEntity
import com.setjy.practiceapp.domain.model.StreamWithTopics
import javax.inject.Inject

data class StreamWithTopicsEntity(
    @Embedded val streamEntity: StreamEntity,
    @Relation(
        parentColumn = "stream_id",
        entityColumn = "stream_id"
    ) val topics: List<TopicEntity>
) : ModelEntity

class StreamWithTopicsEntityMapper @Inject constructor(private val topicEntityMapper: TopicEntityMapper) :
    EntityMapper<StreamWithTopicsEntity, StreamWithTopics> {

    override fun mapToDomain(entity: StreamWithTopicsEntity): StreamWithTopics =
        StreamWithTopics(
            streamId = entity.streamEntity.streamId,
            streamName = entity.streamEntity.streamName,
            isSubscribed = entity.streamEntity.isSubscribed,
            backgroundColor = entity.streamEntity.backgroundColor,
            topics = entity.topics.map(topicEntityMapper::mapToDomain)
        )

    override fun mapToEntity(model: StreamWithTopics): StreamWithTopicsEntity =
        StreamWithTopicsEntity(
            streamEntity = StreamEntity(
                streamId = model.streamId,
                streamName = model.streamName,
                isSubscribed = model.isSubscribed,
                backgroundColor = model.backgroundColor
            ),
            topics = model.topics.map(topicEntityMapper::mapToEntity)
        )
}