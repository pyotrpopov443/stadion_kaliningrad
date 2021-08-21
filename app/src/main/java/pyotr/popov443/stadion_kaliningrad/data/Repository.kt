package pyotr.popov443.stadion_kaliningrad.data

import com.google.android.gms.tasks.Tasks.await
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.getValue
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import pyotr.popov443.stadion_kaliningrad.data.models.Request
import pyotr.popov443.stadion_kaliningrad.data.models.RequestBody
import pyotr.popov443.stadion_kaliningrad.data.models.User

object Repository {

    var uid = ""

    private val database = FirebaseDatabase.getInstance()

    fun addUser(uid: String, full_name: String, email: String) {
        val user = User(email, full_name)
        database.getReference("users/${uid}").setValue(user)
    }

    suspend fun getUser() : User {
        var user = User()
        withContext(Dispatchers.IO) {
            kotlin.runCatching {
                await(database.getReference("users").child(uid).get().addOnSuccessListener {
                    user = it.getValue<User>()!!
                })
            }
        }
        return user
    }

    fun sendRequest(requestList: List<RequestBody>) {
        val requests = database.getReference("request_person")
        val id = requests.push().key.toString()
        requests.child(id).setValue(requestList)
    }

}