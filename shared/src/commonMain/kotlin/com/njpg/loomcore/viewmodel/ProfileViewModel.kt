package com.njpg.loomcore.viewmodel

import androidx.lifecycle.ViewModel
import com.njpg.loomcore.data.Paths
import com.njpg.loomcore.data.SingleObjectRepository
import com.njpg.loomcore.model.Profile
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

/**
 * ViewModel профиля производителя.
 *
 * В отличие от остальных ViewModel, хранит один объект [Profile],
 * а не список. Использует [SingleObjectRepository].
 *
 * Профиль изменяется автоматически через [LaunchedEffect] в [ProfileTab]
 * с задержкой debounce 500 мс после каждого нажатия клавиши.
 */
class ProfileViewModel : ViewModel() {

    private val repo = SingleObjectRepository(
        file = Paths.profileFile, serializer = Profile.serializer(), default = { Profile() })

    private val _profile = MutableStateFlow(repo.load())

    /** Реактивный объект профиля. */
    val profile = _profile.asStateFlow()

    /**
     * Обновляет профиль в памяти и немедленно сохраняет на диск.
     *
     * @param profile  Новое значение профиля.
     */
    fun update(profile: Profile) {
        _profile.value = profile
        repo.save(profile)
    }
}