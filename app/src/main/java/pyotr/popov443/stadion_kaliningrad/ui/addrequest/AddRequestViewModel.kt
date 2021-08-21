package pyotr.popov443.stadion_kaliningrad.ui.addrequest

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import pyotr.popov443.stadion_kaliningrad.data.Repository

class AddRequestViewModel : ViewModel() {

    val username = MutableLiveData<String>().apply {
        viewModelScope.launch {
            value = Repository.getUser().username
        }
    }

}