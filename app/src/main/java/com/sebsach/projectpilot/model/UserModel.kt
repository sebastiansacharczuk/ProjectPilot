package com.sebsach.projectpilot.model

/**
 * @author Sebastian Sacharczuk
 * github: https://github.com/sebastiansacharczuk
 */
data class UserModel(
    var username: String = "",
    var uid: String = "",
    var projectRefs: List<ProjectRef> = emptyList()
)