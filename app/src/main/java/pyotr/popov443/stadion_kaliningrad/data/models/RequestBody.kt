package pyotr.popov443.stadion_kaliningrad.data.models

data class RequestBody(
    val uid: String? = "",
    val forwho: String = listOf(),
    val organisation: String? = "",
    val date: String? = "",
    val time: String? = "",
    val purpose: String? = "",
    val confirmed_head: Boolean? = false,
    val confirmed_director: Boolean? = false,
    val dangerous: Boolean? = false,
    val who: String? = "",
    val passport: String? = "")
