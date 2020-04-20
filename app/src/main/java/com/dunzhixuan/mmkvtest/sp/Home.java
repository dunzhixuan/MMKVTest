package com.dunzhixuan.mmkvtest.sp;

import com.dunzhixuan.mmkvtest.Person;

import java.util.LinkedHashSet;
import java.util.Set;

import me.zeyuan.lib.autopreferences.annotations.Preferences;

/**
 * Created by liufeng on 2018/1/24.
 * 以前支持：String、boolean、int、long、float、double、set
 * 现在支持：String、boolean、int、long、float、double、set、byte、Parcelable
 * <p>首页数据
 */
@Preferences("home_page")
public interface Home {

  /** 全部功能列表 */
  String functionList = "";

  /** 被选中的功能列表 */
  String selectedFunList = "";

  /** 菜单项已显示过新列表 */
  String functionNewSet = "";

  /** 运营页显示控制请求版本号 */
  String promotionVersion = "-1";

  /** TemplateList */
  String templateList = "";

  /** TransactionList */
  String transactionList = "";

  /** banner */
  String bannerList = "";

  /** 优惠券过期时间 */
  long couponPopupExpiredTime = 0;

  /** 是否出现过弹出*/
  boolean isShowedPop = false;

  /** 年龄 */
  int name = 0;

  double doubleTest = 0.0d;

  float floatTest = 0.0f;

  Set<String> setTest = new LinkedHashSet<>();

  Person person = null;

//  byte[] pic = new byte[0];

}
