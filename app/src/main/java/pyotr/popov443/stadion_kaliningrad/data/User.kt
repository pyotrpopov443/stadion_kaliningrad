package pyotr.popov443.stadion_kaliningrad.data

import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
data class User(val username: String? = null, val email: String? = null)
