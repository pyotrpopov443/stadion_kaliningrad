package pyotr.popov443.stadion_kaliningrad.ui.info

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import by.kirich1409.viewbindingdelegate.viewBinding
import pyotr.popov443.stadion_kaliningrad.R
import pyotr.popov443.stadion_kaliningrad.databinding.FragmentHomeBinding
import pyotr.popov443.stadion_kaliningrad.databinding.FragmentInfoBinding
import pyotr.popov443.stadion_kaliningrad.ui.home.HomeViewModel

class InfoFragment : Fragment(R.layout.fragment_info) {

    private val menuViewModel: InfoViewModel by activityViewModels()

    private val binding by viewBinding(FragmentInfoBinding::bind)

}