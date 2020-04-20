package com.dunzhixuan.mmkvtest.sp;

import me.zeyuan.lib.autopreferences.annotations.Preferences;

/** */
@Preferences("app")
public interface App {

  /** 应用是否是首次启动 */
  boolean isAppFirstLaunch = true;
  /** 当前时间同服务器时间的差异记录 */
  long currentServerTimeDiff = 0L;
  /** 学习陪伴引导提示 */
  boolean isShowTipOfAccompany = false;
  /** 是否清除过cookie */
  boolean hasClearCookie = false;
}
