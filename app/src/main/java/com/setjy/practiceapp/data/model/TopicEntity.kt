package com.setjy.practiceapp.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.setjy.practiceapp.data.base.EntityMapper
import com.setjy.practiceapp.data.base.ModelEntity
import com.setjy.practiceapp.domain.model.Topic

@Entity(
    tableName = "topic",
    foreignKeys = [ForeignKey(
        entity = StreamEntity::class,
        parentColumns = ["stream_id"],
        childColumns = ["stream_id"],
        onDelete = ForeignKey.CASCADE,
        onUpdate = ForeignKey.CASCADE
    )]
)
data class TopicEntity(
    @PrimaryKey
    @ColumnInfo(name = "topic_id")
    val topicId: Int,
    @ColumnInfo(name = "topic_name")
    val topicName: String,
    @ColumnInfo(name = "stream_id")
    val streamId: Int,
) : ModelEntity()

class TopicEntityMapper : EntityMapper<Topic, TopicEntity> {
    override fun mapToDomain(entity: TopicEntity): Topic = Topic(
        topicId = entity.topicId,
        topicName = entity.topicName,
        streamId = entity.streamId,
    )

    override fun mapToEntity(model: Topic): TopicEntity = TopicEntity(
        topicId = model.topicId,
        topicName = model.topicName,
        streamId = model.streamId
    )
}