package com.dunzhixuan.mmkvtest.sp;

import me.zeyuan.lib.autopreferences.annotations.Preferences;

/** */
@Preferences("task")
public interface Task {

  /** 任务记录列表 */
  String releaseTaskList = "";
}
