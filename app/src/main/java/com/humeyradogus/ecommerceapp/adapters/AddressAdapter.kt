package com.humeyradogus.ecommerceapp.adapters

import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.humeyradogus.ecommerceapp.R
import com.humeyradogus.ecommerceapp.data.Address
import com.humeyradogus.ecommerceapp.databinding.AddressRvItemBinding

class AddressAdapter : RecyclerView.Adapter<AddressAdapter.AddressViewHolder>() {
    inner class AddressViewHolder(val binding: AddressRvItemBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(address: Address, isSelected: Boolean){
            binding.apply {
                buttonAddress.text = address.addressTitle
                if (isSelected){
                    buttonAddress.background = ColorDrawable(itemView.context.resources.getColor(R.color.g_blue))
                }else{
                    buttonAddress.background = ColorDrawable(itemView.context.resources.getColor(R.color.g_white))
                }
            }

        }
    }


    private val diffUtil = object : DiffUtil.ItemCallback<Address>() {
        override fun areItemsTheSame(oldItem: Address, newItem: Address): Boolean {
            return oldItem.addressTitle == newItem.addressTitle && oldItem.fullName == newItem.fullName
        }

        override fun areContentsTheSame(oldItem: Address, newItem: Address): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, diffUtil)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AddressViewHolder {
        return AddressViewHolder(
            AddressRvItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    var selectedAddress = -1
    override fun onBindViewHolder(holder: AddressViewHolder, position: Int) {

        val address = differ.currentList[position]
        holder.bind(address, selectedAddress == position)

        /*
        if(ADDRESS_CLICK_FLAG == SELECT_ADDRESS_FLAG) {
            if (selectedAddress == position) {
                holder.binding.buttonAddress.apply {
                    setBackgroundColor(resources.getColor(R.color.g_dark_blue))
                    text = address.addressTitle
                    setTextColor(resources.getColor(R.color.white))
                }
            } else {
                holder.binding.buttonAddress.apply {
                    setBackgroundResource(R.drawable.unselected_button_background)
                    text = address.addressTitle
                    setTextColor(resources.getColor(R.color.g_black))
                }
            }
*/
            holder.binding.buttonAddress.setOnClickListener {

                if (selectedAddress >= 0)
                    notifyItemChanged(selectedAddress)
                selectedAddress = holder.adapterPosition
                notifyItemChanged(selectedAddress)
                onClick?.invoke(address)
            }
/*
        }else {
            holder.binding.buttonAddress.apply {
                setBackgroundResource(R.drawable.unselected_button_background)
                text = address.addressTitle
                setTextColor(resources.getColor(R.color.g_black))
            }

            holder.binding.buttonAddress.setOnClickListener {
                onClick?.invoke(address)
            }
        }
*/

    }

    init {
        differ.addListListener{_,_ ->
            notifyItemChanged(selectedAddress)

        }
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    var onClick : ((Address)->Unit)?=null
}