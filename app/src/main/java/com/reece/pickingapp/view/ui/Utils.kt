package com.reece.pickingapp.view.ui

import android.content.Context
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import com.google.android.material.appbar.AppBarLayout
import com.reece.pickingapp.R
import com.reece.pickingapp.models.ErrorLogDTO
import com.reece.pickingapp.repository.UserPreferences
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent

@Module
@InstallIn(ActivityComponent::class)
class Utils() {
    fun changeNavBarTextColorOnScroll(appBarLayout: AppBarLayout, toolbarLayout: Toolbar, context: Context) {
        //Set a listener to know the current visible state of CollapseLayout
        appBarLayout.addOnOffsetChangedListener(object : AppBarLayout.OnOffsetChangedListener {
            var scrollRange = -1
            override fun onOffsetChanged(appBarLayout: AppBarLayout, verticalOffset: Int) {
                if (scrollRange == -1) {
                    scrollRange = appBarLayout.totalScrollRange
                }
                //Check if the view is collapsed
                if (scrollRange + verticalOffset == 0) {
                    toolbarLayout.setTitleTextColor(context.getColor(R.color.white))

                    setUpArrowColor(toolbarLayout, context.getColor(R.color.white), context)
                } else {
                    toolbarLayout.setTitleTextColor(context.getColor(R.color.morsco_blue_1))

                    setUpArrowColor(toolbarLayout, context.getColor(R.color.morsco_blue_1), context)
                }
            }
        })
    }

    fun setUpArrowColor(toolbarLayout: Toolbar, color: Int, context: Context) {
        if (toolbarLayout.navigationIcon != null) {
            val upArrow =
                ContextCompat.getDrawable(context, R.drawable.abc_ic_ab_back_material)
            val wrappedUpArrow = upArrow?.let { DrawableCompat.wrap(it) }
            wrappedUpArrow?.let { DrawableCompat.setTint(it, color) }
            toolbarLayout.navigationIcon = wrappedUpArrow
        }
    }
}