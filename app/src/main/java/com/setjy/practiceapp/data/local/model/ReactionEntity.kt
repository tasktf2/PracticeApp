package com.setjy.practiceapp.data.local.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import com.setjy.practiceapp.data.base.EntityMapper
import com.setjy.practiceapp.data.base.ModelEntity
import com.setjy.practiceapp.domain.model.ReactionDomain

@Entity(
    tableName = "reaction",
    primaryKeys = ["message_id", "user_id", "emoji_code"],
    foreignKeys = [ForeignKey(
        entity = MessageEntity::class,
        parentColumns = ["message_id"],
        childColumns = ["message_id"],
        onDelete = ForeignKey.CASCADE,
        onUpdate = ForeignKey.CASCADE
    )]
)
data class ReactionEntity(
    @ColumnInfo(name = "emoji_code")
    val code: String,
    @ColumnInfo(name = "emoji_name")
    val name: String,
    @ColumnInfo(name = "user_id")
    val userId: Int,
    @ColumnInfo(name = "message_id")
    val messageId: Int
) : ModelEntity

class ReactionsEntityMapper : EntityMapper<ReactionEntity, ReactionDomain> {
    override fun mapToDomain(entity: ReactionEntity): ReactionDomain = ReactionDomain(
        code = entity.code, name = entity.name, userId = entity.userId, messageId = entity.messageId
    )

    override fun mapToEntity(model: ReactionDomain): ReactionEntity = ReactionEntity(
        code = model.code, name = model.name, userId = model.userId, messageId = model.messageId
    )
}
