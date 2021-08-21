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

     suspend fun getRequests() : List<Request> {
         val list = mutableListOf<Request>()
         withContext(Dispatchers.IO) {
             kotlin.runCatching {
                 await(database.getReference("request_person").get().addOnSuccessListener {
                     for (child in it.children) {
                         val request = child.getValue<RequestBody>()!!
                         if (request.uid == uid)
                             list.add(
                                 Request(request.forwho,
                                     request.organisation,
                                     request.date,
                                     request.time, "не проверено"))
                     }
                 })
             }
         }
         return list
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

    fun sendRequest(forwho: String, date: String, time: String,
                    organisation: String, purpose: String, who: String, passport: String) {
        val requests = database.getReference("request_person")
        val id = requests.push().key.toString()
        requests.child(id).setValue(RequestBody(
            uid,  forwho, organisation, date, time, purpose, false,
            confirmed_director = false,
            dangerous = false,
            who = who,
            passport = passport
        ))
    }

}