package pyotr.popov443.stadion_kaliningrad

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import by.kirich1409.viewbindingdelegate.viewBinding
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import pyotr.popov443.stadion_kaliningrad.data.Repository
import pyotr.popov443.stadion_kaliningrad.databinding.ActivitySignUpBinding

class SignUpActivity : AppCompatActivity() {

    private val binding by viewBinding(ActivitySignUpBinding::bind)

    private var auth = FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        binding.toolbar.setNavigationIcon(R.drawable.ic_backarrow)
        binding.toolbar.navigationIcon!!.setTint(Color.BLACK)
        binding.toolbar.setNavigationOnClickListener {
            this.onBackPressed()
        }

        binding.btnSignIn.setOnClickListener {
            val fullName = binding.inputFullName.text.toString()
            val email = binding.inputEmail.text.toString()
            val password = binding.inputPassword.text.toString()
            val confirmPassword = binding.inputConfirmPassword.text.toString()

            when {
                fullName.isEmpty() -> Snackbar
                    .make(binding.root, "Введите ФИО", Snackbar.LENGTH_SHORT)
                    .show()
                email.isEmpty() -> Snackbar
                    .make(binding.root, "Введите почту", Snackbar.LENGTH_SHORT)
                    .show()
                password.isEmpty() -> Snackbar
                    .make(binding.root, "Введите пароль", Snackbar.LENGTH_SHORT)
                    .show()
                confirmPassword.isEmpty() -> Snackbar
                    .make(binding.root, "Подтвердите пароль", Snackbar.LENGTH_SHORT)
                    .show()
                confirmPassword != password -> Snackbar
                    .make(binding.root, "Пароли не совпадают", Snackbar.LENGTH_SHORT)
                    .show()
                else -> signUp(email, password, fullName)
            }
        }
    }

    private fun signUp(email: String, password: String, fullName: String) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val user = auth.currentUser!!
                    Repository.addUser(user.uid, fullName, email)
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

    private fun start() {
        val intent = Intent(this, UserActivity::class.java)
        Repository.uid = auth.currentUser!!.uid
        startActivity(intent)
    }

}