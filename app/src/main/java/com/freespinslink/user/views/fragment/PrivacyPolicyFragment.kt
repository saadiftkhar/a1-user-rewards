package com.freespinslink.user.views.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.freespinslink.user.R
import com.freespinslink.user.databinding.FragmentPrivacyPolicyBinding
import com.freespinslink.user.enums.EnumGames
import com.freespinslink.user.utils.Constants
import com.freespinslink.user.utils.SharedStorage
import com.freespinslink.user.utils.openInBrowser
import com.freespinslink.user.utils.showToast

class PrivacyPolicyFragment : Fragment(), View.OnClickListener {


    private lateinit var binding: FragmentPrivacyPolicyBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_privacy_policy, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupViews()

    }

    private fun setupViews() {
        when (Constants.BUILD_FLAVOUR) {
            EnumGames.COIN_DRAGON.value,
            EnumGames.DICE_DREAMS.value,
            EnumGames.MAFIA_MASTER.value
            -> binding.tvRead.isVisible = true
            else -> binding.tvRead.isVisible = false
        }

        binding.tvAcceptAndContinue.setOnClickListener(this)
        binding.tvRead.setOnClickListener(this)
    }

    private fun onClickCta() {
        if (binding.cbPrivacyTerms.isChecked) {
            SharedStorage.updatePrivacyCheck(true)
            findNavController().navigate(
                PrivacyPolicyFragmentDirections.actionPrivacyPolicyFragmentToRewardsFragment()
            )

        } else {
            requireActivity().showToast("Please accept the privacy terms")
        }
    }

    override fun onClick(p0: View?) {
        when (p0?.id) {
            R.id.tv_accept_and_continue -> onClickCta()
            R.id.tvRead -> Constants.getPPUrl.openInBrowser(requireActivity())
        }
    }
}