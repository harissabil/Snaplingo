package com.igd.snaplingo.ui.screen.task

import com.igd.snaplingo.data.local.entity.SnapEntity
import com.igd.snaplingo.data.local.entity.TaskEntity

data class TaskState(
    val taskList: List<TaskEntity> = emptyList(),
    val snapList: List<SnapEntity> = emptyList(),
)
