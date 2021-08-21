package pyotr.popov443.stadion_kaliningrad.ui.profile

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
import pyotr.popov443.stadion_kaliningrad.databinding.FragmentProfileBinding
import pyotr.popov443.stadion_kaliningrad.ui.home.HomeViewModel

class ProfileFragment : Fragment(R.layout.fragment_profile) {

    private val menuViewModel: ProfileViewModel by activityViewModels()

    private val binding by viewBinding(FragmentProfileBinding::bind)

}