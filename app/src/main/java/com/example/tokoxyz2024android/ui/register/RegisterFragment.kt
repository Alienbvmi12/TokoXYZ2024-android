package com.example.tokoxyz2024android.ui.register

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
import com.example.tokoxyz2024android.databinding.FragmentRegisterBinding
import com.example.tokoxyz2024android.ui.login.LoginViewModel
import com.example.tokoxyz2024android.ui.login.RegisterViewModel


class RegisterFragment: Fragment() {
    private lateinit var binding: FragmentRegisterBinding
    private lateinit var viewModel: RegisterViewModel
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentRegisterBinding.inflate(layoutInflater)
        viewModel = ViewModelProvider(this).get(RegisterViewModel::class.java)

        binding.register2.setOnClickListener{
            viewModel.register(
                binding.nama.text.toString(),
                binding.username.text.toString(),
                binding.alamat.text.toString(),
                binding.password.text.toString(),
                binding.passwordConfirmation.text.toString()
            )
        }
        binding.login2.setOnClickListener{
            findNavController().navigate(R.id.loginFragment)
        }

        viewModel.registerResult.observe(viewLifecycleOwner){
            if(it.status in 200..299) {
                findNavController().navigate(R.id.loginFragment)
            }
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }
}