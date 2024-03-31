package com.example.tokoxyz2024android.ui.login

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.tokoxyz2024android.MainActivity
import com.example.tokoxyz2024android.R
import com.example.tokoxyz2024android.databinding.FragmentLoginBinding


class LoginFragment: Fragment() {
    private lateinit var binding: FragmentLoginBinding
    private lateinit var viewModel: LoginViewModel
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLoginBinding.inflate(layoutInflater)
        viewModel = ViewModelProvider(this).get(LoginViewModel::class.java)

        binding.register.setOnClickListener{
            findNavController().navigate(R.id.registerFragment)
        }
        binding.login.setOnClickListener{
            viewModel.login(binding.username.text.toString(), binding.password.text.toString())
        }

        viewModel.loginResult.observe(viewLifecycleOwner){
            if(it.status in 200..299){
                startActivity(Intent(requireContext(), MainActivity::class.java))
            }
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }
}