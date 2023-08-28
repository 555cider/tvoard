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

import android.util.Log
import org.example.tvoard.common.ACTION_APPEND
import org.example.tvoard.common.ACTION_ERROR
import org.example.tvoard.common.ACTION_NONE
import org.example.tvoard.common.ACTION_UPDATE_COMPLETE
import org.example.tvoard.common.ACTION_UPDATE_COMPOSITION
import org.example.tvoard.common.ACTION_USE_INPUT_AS_RESULT
import org.example.tvoard.common.HANGEUL_END
import org.example.tvoard.common.HANGEUL_JAMO_END
import org.example.tvoard.common.HANGEUL_JAMO_START
import org.example.tvoard.common.HANGEUL_MO_START
import org.example.tvoard.common.HANGEUL_START
import org.example.tvoard.common.KEYSTATE_ALT_MASK
import org.example.tvoard.common.KEYSTATE_CTRL_MASK
import org.example.tvoard.common.KEYSTATE_FN
import org.example.tvoard.common.KEYSTATE_SHIFT_MASK
import org.example.tvoard.common.NUM_OF_FIRST
import org.example.tvoard.common.NUM_OF_LAST
import org.example.tvoard.common.NUM_OF_LAST_INDEX
import org.example.tvoard.common.NUM_OF_MIDDLE
import org.example.tvoard.keyboard.InputTables.FirstJa
import org.example.tvoard.keyboard.InputTables.LastJa
import org.example.tvoard.keyboard.InputTables.Mo
import org.example.tvoard.keyboard.InputTables.NormalKeyMap
import org.example.tvoard.keyboard.InputTables.ShiftedKeyMap

class KoreanAutomata {

    companion object {
        const val TAG = "KoreanAutomata"
    }

    private var state = 0
    private var compositionString = ""
    private var completeString = ""
    private var isKoreanMode = false

    init {
        state = 0
        compositionString = ""
        completeString = ""
        isKoreanMode = false
    }

    fun getState(): Int {
        return state
    }

    fun getCompositionString(): String {
        return compositionString
    }

    fun getCompleteString(): String {
        return completeString
    }

    fun toggleMode() {
        isKoreanMode = !isKoreanMode
    }

    fun isKoreanMode(): Boolean {
        return isKoreanMode
    }

    private fun isHangeul(word: Char): Boolean {
        if (word.code in HANGEUL_START..HANGEUL_END) return true
        return word.code in HANGEUL_JAMO_START..HANGEUL_JAMO_END
    }

    private fun isJamo(word: Char): Boolean {
        return word.code in HANGEUL_JAMO_START..HANGEUL_JAMO_END
    }

    private fun isJa(word: Char): Boolean {
        return word.code in HANGEUL_JAMO_START until HANGEUL_MO_START
    }

    private fun isMo(word: Char): Boolean {
        return word.code in HANGEUL_MO_START..HANGEUL_JAMO_END
    }

    private fun getFirstJaIndex(word: Char): Int {
        var index = -1
        if (isHangeul(word)) {
            if (isJa(word)) {
                index = 0
                while (index < NUM_OF_FIRST) {
                    if (word == FirstJa.Word[index]) break
                    index++
                }
                if (index >= NUM_OF_FIRST) index = -1
            } else if (isMo(word)) {
                index = -1
            } else {
                val offset = word.code - HANGEUL_START
                index = offset / (NUM_OF_MIDDLE * NUM_OF_LAST_INDEX)
            }
        }
        return index
    }

    private fun getMoIndex(word: Char): Int {
        var index = -1
        if (isHangeul(word)) {
            index = if (isMo(word)) {// Mo only character..
                convertMoToIndex(word)
            } else {
                val offset = word.code - HANGEUL_START
                offset % (NUM_OF_MIDDLE * NUM_OF_LAST_INDEX) / NUM_OF_LAST_INDEX
            }
        }
        return index
    }

    private fun getLastJaIndex(word: Char): Int {
        var index = -1
        if (isHangeul(word)) {
            if (isJamo(word)) {
                if (isJa(word)) {
                    index = 0
                    while (index < NUM_OF_LAST_INDEX) {
                        if (word == LastJa.Word[index]) break
                        index++
                    }
                    if (index >= NUM_OF_LAST_INDEX) index = -1
                } else index = -1
            } else {
                val offset = word.code - HANGEUL_START
                index = offset % NUM_OF_LAST_INDEX
            }
        }
        return index
    }

