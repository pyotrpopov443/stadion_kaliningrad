package pyotr.popov443.stadion_kaliningrad.ui.home

import android.content.Intent
import android.os.Bundle
import android.util.TypedValue
import android.view.View
import androidx.core.view.isNotEmpty
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.GridLayoutManager
import by.kirich1409.viewbindingdelegate.viewBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.getValue
import pyotr.popov443.stadion_kaliningrad.R
import pyotr.popov443.stadion_kaliningrad.data.Repository
import pyotr.popov443.stadion_kaliningrad.data.models.Request
import pyotr.popov443.stadion_kaliningrad.data.models.RequestBody
import pyotr.popov443.stadion_kaliningrad.databinding.FragmentHomeBinding
import java.lang.Exception
import java.util.*

class HomeFragment : Fragment(R.layout.fragment_home) {

    private val homeViewModel: HomeViewModel by activityViewModels()

    private val binding by viewBinding(FragmentHomeBinding::bind)

    val requestList = mutableListOf<Request>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.typeActive.setOnClickListener {
            if (!binding.typeActive.isChecked) {
                binding.typeActive.isChecked = true
                binding.typeHistory.isChecked = false
                binding.typeActive.elevation = TypedValue.applyDimension(
                    TypedValue.COMPLEX_UNIT_DIP, 5f, resources.displayMetrics)
                binding.typeHistory.elevation = TypedValue.applyDimension(
                    TypedValue.COMPLEX_UNIT_DIP, 0f, resources.displayMetrics)
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
            }
        }

        val adapter = RecyclerRequestsAdapter(listOf())
        binding.recyclerRequests.layoutManager = GridLayoutManager(context, 1)
        binding.recyclerRequests.adapter = adapter

        val postListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                requestList.clear()
                for (child in dataSnapshot.children) {
                    val request = child.getValue<List<RequestBody>>()!!
                    if (request[0].uid == Repository.uid && upToDate(request[0].date)) {
                        var forWho = request[0].forwho
                        request.forEachIndexed { i, it ->
                            if (i > 0) forWho += ", " + it.forwho
                        }
                        requestList.add(
                            Request(forWho, request[0].organisation,
                                request[0].date, request[0].time, status(request[0]) )
                        )
                    }
                }
                try {
                    if (requestList.isNotEmpty())
                        binding.noRequests.visibility = View.GONE
                    else
                        binding.noRequests.visibility = View.VISIBLE
                } catch (e: Exception) {

                }
                adapter.setRequestsList(requestList)
            }
            override fun onCancelled(databaseError: DatabaseError) {}
        }
        FirebaseDatabase.getInstance().getReference("request_person").addValueEventListener(postListener)

        homeViewModel.username.observe(viewLifecycleOwner, {
            binding.textName.text = homeViewModel.username.value!! + "!"
        })

    }

    private fun status(request: RequestBody) : String {
        if (request.dangerous == true) return "отклонена"
        if (request.confirmed_director == true) return "одобрена"
        return "в ожидании"
    }

    private fun upToDate(date: String?) : Boolean {
        if (date == null) return false
        val data = date.split(".")
        val day = data[0].toInt()
        val month = data[1].toInt()
        val year = data[2].toInt()

        val today = java.util.Calendar.getInstance()
        val currentDay = today.get(Calendar.DAY_OF_MONTH)
        val currentMonth = today.get(Calendar.MONTH)
        val currentYear = today.get(Calendar.YEAR)

        if (year > currentYear) return true
        else if (year == currentYear && month > currentMonth) return true
        else if (year == currentYear && month == currentMonth && day > currentDay) return true
        return false
    }

}