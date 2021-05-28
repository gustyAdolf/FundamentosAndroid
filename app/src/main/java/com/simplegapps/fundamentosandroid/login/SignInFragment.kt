package com.simplegapps.fundamentosandroid.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.simplegapps.fundamentosandroid.R
import com.simplegapps.fundamentosandroid.common.TextChangedWatcher
import com.simplegapps.fundamentosandroid.databinding.FragmentSignInBinding

class SignInFragment : Fragment() {
    private val viewModel: LoginViewModel by activityViewModels();

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return FragmentSignInBinding.inflate(inflater, container, false).apply {
            labelCreateAccount.setOnClickListener {
                viewModel.moveToSignUp()
            }

            viewModel.signInData.observe(viewLifecycleOwner) {
                inputUserName.apply {
                    setText(it.userName)
                    setSelection(it.userName.length)
                }
                inputPassword.apply {
                    setText(it.password)
                    setSelection(it.password.length)
                }
            }

            viewModel.signInEnabled.observe(viewLifecycleOwner) {
                buttonLogin.isEnabled = it
            }

            viewModel.validUsername.observe(viewLifecycleOwner) {
                usernameLayout.error = null
                if (!it) usernameLayout.error = getString(R.string.error_label_username)
            }

            viewModel.validPassword.observe(viewLifecycleOwner) {
                passwordLayout.error = null
                if (!it) passwordLayout.error = getString(R.string.error_label_password)
            }

            inputUserName.apply {
                addTextChangedListener(TextChangedWatcher(viewModel::onNewSignInUserName))
            }

            inputPassword.apply {
                addTextChangedListener(TextChangedWatcher(viewModel::onNewSignInPassword))
            }

            buttonLogin.setOnClickListener {

                viewModel.signIn()
            }




        }.root
    }

    companion object {
        fun newInstance(): SignInFragment = SignInFragment()
    }
}