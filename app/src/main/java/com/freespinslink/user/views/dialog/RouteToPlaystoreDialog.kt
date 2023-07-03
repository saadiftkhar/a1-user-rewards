package com.freespinslink.user.views.dialog

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.freespinslink.user.R
import com.freespinslink.user.databinding.DialogRouteToPlaystoreBinding
import com.freespinslink.user.utils.Constants
import com.freespinslink.user.utils.openInBrowser

class RouteToPlaystoreDialog : DialogFragment(), View.OnClickListener {

    private var _binding: DialogRouteToPlaystoreBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DialogRouteToPlaystoreBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupView()
    }

    private fun setupView() {
        binding.tvTitle.text = requireActivity().getString(
            R.string.app_not_installed,
            Constants.getGameName,
            Constants.getGameName
        )
        binding.ivOpenPlaystore.setOnClickListener(this)
    }

    override fun onClick(p0: View?) {
        when (p0) {
            binding.ivOpenPlaystore -> Constants.openGame(requireContext())
        }
    }

}