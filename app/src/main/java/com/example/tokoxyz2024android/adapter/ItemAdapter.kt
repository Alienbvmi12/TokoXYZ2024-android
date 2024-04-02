package com.example.tokoxyz2024android.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.tokoxyz2024android.data.model.BASE_URL_NO_API
import com.example.tokoxyz2024android.data.model.BarangItem
import com.example.tokoxyz2024android.databinding.ListItemBinding
import com.example.tokoxyz2024android.ui.home.HomeViewModel

class ItemAdapter(private val viewModel: HomeViewModel): ListAdapter<BarangItem, ItemAdapter.ItemAdapterViewHolder>(DiffCallback) {
    class ItemAdapterViewHolder(
        var binding: ListItemBinding,
        val viewModel: HomeViewModel
    ): RecyclerView.ViewHolder(binding.root){
        fun bind(item: BarangItem){
            val url = BASE_URL_NO_API + item.image
            binding.imageView.load(url)
            binding.productName.text = item.namaBarang
            binding.price.text = item.harga.toString()
            binding.rating.text = item.rating.toString()

            binding.add.setOnClickListener{
                val qty = viewModel.addItem(item, 1)
                binding.qty.text = qty.toString()
            }

            binding.min.setOnClickListener{
                val qty = viewModel.addItem(item, -1)
                binding.qty.text = qty.toString()
            }

            binding.executePendingBindings()
        }
    }

    companion object DiffCallback: DiffUtil.ItemCallback<BarangItem>(){
        override fun areItemsTheSame(oldItem: BarangItem, newItem: BarangItem): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: BarangItem, newItem: BarangItem): Boolean {
            return oldItem.kodeBarang == newItem.kodeBarang
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemAdapterViewHolder {
        return ItemAdapterViewHolder(
            ListItemBinding.inflate(LayoutInflater.from(parent.context)),
            viewModel
        )
    }

    override fun onBindViewHolder(holder: ItemAdapterViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }
}