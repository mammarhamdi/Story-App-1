package com.ammartech.storyapplication.view.customview

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import android.view.View
import androidx.appcompat.widget.AppCompatEditText
import com.ammartech.storyapplication.R
import com.google.android.material.textfield.TextInputLayout

class EmailEditText @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = androidx.appcompat.R.attr.editTextStyle
) : AppCompatEditText(context, attrs, defStyleAttr) {

    init {
        hint = "Masukkan email Anda"
        textAlignment = View.TEXT_ALIGNMENT_VIEW_START

        addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
                // Do nothing
            }
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                // Do nothing
            }
            override fun afterTextChanged(s: Editable) {
                if (hasFocus()) validateEmail()
            }
        })
    }

    private fun validateEmail() {
        val emailText = text.toString()
        if (emailText.isBlank() || !android.util.Patterns.EMAIL_ADDRESS.matcher(emailText).matches()) {
            (parent.parent as? TextInputLayout)?.error = context.getString(R.string.invalid_email)
        } else {
            (parent.parent as? TextInputLayout)?.error = null
        }
    }

}