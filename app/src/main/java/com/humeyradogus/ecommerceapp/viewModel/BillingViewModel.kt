package com.humeyradogus.ecommerceapp.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.humeyradogus.ecommerceapp.data.Address
import com.humeyradogus.ecommerceapp.data.CartProduct
import com.humeyradogus.ecommerceapp.firebase.FirebaseCommon
import com.humeyradogus.ecommerceapp.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BillingViewModel @Inject constructor(private val firestore: FirebaseFirestore, private val auth: FirebaseAuth) : ViewModel() {
    private val _adress = MutableStateFlow<Resource<List<Address>>>(Resource.Unspecified())
    val address = _adress.asStateFlow()

    init {
        getUserAddress()
    }

    fun getUserAddress(){
        viewModelScope.launch {
            _adress.emit(Resource.Loading())
        }
        firestore.collection("user").document(auth.uid!!).collection("address").addSnapshotListener{ value, error ->
            if (error != null || value == null){
                viewModelScope.launch { _adress.emit(Resource.Error(error?.message.toString()))}
                return@addSnapshotListener
            }else{
                val address = value?.toObjects(Address::class.java)
                viewModelScope.launch { _adress.emit(Resource.Success(address!!)) }
            }
        }
    }
}