    fun getFirstJa(word: Char): Char {
        val fcCode: Char
        val fcIndex = getFirstJaIndex(word)
        fcCode = if (fcIndex < 0) 0.toChar() else FirstJa.Word[fcIndex]
        return fcCode
    }

    private fun getMo(word: Char): Char {
        val code: Char
        val index = getMoIndex(word)
        code = if (index < 0) 0.toChar() else Mo.Word[index]
        return code
    }

    fun getLastJa(word: Char): Char {
        val index = getLastJaIndex(word)
        return if (index < 0) 0.toChar() else LastJa.Word[index]
    }

    // word should be one of "First Ja" otherwise return -1
    private fun convertFirstJaToIndex(word: Char): Int {
        var index = 0
        while (index < NUM_OF_FIRST) {
            if (word == FirstJa.Word[index]) {
                break
            }
            index++
        }
        if (index == NUM_OF_FIRST) {
            index = -1
        }
        return index
    }

    private fun convertMoToIndex(word: Char): Int {
        if (word < Mo.Word[0]) return -1
        val index = word - Mo.Word[0]
        return if (index >= NUM_OF_MIDDLE) -1 else index
    }

    // word should be one of "Last Ja", otherwise return -1
    private fun convertLastJaToIndex(word: Char): Int {
        var index = 0
        while (index < NUM_OF_LAST_INDEX) {
            if (word == LastJa.Word[index]) {
                break
            }
            index++
        }
        if (index == NUM_OF_LAST_INDEX) {
            index = -1
        }
        return index
    }

    fun combineMoWithIndex(index1: Int, index2: Int): Int {
        var newIndex = -1
        val code1 = Mo.Word[index1]
        val code2 = Mo.Word[index2]
        val newWord = combineMoWithWord(code1, code2)
        if (newWord != 0.toChar()) {
            newIndex = convertMoToIndex(newWord)
        }
        return newIndex
    }

    private fun combineMoWithWord(word1: Char, word2: Char): Char {
        var newWord = 0.toChar()
        if (word1.code == 0x3157) { // ㅗ
            when (word2.code) {
                0x314F -> newWord = 0x3158.toChar()
                0x3150 -> newWord = 0x3159.toChar()
                0x3163 -> newWord = 0x315A.toChar()
            }
        } else if (word1.code == 0x315C) { // ㅜ
            when (word2.code) {
                0x3153 -> newWord = 0x315D.toChar()
                0x3154 -> newWord = 0x315E.toChar()
                0x3163 -> newWord = 0x315F.toChar()
            }
        } else if (word1.code == 0x3161) { // ㅡ
            if (word2.code == 0x3163)
                newWord = 0x3162.toChar()
        }
        return newWord
    }

    private fun combineLastJaWithIndex(index1: Int, index2: Int): Int {
        val newIndex: Int
        var newWord = 0.toChar()
        if (LastJa.Code[index1] == 0x3131 && LastJa.Code[index2] == 0x3145) {
            newWord = 0x3133.toChar()
        }
        if (LastJa.Code[index1] == 0x3142 && LastJa.Code[index2] == 0x3145) {
            newWord = 0x3144.toChar()
        }

        // you may not use this file except in compliance with the License.
        if (LastJa.Code[index1] == 0x3134) {
            when (LastJa.Code[index2]) {
                0x3148 -> newWord = 0x3135.toChar()
                0x314E -> newWord = 0x3136.toChar()
            }
        }
        if (LastJa.Code[index1] == 0x3139) {
            when (LastJa.Code[index2]) {
                0x3131 -> newWord = 0x313A.toChar()
                0x3141 -> newWord = 0x313B.toChar()
                0x3142 -> newWord = 0x313C.toChar()
                0x3145 -> newWord = 0x313D.toChar()
                0x314C -> newWord = 0x313E.toChar()
                0x314D -> newWord = 0x313F.toChar()
                0x314E -> newWord = 0x3140.toChar()
            }
        }
        newIndex = if (newWord == 0.toChar()) -1 else convertLastJaToIndex(newWord)
        return newIndex
    }

