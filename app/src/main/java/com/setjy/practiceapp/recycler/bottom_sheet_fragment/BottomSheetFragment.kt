package com.setjy.practiceapp.recycler.bottom_sheet_fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.recyclerview.widget.GridLayoutManager
import by.kirich1409.viewbindingdelegate.viewBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.setjy.practiceapp.R
import com.setjy.practiceapp.data.Data.emojiUISet
import com.setjy.practiceapp.databinding.FragmentBottomSheetBinding
import com.setjy.practiceapp.recycler.Adapter
import com.setjy.practiceapp.recycler.base.ViewTyped

class BottomSheetFragment : BottomSheetDialogFragment() {

    private val binding: FragmentBottomSheetBinding by viewBinding()

    private val holderFactory: BottomSheetHolderFactory = BottomSheetHolderFactory(
        this::onEmojiClick
    )
    private val adapter: Adapter<ViewTyped> = Adapter(holderFactory)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_bottom_sheet, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.rvBottomSheet.adapter = adapter
        binding.rvBottomSheet.layoutManager =
            GridLayoutManager(context, 7, GridLayoutManager.VERTICAL, false)
        adapter.items = emojiUISet
    }

    private fun onEmojiClick(emojiCode: String) {
        val pressedEmoji =
            emojiUISet.find { it.code == emojiCode }!!
        val emojiCode = pressedEmoji.code
        val emojiName = pressedEmoji.emojiName
        parentFragmentManager.setFragmentResult(
            REQUEST_KEY,
            bundleOf(BUNDLE_KEY to arrayOf(emojiName,emojiCode))
        )
        dismiss()
    }

    companion object {
        internal const val REQUEST_KEY: String = "bottom_fragment"
        internal const val BUNDLE_KEY: String = "bundleKey"
        internal const val EMOJI_NAME_INDEX: Int = 0
        internal const val EMOJI_CODE_INDEX: Int = 1
    }

}
