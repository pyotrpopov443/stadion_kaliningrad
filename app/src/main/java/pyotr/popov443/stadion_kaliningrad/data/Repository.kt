package pyotr.popov443.stadion_kaliningrad.data

import com.google.firebase.database.FirebaseDatabase

object Repository {

    private val database = FirebaseDatabase.getInstance()

    fun addUser(id: String) {
        val user = User("petr", "myemail")
        database.getReference("users/${id}").setValue(user)
    }

}