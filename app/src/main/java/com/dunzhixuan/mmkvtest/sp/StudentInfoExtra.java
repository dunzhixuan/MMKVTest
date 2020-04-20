package com.dunzhixuan.mmkvtest.sp;

import java.util.Set;

import me.zeyuan.lib.autopreferences.annotations.Preferences;

/** */
@Preferences("student_info_extra")
public interface StudentInfoExtra {

  /** 学生其他信息列表 */
  String studentExtraList = "";

  /** 学生展示过的任务的taskKey */
  Set<String> taskKeys = null;
}
