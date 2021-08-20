package pyotr.popov443.stadion_kaliningrad

import android.os.Bundle
import android.os.PersistableBundle
import androidx.appcompat.app.AppCompatActivity
import by.kirich1409.viewbindingdelegate.viewBinding
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseUser
import pyotr.popov443.stadion_kaliningrad.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private val binding by viewBinding(ActivityMainBinding::bind)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val name = intent.extras!!.getString("name")
        val email = intent.extras!!.getString("email")

        binding.textView.text = "$name $email"
    }

}