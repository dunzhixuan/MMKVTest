package com.dunzhixuan.mmkvtest.sp;

import me.zeyuan.lib.autopreferences.annotations.Preferences;

@Preferences("debug")
public interface Debug {

  /** 开关新皮肤显示，默认为显示新皮肤 */
  boolean isSkinShowNew = true;
}
