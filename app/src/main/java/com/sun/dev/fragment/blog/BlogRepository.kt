package com.sun.dev.fragment.blog

import android.text.Html
import com.sun.dev.util.doInBackground
import com.sun.dev.util.net.RetrofitManager
import io.reactivex.Observable

/**
 * Created by SunLion on 2019/12/13.
 */
class BlogRepository {
    fun  getBlog(): Observable<Html> {
        return RetrofitManager.getBlogData().doInBackground()
    }
}