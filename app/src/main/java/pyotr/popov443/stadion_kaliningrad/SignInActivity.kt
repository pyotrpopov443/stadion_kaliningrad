package pyotr.popov443.stadion_kaliningrad

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
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

        binding.btnNoAccount.setOnClickListener {
            val signUpIntent = Intent(this, SignUpActivity::class.java)
            startActivity(signUpIntent)
        }

        binding.btnSignIn.setOnClickListener {
            val email = binding.inputEmail.text.toString()
            val password = binding.inputPassword.text.toString()
            when {
                email.isEmpty() -> Snackbar
                    .make(binding.root, "Введите почту", Snackbar.LENGTH_SHORT)
                    .show()
                password.isEmpty() -> Snackbar
                    .make(binding.root, "Введите пароль", Snackbar.LENGTH_SHORT)
                    .show()
                else -> signIn(email, password)
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
                        .make(binding.root, "Неверно введены данные или пользователя не существует", Snackbar.LENGTH_SHORT)
                        .show()
                }
            }
    }

    private fun start() {
        val intent = Intent(this, UserActivity::class.java)
        Repository.uid = auth.currentUser!!.uid
        startActivity(intent)
    }

}