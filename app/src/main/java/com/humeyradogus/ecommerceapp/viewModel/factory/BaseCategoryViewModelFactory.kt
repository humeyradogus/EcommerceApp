package com.humeyradogus.ecommerceapp.viewModel.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.firestore.FirebaseFirestore
import com.humeyradogus.ecommerceapp.data.Category
import com.humeyradogus.ecommerceapp.viewModel.CategoryViewModel

class BaseCategoryViewModelFactory(private val firestore: FirebaseFirestore, private val category: Category): ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return CategoryViewModel(firestore,category) as T
    }
}