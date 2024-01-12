package com.sebsach.projectpilot.utils

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore

/**
 * @author Sebastian Sacharczuk
 * github: https://github.com/sebastiansacharczuk
 */
class FirebaseUtil {

    companion object {
        fun currentUserId(): String? {
            return FirebaseAuth.getInstance().uid
        }

        fun isLoggedIn(): Boolean{
            return currentUserId() != null
        }

        fun currentUserDetails(): DocumentReference? {
            return currentUserId()?.let {
                FirebaseFirestore.getInstance().collection("users").document(
                    it
                )
            }
        }
    }
}