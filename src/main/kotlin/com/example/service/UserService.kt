package com.example.service

import com.example.model.User
import com.example.model.UserRequest
import org.springframework.stereotype.Service
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.atomic.AtomicLong

@Service
class UserService {

    private val users = ConcurrentHashMap<Long, User>()
    private val idCounter = AtomicLong(0)

    init {
        // Initialize with sample data
        users[1] = User(1, "John Doe", "john@example.com")
        users[2] = User(2, "Jane Smith", "jane@example.com")
        idCounter.set(2)
    }

    fun getAllUsers(): List<User> = users.values.sortedBy { it.id }

    fun getUserById(id: Long): User {
        return users[id] ?: throw NoSuchElementException("User not found with id: $id")
    }

    fun createUser(request: UserRequest): User {
        val id = idCounter.incrementAndGet()
        val user = User(id, request.name, request.email)
        users[id] = user
        return user
    }

    fun updateUser(id: Long, request: UserRequest): User {
        if (!users.containsKey(id)) {
            throw NoSuchElementException("User not found with id: $id")
        }
        val user = User(id, request.name, request.email)
        users[id] = user
        return user
    }

    fun deleteUser(id: Long) {
        users.remove(id) ?: throw NoSuchElementException("User not found with id: $id")
    }
}