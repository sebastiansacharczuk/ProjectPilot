package com.sebsach.projectpilot.models

/**
 * @author Sebastian Sacharczuk
 * github: https://github.com/sebastiansacharczuk
 */
data class UserModel(
    var username: String = "",
    var uid: String = "",
    var projectRefs: List<Map<String, String>> = emptyList()
)