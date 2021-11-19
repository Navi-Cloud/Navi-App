package com.navi.file.view.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.navi.file.databinding.DialogOptionBoxBinding
import com.navi.file.view.activity.SettingActivity
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class OptionBottomSheetFragment @Inject constructor() : BottomSheetDialogFragment() {
    companion object {
        val fragmentTag = "OptionBottomSheetFragment"
    }

    private val logTag: String = this::class.java.simpleName
    private var _dialogOptionBoxBinding: DialogOptionBoxBinding? = null
    private val dialogOptionBoxBinding: DialogOptionBoxBinding get() = _dialogOptionBoxBinding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _dialogOptionBoxBinding = DialogOptionBoxBinding.inflate(inflater, container, false)
        return dialogOptionBoxBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        dialogOptionBoxBinding.apply {
            deleteRecentLayout.setOnClickListener {

            }
            storageAnalysisLayout.setOnClickListener {

            }
            recycleBinLayout.setOnClickListener {

            }
            settingLayout.setOnClickListener {
                val intent = Intent(activity, SettingActivity::class.java)
                startActivity(intent)
            }
        }

    }

    override fun onDestroyView() {
        _dialogOptionBoxBinding = null
        super.onDestroyView()
    }
}