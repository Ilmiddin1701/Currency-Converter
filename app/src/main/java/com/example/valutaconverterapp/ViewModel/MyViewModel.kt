package com.example.valutaconverterapp.ViewModel

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.valutaconverterapp.Models.Courses
import com.example.valutaconverterapp.Retrofit.ApiClient
import com.example.valutaconverterapp.Utils.MyData
import com.example.valutaconverterapp.Utils.Resource
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch

class MyViewModel() : ViewModel() {

    private val liveCourses = MutableLiveData<Resource<List<Courses>>>()
    private val apiService = ApiClient.getApiService()

    fun getCourses(context: LifecycleOwner) : MutableLiveData<Resource<List<Courses>>> {
        MyData.internetLiveData.observe(context) {
            if (it) {
                viewModelScope.launch {
                    liveCourses.postValue(Resource.loading(null))
                    try {
                        coroutineScope {
                            val list: List<Courses> = async {
                                apiService.getCourses()
                            }.await()
                            liveCourses.postValue(Resource.success(list))
                        }
                    } catch (e: Exception) {
                        liveCourses.postValue(Resource.error("Nimadir xato ketdi!"))
                    }
                }
            }
        }
        return liveCourses
    }
}