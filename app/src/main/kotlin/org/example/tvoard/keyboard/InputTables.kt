package org.example.tvoard.keyboard

object InputTables {

    object FirstJa {
        val Word = charArrayOf(
            'ㄱ', 'ㄲ', 'ㄴ', 'ㄷ', 'ㄸ',
            'ㄹ', 'ㅁ', 'ㅂ', 'ㅃ', 'ㅅ',
            'ㅆ', 'ㅇ', 'ㅈ', 'ㅉ', 'ㅊ',
            'ㅋ', 'ㅌ', 'ㅍ', 'ㅎ'
        )
        val Code = intArrayOf(
            0x3131, 0x3132, 0x3134, 0x3137, 0x3138,
            0x3139, 0x3141, 0x3142, 0x3143, 0x3145,
            0x3146, 0x3147, 0x3148, 0x3149, 0x314A,
            0x314B, 0x314C, 0x314D, 0x314E
        )
    }

    object Mo {
        val Word = charArrayOf(
            'ㅏ', 'ㅐ', 'ㅑ', 'ㅒ', 'ㅓ',
            'ㅔ', 'ㅕ', 'ㅖ', 'ㅗ', 'ㅘ',
            'ㅙ', 'ㅚ', 'ㅛ', 'ㅜ', 'ㅝ',
            'ㅞ', 'ㅟ', 'ㅠ', 'ㅡ', 'ㅢ',
            'ㅣ'
        )
        val Code = intArrayOf(
            0x314F, 0x3150, 0x3151, 0x3152, 0x3153,
            0x3154, 0x3155, 0x3156, 0x3157, 0x3158,
            0x3159, 0x315A, 0x315B, 0x315C, 0x315D,
            0x315E, 0x315F, 0x3160, 0x3161, 0x3162,
            0x3163
        )
        val iMiddle = intArrayOf(
            -1, -1, -1, -1, -1,
            -1, -1, -1, -1, 8,
            8, 8, -1, -1, 13,
            13, 13, -1, -1, 18,
            -1
        )
    }

    object LastJa {
        val Word = charArrayOf(
            0x0.toChar(), 'ㄱ', 'ㄲ', 'ㄳ', 'ㄴ',
            'ㄵ', 'ㄶ', 'ㄷ', 'ㄹ', 'ㄺ',
            'ㄻ', 'ㄼ', 'ㄽ', 'ㄾ', 'ㄿ',
            'ㅀ', 'ㅁ', 'ㅂ', 'ㅄ', 'ㅅ',
            'ㅆ', 'ㅇ', 'ㅈ', 'ㅊ', 'ㅋ',
            'ㅌ', 'ㅍ', 'ㅎ'
        )
        val Code = intArrayOf(
            0x0, 0x3131, 0x3132, 0x3133, 0x3134,
            0x3135, 0x3136, 0x3137, 0x3139, 0x313A,
            0x313B, 0x313C, 0x313D, 0x313E, 0x313F,
            0x3140, 0x3141, 0x3142, 0x3144, 0x3145,
            0x3146, 0x3147, 0x3148, 0x314A, 0x314B,
            0x314C, 0x314D, 0x314E
        )
        val iLast = intArrayOf(
            -1, -1, -1, 1, -1,
            4, 4, -1, -1, 8,
            8, 8, 8, 8, 8,
            8, -1, -1, 17, -1,
            -1, -1, -1, -1, -1,
            -1, -1, -1
        )
        val iFirst = intArrayOf(
            -1, -1, -1, 9, -1,
            12, 18, -1, -1, 0,
            6, 7, 9, 16, 17,
            18, -1, -1, 9, -1,
            -1, -1, -1, -1, -1,
            -1, -1, -1
        )
    }

    object NormalKeyMap {
        val Word = charArrayOf(
            0x3141.toChar(), 0x3160.toChar(), 0x314A.toChar(), 0x3147.toChar(), 0x3137.toChar(),
            0x3139.toChar(), 0x314E.toChar(), 0x3157.toChar(), 0x3151.toChar(), 0x3153.toChar(),
            0x314F.toChar(), 0x3163.toChar(), 0x3161.toChar(), 0x315C.toChar(), 0x3150.toChar(),
            0x3154.toChar(), 0x3142.toChar(), 0x3131.toChar(), 0x3134.toChar(), 0x3145.toChar(),
            0x3155.toChar(), 0x314D.toChar(), 0x3148.toChar(), 0x314C.toChar(), 0x315B.toChar(),
            0x314B.toChar()
        )

