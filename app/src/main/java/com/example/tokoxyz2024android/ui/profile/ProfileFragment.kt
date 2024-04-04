package com.example.tokoxyz2024android.ui.profile

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.tokoxyz2024android.AuthActivity
import com.example.tokoxyz2024android.data.storage.LoginSessionManager
import com.example.tokoxyz2024android.databinding.FragmentProfileBinding

class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val viewModel =
            ViewModelProvider(this).get(ProfileViewModel::class.java)
        _binding = FragmentProfileBinding.inflate(inflater, container, false)

        viewModel.profileResult.observe(viewLifecycleOwner){
            val data = it.data!!
            if(it.status in 200..299){
                binding.name.text = data["nama"]
                binding.username.text = "@" + data["username"]
                binding.address.text = data["alamat"]
            }
        }
        viewModel.getProfile()

        viewModel.logoutResult.observe(viewLifecycleOwner){
            if(it.status in 200..299){
                Toast.makeText(requireContext(), "Berhasil Logout", Toast.LENGTH_SHORT).show()
                startActivity(Intent(requireContext(), AuthActivity::class.java))
            }
        }

        binding.logout.setOnClickListener{
            viewModel.logout()
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}