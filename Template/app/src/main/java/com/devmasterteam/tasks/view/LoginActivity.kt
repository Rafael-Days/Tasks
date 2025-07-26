package com.devmasterteam.tasks.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowInsetsCompat
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import com.devmasterteam.tasks.R
import com.devmasterteam.tasks.databinding.ActivityLoginBinding
import com.devmasterteam.tasks.viewmodel.LoginViewModel
import java.util.concurrent.Executor

class LoginActivity : AppCompatActivity(), View.OnClickListener {

    private val viewModel: LoginViewModel by viewModels()
    private val binding: ActivityLoginBinding by lazy { ActivityLoginBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        supportActionBar?.hide()

        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(
                systemBars.left + binding.main.paddingStart, systemBars.top,
                systemBars.right + binding.main.paddingEnd, systemBars.bottom
            )
            insets
        }

        viewModel.verifyAuthentication()

        // Eventos
        binding.buttonLogin.setOnClickListener(this)
        binding.textRegister.setOnClickListener(this)

        // Observadores
        observe()
    }

    override fun onClick(v: View) {
        if (v.id == R.id.button_login) {
            handleLogin()
        } else if (v.id == R.id.text_register) {
            startActivity(Intent(this, RegisterActivity::class.java))
        }
    }

    private fun observe() {
        viewModel.login.observe(this){
            if(it.status()){
                startActivity(Intent(applicationContext, MainActivity::class.java))
            } else {
                Toast.makeText(applicationContext, it.message(), Toast.LENGTH_SHORT).show()
            }
        }

        viewModel.loggedUser.observe(this){
            if(it){
                showAuthentication()
            }
        }
    }

    private fun showAuthentication(){
        val executor: Executor = ContextCompat.getMainExecutor(this)

        val biometricPrompt = BiometricPrompt(this, executor,
            object : BiometricPrompt.AuthenticationCallback() {
                override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                    // Lógica de acordo com a aplicação
                    super.onAuthenticationSucceeded(result)
                    startActivity(Intent(applicationContext, MainActivity::class.java))
                    finish()
                }

                override fun onAuthenticationFailed() {
                    super.onAuthenticationFailed()
                }

                override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                    // Lógica de acordo com a aplicação
                    super.onAuthenticationError(errorCode, errString)
                }
            })

        val info: BiometricPrompt.PromptInfo = BiometricPrompt.PromptInfo.Builder()
            .setTitle("Impressão Digital")
            .setSubtitle("Etapa de Segurança")
            .setDescription("Bota o dedo ai")
            .setNegativeButtonText("Cancelar")
            .build()

        // Exibe para o usuário
        biometricPrompt.authenticate(info)
    }

    private fun handleLogin() {
        val email = binding.editEmail.text.toString()
        val password = binding.editPassword.text.toString()

        viewModel.login(email, password)
    }
}