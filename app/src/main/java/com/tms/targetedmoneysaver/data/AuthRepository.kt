package com.tms.targetedmoneysaver.data


import com.google.firebase.auth.FirebaseAuth
import com.tms.targetedmoneysaver.data.remote.response.AuthResponse

class AuthRepository(private val firebaseAuth:FirebaseAuth) {

    fun loginUser(
        email: String,
        password: String,
        callback: (AuthResponse) -> Unit
    ) {
        firebaseAuth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val user = firebaseAuth.currentUser
                    user?.getIdToken(true)?.addOnCompleteListener { tokenTask ->
                        if (tokenTask.isSuccessful) {
                            val token = tokenTask.result?.token
                            callback(AuthResponse(true, "Login Success", token ?: ""))
                        } else {
                            callback(AuthResponse(false, "Token failed to retrieve", ""))
                        }
                    }
                } else {
                    callback(AuthResponse(false, "Login failed, invalid email or password" , ""))
                }
            }
    }

    fun registerUser(
        email: String,
        password: String,
        callback: (AuthResponse) -> Unit
    ) {
        firebaseAuth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val user = firebaseAuth.currentUser
                    user?.getIdToken(true)?.addOnCompleteListener { tokenTask ->
                        if (tokenTask.isSuccessful) {
                            val token = tokenTask.result?.token
                            callback(AuthResponse(true, "Register Success", token ?: ""))
                        } else {
                            callback(AuthResponse(false, "Token failed to retrieve", ""))
                        }
                    }
                } else {
                    callback(AuthResponse(false, "Register failed" , ""))
                }
            }
    }

    fun signOut(callback: (Boolean) -> Unit) {
        val currentUser = firebaseAuth.currentUser
        if (currentUser != null) {
            firebaseAuth.signOut()
            callback(true)
        } else {
            callback(false)
        }
    }

}