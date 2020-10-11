package com.branch.chat.screens

import android.content.Intent
import android.os.Bundle
import android.view.View.GONE
import android.view.View.VISIBLE
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.branch.chat.R
import com.branch.chat.databinding.ActivityLoginBinding
import com.branch.chat.network.STATUS
import com.branch.chat.utils.PreferenceManager
import com.branch.chat.utils.Utils
import com.branch.chat.viewmodels.LoginViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.view_progress_overlay.*
import javax.inject.Inject

@AndroidEntryPoint
class LoginActivity : AppCompatActivity(R.layout.activity_login) {

    private val viewModel: LoginViewModel by viewModels()
    private lateinit var binding: ActivityLoginBinding

    @Inject
    lateinit var preferenceManager: PreferenceManager
    @Inject
    lateinit var utils: Utils

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (preferenceManager.isLoggedIn) {
            performLogin()
            finish()
        }

        binding = DataBindingUtil.setContentView(this, R.layout.activity_login)
        binding.loginViewModel = viewModel
        observeData()
    }

    private fun observeData() {
        viewModel.apply {

            loginModel.observe(this@LoginActivity, { loginModel ->
                when {
                    loginModel.email == null -> emailEditText.error = "Invalid Entry"
                    loginModel.password == null -> passwordEditText.error = "Invalid Entry"
                    else -> viewModel.login(loginModel)
                }
            })

            loadingStatus.observe(this@LoginActivity, { status ->
                when (status) {
                    is STATUS.LOADING -> {
                        showLoading(true)
                    }
                    is STATUS.FAILED -> {
                        utils.showToast(status.message)
                        showLoading(false)
                    }
                    is STATUS.SUCCESS -> {
                        performLogin()
                    }
                }
            })
        }
    }

    private fun performLogin() {
        startActivity(Intent(this@LoginActivity, MainActivity::class.java))
    }

    private fun showLoading(show: Boolean) {
        progressOverlay.visibility = if (show) VISIBLE else GONE
    }
}