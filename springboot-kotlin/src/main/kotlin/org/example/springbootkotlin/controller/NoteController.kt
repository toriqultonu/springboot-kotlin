package org.example.springbootkotlin.controller

import org.bson.types.ObjectId
import org.example.springbootkotlin.database.model.Note
import org.example.springbootkotlin.database.repository.NoteRepository
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.time.Instant

@RestController
@RequestMapping("/api/notes")
class NoteController(
    private val noteRepository: NoteRepository
) {

    data class NoteRequest(
        val id: String?,
        val title: String,
        val content: String,
        val color: Long,
        val ownerId: String
    )

    data class NoteResponse(
        val id: String,
        val title: String,
        val content: String,
        val color: Long,
        val createdAt: Instant
    )

    @PostMapping
    fun save(body: NoteRequest): NoteResponse {
        val note = noteRepository.save(
            Note(
                id = body.id?.let { ObjectId(it) } ?: ObjectId.get(),
                title = body.title,
                content = body.content,
                color = body.color,
                ownerId = ObjectId(body.ownerId),
                createdAt = Instant.now(),
            )
        )

        return note.toResponse()
    }

    @GetMapping
    fun findByOwnerId(@RequestParam(required = true) ownerId: String): List<NoteResponse> {
        return noteRepository.findByOwnerId(ObjectId(ownerId))
            .map { it.toResponse() }
    }
}

private fun Note.toResponse(): NoteController.NoteResponse {
    return NoteController.NoteResponse(
        id = this.id.toHexString(),
        title = this.title,
        content = this.content,
        color = this.color,
        createdAt = this.createdAt,
    )
}