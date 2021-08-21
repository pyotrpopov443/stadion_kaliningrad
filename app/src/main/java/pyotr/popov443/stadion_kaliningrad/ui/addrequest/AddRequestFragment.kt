package pyotr.popov443.stadion_kaliningrad.ui.addrequest

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ListAdapter
import android.widget.ListView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import by.kirich1409.viewbindingdelegate.viewBinding
import com.google.android.material.snackbar.Snackbar
import pyotr.popov443.stadion_kaliningrad.R
import pyotr.popov443.stadion_kaliningrad.data.Repository
import pyotr.popov443.stadion_kaliningrad.data.models.RequestBody
import pyotr.popov443.stadion_kaliningrad.databinding.FragmentAddRequestBinding


class AddRequestFragment : Fragment(R.layout.fragment_add_request) {

    private val addRequestViewModel: AddRequestViewModel by activityViewModels()

    private val binding by viewBinding(FragmentAddRequestBinding::bind)

    private val list = mutableListOf<String>()
    private val requestList = mutableListOf<RequestBody>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.listForwho.adapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_list_item_1, list
        )

        binding.btnAddForwho.setOnClickListener {
            val forwho = binding.inputForwho.text.toString()
            val passport = binding.inputPassport.text.toString()
            val date = binding.inputDate.text.toString()
            val time = binding.inputTime.text.toString()
            val organisation = binding.inputOrganisation.text.toString()
            val purpose = binding.inputPurpose.text.toString()
            val who = binding.inputWho.text.toString()
            if (forwho.isEmpty() || passport.isEmpty()) return@setOnClickListener
            binding.inputForwho.setText("")
            binding.inputPassport.setText("")

            binding.inputDate.isEnabled = false
            binding.inputTime.isEnabled = false
            binding.inputOrganisation.isEnabled = false
            binding.inputPurpose.isEnabled = false
            binding.inputWho.isEnabled = false

            requestList.add(RequestBody(
                Repository.uid, forwho, passport, organisation, date, time, purpose,
                confirmed_head = false, confirmed_director = false, dangerous = false, who
            ))
            list.add("Заявка на: $forwho\nПаспорт: $passport\nДата: $date\nВремя: $time\n" +
                    "Организация: $organisation\nЦель: $purpose\nФИО: $who")
            binding.listForwho.adapter = ArrayAdapter(
                requireContext(),
                android.R.layout.simple_list_item_1, list
            )
            setListViewHeightBasedOnChildren(binding.listForwho)

            binding.btnSendRequest.visibility = View.VISIBLE
        }

        binding.btnSendRequest.setOnClickListener {
            Snackbar
                .make(binding.root, "Заявка отправлена", Snackbar.LENGTH_SHORT)
                .show()
            Repository.sendRequest(requestList)
            requireActivity().supportFragmentManager.popBackStack();
        }

        addRequestViewModel.username.observe(viewLifecycleOwner, {
            binding.inputForwho.setText(it)
            binding.inputWho.setText(it)
        })

    }

    private fun setListViewHeightBasedOnChildren(myListView: ListView) {
        val adapter: ListAdapter = myListView.adapter
        var totalHeight = 0
        for (i in 0 until adapter.count) {
            val item: View = adapter.getView(i, null, myListView)
            item.measure(0, 0)
            totalHeight += item.measuredHeight
        }
        val params: ViewGroup.LayoutParams = myListView.layoutParams
        params.height = totalHeight + myListView.dividerHeight * (adapter.count - 1)
        myListView.layoutParams = params
    }

}