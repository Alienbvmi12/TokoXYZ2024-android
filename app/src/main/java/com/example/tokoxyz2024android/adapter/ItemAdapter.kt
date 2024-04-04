package com.example.tokoxyz2024android.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.tokoxyz2024android.data.model.BASE_URL_NO_API
import com.example.tokoxyz2024android.data.model.BarangItem
import com.example.tokoxyz2024android.databinding.FragmentHomeBinding
import com.example.tokoxyz2024android.databinding.FragmentLoginBinding
import com.example.tokoxyz2024android.databinding.ListItemBinding
import com.example.tokoxyz2024android.ui.home.HomeViewModel
import java.text.NumberFormat
import java.util.Formatter
import java.util.Locale

class ItemAdapter(private val viewModel: HomeViewModel, private val bindingHome: FragmentHomeBinding): ListAdapter<BarangItem, ItemAdapter.ItemAdapterViewHolder>(DiffCallback) {
    class ItemAdapterViewHolder(
        var binding: ListItemBinding,
        private var bindingHome: FragmentHomeBinding,
        val viewModel: HomeViewModel
    ): RecyclerView.ViewHolder(binding.root){
        fun bind(item: BarangItem){
            val url = BASE_URL_NO_API + item.image
            binding.imageView.load(url)
            binding.productName.text = item.namaBarang

            val formater = NumberFormat.getNumberInstance(Locale("id", "ID"))

            binding.price.text = "Rp. " + formater.format(item.harga.toString().toInt())
            binding.rating.text = item.rating.toString()

            binding.qty.text = viewModel.getSelectedItemQty(item.kodeBarang).toString()

            binding.add.setOnClickListener{
                val qty = viewModel.addItem(item, 1)
                binding.qty.text = qty.toString()
                countTotal()
            }

            binding.min.setOnClickListener{
                val qty = viewModel.addItem(item, -1)
                binding.qty.text = qty.toString()
                countTotal()
            }

            binding.executePendingBindings()
        }

        fun countTotal(){
            val items = viewModel.checkoutItem.value!!.values!!.toList()
            var total = 0
            for (item in items!!){
                total += item!!.harga * item!!.qty!!.toInt()
            }

            Log.i("Count", total.toString())

            val formatter = NumberFormat.getNumberInstance(Locale("id", "ID"))
            bindingHome.checkout.text = "Bayar Sekarang                        Rp. " + formatter.format(total)
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
            bindingHome,
            viewModel
        )
    }

    override fun onBindViewHolder(holder: ItemAdapterViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }
}