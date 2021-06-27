package uz.texnopos.texnoposedufinance.core

import android.app.Activity
import android.view.Gravity
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.fragment.app.Fragment

open class BaseFragment(resId: Int) : Fragment(resId) {

    fun toastSH(msg: String?) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
    }
    fun toastSHTop(msg: String?) {
        val toast = Toast.makeText(context, msg, Toast.LENGTH_SHORT)
        toast.setGravity(Gravity.TOP, 0, 0)
        toast.show()

    }

    fun toastLN(msg: String?) {
        Toast.makeText(context, msg, Toast.LENGTH_LONG).show()
    }

    fun toastLNCenter(msg: String?) {
        val toast = Toast.makeText(context, msg, Toast.LENGTH_LONG)
        toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0)
        toast.show()
    }

    protected fun hideKeyboard(activity: Activity) {
        val imm: InputMethodManager =
            activity.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        var view = activity.currentFocus
        if (view == null) {
            view = View(activity)
        }
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }
    fun textFormat(t: String): String{
        var text = t
        text = text.reversed()
        text = text.subSequence(0, text.length)
            .chunked(3) // group every 3 chars
            .joinToString(" ")
        return text.reversed()
    }
}