    private fun combineLastJaWithWord(word1: Char, word2: Char): Char {
        var newWord: Char = 0.toChar()
        if (word1.code == 0x3131 && word2.code == 0x3145) { // ㄱ
            newWord = 'ㄳ'
        } else if (word1.code == 0x3142 && word2.code == 0x3145) {
            newWord = 'ㅄ'
        } else if (word1.code == 0x3134) { // ㄴ
            if (word2.code == 0x3148) { // ㅈ
                newWord = 'ㄵ'
            } else if (word2.code == 0x314E) { // ㅎ
                newWord = 'ㄶ'
            }
        } else if (word1.code == 0x3139) { // ㄹ
            when (word2.code) {
                0x3131 -> newWord = 0x313A.toChar()
                0x3141 -> newWord = 0x313B.toChar()
                0x3142 -> newWord = 0x313C.toChar()
                0x3145 -> newWord = 0x313D.toChar()
                0x314C -> newWord = 0x313E.toChar()
                0x314D -> newWord = 0x313F.toChar()
                0x314E -> newWord = 0x3140.toChar()
            }
        }
        return newWord
    }

    private fun composeWordWithIndexes(firstJaIndex: Int, moIndex: Int, lastJaIndex: Int): Char {
        var word = 0.toChar()
        if (firstJaIndex in 0 until NUM_OF_FIRST) {
            if (moIndex in 0 until NUM_OF_MIDDLE) {
                if (lastJaIndex in 0 until NUM_OF_LAST) {
                    val offset =
                        firstJaIndex * NUM_OF_MIDDLE * NUM_OF_LAST_INDEX + moIndex * NUM_OF_LAST_INDEX + lastJaIndex
                    word = (offset + HANGEUL_START).toChar()
                }
            }
        }
        return word
    }

    private fun getAlphabetIndex(code: Char): Int {
        if (code in 'a'..'z') return (code - 'a')
        return if (code in 'A'..'Z') (code - 'A') else -1
    }

    // Input is ended by external causes
    fun finishAutomataWithoutInput(): Int {
        Log.v(TAG, "[Enter] finishAutomataWithoutInput()")
        val ret = ACTION_NONE
        if (isKoreanMode) {
            completeString = ""
            compositionString = ""
            state = 0
        }
        Log.v(TAG, "[Leave] finishAutomataWithoutInput()")
        return ret
    }

    fun doBackSpace(): Int {
        Log.v(TAG, "doBackSpace: 0.")

        val result: Int
        val word: Char = if (compositionString !== "") compositionString[0] else 0.toChar()
        Log.v(TAG, "doBackSpace: 1. code=$word")
        if (state != 0 && word == 0.toChar()) {
            return ACTION_ERROR
        }

        Log.v(TAG, "doBackSpace: 2. mState=$state")
        when (state) {
            0 -> result = ACTION_USE_INPUT_AS_RESULT
            1, 4 -> {
                compositionString = ""
                state = 0
                result = ACTION_USE_INPUT_AS_RESULT
            }

            2 -> {
                run {
                    val index = getFirstJaIndex(word)
                    compositionString = FirstJa.Word[index] + ""
                    state = 1
                }
                return ACTION_UPDATE_COMPOSITION
            }

            3 -> {
                run {
                    val index = getLastJaIndex(word)
                    compositionString = (word.code - index).toChar() + ""
                    state = 2
                }
                return ACTION_UPDATE_COMPOSITION
            }

            5 -> {
                run {
                    val index = getMoIndex(word)
                    if (index < 0) {
                        return ACTION_ERROR
                    }
                    val newIndex = Mo.iMiddle[index]
                    if (newIndex < 0) {
                        return ACTION_ERROR
                    }
                    compositionString = Mo.Word[newIndex] + ""
                    state = 4
                }
                return ACTION_UPDATE_COMPOSITION
            }

            10 -> {
                run {
                    val index = getLastJaIndex(word)
                    if (index < 0) {
                        return ACTION_ERROR
                    }
                    val newIndex = LastJa.iLast[index]
                    if (newIndex < 0) {
                        return ACTION_ERROR
                    }
                    compositionString = LastJa.Word[newIndex] + ""
                    state = 1
                }
                return ACTION_UPDATE_COMPOSITION
            }

            11 -> {
                run {
                    val index = getLastJaIndex(word)
                    if (index < 0) {
                        return ACTION_ERROR
                    }
                    val newIndex = LastJa.iLast[index]
                    if (newIndex < 0) {
                        return ACTION_ERROR
                    }
                    compositionString = (word.code - index + newIndex).toChar() + ""
                    state = 3
                }
                return ACTION_UPDATE_COMPOSITION
            }

            20 -> {
                run {
                    val firstJaIndex = getFirstJaIndex(word)
                    val moIndex = getMoIndex(word)
                    val newIndex = Mo.iMiddle[moIndex]
                    if (newIndex < 0) {
                        return ACTION_ERROR
                    }
                    compositionString = composeWordWithIndexes(firstJaIndex, newIndex, 0) + ""
                    state = 2
                }
                return ACTION_UPDATE_COMPOSITION
            }

            21 -> {
                run {
                    val index = getLastJaIndex(word)
                    compositionString = (word.code - index).toChar() + ""
                    state = 20
                }
                return ACTION_UPDATE_COMPOSITION
            }

            22 -> {
                run {
                    val index = getLastJaIndex(word)
                    if (index < 0) {
                        return ACTION_ERROR
                    }
                    val newIndex = LastJa.iLast[index]
                    if (newIndex < 0) {
                        return ACTION_ERROR
                    }
                    compositionString = (word.code - index + newIndex).toChar() + ""
                    state = 21
                }
                return ACTION_UPDATE_COMPOSITION
            }

            else -> return ACTION_ERROR // error. should not be here in any circumstance.
        }
        return result
    }

