package org.example.tvoard.common

// Action Codes
const val ACTION_ERROR = -1
const val ACTION_NONE = 0
const val ACTION_UPDATE_COMPOSITION = 1
const val ACTION_UPDATE_COMPLETE = 2
const val ACTION_USE_INPUT_AS_RESULT = 4
const val ACTION_APPEND = 8
const val ACTION_DOWN = 0

// keyCode
const val KEYCODE_ALT = -6
const val KEYCODE_DELETE = -5
const val KEYCODE_DONE = -4
const val KEYCODE_CANCEL = -3
const val KEYCODE_MODE_CHANGE = -2
const val KEYCODE_SHIFT = -1
const val KEYCODE_HOME = 3
const val KEYCODE_BACK = 4
const val KEYCODE_DPAD_UP = 19
const val KEYCODE_DPAD_DOWN = 20
const val KEYCODE_DPAD_LEFT = 21
const val KEYCODE_DPAD_RIGHT = 22
const val KEYCODE_DPAD_CENTER = 23
const val KEYCODE_A = 29
const val KEYCODE_D = 32
const val KEYCODE_I = 37
const val KEYCODE_N = 42
const val KEYCODE_O = 43
const val KEYCODE_R = 46
const val KEYCODE_SPACE = 62
const val KEYCODE_ENTER = 66
const val KEYCODE_DEL = 67 // Backspace
const val KEYCODE_MENU = 82
const val KEYCODE_ESCAPE = 111
const val KEYCODE_HANJA = 212 // KeyEvent.KEYCODE_EISU is available from API 16
const val KEYCODE_HANGEUL = 218
const val META_ALT_ON = 2

// num
const val NUM_OF_FIRST = 19
const val NUM_OF_MIDDLE = 21
const val NUM_OF_LAST = 27
const val NUM_OF_LAST_INDEX = NUM_OF_LAST + 1 // add 1 for non-last consonant added characters

// keyState
const val KEYSTATE_NONE = 0
const val KEYSTATE_SHIFT = 1
const val KEYSTATE_SHIFT_LEFT = 1
const val KEYSTATE_SHIFT_RIGHT = 2
const val KEYSTATE_SHIFT_MASK = 3
const val KEYSTATE_ALT = 4
const val KEYSTATE_ALT_LEFT = 4
const val KEYSTATE_ALT_RIGHT = 8
const val KEYSTATE_ALT_MASK = 12
const val KEYSTATE_CTRL = 16
const val KEYSTATE_CTRL_LEFT = 16
const val KEYSTATE_CTRL_RIGHT = 32
const val KEYSTATE_CTRL_MASK = 48
const val KEYSTATE_FN = 64 // just for future usage...
const val BACK_SPACE = 0x8.toChar()

// Korean
const val HANGEUL_START = 0xAC00
const val HANGEUL_END = 0xD7A3
const val HANGEUL_JAMO_START = 0x3131
const val HANGEUL_MO_START = 0x314F
const val HANGEUL_JAMO_END = 0x3163