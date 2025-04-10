package org.example.springbootkotlin.database.repository

import org.bson.types.ObjectId
import org.example.springbootkotlin.database.model.Note
import org.springframework.data.mongodb.repository.MongoRepository

interface NoteRepository: MongoRepository<Note, ObjectId> {

    fun findByOwnerId(ownerId: ObjectId): List<Note>

}

