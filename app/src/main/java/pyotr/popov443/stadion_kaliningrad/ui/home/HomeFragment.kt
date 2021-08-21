package pyotr.popov443.stadion_kaliningrad.ui.home

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.util.TypedValue
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.GridLayoutManager
import by.kirich1409.viewbindingdelegate.viewBinding
import kotlinx.coroutines.coroutineScope
import pyotr.popov443.stadion_kaliningrad.CreateRequestActivity
import pyotr.popov443.stadion_kaliningrad.R
import pyotr.popov443.stadion_kaliningrad.data.Repository
import pyotr.popov443.stadion_kaliningrad.databinding.FragmentHomeBinding

class HomeFragment : Fragment(R.layout.fragment_home) {

    private val homeViewModel: HomeViewModel by activityViewModels()

    private val binding by viewBinding(FragmentHomeBinding::bind)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnCreateRequest.setOnClickListener {
            val createRequestIntent = Intent(requireActivity(), CreateRequestActivity::class.java)
            startActivity(createRequestIntent)
        }

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

        homeViewModel.requests.observe(viewLifecycleOwner, {
            adapter.setRequestsList(it)
            if (it.isNotEmpty())
                binding.noRequests.visibility = View.GONE
            else
                binding.noRequests.visibility = View.VISIBLE
        })

        homeViewModel.username.observe(viewLifecycleOwner, {
            binding.textName.text = homeViewModel.username.value!! + "!"
        })

    }

}