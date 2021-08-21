package pyotr.popov443.stadion_kaliningrad

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import pyotr.popov443.stadion_kaliningrad.data.Repository

class SignInViewModel : ViewModel() {

    var uid = MutableLiveData("")

    val role = MutableLiveData("null")

    fun checkRole() {
        viewModelScope.launch {
            role.value = Repository.getUser().role
        }
    }

}