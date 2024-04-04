package com.example.tokoxyz2024android.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.tokoxyz2024android.R
import com.example.tokoxyz2024android.data.model.BASE_URL_NO_API
import com.example.tokoxyz2024android.data.model.BarangItem
import com.example.tokoxyz2024android.databinding.ListItemBinding
import com.example.tokoxyz2024android.ui.home.HomeViewModel
import java.text.NumberFormat
import java.util.Locale

class InvoiceAdapter(private val dataset: List<Map<String, Any>>): RecyclerView.Adapter<InvoiceAdapter.InvoiceAdapterViewHolder>(){
    class InvoiceAdapterViewHolder(val view: View): RecyclerView.ViewHolder(view){
        val barang: TextView = view.findViewById(R.id.nama_barang)
        val harga: TextView = view.findViewById(R.id.harga_barang)
        val qty: TextView = view.findViewById(R.id.qty_barang)
        val subtotal: TextView = view.findViewById(R.id.subtotal_barang)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): InvoiceAdapterViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_item_invoice, parent,false)
        return InvoiceAdapterViewHolder(view)
    }

    override fun getItemCount(): Int {
        return dataset.size
    }

    override fun onBindViewHolder(holder: InvoiceAdapterViewHolder, position: Int) {
        val item = dataset[position]
        val formater = NumberFormat.getNumberInstance(Locale("id", "ID"))
        holder.barang.text = item["barang"].toString()
        holder.harga.text = "Rp. " + formater.format(item["harga"].toString().toDouble())
        holder.qty.text = item["qty"].toString().toDouble().toInt().toString()
        holder.subtotal.text = "Rp. " + formater.format(item["harga"].toString().toDouble() * item["qty"].toString().toDouble())
    }
}