    fun doAutomata(word: Char, keyState: Int): Int {
        Log.v(TAG, "doAutomata: 0. word=$word KeyState=$keyState mState=$state")

        // 1. word가 [a, ... z, A, ... Z]에 속하지 않은 경우
        val alphaIndex = getAlphabetIndex(word)
        Log.v(TAG, "doAutomata: 1. alphaIndex=$alphaIndex")
        if (alphaIndex < 0) {
            var result = ACTION_NONE
            if (isKoreanMode) {
                // flush Korean characters first.
                completeString = compositionString
                compositionString = ""
                state = 0
                result = ACTION_UPDATE_COMPLETE or ACTION_UPDATE_COMPOSITION
            }
            // process the code as English
            if (keyState and (KEYSTATE_ALT_MASK or KEYSTATE_CTRL_MASK or KEYSTATE_FN) == 0) {
                result = result or ACTION_USE_INPUT_AS_RESULT
            }
            return result
        }

        // 2. 한글 키보드 아닌 경우 => 입력한 대로 입력
        Log.v(TAG, "doAutomata: 2. mKoreanMode=$isKoreanMode")
        if (!isKoreanMode) {
            return ACTION_USE_INPUT_AS_RESULT
        }

        // 3. 한글 키보드인 경우 => Shift 여부에 따라 글자 변환하여 입력
        val hCode: Char =
            if (keyState and KEYSTATE_SHIFT_MASK == 0) NormalKeyMap.Word[alphaIndex] else ShiftedKeyMap.Word[alphaIndex]
        Log.v(TAG, "doAutomata: 3. hCode=$hCode mState=$state")
        return when (state) {
            0 -> doState00(hCode)
            1 -> doState01(hCode)
            2 -> doState02(hCode)
            3 -> doState03(hCode)
            4 -> doState04(hCode)
            5 -> doState05(hCode)
            10 -> doState10(hCode)
            11 -> doState11(hCode)
            20 -> doState20(hCode)
            21 -> doState21(hCode)
            22 -> doState22(hCode)
            else -> ACTION_ERROR // error. should not be here in any circumstance.
        }
    }

    /**
     * 조합: NULL
     */
    private fun doState00(word: Char): Int {
        Log.v(TAG, "-doState00: 0. word=$word")
        state = if (isJa(word)) 1 else 4
        completeString = ""
        compositionString = word + ""
        return ACTION_UPDATE_COMPOSITION or ACTION_APPEND
    }

    /**
     * 조합: single ja only
     * 예시: ㄱ
     */
    private fun doState01(word: Char): Int {
        Log.v(TAG, "-doState01: 0. word=$word")

        Log.v(TAG, "-doState01: 1. compositionString=$compositionString")
        if (compositionString === "") {
            return ACTION_ERROR
        }

        Log.v(TAG, "-doState01: 2. isJa=${isJa(word)}")
        if (isJa(word)) {
            // can't combine last "ja"s
            val newWord = combineLastJaWithWord(compositionString[0], word)
            Log.v(TAG, "-doState01: 3. newWord=$newWord")
            return if (newWord == 0.toChar()) {
                state = 1
                completeString = compositionString // flush
                compositionString = word + ""
                ACTION_UPDATE_COMPLETE or ACTION_UPDATE_COMPOSITION
            } else { // can combine last "ja"s
                state = 10
                completeString = ""
                compositionString = newWord + ""
                ACTION_UPDATE_COMPOSITION
            }
        }

        val firstJaIndex = convertFirstJaToIndex(compositionString[0])
        val moIndex = convertMoToIndex(word)
        val newWord = composeWordWithIndexes(firstJaIndex, moIndex, 0)
        state = 2
        completeString = ""
        compositionString = newWord + ""
        return ACTION_UPDATE_COMPOSITION
    }

