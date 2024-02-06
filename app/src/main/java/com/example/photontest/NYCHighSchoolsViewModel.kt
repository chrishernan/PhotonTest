package com.example.photontest

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

class NYCHighSchoolsViewModel(): ViewModel() {

    val NYCRepository = NYCRepository(NYCApiService.service)

    private val viewModelScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)
    private var _highSchoolLiveData = MutableLiveData<List<NYCHighSchool>>()
    internal val highSchoolLiveData: LiveData<List<NYCHighSchool>>
        get() = _highSchoolLiveData


    internal fun fetch() {
        viewModelScope.launch {
            NYCRepository.fetch().onSuccess { highSchools ->
                _highSchoolLiveData.postValue(highSchools)
            }.onFailure { exception ->
                Log.e("FETCHING FAILURE","Cause: ${exception.cause}    Message: ${exception.message}")
            }
        }
    }
}