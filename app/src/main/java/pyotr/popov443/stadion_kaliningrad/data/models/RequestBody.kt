package pyotr.popov443.stadion_kaliningrad.data.models

data class RequestBody(
    val uid: String? = "",
    val forwho: String? = "",
    val passport: String? = "",
    val organisation: String? = "",
    val date: String? = "",
    val time: String? = "",
    val purpose: String? = "",
    val confirmed_head: Boolean? = false,
    var confirmed_director: Boolean? = false,
    var dangerous: Boolean? = false,
    val who: String? = "",
    var id: String? = "")