    /**
     * 조합: single ja + single mo
     * 예시: 가
     */
    private fun doState02(word: Char): Int {
        Log.v(TAG, "-doState02: 0. word=$word")

        Log.v(TAG, "-doState02: 1. compositionString=$compositionString")
        if (compositionString === "") {
            return ACTION_ERROR
        }

        Log.v(TAG, "-doState02: 2. isJa=${isJa(word)}")
        if (isJa(word)) {
            val index = getLastJaIndex(word)
            Log.v(TAG, "-doState02: 3. index=$index")
            return if (index != -1) {
                state = 3
                completeString = ""
                compositionString = (compositionString[0].code + index).toChar() + ""
                ACTION_UPDATE_COMPOSITION
            } else {
                state = 1
                completeString = compositionString
                compositionString = word + ""
                ACTION_UPDATE_COMPLETE or ACTION_UPDATE_COMPOSITION
            }
        } else { // Mo
            val code = getMo(compositionString[0])
            val newWord = combineMoWithWord(code, word)
            Log.v(TAG, "-doState02: 3. code=$code newWord=$newWord")
            return if (newWord != 0.toChar()) {
                val fcIndex = getFirstJaIndex(compositionString[0])
                val vIndex = convertMoToIndex(newWord)
                state = 20
                completeString = ""
                compositionString = composeWordWithIndexes(fcIndex, vIndex, 0) + ""
                ACTION_UPDATE_COMPOSITION
            } else {
                state = 4
                completeString = compositionString
                compositionString = word + ""
                ACTION_UPDATE_COMPLETE or ACTION_UPDATE_COMPOSITION
            }
        }
    }

    /**
     * 조합: single ja + single mo + single ja
     * 예시: 각
     */
    private fun doState03(word: Char): Int {
        Log.v(TAG, "-doState03: 0. word=$word")

        Log.v(TAG, "-doState03: 1. compositionString=${compositionString}")
        if (compositionString === "") {
            return ACTION_ERROR
        }

        Log.v(TAG, "-doState03: 2. isJa=${isJa(word)}")
        if (isJa(word)) {
            val index = getLastJaIndex(compositionString[0])
            Log.v(TAG, "-doState03: 3. index=$index")
            if (index < 0) {
                return ACTION_ERROR
            }

            val newWord = combineLastJaWithWord(LastJa.Word[index], word)
            Log.v(TAG, "-doState03: 4. newWord=$newWord")
            return if (newWord != 0.toChar()) { // Last "Ja"s can be combined
                completeString = ""
                compositionString =
                    (compositionString[0].code - index + getLastJaIndex(newWord)).toChar() + ""
                state = 11
                ACTION_UPDATE_COMPOSITION
            } else {
                completeString = compositionString
                compositionString = word + ""
                state = 1
                ACTION_UPDATE_COMPLETE or ACTION_UPDATE_COMPOSITION
            }
        } else { // Mo
            val lastJaIndex = getLastJaIndex(compositionString[0])
            Log.v(TAG, "-doState03: 3. lastJaIndex=$lastJaIndex")
            if (lastJaIndex < 0) {
                return ACTION_ERROR
            }

            completeString =
                (compositionString[0].code - lastJaIndex).toChar() + "" // remove last Ja and flush it.
            val firstJaIndex = getFirstJaIndex(LastJa.Word[lastJaIndex])
            Log.v(TAG, " -doState03: 4. firstJaIndex=$firstJaIndex")
            if (firstJaIndex < 0) {
                return ACTION_ERROR
            }

            val moIndex = getMoIndex(word)
            Log.v(TAG, "-doState03: 5. moIndex=$moIndex")
            compositionString =
                composeWordWithIndexes(
                    firstJaIndex,
                    moIndex,
                    0
                ) + "" // compose new composition string
            state = 2
            return ACTION_UPDATE_COMPLETE or ACTION_UPDATE_COMPOSITION
        }
    }

