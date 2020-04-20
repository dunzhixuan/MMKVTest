package com.dunzhixuan.mmkvtest.sp;

import me.zeyuan.lib.autopreferences.annotations.Preferences;

@Preferences("guideVersion")
public interface GuideVersion {

  /** 引导页版本号 */
  int guideVersion = -1;
  /** 图片引导功能版本号 */
  int picFunctionGuideVersion = -1;
}
