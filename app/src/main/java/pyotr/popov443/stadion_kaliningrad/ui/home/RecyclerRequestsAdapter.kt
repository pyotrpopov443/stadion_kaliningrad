package pyotr.popov443.stadion_kaliningrad.ui.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import pyotr.popov443.stadion_kaliningrad.data.models.Request
import pyotr.popov443.stadion_kaliningrad.data.models.RequestBody
import pyotr.popov443.stadion_kaliningrad.databinding.ItemRequestBinding

class RecyclerRequestsAdapter(private var list: List<Request>) :
    RecyclerView.Adapter<RecyclerRequestsAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemRequestBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.forwho.text = list[position].forwho
        holder.binding.organisation.text = list[position].organisation
        holder.binding.date.text = list[position].date
        holder.binding.time.text = list[position].time
        holder.binding.state.text = list[position].state
    }

    fun setRequestsList(requestsList: List<Request>) {
        list = requestsList
        notifyDataSetChanged()
    }

    override fun getItemCount() = list.size

    inner class ViewHolder(val binding: ItemRequestBinding) :
        RecyclerView.ViewHolder(binding.root)

}