    /**
     * 조합: single Mo
     * 예시: ㅏ
     */
    private fun doState04(word: Char): Int {
        Log.v(TAG, "-doState04: 0. word=$word")

        Log.v(TAG, "-doState04: 1. compositionString=$compositionString")
        if (compositionString === "") {
            return ACTION_ERROR
        }

        Log.v(TAG, "-doState04: 2. isJa=${isJa(word)}")
        if (isJa(word)) {
            completeString = compositionString
            compositionString = word + ""
            state = 1
            return ACTION_UPDATE_COMPLETE or ACTION_UPDATE_COMPOSITION
        }

        val newWord = combineMoWithWord(compositionString[0], word)
        Log.v(TAG, "-doState04: 3. newWord=$newWord")
        return if (newWord != 0.toChar()) {
            completeString = ""
            compositionString = newWord + ""
            state = 5
            ACTION_UPDATE_COMPOSITION
        } else {
            completeString = compositionString
            compositionString = word + ""
            state = 4
            ACTION_UPDATE_COMPLETE or ACTION_UPDATE_COMPOSITION
        }
    }

    /**
     * 조합: a combined Mo
     * 예시: ㅘ
     */
    private fun doState05(word: Char): Int {
        Log.v(TAG, "-doState05: 0. word=$word")

        Log.v(TAG, "-doState05: 1. compositionString=$compositionString")
        if (compositionString === "") {
            return ACTION_ERROR
        }

        Log.v(TAG, "-doState05: 2. isJa=${isJa(word)}")
        return if (isJa(word)) {
            completeString = compositionString
            compositionString = word + ""
            state = 1
            ACTION_UPDATE_COMPLETE or ACTION_UPDATE_COMPOSITION
        } else {
            completeString = compositionString
            compositionString = word + ""
            state = 4
            ACTION_UPDATE_COMPLETE or ACTION_UPDATE_COMPOSITION
        }
    }

    /**
     * 조합: a combined Ja
     * 예시: ㄳ
     */
    private fun doState10(word: Char): Int {
        Log.v(TAG, "-doState10: 0. word=$word")

        Log.v(TAG, "-doState10: 1. compositionString=$compositionString")
        if (compositionString === "") {
            return ACTION_ERROR
        }

        Log.v(TAG, "-doState10: 2. isJa=${isJa(word)}")
        return if (isJa(word)) {
            completeString = compositionString
            compositionString = word + ""
            state = 1
            ACTION_UPDATE_COMPLETE or ACTION_UPDATE_COMPOSITION
        } else {
            val lcIndex0 = getLastJaIndex(compositionString[0])
            val lcIndex1 = LastJa.iLast[lcIndex0]
            val fcIndex = LastJa.iFirst[lcIndex0]
            val vIndex = getMoIndex(word)
            completeString = "${LastJa.Code[lcIndex1]}"
            compositionString = composeWordWithIndexes(fcIndex, vIndex, 0) + ""
            state = 2
            ACTION_UPDATE_COMPLETE or ACTION_UPDATE_COMPOSITION
        }
    }

    /**
     * 조합: single Ja + single Mo + a combined Ja
     * 예시: 갃
     */
    private fun doState11(word: Char): Int {
        Log.v(TAG, "-doState11: 0. word=$word")

        Log.v(TAG, "-doState11: 1. compositionString=$compositionString")
        if (compositionString === "") {
            return ACTION_ERROR
        }

        Log.v(TAG, "-doState11: 2. isJa=${isJa(word)}")
        return if (isJa(word)) {
            completeString = compositionString
            compositionString = word + ""
            state = 1
            ACTION_UPDATE_COMPLETE or ACTION_UPDATE_COMPOSITION
        } else {
            val lcIndex = getLastJaIndex(compositionString[0])
            val vIndex = getMoIndex(compositionString[0])
            val fcIndex = getFirstJaIndex(compositionString[0])
            val lcIndexNew = LastJa.iLast[lcIndex]
            val vIndexNew = convertMoToIndex(word)
            val fcIndexNew = LastJa.iFirst[lcIndex]
            completeString = composeWordWithIndexes(fcIndex, vIndex, lcIndexNew) + ""
            compositionString = composeWordWithIndexes(fcIndexNew, vIndexNew, 0) + ""
            state = 2
            ACTION_UPDATE_COMPLETE or ACTION_UPDATE_COMPOSITION
        }
    }

