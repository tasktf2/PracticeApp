package com.setjy.practiceapp.data.model

import androidx.room.Embedded
import androidx.room.Relation
import com.setjy.practiceapp.data.base.EntityMapper
import com.setjy.practiceapp.data.base.ModelEntity
import com.setjy.practiceapp.domain.model.StreamWithTopics

data class StreamWithTopicsEntity(
    @Embedded val streamEntity: StreamEntity,
    @Relation(
        parentColumn = "stream_id",
        entityColumn = "stream_id"
    ) val topics: List<TopicEntity>
) : ModelEntity()

class StreamWithTopicsEntityMapper constructor(private val topicEntityMapper: TopicEntityMapper) :
    EntityMapper<StreamWithTopics, StreamWithTopicsEntity> {

    override fun mapToDomain(entity: StreamWithTopicsEntity): StreamWithTopics =
        StreamWithTopics(
            streamId = entity.streamEntity.streamId,
            streamName = entity.streamEntity.streamName,
            isSubscribed = entity.streamEntity.isSubscribed,
            backgroundColor = entity.streamEntity.backgroundColor,
            topics = entity.topics.map { topicEntityMapper.mapToDomain(it) }
        )

    override fun mapToEntity(model: StreamWithTopics): StreamWithTopicsEntity =
        StreamWithTopicsEntity(
            streamEntity = StreamEntity(
                streamId = model.streamId,
                streamName = model.streamName,
                isSubscribed = model.isSubscribed,
                backgroundColor = model.backgroundColor
            ),
            topics = model.topics.map { topicEntityMapper.mapToEntity(it) }
        )
}