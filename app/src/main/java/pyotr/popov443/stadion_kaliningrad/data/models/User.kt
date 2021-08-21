package pyotr.popov443.stadion_kaliningrad.data.models

import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
data class User(val email: String? = null, val username: String? = null)
