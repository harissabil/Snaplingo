package com.igd.snaplingo.ui.screen.onboarding.domain.usecase

import com.harissabil.fisch.core.datastore.local_user_manager.domain.LocalUserManager
import javax.inject.Inject

class SaveAppEntry @Inject constructor(
    private val localUserManager: LocalUserManager
) {

    suspend operator fun invoke(){
        localUserManager.saveAppEntry()
    }
}