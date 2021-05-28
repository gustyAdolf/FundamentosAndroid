package com.simplegapps.fundamentosandroid.login

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar
import com.simplegapps.fundamentosandroid.R
import com.simplegapps.fundamentosandroid.databinding.ActivityLoginBinding
import com.simplegapps.fundamentosandroid.di.DIProvider
import com.simplegapps.fundamentosandroid.topics.TopicsActivity

class LoginActivity : AppCompatActivity() {

    private val binding: ActivityLoginBinding by lazy { ActivityLoginBinding.inflate(layoutInflater) }
    private val signInFragment: SignInFragment by lazy { SignInFragment.newInstance() }
    private val signUpFragment: SignUpFragment by lazy { SignUpFragment.newInstance() }
    private val viewModel: LoginViewModel by viewModels { DIProvider.loginViewModelProviderFactory }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        viewModel.state.observe(this) {
            when (it) {
                is LoginViewModel.State.SignIn -> moveTo(signInFragment)
                is LoginViewModel.State.SigningIn -> {
                }
                is LoginViewModel.State.SignedIn -> navigateToTopics()
                is LoginViewModel.State.SignUp -> moveTo(signUpFragment)
                is LoginViewModel.State.SigningUp -> {
                }
                is LoginViewModel.State.SignedUp -> navigateToTopics()
            }
        }

        viewModel.loading.observe(this) {
            binding.viewLoading.root.isVisible = it
        }

        viewModel.errorSignIn.observe(this) {
            if (it) {
                Snackbar.make(binding.root, getString(R.string.error_signin), Snackbar.LENGTH_SHORT).show()
            }
        }
    }

    private fun moveTo(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(binding.fragmentContainer.id, fragment)
            .commit()
    }

    private fun navigateToTopics() {
        startActivity(TopicsActivity.createIntent(this))
        finish()
    }
}