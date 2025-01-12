package com.yeditepe.mindup.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth

class AuthViewModel : ViewModel() {
    private val auth : FirebaseAuth = FirebaseAuth.getInstance()
    private val _authState = MutableLiveData<AuthState>()
    val authState : LiveData<AuthState> = _authState

    init {
        checkAuthStatus()
    }


    fun checkAuthStatus(){
        if(auth.currentUser == null){
            _authState.value = AuthState.UnAuthenticated
        }
        else{
            _authState.value = AuthState.Authenticated
        }
    }



    fun login(email : String , password  :String){
        if(email.isEmpty() || password.isEmpty()){
            _authState.value = AuthState.Error("Email and password cannot be empty")
            return
        }
        _authState.value = AuthState.Loading
        auth.signInWithEmailAndPassword(email,password)
            .addOnCompleteListener {task ->
                if(task.isSuccessful){
                    _authState.value = AuthState.Authenticated
                }else{
                    _authState.value = AuthState.Error(task.exception?.message ?: "Something went wrong")

                }

            }

    }


    fun signup(email : String , password  :String){
        if(email.isEmpty() || password.isEmpty()){
            _authState.value = AuthState.Error("Email and password cannot be empty")
            return
        }
        _authState.value = AuthState.Loading
        auth.createUserWithEmailAndPassword(email,password)
            .addOnCompleteListener {task ->
                if(task.isSuccessful){
                    _authState.value = AuthState.Authenticated
                }else{
                    _authState.value = AuthState.Error(task.exception?.message ?: "Something went wrong")

                }

            }

    }

    fun signOut(){
        auth.signOut()
        _authState.value = AuthState.UnAuthenticated
    }

    fun getCurrentUser() = auth.currentUser

    fun updateEmail(newEmail: String, onComplete: (Boolean) -> Unit) {
        auth.currentUser?.updateEmail(newEmail)
            ?.addOnCompleteListener { task ->
                onComplete(task.isSuccessful)
                if (task.isSuccessful) {
                    _authState.value = AuthState.Authenticated
                } else {
                    _authState.value = AuthState.Error(task.exception?.message ?: "Failed to update email")
                }
            }
    }

    fun updatePassword(newPassword: String, onComplete: (Boolean) -> Unit) {
        auth.currentUser?.updatePassword(newPassword)
            ?.addOnCompleteListener { task ->
                onComplete(task.isSuccessful)
                if (task.isSuccessful) {
                    _authState.value = AuthState.Authenticated
                } else {
                    _authState.value = AuthState.Error(task.exception?.message ?: "Failed to update password")
                }
            }
    }

    fun deleteAccount(onComplete: (Boolean) -> Unit) {
        auth.currentUser?.delete()
            ?.addOnCompleteListener { task ->
                onComplete(task.isSuccessful)
                if (task.isSuccessful) {
                    _authState.value = AuthState.UnAuthenticated
                } else {
                    _authState.value = AuthState.Error(task.exception?.message ?: "Failed to delete account")
                }
            }
    }

    fun getAuth(): FirebaseAuth = auth
}
sealed class AuthState {
    data object Authenticated : AuthState()
    data object UnAuthenticated : AuthState()
    data object Loading : AuthState()
    data class Error(val message : String) :AuthState()

}