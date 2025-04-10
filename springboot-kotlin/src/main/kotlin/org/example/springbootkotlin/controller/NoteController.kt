package org.example.springbootkotlin.controller

import org.bson.types.ObjectId
import org.example.springbootkotlin.database.model.Note
import org.example.springbootkotlin.database.repository.NoteRepository
import org.springframework.web.bind.annotation.*
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
    fun save(@RequestBody body: NoteRequest): NoteResponse {
        val note = noteRepository.save(
            Note(
                id = body.id?.let { ObjectId(it) } ?: ObjectId.get(),
                title = body.title,
                content = body.content,
                color = body.color,
                createdAt = Instant.now(),
                ownerId = ObjectId()
            )
        )

        return note.toResponse()
    }

    @GetMapping
    fun findByOwnerId(@RequestParam(required = true) ownerId: String): List<NoteResponse> {
        return noteRepository.findByOwnerId(ObjectId(ownerId))
            .map { it.toResponse() }
    }

    @DeleteMapping(path = ["/{id}"])
    fun deleteById(@PathVariable id: String) {
        noteRepository.deleteById(ObjectId(id))
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