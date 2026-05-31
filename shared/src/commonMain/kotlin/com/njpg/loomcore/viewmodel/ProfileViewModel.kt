package com.njpg.loomcore.viewmodel

import androidx.lifecycle.ViewModel
import com.njpg.loomcore.data.Paths
import com.njpg.loomcore.data.SingleObjectRepository
import com.njpg.loomcore.model.Profile
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class ProfileViewModel : ViewModel() {

    private val repo = SingleObjectRepository(
        file = Paths.profileFile, serializer = Profile.serializer(), default = { Profile() })

    private val _profile = MutableStateFlow(repo.load())
    val profile = _profile.asStateFlow()

    fun update(profile: Profile) {
        _profile.value = profile
        repo.save(profile)
    }
}