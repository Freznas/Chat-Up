package com.example.chatup

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import java.net.URL

class UserDao {
    val KEY_ID ="id"
    val KEY_NAME = "name"
    val KEY_PASSWORD = "password"
    val KEY_EMAIL = "email"
    val KEY_PRESENTATION = "presentation"
    val KEY_PROFILEPICTURE = "profilepicture"
    val KEY_FRIENDS = "friends"
    fun addUser(user: User, callback: (Boolean) -> Unit)
    {
        val usersCollection = FirebaseFirestore.getInstance().collection("users")

        val lowercaseUsername = user.name?.lowercase()

        //Sends request and checks if the username already exists
        usersCollection
            //Filter to username
            .whereEqualTo(KEY_NAME, lowercaseUsername)
            .get()
            .addOnSuccessListener { querySnapshot ->
                //Doing the check
                if (!querySnapshot.isEmpty) {
                    Log.e("ERROR", "Username already exists")
                    callback(false)

                } else {
                    //Max code for adding user to firestore
                    val dataToStore = HashMap<String, Any>()
                    dataToStore[KEY_ID] = user.id as Any
                    dataToStore[KEY_NAME] = user.name as Any
                    dataToStore[KEY_PASSWORD] = user.password as Any
                    dataToStore[KEY_PRESENTATION] = user.presentation as Any
                    dataToStore[KEY_PROFILEPICTURE] = user.profilePicture as Any
                    dataToStore[KEY_EMAIL] = user.email as Any
                    dataToStore[KEY_FRIENDS] = user.friends as Any

                    FirebaseFirestore
                        .getInstance()
                        .document("users/${user.id}")
                        .set(dataToStore)
                        .addOnSuccessListener { log ->
                            Log.w(
                                "SUCCESS",
                                "User added to firestore with id : ${user.id}"
                            )
                            callback(true)
                        }
                        .addOnFailureListener { log -> Log.e("ERROR", "Failed to add user to firestore")
                            callback(false)
                        }
                }
            }
            .addOnFailureListener { exception ->
                Log.e("ERROR", "Error checking username availability", exception)
                callback(false)
            }

    }
    fun updateUser(user: User)
    {
        val userRef = FirebaseFirestore.getInstance().collection("users").document(user.id)
        val updates: Map<String, Any>  = hashMapOf(
            "name" to user.name as Any,
            "password" to user.password as Any
        )
        userRef.update( updates)
           .addOnSuccessListener {
               // Update successful
               println("User document successfully updated!")
           }
           .addOnFailureListener { e ->
               // Handle any errors
               println("Error updating user document: $e")
           }
    }
    fun deleteUser(user: User){
        FirebaseFirestore
            .getInstance()
            .document("users/${user.id}")
            .delete()
            .addOnSuccessListener { log -> Log.w("SUCCSESS", "$user deleted") }
            .addOnFailureListener { log -> Log.e("ERROR", "Failed to delete user in firestore") }
    }
    fun getAllUsers(activity: MaxTestActivity){
        val users = ArrayList<User>()

        FirebaseFirestore
            .getInstance()
            .collection("users")
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {

                    val id = document.getString(KEY_ID)
                    val name = document.getString(KEY_NAME)
                    val password = document.getString(KEY_PASSWORD)
                    val email = document.getString(KEY_EMAIL)
                    val presentation = document.getString(KEY_PRESENTATION)
                    val profilePicture = document.getString(KEY_PROFILEPICTURE)

                    val user = User(id!!, name, password, email)
                    user.presentation = presentation
                    user.profilePicture = URL(profilePicture)
                    users.add(user)
                }
                Log.i("SUCCSESS", " FETCHED USERS FROM FIRESTORE")
                activity.showUsers(users)
            }.addOnFailureListener { log -> Log.e("ERROR", "Failed to fetch USERS from firestore") }
    }
