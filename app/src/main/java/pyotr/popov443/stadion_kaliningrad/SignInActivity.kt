package pyotr.popov443.stadion_kaliningrad

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import by.kirich1409.viewbindingdelegate.viewBinding
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import pyotr.popov443.stadion_kaliningrad.data.Repository
import pyotr.popov443.stadion_kaliningrad.databinding.ActivitySignInBinding

class SignInActivity : AppCompatActivity() {

    private val binding by viewBinding(ActivitySignInBinding::bind)

    private var auth = FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)

        /*binding.signIn.setOnClickListener {
            val email = binding.inputEmail.text.toString()
            val password = binding.inputPassword.text.toString()
            signIn(email, password)
        }

        binding.signUp.setOnClickListener {
            val email = binding.inputEmail.text.toString()
            val password = binding.inputPassword.text.toString()
            signUp(email, password)
        }*/
    }

    private fun signUp(email: String, password: String) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    Repository.addUser(auth.currentUser!!.uid)
                    start()
                } else {
                    Snackbar
                        .make(binding.root,
                        "Вы ввели некорректные данные или такой пользователь уже зарегистрирован",
                        Snackbar.LENGTH_SHORT)
                        .show()
                }
            }
    }

    private fun signIn(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    start()
                } else {
                    Snackbar
                        .make(binding.root, "Неверные данные", Snackbar.LENGTH_SHORT)
                        .show()
                }
            }
    }

    private fun start() {
        val intent = Intent(this, MainActivity::class.java)
        val user = auth.currentUser!!
        val bundle = bundleOf(
            Pair("name", user.displayName),
            Pair("email", user.email))
        intent.putExtras(bundle)
        startActivity(intent)
    }

}