package com.sebsach.projectpilot.models

/**
 * @author Sebastian Sacharczuk
 * github: https://github.com/sebastiansacharczuk
 */
data class ProjectModel(
    val id: String = "",
    val leader: String = "",
    val name: String = "",
    var members: List<String?> = emptyList(),
    var tasks: List<Map<String, Any>> = emptyList(),
    var chat: List<Map<String, Any>> = emptyList()
)

