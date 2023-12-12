package com.humeyradogus.ecommerceapp.fragments.loginRegister

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.humeyradogus.ecommerceapp.R
import com.humeyradogus.ecommerceapp.activities.ShoppingActivity
import com.humeyradogus.ecommerceapp.databinding.FragmentIntroductionBinding
import com.humeyradogus.ecommerceapp.databinding.FragmentLoginBinding
import com.humeyradogus.ecommerceapp.util.Resource
import com.humeyradogus.ecommerceapp.viewModel.IntroductionViewModel
import com.humeyradogus.ecommerceapp.viewModel.IntroductionViewModel.Companion.ACCOUNT_OPTIONS_FRAGMENT
import com.humeyradogus.ecommerceapp.viewModel.IntroductionViewModel.Companion.SHOPPING_ACTIVITY
import com.humeyradogus.ecommerceapp.viewModel.MainCategoryViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest

@AndroidEntryPoint
class IntroductionFragment : Fragment(R.layout.fragment_introduction) {
    private lateinit var binding: FragmentIntroductionBinding
    private val viewModel by viewModels<IntroductionViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentIntroductionBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        lifecycleScope.launchWhenStarted {
            viewModel.navigate.collectLatest {
                when (it) {
                    SHOPPING_ACTIVITY -> {
                        Intent(requireActivity(), ShoppingActivity::class.java).also { intent ->
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                            startActivity(intent)
                        }
                    }
                        ACCOUNT_OPTIONS_FRAGMENT -> {
                            findNavController().navigate(R.id.action_introductionFragment_to_accountOptionsFragment)
                        }
                    else -> Unit
                    }
                }
            }

            binding.buttonStart.setOnClickListener {
                viewModel.startButtonClick()
                findNavController().navigate(R.id.action_introductionFragment_to_accountOptionsFragment)
            }
        }
    }