//    Function to search for users
    fun getUserByUserName(username: String, callback: (User) -> Unit) {
    FirebaseFirestore
        .getInstance()
        .collection("users")
        .whereEqualTo("name", username)
        .get()
        .addOnSuccessListener { result ->
            val user = result.documents.firstOrNull()
            if(user != null)
            {
                val id = user.getString(KEY_ID)
                val name = user.getString(KEY_NAME)
                val password = user.getString(KEY_PASSWORD)
                val mail = user.getString(KEY_EMAIL)
                val presentation = user.getString(KEY_PRESENTATION)
                val profilePicture = user.getString(KEY_PROFILEPICTURE)
                val foundUser = User(id!!, name, password, mail)
                foundUser.presentation = presentation
                foundUser.profilePicture = URL(profilePicture)
                callback (foundUser)
            }
            Log.i("SUCCSESS", " FETCHED USER FROM FIRESTORE")
        }.addOnFailureListener { log -> Log.e("ERROR", "Failed to fetch USERS from firestore")
        callback(User("", "","",""))}
}

    fun searchUsers(query:String,callback:(List<User>)->Unit){
        val users = mutableListOf<User>()
        FirebaseFirestore.getInstance()
            .collection("users")
            .whereEqualTo(KEY_NAME,query)
            .get()
            .addOnSuccessListener { result ->
                for (document in result)
                {
//                    Fetching user data from firestore document
                    val id = document.getString(KEY_ID)?:""
                    val name =document.getString(KEY_NAME)?:""
                    val password = document.getString(KEY_PASSWORD)?:""
                    val email = document.getString(KEY_EMAIL)?:""
                    val presentation = document.getString(KEY_PRESENTATION)?:""
                    val profilePicture = document.getString(KEY_PROFILEPICTURE)?:""

                    val user=User(id,name,password,email)
                    user.presentation=presentation
                    user.profilePicture=URL(profilePicture)
                    users.add(user)

                }
                callback(users)
            }.addOnFailureListener {
                Log.e("ERROR","Failed to search users")
                callback(emptyList())
            }

    }
    fun checkUserCredentials(username: String, password: String, callback: (Boolean) -> Unit)
    {
        val usersCollection = FirebaseFirestore.getInstance().collection("users")

        usersCollection
            .whereEqualTo("name", username )
            .whereEqualTo( "password", password)
            .get().addOnSuccessListener { querySnapshot ->
                if (querySnapshot.isEmpty) { // No matching documents found
                    callback(false)
                } else {// Found document
                    callback(true)
                }
            }
            .addOnFailureListener {exception ->
                Log.e("Error"," Query failed", exception)
            }
    }

    fun addFriend(currentUser: User?, friendUser: User?, callback: (Boolean) -> Unit) {
        if (currentUser == null || friendUser == null) {
            callback(false)
            return
        }
        val currentUserId = currentUser.id
        val friendId = friendUser.id
        val userRef = FirebaseFirestore.getInstance().collection("users").document(currentUserId)

        // Get the current user's document from Firestore
        userRef.get()
            .addOnSuccessListener { documentSnapshot ->
                val user = documentSnapshot.toObject(User::class.java)
                if (user != null) {
                    if (!user.friends.contains(friendId)) {
                        // Add the friend's ID to the current user's friends list
                        user.friends.add(friendId)
                        // Update the friends field in Firestore with the updated friends list
                        userRef.update("friends", user.friends)
                            .addOnSuccessListener {
                                callback(true)
                            }
                            .addOnFailureListener { e ->
                                callback(false)
                            }
                    } else {
                        // Friend already exists in the list
                        callback(false)
                    }
                } else {
                    callback(false)
                }
            }
            .addOnFailureListener { e ->
                callback(false)
            }
    }


    fun removeFriend(currentUser: User?, friendUser: User?, callback: (Boolean) -> Unit) {
        if (currentUser == null || friendUser == null) {
            callback(false)
            return
        }
        val currentUserId = currentUser.id
        val friendId = friendUser.id
        val userRef = FirebaseFirestore.getInstance().collection("users").document(currentUserId)

        // Get the current user's document from Firestore
        userRef.get().addOnSuccessListener { documentSnapshot ->
            val user = documentSnapshot.toObject(User::class.java)
            if (user != null) {
                // Check if friend exists in the current user's friends list
                if (user.friends.contains(friendId)) {
                    // Remove the friend's ID from the current user's friends list
                    user.friends.remove(friendId)
                    // Update the friends field in Firestore with the updated friends list
                    userRef.update("friends", user.friends)
                        .addOnSuccessListener {
                            callback(true)
                        }
                        .addOnFailureListener { e ->
                            callback(false)
                        }
                } else {
                    // Friend does not exist in the list
                    callback(false)
                    // Optionally, you can provide a toast or another form of notification here
                }
            } else {
                callback(false)
            }
        }.addOnFailureListener { e ->
            callback(false)
        }
    }







}






