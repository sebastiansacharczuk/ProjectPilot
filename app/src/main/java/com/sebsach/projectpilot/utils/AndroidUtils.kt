package com.sebsach.projectpilot.utils

import android.content.Context
import android.widget.Toast

/**
 * @author Sebastian Sacharczuk
 * github: https://github.com/sebastiansacharczuk
 */
class AndroidUtils {
    companion object{
        fun makeToast(context: Context, message: String){
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
        }
    }
}