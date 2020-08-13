package com.example.bookarena.database

import androidx.room.Database


@Database(entities = [BookEntity::class],version = 1)
abstract class BookDatabase {
    abstract fun bookDao() : BookDao
}