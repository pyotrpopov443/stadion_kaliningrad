package pyotr.popov443.stadion_kaliningrad.ui.home

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import pyotr.popov443.stadion_kaliningrad.data.Repository
import pyotr.popov443.stadion_kaliningrad.data.models.Request
import pyotr.popov443.stadion_kaliningrad.data.models.RequestBody

class HomeViewModel : ViewModel() {

    val requests = MutableLiveData<List<Request>>().apply {
        viewModelScope.launch {
            value = Repository.getRequests()
        }
    }

    val username = MutableLiveData<String>().apply {
        viewModelScope.launch {
            value = Repository.getUser().username
        }
    }

}