package com.humeyradogus.ecommerceapp.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import androidx.recyclerview.widget.AsyncListDiffer
import com.humeyradogus.ecommerceapp.data.Product
import com.humeyradogus.ecommerceapp.databinding.SpecialRvItemBinding
import com.humeyradogus.ecommerceapp.util.Constants.IMAGES

class SpecialProductsAdapter :
    RecyclerView.Adapter<SpecialProductsAdapter.SpecialProductsViewHolder>() {

    inner class SpecialProductsViewHolder(private val binding: SpecialRvItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(product: Product) {
            binding.apply {
                Glide.with(itemView).load(product.images[0]).into(imgSpecialRvItem)
                tvSpecialProductPrice.text = product.price.toString()
                tvSpecialProductName.text = product.name
            }
        }
    }

    private val diffCallBack = object : DiffUtil.ItemCallback<Product>() {
        override fun areItemsTheSame(oldItem: Product, newItem: Product): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Product, newItem: Product): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this@SpecialProductsAdapter, diffCallBack)


    var onItemClick: ((Product) -> Unit)? = null

    var onAddToCartClick: ((Product) -> Unit)? = null


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SpecialProductsViewHolder {
        return SpecialProductsViewHolder(SpecialRvItemBinding.inflate(LayoutInflater.from(parent.context),parent, false))
    }

    override fun onBindViewHolder(holder: SpecialProductsViewHolder, position: Int) {
        val product = differ.currentList[position]
        holder.bind(product)

        /**

        holder.itemView.setOnClickListener {
            onItemClick?.invoke(product)
        }


        holder.binding.btnAddToCart.setOnClickListener {
            onAddToCartClick?.invoke(product)
        }*/
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }
}