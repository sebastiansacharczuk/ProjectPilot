package com.sebsach.projectpilot.model

/**
 * @author Sebastian Sacharczuk
 * github: https://github.com/sebastiansacharczuk
 */
data class ProjectModel(
    val leader: String,
    val name: String,
    var members: List<String?> = emptyList(),
    var tasks: List<String> = emptyList()
)

