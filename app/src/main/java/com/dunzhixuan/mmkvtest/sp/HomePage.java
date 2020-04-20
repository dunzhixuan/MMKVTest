package com.dunzhixuan.mmkvtest.sp;

import me.zeyuan.lib.autopreferences.annotations.Preferences;

/**
 * Created by liufeng on 2018/1/24.
 *
 * <p>首页数据
 */
@Preferences("home_page")
public interface HomePage {

  /** 全部功能列表 */
  String functionList = "";

  /** 被选中的功能列表 */
  String selectedFunList = "";

  /** 菜单项已显示过新列表 */
  String functionNewSet = "";

  /** 运营页显示控制请求版本号 */
  String promotionVersion = "-1";

  /** 优惠券过期时间 */
  long couponPopupExpiredTime = 0;

  /** TemplateList */
  String templateList = "";

  /** TransactionList */
  String transactionList = "";

  /** banner */
  String bannerList = "";
}
