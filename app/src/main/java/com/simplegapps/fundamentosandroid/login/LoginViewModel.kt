package com.simplegapps.fundamentosandroid.login

import androidx.lifecycle.*
import com.simplegapps.fundamentosandroid.common.Validators
import com.simplegapps.fundamentosandroid.model.LogIn
import com.simplegapps.fundamentosandroid.repository.Repository

class LoginViewModel(private val repository: Repository) : ViewModel() {
    private val _state: MutableLiveData<State> =
        MutableLiveData<State>().apply { postValue(State.SignIn) }
    private val _signInData = MutableLiveData<SignInData>().apply { postValue(SignInData("", "")) }
    private val _signUpData =
        MutableLiveData<SignUpData>().apply { postValue(SignUpData("", "", "", "")) }
    val state: LiveData<State> = _state
    val signInData: LiveData<SignInData> = _signInData
    val signUpData: LiveData<SignUpData> = _signUpData
    val signInEnabled: LiveData<Boolean> =
        Transformations.map(_signInData) { it?.isValid() ?: false }
    val signUpEnabled: LiveData<Boolean> =
        Transformations.map(_signUpData) { it?.isValid() ?: false }
    val loading: LiveData<Boolean> = Transformations.map(_state) {
        when (it) {
            State.SignIn -> false
            State.SignedIn -> true
            State.SignUp,
            State.SignedUp,
            State.SigningIn,
            State.SigningUp -> true
        }
    }

    val errorSignIn: MutableLiveData<Boolean> = MutableLiveData(false)
    val validUsername: LiveData<Boolean> = Transformations.map(_signInData) { it?.isValidUsername() ?: false}
    val validPassword: LiveData<Boolean> = Transformations.map(_signInData) { it?.isValidPassword() ?: false}

    fun onNewSignInUserName(userName: String) {
        onNewSignInData(_signInData.value?.copy(userName = userName))
    }

    fun onNewSignInPassword(password: String) {
        onNewSignInData(_signInData.value?.copy(password = password))
    }

    fun onNewSignUpUserName(userName: String) {
        onNewSignUpData(_signUpData.value?.copy(userName = userName))
    }

    fun onNewSignUpEmail(email: String) {
        onNewSignUpData(_signUpData.value?.copy(email = email))
    }

    fun onNewSignUpPassword(password: String) {
        onNewSignUpData(_signUpData.value?.copy(password = password))
    }

    fun onNewSignUpConfirmPassword(confirmPassword: String) {
        onNewSignUpData(_signUpData.value?.copy(confirmPassword = confirmPassword))
    }

    private fun onNewSignInData(signInData: SignInData?) {
        signInData?.takeUnless { it == _signInData.value }?.let(_signInData::postValue)
    }

    private fun onNewSignUpData(signUpData: SignUpData?) {
        signUpData?.takeUnless { it == _signUpData.value }?.let(_signUpData::postValue)
    }

    fun moveToSignIn() {
        _state.postValue(State.SignIn)
    }

    fun moveToSignUp() {
        _state.postValue(State.SignUp)
    }

    fun signIn() {
        signInData.value?.takeIf { it.isValid() }?.let {
            repository.signIn(it.userName, it.password) {
                if (it is LogIn.Success) {
                    _state.postValue(State.SignedIn)
                } else {
                    errorSignIn.postValue(true)
                }
            }
        }
    }

    fun signUp() {
        signUpData.value?.takeIf { it.isValid() }?.let {
            repository.signup(it.userName, it.email, it.password) {
                // TODO
            }
        }
    }

    sealed class State {
        object SignIn : State()
        object SigningIn : State()
        object SignedIn : State()
        object SignUp : State()
        object SigningUp : State()
        object SignedUp : State()
    }

    data class SignInData(
        val userName: String,
        val password: String,
    )

    data class SignUpData(
        val email: String,
        val userName: String,
        val password: String,
        val confirmPassword: String,
    )

    class LoginViewModelProviderFactory(private val repository: Repository) :
        ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T = when (modelClass) {
            LoginViewModel::class.java -> LoginViewModel(repository) as T
            else -> throw IllegalArgumentException("LoginViewModelFactory can only create instances of the LoginViewModel")
        }
    }
}


private fun LoginViewModel.SignInData.isValid(): Boolean =
    userName.isNotBlank() && password.isNotBlank()

private fun LoginViewModel.SignInData.isValidUsername(): Boolean =
    Validators.isValidUsername(userName)

private fun LoginViewModel.SignInData.isValidPassword(): Boolean =
    Validators.isValidPassword(password)

private fun LoginViewModel.SignUpData.isValid(): Boolean = userName.isNotBlank() &&
        email.isNotBlank() &&
        password == confirmPassword &&
        password.isNotBlank()

