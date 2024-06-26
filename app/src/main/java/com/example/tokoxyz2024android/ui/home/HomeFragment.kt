package com.example.tokoxyz2024android.ui.home

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.postDelayed
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.tokoxyz2024android.InvoiceActivity
import com.example.tokoxyz2024android.adapter.ItemAdapter
import com.example.tokoxyz2024android.databinding.FragmentHomeBinding
import com.google.gson.Gson
import java.text.NumberFormat
import java.util.Locale

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val viewModel =
            ViewModelProvider(this).get(HomeViewModel::class.java)
        viewModel.init()

        _binding = FragmentHomeBinding.inflate(inflater, container, false)

        viewModel.searchBarang()
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel

        binding.recyclerView.adapter = ItemAdapter(viewModel, binding)

        binding.search.addTextChangedListener(object: TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                viewModel.searchBarang(binding.search.text.toString())
            }
        })


        binding.checkout.setOnClickListener{
            viewModel.checkout()
        }

        viewModel.coResponse.observe(viewLifecycleOwner){
            if(it.status in 200..299){
                val datJson = Gson().toJson(it)
                val intent = Intent(requireContext(), InvoiceActivity::class.java)
                intent.putExtra("transaksi", datJson)
                startActivity(intent)
            }
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}