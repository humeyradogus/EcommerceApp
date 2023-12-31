package com.humeyradogus.ecommerceapp.fragments.categories

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.humeyradogus.ecommerceapp.R
import com.humeyradogus.ecommerceapp.adapters.BestDealsAdapter
import com.humeyradogus.ecommerceapp.adapters.BestProductAdapter
import com.humeyradogus.ecommerceapp.adapters.SpecialProductsAdapter
import com.humeyradogus.ecommerceapp.databinding.FragmentHomeBinding
import com.humeyradogus.ecommerceapp.databinding.FragmentMainCategoryBinding
import com.humeyradogus.ecommerceapp.util.Resource
import com.humeyradogus.ecommerceapp.util.showBottomNavigationView
import com.humeyradogus.ecommerceapp.viewModel.MainCategoryViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest

private val TAG = "MainCategoryFragment"
@AndroidEntryPoint
class MainCategoryFragment: Fragment(R.layout.fragment_main_category) {
    private lateinit var binding: FragmentMainCategoryBinding
    private lateinit var specialProductsAdapter: SpecialProductsAdapter
    private lateinit var bestDealsAdapter: BestDealsAdapter
    private lateinit var bestProductAdapter: BestProductAdapter
    private val viewModel by viewModels<MainCategoryViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMainCategoryBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupSpecialProductsRv()
        setupBestDealsRv()
        setupBestProductsRv()

        specialProductsAdapter.onClick = {
            val b = Bundle().apply { putParcelable("product",it) }
            findNavController().navigate(R.id.action_homeFragment_to_productDetailsFragment, b)
        }

        bestDealsAdapter.onClick = {
            val b = Bundle().apply { putParcelable("product",it) }
            findNavController().navigate(R.id.action_homeFragment_to_productDetailsFragment, b)
        }

        bestProductAdapter.onClick = {
            val b = Bundle().apply { putParcelable("product",it) }
            findNavController().navigate(R.id.action_homeFragment_to_productDetailsFragment, b)
        }

        lifecycleScope.launchWhenStarted{
            viewModel.specialProducts.collectLatest {
                when (it){
                    is Resource.Loading ->{
                        showLoading()
                    }
                    is Resource.Success ->{
                        specialProductsAdapter.differ.submitList(it.data)
                        hideLoading()
                    }
                    is Resource.Error ->{
                        hideLoading()
                        Log.e(TAG,it.message.toString())
                        Toast.makeText(requireContext(),it.message,Toast.LENGTH_LONG).show()
                    }
                    else -> Unit
                }
            }
        }


        lifecycleScope.launchWhenStarted {
            viewModel.bestDealsProducts.collectLatest {
                when (it){
                    is Resource.Loading ->{
                        showLoading()
                    }
                    is Resource.Success ->{
                        bestDealsAdapter.differ.submitList(it.data)
                        hideLoading()
                    }
                    is Resource.Error ->{
                        hideLoading()
                        Log.e(TAG,it.message.toString())
                        Toast.makeText(requireContext(),it.message,Toast.LENGTH_LONG).show()
                    }
                    else -> Unit
                }
            }
        }


        lifecycleScope.launchWhenStarted {
            viewModel.bestProducts.collectLatest {
                when (it){
                    is Resource.Loading ->{
                        showLoading()
                    }
                    is Resource.Success ->{
                        bestProductAdapter.differ.submitList(it.data)
                        hideLoading()
                    }
                    is Resource.Error ->{
                        hideLoading()
                        Log.e(TAG,it.message.toString())
                        Toast.makeText(requireContext(),it.message,Toast.LENGTH_LONG).show()
                    }
                    else -> Unit
                }
            }
        }
    }

    private fun setupBestProductsRv() {
        bestProductAdapter = BestProductAdapter()
        binding.rvBestProducts.apply {
            layoutManager = GridLayoutManager(requireContext(),2,GridLayoutManager.VERTICAL,false)
            adapter = bestProductAdapter
        }
    }

    private fun setupBestDealsRv() {
        bestDealsAdapter = BestDealsAdapter()
        binding.rvBestDealsProducts.apply {
            layoutManager = LinearLayoutManager(requireContext(),LinearLayoutManager.HORIZONTAL,false)
            adapter = bestDealsAdapter
        }
    }

    private fun hideLoading() {
        binding.mainCategoryProgressBar.visibility = View.GONE
    }

    private fun showLoading() {
        binding.mainCategoryProgressBar.visibility = View.VISIBLE
    }

    private fun setupSpecialProductsRv() {
        specialProductsAdapter = SpecialProductsAdapter()
        binding.rvSpecialProducts.apply {
            layoutManager = LinearLayoutManager(requireContext(),LinearLayoutManager.HORIZONTAL,false)
            adapter = specialProductsAdapter
        }
    }

    override fun onResume() {
        super.onResume()

        showBottomNavigationView()
    }
}