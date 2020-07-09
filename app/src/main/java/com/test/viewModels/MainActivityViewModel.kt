package com.test.viewModels

import android.content.res.Resources
import androidx.lifecycle.ViewModel
import javax.inject.Inject

interface MainActivityViewModel {

}

class MainActivityViewModelImpl @Inject constructor(val resources: Resources) :
        ViewModel(), MainActivityViewModel {

}