import android.content.Context
import android.util.DisplayMetrics
import android.util.TypedValue

fun dpToPx(context: Context, dp: Int): Int = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp.toFloat(), context.resources.displayMetrics).toInt()

fun dpToPx(context: Context, dp: Float): Float = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, context.resources.displayMetrics)

fun pXToDp(context: Context, px: Int): Int = px / context.resources.displayMetrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT