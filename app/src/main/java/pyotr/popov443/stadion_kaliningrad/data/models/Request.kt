package pyotr.popov443.stadion_kaliningrad.data.models

data class Request(
    val list: List<String>? = listOf(),
    val organisation: String? = "",
    val date: String? = "",
    val time: String? = "",
    val state: String? = "")
