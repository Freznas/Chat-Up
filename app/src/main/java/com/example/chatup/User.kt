package com.example.chatup

import java.io.Serializable
import java.net.URL

class User (val id: String,
            val name: String?,
            val password: String?,
            val email: String?,
            var friends: MutableList<String> = mutableListOf()
):Serializable{
    constructor() : this("", "", "", "", mutableListOf())
    // When make User presentation and pic is optional
    var presentation: String? ="placeholder"
    var profilePicture: URL? = URL("https://www.youtube.com/")


    override fun toString(): String {
        return "User\nname: $name\npassword: $password\n" +
                "mail: $email\npresentation: $presentation\nprofilePicture: $profilePicture"
    }


}