package pyotr.popov443.stadion_kaliningrad.ui.home

import android.content.Intent
import android.os.Bundle
import android.util.Log
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
import java.time.LocalDate
import java.util.*

class HomeFragment : Fragment(R.layout.fragment_home) {

    private val homeViewModel: HomeViewModel by activityViewModels()

    private val binding by viewBinding(FragmentHomeBinding::bind)

    var listener: ValueEventListener? = null
    private val adapter = RecyclerRequestsAdapter(listOf())
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

        binding.recyclerRequests.layoutManager = GridLayoutManager(context, 1)
        binding.recyclerRequests.adapter = adapter

        listener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                requestList.clear()
                for (child in dataSnapshot.children) {
                    val request = child.getValue<List<RequestBody>>()!!
                    if (request[0].uid == Repository.uid) {
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
                if (requestList.isNotEmpty())
                    binding.noRequests.visibility = View.GONE
                else
                    binding.noRequests.visibility = View.VISIBLE
                filterRequests()
            }
            override fun onCancelled(databaseError: DatabaseError) {}
        }
        FirebaseDatabase.getInstance().getReference("request_person").addValueEventListener(listener!!)

        homeViewModel.username.observe(viewLifecycleOwner, {
            binding.textName.text = homeViewModel.username.value!! + "!"
        })

    }

    override fun onDestroyView() {
        if (listener != null)
            FirebaseDatabase.getInstance().getReference("request_person").removeEventListener(listener!!)
        super.onDestroyView()
    }

    private fun status(request: RequestBody) : String {
        if (request.dangerous == true) return "отклонена"
        if (request.confirmed_director == true) return "одобрена"
        return "в ожидании"
    }

    private fun filterRequests() {
        val list = mutableListOf<Request>()
        requestList.forEach {
            if (toShow(it.date)) list.add(it)
        }
        adapter.setRequestsList(list)
    }

    private fun toShow(date: String?) : Boolean {
        if (date == null) return false
        return if (binding.typeActive.isChecked)
            upToDate(date)
        else !upToDate(date)
    }

    private fun upToDate(date: String) : Boolean {
        val data = date.split(".")
        val day = data[0].toInt()
        val month = data[1].toInt()-1
        val year = data[2].toInt()
        val time = Date(year, month, day).time

        val today = Calendar.getInstance()
        val currentDay = today.get(Calendar.DAY_OF_MONTH)
        val currentMonth = today.get(Calendar.MONTH)
        val currentYear = today.get(Calendar.YEAR)
        val currentTime = Date(currentYear, currentMonth, currentDay).time

        return time > currentTime
    }

}