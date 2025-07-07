package com.example.myapplication

import android.app.Dialog
import android.content.Context
import android.widget.Button
import android.widget.TextView

object DialogUtils {
    fun showCustomPopup(
        context: Context,
        message: String,
        buttonText: String,
        onClick: () -> Unit
    ) {
        val dialog = Dialog(context)
        dialog.setContentView(R.layout.dialog)

        val dialogMessage = dialog.findViewById<TextView>(R.id.dialogMessage)
        val dialogButton = dialog.findViewById<Button>(R.id.dialogButton)

        dialogMessage.text = message
        dialogButton.text = buttonText

        dialogButton.setOnClickListener {
            onClick()
            dialog.dismiss()
        }

        dialog.show()
    }
}
