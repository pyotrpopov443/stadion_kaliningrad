package pyotr.popov443.stadion_kaliningrad

import android.os.Bundle
import android.widget.ArrayAdapter
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import by.kirich1409.viewbindingdelegate.viewBinding
import com.google.android.material.snackbar.Snackbar
import pyotr.popov443.stadion_kaliningrad.data.Repository
import pyotr.popov443.stadion_kaliningrad.databinding.ActivityCreateRequestBinding

class CreateRequestActivity : AppCompatActivity() {

    private val createRequestViewModel: CreateRequestViewModel by viewModels()

    private val binding by viewBinding(ActivityCreateRequestBinding::bind)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_request)

        binding.btnSendRequest.setOnClickListener {
            val forwho = binding.inputForwho.text.toString()
            val date = binding.inputDate.text.toString()
            val time = binding.inputTime.text.toString()
            val organisation = binding.inputOrganisation.text.toString()
            val purpose = binding.inputPurpose.text.toString()
            val who = binding.inputWho.text.toString()
            val passport = binding.inputPassport.text.toString()

            when {
                forwho.isEmpty() || date.isEmpty() || time.isEmpty() || organisation.isEmpty()
                        || purpose.isEmpty() || who.isEmpty() || passport.isEmpty()  -> Snackbar
                    .make(binding.root, "Заполните все поля", Snackbar.LENGTH_SHORT)
                    .show()
                else -> {
                    Repository.sendRequest(forwho, date, time, organisation, purpose, who, passport)
                }
            }
        }

    }



}