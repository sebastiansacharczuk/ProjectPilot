package com.sebsach.projectpilot.utils

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.toObject
import com.sebsach.projectpilot.models.ProjectModel
import com.sebsach.projectpilot.models.UserModel
import kotlinx.coroutines.tasks.await

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
            return true
        }
        fun allUsers(): CollectionReference {
            return FirebaseFirestore.getInstance().collection("users")
        }
        private fun allProjects(): CollectionReference {
            return FirebaseFirestore.getInstance().collection("projects")
        }
        fun userById(id: String): UserModel {
            var user = UserModel()
            allUsers().document(id)
                .get().
                addOnSuccessListener { documentSnapshot -> user =
                    documentSnapshot.toObject<UserModel>()!!
                }
            return user
        }
        suspend fun getUserProjectRefs(userId: String): List<Map<String, String>> {
            val documentSnapshot = allUsers().document(userId)
                .get()
                .await() // This will suspend the function until the result is available

            return documentSnapshot.get("projectRefs") as List<Map<String, String>>
        }
        fun usersById(ids: List<String?>, onSuccess: (List<UserModel>) -> Unit) {
            val usersCollection = allUsers()

            // Create a new list to hold the user documents
            val users = mutableListOf<UserModel>()

            // Fetch each user by ID
            for (id in ids) {
                if (id != null) {
                    usersCollection.document(id).get()
                        .addOnSuccessListener { document ->
                            val user = document.toObject<UserModel>()
                            if (user != null) {
                                users.add(user)
                                println("User fetched with ID: $id")
                            } else {
                                println("No user found with ID: $id")
                            }
                        }
                        .addOnFailureListener { exception ->
                            println("Error getting user with ID: $id, ${exception.message}")
                        }
                        .addOnCompleteListener {
                            onSuccess(users)
                        }
                }
            }
        }
        fun createProject(projectName: String) {
            val projectRef = allProjects().document()

            currentUserId()?.let {
                addProjectRef(it, projectName, projectRef.id)
                projectRef.set(ProjectModel(projectRef.id, it, projectName, listOf(currentUserId())))
            }
        }
        fun addNewTask(id: String, task: String){
            allProjects().document(id).update("tasks", FieldValue.arrayUnion(mapOf("task" to task, "done" to false)))
        }
        fun addProjectRef(uid: String, projectName: String, referenceID: String) {
            allUsers().document(uid)
                .update("projectRefs", FieldValue.arrayUnion(mapOf("id" to referenceID, "name" to projectName)))
            println(referenceID + projectName)
        }
        fun addUserToProject(projectName: String, projectId: String, uid: String) {
            allProjects().document(projectId).update("members", FieldValue.arrayUnion(uid))
            addProjectRef(uid, projectName, projectId)
        }
        fun removeUserFromProject(projectName: String, projectId: String, uid: String) {
            allProjects().document(projectId)
                .update("members", FieldValue.arrayRemove(uid))

            allUsers().document(uid).update("projectRefs", FieldValue.arrayRemove(mapOf("id" to projectId, "name" to projectName)))
        }
        fun deleteProject(projectId: String){
            allProjects().document(projectId).delete()
        }
        fun changeLeader(uid: String, projectId: String){
            allProjects().document(projectId)
                .update("leader", uid)
        }

        fun getProjectDetails(
            id: String,
            onSuccess: (ProjectModel) -> Unit,
            onFailure: () -> Unit
        ) {
            allProjects().document(id)
                .addSnapshotListener { snapshot, e ->
                    if (e != null || snapshot == null || !snapshot.exists()) {
                        onFailure()
                    } else {
                        val projectModel = snapshot.toObject(ProjectModel::class.java)
                        println("Success: $projectModel")
                        if (projectModel != null) {
                            onSuccess(projectModel)
                        } else {
                            onFailure()
                        }
                    }
                }
        }



    }
}