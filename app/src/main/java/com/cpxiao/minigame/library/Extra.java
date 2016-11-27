package com.cpxiao.minigame.library;//package com.cpxiao.androidminigame.library;

/**
 * Extra
 *
 * @author cpxiao on 2016/8/31
 */
public final class Extra {

    /**
     * Intent或Bundle的name
     */
    public static final class Name {
        /**
         * bundle
         */
        public static final String INTENT_BUNDLE = "INTENT_BUNDLE";
        /**
         * 游戏类型
         */
        public static final String GAME_TYPE = "GAME_TYPE";
        /**
         * 游戏模式
         */
        public static final String GAME_MODE = "GAME_MODE";

        /**
         * 游戏难度
         */
        public static final String GAME_DIFFICULTY = "GAME_DIFFICULTY";

        /**
         * 关卡
         */
        public static final String LEVEL = "LEVEL";
        /**
         * 分数
         */
        public static final String SCORE = "SCORE";
    }

    /**
     * SharedPreferences 的key
     */
    public static final class Key {
        /**
         * 关卡，默认为1
         */
        public static final int LEVEL_DEFAULT = 1;
        public static final String LEVEL = "LEVEL";

        /**
         * 最高分，默认为0分
         */
        public static final int BEST_SCORE_DEFAULT = 0;
        public static final String BEST_SCORE = "BEST_SCORE";

        /**
         * 音效开关设置，默认开启
         */
        public static final boolean SOUND_ON_DEFAULT = true;
        public static final String SOUND_ON = "SOUND_ON";

        /**
         * 音乐开关设置，默认开启
         */
        public static final boolean BGM_ON_DEFAULT = true;
        public static final String BGM_ON = "BGM_ON";

        /**
         * 游戏难度
         */
        public static final String GAME_DIFFICULTY = "GAME_DIFFICULTY";

        /**
         * 游戏模式
         */
        public static final String GAME_MODE = "GAME_DIFFICULTY";

    }

}
