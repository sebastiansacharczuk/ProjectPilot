package com.sebsach.projectpilot.utils

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.sebsach.projectpilot.model.ProjectModel

/**
 * @author Sebastian Sacharczuk
 * github: https://github.com/sebastiansacharczuk
 */
class FirebaseUtils {

    companion object {
        fun currentUserId(): String? {
            return FirebaseAuth.getInstance().uid
        }
        fun currentUserEmail(): String? {
            return FirebaseAuth.getInstance().currentUser?.email
        }

        fun isLoggedIn(): Boolean{
            return currentUserId() != null
        }

        fun currentUserDetails(): DocumentReference? {
            return currentUserId()?.let { FirebaseFirestore.getInstance().collection("users").document() }
        }

        fun allUsersCollectionReference(): CollectionReference {
            return FirebaseFirestore.getInstance().collection("users")
        }

        fun projectDetails(id: String): DocumentReference {
            return FirebaseFirestore.getInstance().collection("projects").document(id)
        }
        fun createProject(projectName: String, userName: String) {
            FirebaseFirestore.getInstance().collection("projects").document()
                .set(ProjectModel(userName, projectName, listOf(currentUserId())))
        }
    }
}