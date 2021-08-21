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
import pyotr.popov443.stadion_kaliningrad.databinding.FragmentAddRequestBinding


class AddRequestFragment : Fragment(R.layout.fragment_add_request) {

    private val addRequestViewModel: AddRequestViewModel by activityViewModels()

    private val binding by viewBinding(FragmentAddRequestBinding::bind)

    private val list = mutableListOf<String>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.listForwho.adapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_list_item_1, list
        )

        binding.btnAddForwho.setOnClickListener {
            val forwho = binding.inputForwho.text.toString()
            val passport = binding.inputPassport.text.toString()
            if (forwho.isEmpty() || passport.isEmpty()) return@setOnClickListener
            list.add("$forwho $passport")
            binding.listForwho.adapter = ArrayAdapter(
                requireContext(),
                android.R.layout.simple_list_item_1, list
            )
            setListViewHeightBasedOnChildren(binding.listForwho)
            binding.inputForwho.setText("")
            binding.inputPassport.setText("")

            binding.inputDate.isEnabled = false
            binding.inputTime.isEnabled = false
            binding.inputOrganisation.isEnabled = false
            binding.inputPurpose.isEnabled = false
            binding.inputWho.isEnabled = false
        }

        binding.btnSendRequest.setOnClickListener {
            val date = binding.inputDate.text.toString()
            val time = binding.inputTime.text.toString()
            val organisation = binding.inputOrganisation.text.toString()
            val purpose = binding.inputPurpose.text.toString()
            val who = binding.inputWho.text.toString()

            when {
                list.isEmpty() || date.isEmpty() || time.isEmpty() || organisation.isEmpty()
                        || purpose.isEmpty() || who.isEmpty()  -> Snackbar
                    .make(binding.root, "Заполните все поля", Snackbar.LENGTH_SHORT)
                    .show()
                else -> {
                    Snackbar
                        .make(binding.root, "Заявка отправлена", Snackbar.LENGTH_SHORT)
                        .show()
                    Repository.sendRequest(list, date, time, organisation, purpose, who)
                }
            }
        }
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