package com.tms.targetedmoneysaver.data


import com.google.firebase.auth.FirebaseAuth

class AuthRepository(
) {
    private val firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()

    fun loginUser(
        email: String,
        password: String,
        callback: (Boolean, String?, String?) -> Unit
    ) {
        firebaseAuth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val user = firebaseAuth.currentUser
                    user?.getIdToken(true)?.addOnCompleteListener { tokenTask ->
                        if (tokenTask.isSuccessful) {
                            val token = tokenTask.result?.token
                            callback(true, null, token)
                        } else {
                            callback(false, tokenTask.exception?.message, null)
                        }
                    }
                } else {
                    callback(false, task.exception?.message, null)
                }
            }
    }

}