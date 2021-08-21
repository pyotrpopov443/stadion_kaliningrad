package pyotr.popov443.stadion_kaliningrad

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import pyotr.popov443.stadion_kaliningrad.data.models.RequestBody
import pyotr.popov443.stadion_kaliningrad.databinding.ItemAccessBinding

class RecyclerAdminRequestsAdapter(val callback: AdminRequestCallback,
                                   private var list: List<List<RequestBody>>) :
    RecyclerView.Adapter<RecyclerAdminRequestsAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemAccessBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.textFullName.text = list[position][0].who
        if (list[position][0].confirmed_head == true)
            holder.binding.imageHead.setImageResource(R.drawable.ic_accept)
        if (list[position][0].dangerous == true)
            holder.binding.imageSecurity.setImageResource(R.drawable.ic_decline)
        else holder.binding.imageSecurity.setImageResource(R.drawable.ic_wait)

        holder.binding.imageAccept.setImageResource(R.drawable.ic_accept_gray)
        holder.binding.imageDecline.setImageResource(R.drawable.ic_decline_gray)
        if (list[position][0].dangerous == true) {
            holder.binding.imageAccept.setImageResource(R.drawable.ic_accept_gray)
            holder.binding.imageDecline.setImageResource(R.drawable.ic_decline)
        }
        if (list[position][0].confirmed_director == true) {
            holder.binding.imageAccept.setImageResource(R.drawable.ic_accept)
            holder.binding.imageDecline.setImageResource(R.drawable.ic_decline_gray)
        }

        val purpose = list[position][0].purpose
        val organisation = list[position][0].organisation
        var forWho = list[position][0].forwho
        list[position].forEachIndexed { i, it ->
            if(i > 0) forWho += ", " + it.forwho
        }
        val date = list[position][0].date
        val time = list[position][0].time

        holder.binding.textInfo.text = "Цель визита: $purpose\nОрганизация: $organisation\n" +
                "Посетители: $forWho\nДата посещения: $date $time"

        holder.binding.root.setOnClickListener {
            if (holder.binding.textInfo.visibility == View.VISIBLE)
                holder.binding.textInfo.visibility = View.GONE
            else holder.binding.textInfo.visibility = View.VISIBLE
        }

        holder.binding.imageAccept.setOnClickListener {
            callback.onAcceptClickListener(list[position][0].id!!)
        }
        holder.binding.imageDecline.setOnClickListener {
            callback.onDeclineClickListener(list[position][0].id!!)
        }
    }

    fun setRequestsList(requestsList: List<List<RequestBody>>) {
        list = requestsList
        notifyDataSetChanged()
    }

    override fun getItemCount() = list.size

    inner class ViewHolder(val binding: ItemAccessBinding) :
        RecyclerView.ViewHolder(binding.root)

    interface AdminRequestCallback {

        fun onAcceptClickListener(id: String)
        fun onDeclineClickListener(id: String)

    }

}