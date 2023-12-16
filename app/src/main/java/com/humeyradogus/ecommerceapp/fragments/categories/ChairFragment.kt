package com.humeyradogus.ecommerceapp.fragments.categories

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.firestore.FirebaseFirestore
import com.humeyradogus.ecommerceapp.data.Category
import com.humeyradogus.ecommerceapp.util.Resource
import com.humeyradogus.ecommerceapp.viewModel.CategoryViewModel
import com.humeyradogus.ecommerceapp.viewModel.factory.BaseCategoryViewModelFactory
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import javax.inject.Inject

@AndroidEntryPoint
class ChairFragment: BaseCategoryFragment() {

    @Inject
    lateinit var firestore: FirebaseFirestore

    val viewModel by viewModels<CategoryViewModel> {
        BaseCategoryViewModelFactory(firestore, Category.Chair)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        lifecycleScope.launchWhenStarted{
            viewModel.offerProducts.collectLatest {
                when (it){
                    is Resource.Loading ->{

                    }
                    is Resource.Success ->{
                        offerAdapter.differ.submitList(it.data)
                    }
                    is Resource.Error ->{
                        Snackbar.make(requireView(),it.message.toString(), Snackbar.LENGTH_LONG).show()
                    }
                    else -> Unit
                }
            }
        }

        lifecycleScope.launchWhenStarted{
            viewModel.bestProducts.collectLatest {
                when (it){
                    is Resource.Loading ->{

                    }
                    is Resource.Success ->{
                        bestProductsAdapter.differ.submitList(it.data)
                    }
                    is Resource.Error ->{
                        Snackbar.make(requireView(),it.message.toString(), Snackbar.LENGTH_LONG).show()
                    }
                    else -> Unit
                }
            }
        }
    }


}