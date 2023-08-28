/*
 * Copyright (C) 2008-2009 Google Inc.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package org.example.tvoard.keyboard

import android.content.Context
import android.graphics.Paint
import android.graphics.drawable.Drawable
import android.inputmethodservice.KeyboardView
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.accessibility.AccessibilityManager
import android.widget.PopupWindow
import android.widget.TextView

class MyKeyboardView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleRes: Int = 0
) : KeyboardView(context, attrs) {

    companion object {
        const val TAG = "MyKeyboardView"
    }

    private var mLabelTextSize = 0
    private var mKeyTextSize = 0
    private var mTextColor = 0
    private var mBackgroundColor = 0
    private var mPrimaryColor = 0

    private var mPreviewText: TextView? = null
    private val mPopupPreview: PopupWindow
    private var mPreviewTextSizeLarge = 0
    private var mPreviewHeight = 0

    private val mPopupKeyboard: PopupWindow
    private var mPopupParent: View

    private var mVerticalCorrection = 0

    private val mPaint: Paint
    private var mPopupLayout = 0
    private var mPopupMaxMoveDistance = 0f
    private var mTopSmallNumberSize = 0f
    private var mTopSmallNumberMarginWidth = 0f
    private var mTopSmallNumberMarginHeight = 0f
    private val mSpaceMoveThreshold: Int

    private var mKeyBackground: Drawable? = null

    /** The accessibility manager for accessibility support  */
    private val mAccessibilityManager: AccessibilityManager

    init {
        val attributes =
            context.obtainStyledAttributes(
                attrs,
                org.example.tvoard.R.styleable.MyKeyboardView,
                0,
                defStyleRes
            )
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val keyTextSize = 0
        try {
            for (i in 0 until attributes.indexCount) {
                Log.v(TAG, attributes.getIndex(i).toString())
                when (val attr = attributes.getIndex(i)) {
                    org.example.tvoard.R.styleable.MyKeyboardView_keyTextSize -> mKeyTextSize =
                        attributes.getDimensionPixelSize(attr, 18)
                }
            }
        } finally {
            attributes.recycle()
        }

        mPopupLayout = org.example.tvoard.R.layout.keyboard_view_popup
        mKeyBackground = resources.getDrawable(
            org.example.tvoard.R.drawable.key_selector,
            context.theme
        )
        mVerticalCorrection =
            resources.getDimension(org.example.tvoard.R.dimen.vertical_correction).toInt()
        mLabelTextSize = resources.getDimension(org.example.tvoard.R.dimen.label_text_size).toInt()
        mPreviewHeight = resources.getDimension(org.example.tvoard.R.dimen.key_height).toInt()
        mSpaceMoveThreshold =
            resources.getDimension(org.example.tvoard.R.dimen.medium_margin).toInt()
        mTextColor = resources.getColor(org.example.tvoard.R.color.black, context.theme)
        mBackgroundColor = resources.getColor(
            org.example.tvoard.R.color.black,
            context.theme
        )
        mPrimaryColor =
            resources.getColor(org.example.tvoard.R.color.color_primary, context.theme)
        mPopupPreview = PopupWindow(context)
        mPreviewText =
            inflater.inflate(
                resources.getLayout(org.example.tvoard.R.layout.keyboard_key_preview),
                null
            ) as TextView
        mPreviewTextSizeLarge =
            context.resources.getDimension(org.example.tvoard.R.dimen.preview_text_size).toInt()
        mPopupPreview.contentView = mPreviewText
        mPopupPreview.setBackgroundDrawable(null)

        mPopupPreview.isTouchable = false
        mPopupKeyboard = PopupWindow(context)
        mPopupKeyboard.setBackgroundDrawable(null)
        mPopupParent = this
        mPaint = Paint()
        mPaint.isAntiAlias = true
        mPaint.textSize = keyTextSize.toFloat()
        mPaint.textAlign = Paint.Align.CENTER
        mPaint.alpha = 255
        mAccessibilityManager =
            (context.getSystemService(Context.ACCESSIBILITY_SERVICE) as AccessibilityManager)
        mPopupMaxMoveDistance =
            resources.getDimension(org.example.tvoard.R.dimen.popup_max_move_distance)
        mTopSmallNumberSize = resources.getDimension(org.example.tvoard.R.dimen.small_text_size)
        mTopSmallNumberMarginWidth =
            resources.getDimension(org.example.tvoard.R.dimen.top_small_number_margin_width)
        mTopSmallNumberMarginHeight =
            resources.getDimension(org.example.tvoard.R.dimen.top_small_number_margin_height)
    }

}