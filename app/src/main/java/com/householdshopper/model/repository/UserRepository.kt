package com.householdshopper.model.repository

import com.householdshopper.model.User

class UserRepository {
    fun getUser(): User {
        // Fetch data from db
        return User(1,"Gmto","test@gmail.com","test123");
    }
}