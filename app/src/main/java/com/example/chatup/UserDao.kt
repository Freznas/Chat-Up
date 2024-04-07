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
    fun addUser(user: User)
    {
        val dataToStore = HashMap<String, Any>()
        dataToStore[KEY_ID] = user.id as Any
        dataToStore[KEY_NAME] = user.name as Any
        dataToStore[KEY_PASSWORD] = user.password as Any
        dataToStore[KEY_PRESENTATION] = user.presentation as Any
        dataToStore[KEY_PROFILEPICTURE] = user.profilePicture as Any
        dataToStore[KEY_EMAIL] = user.email as Any

        FirebaseFirestore
            .getInstance()
            .document("users/${user.id}")
            .set(dataToStore)
            .addOnSuccessListener { log ->
                Log.w(
                    "SUCCSESS",
                    "User added to firestore with id : ${user.id}"
                )
            }
            .addOnFailureListener { log -> Log.e("ERROR", "Failed to add user to firestore") }
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
    fun searchUsers(query:String,callback:(List<User>)->Unit){
        val users = mutableListOf<User>()
        FirebaseFirestore.getInstance()
            .collection("users")
            .whereEqualTo(KEY_NAME,query)
            .get()
            .addOnSuccessListener { result ->
                for (document in result)
                {
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
}