        val Code = intArrayOf(
            0x3141, 0x3160, 0x314A, 0x3147, 0x3137,
            0x3139, 0x314E, 0x3157, 0x3151, 0x3153,
            0x314F, 0x3163, 0x3161, 0x315C, 0x3150,
            0x3154, 0x3142, 0x3131, 0x3134, 0x3145,
            0x3155, 0x314D, 0x3148, 0x314C, 0x315B,
            0x314B
        )
        val FirstIndex = intArrayOf(
            6, -1, 14, 11, 3,
            5, 18, -1, -1, -1,
            -1, -1, -1, -1, -1,
            -1, 7, 0, 2, 9,
            -1, 17, 12, 16, -1,
            15
        )
        val MiddleIndex = intArrayOf(
            -1, 17, -1, -1, -1,
            -1, -1, 8, 2, 4,
            0, 20, 18, 13, 1,
            5, -1, -1, -1, -1,
            6, -1, -1, -1, 12,
            -1
        )
        val LastIndex = intArrayOf(
            16, -1, 23, 21, 7,
            8, 27, -1, -1, -1,
            -1, -1, -1, -1, -1,
            -1, 17, 1, 4, 19,
            -1, 26, 22, 25, -1,
            24
        )
    }

    object ShiftedKeyMap {
        val Word = charArrayOf(
            'ㅁ', 'ㅠ', 'ㅊ', 'ㅇ', 'ㄸ',
            'ㄹ', 'ㅎ', 'ㅗ', 'ㅑ', 'ㅓ',
            'ㅏ', 'ㅣ', 'ㅡ', 'ㅜ', 'ㅒ',
            'ㅖ', 'ㅃ', 'ㄲ', 'ㄴ', 'ㅆ',
            'ㅕ', 'ㅍ', 'ㅉ', 'ㅌ', 'ㅛ',
            'ㅋ'
        )
        val Code = intArrayOf(
            0x3141, 0x3160, 0x314A, 0x3147, 0x3138,
            0x3139, 0x314E, 0x3157, 0x3151, 0x3153,
            0x314F, 0x3163, 0x3161, 0x315C, 0x3152,
            0x3156, 0x3143, 0x3132, 0x3134, 0x3146,
            0x3155, 0x314D, 0x3149, 0x314C, 0x315B,
            0x314B
        )
        val FirstIndex = intArrayOf(
            6, -1, 14, 11, 4,
            5, 18, -1, -1, -1,
            -1, -1, -1, -1, -1,
            -1, 8, 1, 2, 10,
            -1, 17, 13, 16, -1,
            15
        )
        val MiddleIndex = intArrayOf(
            -1, 17, -1, -1, -1,
            -1, -1, 8, 2, 4,
            0, 20, 18, 13, 3,
            7, -1, -1, -1, -1,
            6, -1, -1, -1, 12,
            -1
        )
        val LastIndex = intArrayOf(
            16, -1, 23, 21, -1,
            8, 27, -1, -1, -1,
            -1, -1, -1, -1, -1,
            -1, -1, 2, 4, 20,
            -1, 26, -1, 25, -1,
            24
        )
    }

    // formula to get HANGEUL_CODE by composing consonants and vowel indexes
    // HANGEUL_CODE = HANGEUL_START + iFirst*NUM_OF_MIDDLE*NUM_OF_LAST_INDEX + iMiddle*NUM_OF_LAST_INDEX + iLast

    // getting the first consonant index from code
    // iFirst = (vCode - HANGEUL_START) / (NUM_OF_MIDDLE * NUM_OF_LAST_INDEX)

    // getting the vowel index from code
    // iMiddle = ((vCode - HANGEUL_START) % (NUM_OF_MIDDLE * NUM_OF_LAST_INDEX)) / NUM_OF_LAST_INDEX

    // getting the last consonant index from code
    // iLast = (vCode - HANGEUL_START) % NUM_OF_LAST_INDEX

}
