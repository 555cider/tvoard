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
import android.content.res.Resources
import android.inputmethodservice.Keyboard
import android.view.inputmethod.EditorInfo

class MyKeyboard(context: Context?, xmlLayoutResId: Int) : Keyboard(context, xmlLayoutResId) {

    private var mEnterKey: Key? = null

    /**
     * This looks at the ime options given by the current editor, to set the
     * appropriate label on the keyboard's enter key (if it has one).
     */
    fun setImeOptions(res: Resources, options: Int) {
        if (mEnterKey == null) {
            return
        }
        when (options and (EditorInfo.IME_MASK_ACTION or EditorInfo.IME_FLAG_NO_ENTER_ACTION)) {
            EditorInfo.IME_ACTION_GO -> {
                mEnterKey!!.iconPreview = null
                mEnterKey!!.icon = null
                mEnterKey!!.label = res.getText(org.example.tvoard.R.string.label_go_key)
            }

            EditorInfo.IME_ACTION_NEXT -> {
                mEnterKey!!.iconPreview = null
                mEnterKey!!.icon = null
                mEnterKey!!.label = res.getText(org.example.tvoard.R.string.label_next_key)
            }

            EditorInfo.IME_ACTION_SEARCH -> {
                mEnterKey!!.icon = res.getDrawable(
                    org.example.tvoard.R.drawable.ic_key_search, null
                )
                mEnterKey!!.label = null
            }

            EditorInfo.IME_ACTION_SEND -> {
                mEnterKey!!.iconPreview = null
                mEnterKey!!.icon = null
                mEnterKey!!.label = res.getText(org.example.tvoard.R.string.label_send_key)
            }

            else -> {
                mEnterKey!!.icon = res.getDrawable(
                    org.example.tvoard.R.drawable.ic_key_return, null
                )
                mEnterKey!!.label = null
            }
        }
    }
}