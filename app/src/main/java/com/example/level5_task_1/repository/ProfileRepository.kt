package com.example.level5_task_1.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.level5_task_1.model.Profile
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withTimeout

class ProfileRepository {
    private var firestore: FirebaseFirestore = FirebaseFirestore.getInstance()
    private var profileDocument =
        firestore.collection("profiles").document("profile")

    private val _profile: MutableLiveData<Profile> = MutableLiveData()

    val profile: LiveData<Profile>
        get() = _profile

    // The CreateProfileFragment can use this to see if creation succeeded
    private val _createSuccess: MutableLiveData<Boolean> = MutableLiveData()

    val createSuccess: LiveData<Boolean>
        get() = _createSuccess

    suspend fun getProfile() {
        try {
            withTimeout(5_000) {
                val data = profileDocument
                    .get()
                    .await()

                val firstName = data.getString("firstName").toString()
                val lastName = data.getString("lastName").toString()
                val description = data.getString("description").toString()
                val imageUri = data.getString("imageUri").toString()

                _profile.value = Profile(firstName, lastName, description, imageUri)
            }
        } catch (e: Exception) {
            throw ProfileRetrievalError("Retrieval-firebase-task was unsuccessful")
        }
    }

    suspend fun createProfile(profile: Profile) {
        // Persist data to Firestore
        try {
            withTimeout(5_000) {
                profileDocument
                    .set(profile)
                    .await()

                _createSuccess.value = true
                Log.i("askjckjasnckj", "SE SUBIO")
            }

        } catch (e: Exception) {
            Log.i("askjckjasnckj", "NO SE SUBIO")
            throw ProfileSaveError(e.message.toString(), e)
        }
    }

    fun reset() {
        _createSuccess.value = false
    }

    class ProfileSaveError(message: String, cause: Throwable) : Exception(message, cause)
    class ProfileRetrievalError(message: String) : Exception(message)
}