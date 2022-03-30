package com.setjy.practiceapp.recycler.bottom_sheet_fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.os.bundleOf
import androidx.recyclerview.widget.GridLayoutManager
import by.kirich1409.viewbindingdelegate.viewBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.setjy.practiceapp.Data.emojiUISet
import com.setjy.practiceapp.R
import com.setjy.practiceapp.databinding.FragmentBottomSheetBinding
import com.setjy.practiceapp.recycler.Adapter
import com.setjy.practiceapp.recycler.base.ViewTyped

class BottomSheetFragment : BottomSheetDialogFragment() {

    private val binding: FragmentBottomSheetBinding by viewBinding()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_bottom_sheet, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val click: (View) -> Unit = { emoji: View ->
            val pressedEmojiCode: String? =
                emojiUISet.find { it.codeString == (emoji as TextView).text.toString() }?.code
            parentFragmentManager.setFragmentResult(
                REQUEST_KEY,
                bundleOf(BUNDLE_KEY to pressedEmojiCode)
            )
            dismiss()
        }

        val holderFactory = BottomSheetHolderFactory(click)
        val adapter = Adapter<ViewTyped>(holderFactory)
        binding.rvBottomSheet.adapter = adapter
        binding.rvBottomSheet.layoutManager =
            GridLayoutManager(context, 7, GridLayoutManager.VERTICAL, false)
        adapter.items = emojiUISet
    }

    companion object {
        internal const val REQUEST_KEY: String = "bottom_fragment"
        internal const val BUNDLE_KEY: String = "bundleKey"
    }

}
