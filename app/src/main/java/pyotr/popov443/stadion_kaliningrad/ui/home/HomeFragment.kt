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
                    val request = child.getValue<RequestBody>()!!
                    if (request.uid == Repository.uid)
                        requestList.add(
                            Request(request.list,
                                request.organisation,
                                request.date,
                                request.time, "не проверено")
                        )
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

}