    /**
     * 조합: single Ja + a combined Mo
     * 예시: 과
     */
    private fun doState20(word: Char): Int {
        Log.v(TAG, "-doState20: 0. word=$word")

        Log.v(TAG, "-doState20: 1. compositionString=$compositionString")
        if (compositionString === "") {
            return ACTION_ERROR
        }

        Log.v(TAG, "-doState20: 2. isJa=${isJa(word)}")
        if (isJa(word)) {
            val lcIndex = convertLastJaToIndex(word)
            // cannot compose the code with composition string. flush it.
            return if (lcIndex < 0) {
                completeString = compositionString
                compositionString = word + ""
                state = 1
                ACTION_UPDATE_COMPLETE or ACTION_UPDATE_COMPOSITION
            } else { // compose..과
                val newWord = compositionString[0]
                completeString = ""
                compositionString = (newWord.code + lcIndex).toChar() + ""
                state = 21
                ACTION_UPDATE_COMPOSITION
            }
        } else {
            completeString = compositionString
            compositionString = word + ""
            state = 4
            return ACTION_UPDATE_COMPLETE or ACTION_UPDATE_COMPOSITION
        }
    }

    /**
     * 조합: single Ja + a combined Mo + single Ja
     * 예시: 곽
     */
    private fun doState21(word: Char): Int {
        Log.v(TAG, "-doState21: 0. word=$word")

        Log.v(TAG, "-doState21: 1. compositionString=$compositionString")
        if (compositionString === "") {
            return ACTION_ERROR
        }

        Log.v(TAG, "-doState21: 2. isJa=${isJa(word)}")
        if (isJa(word)) {
            val lcIndex = getLastJaIndex(compositionString[0])
            val lcIndexTemp = convertLastJaToIndex(word)
            if (lcIndexTemp < 0) {
                state = 1
                completeString = compositionString
                compositionString = word + ""
                return ACTION_UPDATE_COMPLETE or ACTION_UPDATE_COMPOSITION
            }

            val lcIndexNew = combineLastJaWithIndex(lcIndex, lcIndexTemp)
            Log.v(TAG, "-doState21: 3. lcIndexNew=$lcIndexNew")
            return if (lcIndexNew < 0) {
                state = 1
                completeString = compositionString
                compositionString = word + ""
                ACTION_UPDATE_COMPLETE or ACTION_UPDATE_COMPOSITION
            } else {
                val newWord = compositionString[0]
                completeString = ""
                compositionString = (newWord.code - lcIndex + lcIndexNew).toChar() + ""
                state = 22
                ACTION_UPDATE_COMPOSITION
            }
        } else {
            val newWord = compositionString[0]
            val lcIndex = getLastJaIndex(newWord)
            val fcIndex = convertFirstJaToIndex(LastJa.Word[lcIndex])
            val vIndex = convertMoToIndex(word)
            completeString = (newWord.code - lcIndex).toChar() + ""
            compositionString = composeWordWithIndexes(fcIndex, vIndex, 0) + ""
            state = 2
            return ACTION_UPDATE_COMPLETE or ACTION_UPDATE_COMPOSITION
        }
    }

    /**
     * 조합: single ja + a combined mo + a combined ja
     * 예시: 곿
     */
    private fun doState22(word: Char): Int {
        Log.v(TAG, "-doState22: 0. word=$word")

        Log.v(TAG, "-doState22: 1. compositionString=$compositionString")
        if (compositionString === "") {
            return ACTION_ERROR
        }

        Log.v(TAG, "-doState22: 2. isJa=${isJa(word)}")
        return if (isJa(word)) {
            completeString = compositionString
            compositionString = word + ""
            state = 1
            ACTION_UPDATE_COMPLETE or ACTION_UPDATE_COMPOSITION
        } else {
            val tempChar = compositionString[0]
            val lcIndex0 = getLastJaIndex(tempChar)
            val lcIndex1 = LastJa.iLast[lcIndex0]
            val fcIndex = LastJa.iFirst[lcIndex0]
            val vIndex = getMoIndex(word)
            completeString = (tempChar.code - lcIndex0 + lcIndex1).toChar() + ""
            compositionString = composeWordWithIndexes(fcIndex, vIndex, 0) + ""
            state = 2
            ACTION_UPDATE_COMPLETE or ACTION_UPDATE_COMPOSITION
        }
    }

}