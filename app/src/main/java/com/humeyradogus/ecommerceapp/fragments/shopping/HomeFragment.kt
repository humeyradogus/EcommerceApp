package com.humeyradogus.ecommerceapp.fragments.shopping

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.material.tabs.TabLayoutMediator
import com.humeyradogus.ecommerceapp.R
import com.humeyradogus.ecommerceapp.adapters.HomeViewpagerAdapter
import com.humeyradogus.ecommerceapp.databinding.FragmentHomeBinding
import com.humeyradogus.ecommerceapp.fragments.categories.*

class HomeFragment : Fragment(R.layout.fragment_home) {
    private lateinit var binding: FragmentHomeBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val categoriesFragments = arrayListOf<Fragment>(
            MainCategoryFragment(),
            ChairFragment(),
            CupboardFragment(),
            TableFragment(),
            FurnitureFragment(),
            AccessoryFragment()
        )

        binding.viewPageHome.isUserInputEnabled = false

        val viewPager2Adapter = HomeViewpagerAdapter(categoriesFragments, childFragmentManager, lifecycle)

        binding.viewPageHome.adapter = viewPager2Adapter
        TabLayoutMediator(binding.tabLayout, binding.viewPageHome){ tab, position ->
            when(position){
                0 -> tab.text = "Home"
                1-> tab.text = "Chair"
                2 -> tab.text = "Cupboard"
                3 -> tab.text = "Table"
                4 -> tab.text = "Accessory"
                5 -> tab.text = "Furniture"
            }
        }.attach()

    }
}