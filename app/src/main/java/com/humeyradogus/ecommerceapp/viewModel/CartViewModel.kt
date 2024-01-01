package com.humeyradogus.ecommerceapp.viewModel

import android.view.View
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.humeyradogus.ecommerceapp.data.CartProduct
import com.humeyradogus.ecommerceapp.data.Category
import com.humeyradogus.ecommerceapp.data.Product
import com.humeyradogus.ecommerceapp.firebase.FirebaseCommon
import com.humeyradogus.ecommerceapp.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CartViewModel @Inject constructor(private val firestore: FirebaseFirestore, private val auth: FirebaseAuth,
private val firebaseCommon: FirebaseCommon) : ViewModel(){

    private val _cartProducts = MutableStateFlow<Resource<List<CartProduct>>>(Resource.Unspecified())
    val cartProducts = _cartProducts.asStateFlow()

    private var cartProductDocuments = emptyList<DocumentSnapshot>()

    init{
        getCartProducts()
    }

    fun getCartProducts(){
        viewModelScope.launch {
            _cartProducts.emit(Resource.Loading())
        }
        firestore.collection("user").document(auth.uid!!).collection("cart").addSnapshotListener{ value, error ->
            if (error != null || value == null){
                viewModelScope.launch { _cartProducts.emit(Resource.Error(error?.message.toString()))}
            }else{
                cartProductDocuments = value!!.documents
                val cartProducts = value.toObjects(CartProduct::class.java)
                viewModelScope.launch { _cartProducts.emit(Resource.Success(cartProducts)) }
            }
        }
    }

    fun changeQuantity(cartProduct: CartProduct, quantityChanging: FirebaseCommon.QuantityChanging){
        val index = cartProducts.value.data?.indexOf(cartProduct)

        if(index != null && index != -1){
            val documentId= cartProductDocuments[index].id

            when(quantityChanging){
                FirebaseCommon.QuantityChanging.INCREASE->{
                    increaseQuantity(documentId)
                }
                FirebaseCommon.QuantityChanging.DECREASE->{
                    decreaseQuantity(documentId)
                }
            }
        }

    }

    private fun decreaseQuantity(documentId: String) {
        firebaseCommon.decreaseQuantity(documentId){ result, exception ->
            if (exception != null){
                viewModelScope.launch { _cartProducts.emit(Resource.Error(exception.message.toString())) }
            }
        }
    }

    private fun increaseQuantity(documentId: String) {
        firebaseCommon.increaseQuantity(documentId){ result, exception ->
            if (exception != null){
                viewModelScope.launch { _cartProducts.emit(Resource.Error(exception.message.toString())) }
            }
        }
    }
}