package pyotr.popov443.stadion_kaliningrad

import android.os.Bundle
import android.util.TypedValue
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import by.kirich1409.viewbindingdelegate.viewBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.getValue
import pyotr.popov443.stadion_kaliningrad.data.Repository
import pyotr.popov443.stadion_kaliningrad.data.models.RequestBody
import pyotr.popov443.stadion_kaliningrad.databinding.ActivityAdminBinding
import java.util.*

class AdminActivity : AppCompatActivity(), RecyclerAdminRequestsAdapter.AdminRequestCallback {

    private val binding by viewBinding(ActivityAdminBinding::bind)

    var listener: ValueEventListener? = null
    private val adapter = RecyclerAdminRequestsAdapter(this, listOf())
    private val requestList = mutableListOf<List<RequestBody>>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin)

        binding.recyclerAdminRequests.layoutManager = GridLayoutManager(applicationContext, 1)
        binding.recyclerAdminRequests.adapter = adapter

        binding.typeActive.setOnClickListener {
            if (!binding.typeActive.isChecked) {
                binding.typeActive.isChecked = true
                binding.typeHistory.isChecked = false
                binding.typeActive.elevation = TypedValue.applyDimension(
                    TypedValue.COMPLEX_UNIT_DIP, 5f, resources.displayMetrics)
                binding.typeHistory.elevation = TypedValue.applyDimension(
                    TypedValue.COMPLEX_UNIT_DIP, 0f, resources.displayMetrics)
                filterRequests()
            }
        }

        binding.typeHistory.setOnClickListener {
            if (!binding.typeHistory.isChecked) {
                binding.typeActive.isChecked = false
                binding.typeHistory.isChecked = true
                binding.typeActive.elevation = TypedValue.applyDimension(
                    TypedValue.COMPLEX_UNIT_DIP, 0f, resources.displayMetrics
                )
                binding.typeHistory.elevation = TypedValue.applyDimension(
                    TypedValue.COMPLEX_UNIT_DIP, 5f, resources.displayMetrics
                )
                filterRequests()
            }
        }

        listener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                requestList.clear()
                for (child in dataSnapshot.children) {
                    val list = mutableListOf<RequestBody>()
                    val request = child.getValue<List<RequestBody>?>()!!
                    val id = child.key
                    val uid = request[0].uid
                    request.forEach {
                        val forWho = it.forwho
                        val organisation = it.organisation
                        val date = it.date
                        val time = it.time
                        val purpose = it.purpose
                        val confirmed_head = it.confirmed_head
                        val confirmed_director = it.confirmed_director
                        val dangerous = it.dangerous
                        val who = it.who
                        list.add(
                            RequestBody(uid, forWho, "", organisation, date, time,
                                purpose, confirmed_head, confirmed_director, dangerous, who, id))
                    }
                    if (list.isNotEmpty()) requestList.add(list)
                }
                filterRequests()
            }
            override fun onCancelled(databaseError: DatabaseError) {}
        }
        FirebaseDatabase.getInstance().getReference("request_person").addValueEventListener(listener!!)

    }

    private fun filterRequests() {
        val list = mutableListOf<List<RequestBody>>()
        requestList.forEach {
            if (binding.typeActive.isChecked &&
                (it[0].dangerous == false && it[0].confirmed_director == false))
                list.add(it)
            if (binding.typeHistory.isChecked &&
                (it[0].dangerous == true || it[0].confirmed_director == true))
                list.add(it)
        }
        adapter.setRequestsList(list)
    }

    override fun onAcceptClickListener(id: String) {
        val requestBody = requestList.find { it[0].id == id } ?: listOf()
        requestBody.forEach {
            it.confirmed_director = true
            it.dangerous = false
        }
        Repository.sendRequest(requestBody, requestBody[0].id)
        filterRequests()
    }

    override fun onDeclineClickListener(id: String) {
        val requestBody = requestList.find { it[0].id == id } ?: listOf()
        requestBody.forEach {
            it.confirmed_director = false
            it.dangerous = true
        }
        Repository.sendRequest(requestBody, requestBody[0].id)
        filterRequests()
    }

}