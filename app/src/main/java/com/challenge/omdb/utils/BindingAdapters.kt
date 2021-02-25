package com.challenge.omdb.utils

import android.content.res.ColorStateList
import android.util.TypedValue
import android.view.View
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.challenge.omdb.R
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup

@BindingAdapter("visibleOrGone")
fun visibleOrGone(view: View, visible: Boolean) {
    view.visibility = if (visible) View.VISIBLE else View.GONE
}


@BindingAdapter("moviePropertyText")
fun moviePropertyText(textView: TextView, text: String?) {
    if (!text.isNullOrBlank() && !text.contains("N/A")) {
        textView.text = text
    } else {
        textView.text = "---"
    }
}

@BindingAdapter("movieRunTimePropertyText")
fun movieRunTimePropertyText(textView: TextView, text: String?) {
    if (!text.isNullOrBlank() && !text.contains("N/A")) {
        textView.text = text
    } else {
        textView.text = EMPTY_STRING
    }
}

@BindingAdapter("movieCategories")
fun movieCategories(chipGroup: ChipGroup, categoriesString: String?) {
    if (!categoriesString.isNullOrBlank() && !categoriesString.contains("N/A")) {
        val categories = categoriesString.split(",")
        val context = chipGroup.context
        val chipStrokeWidth = TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            1f,
            context.resources.displayMetrics
        )
        categories.forEach { category ->
            val chip = Chip(context)
            chip.text = category
            chip.setTextColor(context.getColor(R.color.white))
            chip.chipStrokeWidth = chipStrokeWidth
            chip.chipStrokeColor = ColorStateList.valueOf(context.getColor(R.color.greyPrimary))
            chip.setChipBackgroundColorResource(R.color.greyPrimary)
            chipGroup.addView(chip)
        }

    }
}
