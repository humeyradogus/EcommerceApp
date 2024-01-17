package com.humeyradogus.ecommerceapp.fragments.shopping

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.humeyradogus.ecommerceapp.R
import com.humeyradogus.ecommerceapp.adapters.AddressAdapter
import com.humeyradogus.ecommerceapp.adapters.BillingProductsAdapter
import com.humeyradogus.ecommerceapp.data.Address
import com.humeyradogus.ecommerceapp.data.CartProduct
import com.humeyradogus.ecommerceapp.data.Order
import com.humeyradogus.ecommerceapp.data.OrderStatus
import com.humeyradogus.ecommerceapp.databinding.FragmentBillingBinding
import com.humeyradogus.ecommerceapp.util.Resource
import com.humeyradogus.ecommerceapp.viewModel.BillingViewModel
import com.humeyradogus.ecommerceapp.viewModel.OrderViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest

@AndroidEntryPoint
class BillingFragment : Fragment() {
    private lateinit var binding: FragmentBillingBinding
    private val addressAdapter by lazy { AddressAdapter() }
    private val billingProductsAdapter by lazy { BillingProductsAdapter() }
    private val billingViewModel by viewModels<BillingViewModel>()
    private val args by navArgs<BillingFragmentArgs>()
    private var products = emptyList<CartProduct>()
    private var totalPrice = 0f

    private var selectedAddress: Address? = null
    private val orderViewModel by viewModels<OrderViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        products = args.products.toList()
        totalPrice = args.totalPrice

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentBillingBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpBillingProductsRv()
        setUpAddressRv()

        if(!args.payment){
            binding.apply {
                buttonPlaceOrder.visibility = View.INVISIBLE
                totalBoxContainer.visibility = View.INVISIBLE
                middleLine.visibility = View.INVISIBLE
                bottomLine.visibility = View.INVISIBLE
            }
        }

        binding.imageAddAddress.setOnClickListener {
            findNavController().navigate(R.id.action_billingFragment_to_addressFragment)
        }

        lifecycleScope.launchWhenStarted{
            billingViewModel.address.collectLatest {
                when (it){
                    is Resource.Loading ->{
                        binding.progressbarAddress.visibility = View.VISIBLE
                    }
                    is Resource.Success ->{
                        addressAdapter.differ.submitList(it.data)
                        binding.progressbarAddress.visibility = View.GONE
                    }
                    is Resource.Error ->{
                        Toast.makeText(requireContext(),"error ${it.message}", Toast.LENGTH_LONG).show()
                    }
                    else -> Unit
                }
            }
        }

        lifecycleScope.launchWhenStarted{
            orderViewModel.order.collectLatest {
                when (it){
                    is Resource.Loading ->{
                    }
                    is Resource.Success ->{
                        findNavController().navigateUp()
                        Snackbar.make(requireView(),"Your order was placed", Snackbar.LENGTH_LONG).show()
                    }
                    is Resource.Error ->{
                        Toast.makeText(requireContext(),"error ${it.message}", Toast.LENGTH_LONG).show()
                    }
                    else -> Unit
                }
            }
        }

        billingProductsAdapter.differ.submitList(products)

        binding.tvTotalPrice.text = "$ ${totalPrice}"

        addressAdapter.onClick = {
            selectedAddress = it
            if (!args.payment){
                val b = Bundle().apply { putParcelable("address",selectedAddress) }
                findNavController().navigate(R.id.action_billingFragment_to_addressFragment,b)
            }
        }
        binding.buttonPlaceOrder.setOnClickListener {
            if(selectedAddress == null){
                Toast.makeText(requireContext(),"Please select an address.", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }
            showOrderConfirmationDialog()
        }

    }

    private fun showOrderConfirmationDialog() {
        val alertDialog = AlertDialog.Builder(requireContext()).apply {
            setTitle("Order items")
            setMessage("Do you want to order your cart items?")
            setNegativeButton("Cancel"){ dialog, _ ->
                dialog.dismiss()
            }
            setPositiveButton("Yes"){ dialog, _ ->
                val order = Order(OrderStatus.Ordered.status,totalPrice,products,selectedAddress!!)
                orderViewModel.placeOrder(order)
                dialog.dismiss()
            }
        }
        alertDialog.create()
        alertDialog.show()
    }

    private fun setUpAddressRv() {
        binding.rvAddress.apply {
            layoutManager = LinearLayoutManager(requireContext(),RecyclerView.HORIZONTAL,false)
            adapter = addressAdapter
        }
    }

    private fun setUpBillingProductsRv() {
        binding.rvProducts.apply {
            layoutManager = LinearLayoutManager(requireContext(),RecyclerView.HORIZONTAL,false)
            adapter = billingProductsAdapter
        }
    }
}