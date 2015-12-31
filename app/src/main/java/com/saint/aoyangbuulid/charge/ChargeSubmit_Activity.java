package com.saint.aoyangbuulid.charge;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.saint.aoyangbuulid.BaseActivity;
import com.saint.aoyangbuulid.R;
import com.saint.aoyangbuulid.charge.utils.BillUtils;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import cn.beecloud.BCPay;
import cn.beecloud.BCQuery;
import cn.beecloud.BeeCloud;
import cn.beecloud.async.BCCallback;
import cn.beecloud.async.BCResult;
import cn.beecloud.entity.BCBillOrder;
import cn.beecloud.entity.BCPayResult;
import cn.beecloud.entity.BCQueryBillResult;
import cn.beecloud.entity.BCReqParams;

/**
 * Created by zzh on 15-11-16.
 */
public class ChargeSubmit_Activity extends BaseActivity {
    private static final String TAG = "chargeSubmit_Activity";

    public ImageButton button_end;
    public TextView text_money;
    public  String money;
    private CheckBox ck_pay,ck_wechat,ck_cash;
    private ProgressDialog loadingDialog;

//    支付结果返回入口
    BCCallback bcCallback=new BCCallback() {
        @Override
        public void done(BCResult bcResult) {
            final BCPayResult bcPayResult= (BCPayResult) bcResult;
            //此处关闭loading界面
            loadingDialog.dismiss();
            //根据你自己的需求处理支付结果
            //需要注意的是，此处如果涉及到UI的更新，请在UI主进程或者Handler操作
            ChargeSubmit_Activity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    String result = bcPayResult.getResult();
                    if (result.equals(BCPayResult.RESULT_SUCCESS)) {
                        Toast.makeText(ChargeSubmit_Activity.this, "用户支付成功", Toast.LENGTH_LONG).show();
                    } else if (result.equals(BCPayResult.RESULT_CANCEL)) {
                        Toast.makeText(ChargeSubmit_Activity.this, "用户取消支付", Toast.LENGTH_LONG).show();
                    } else if (result.equals(BCPayResult.RESULT_FAIL)) {
                        Log.e("=====================>","自负失败");
                        String toastMsg = "支付失败, 原因: " + bcPayResult.getErrCode() +
                                " # " + bcPayResult.getErrMsg() +
                                " # " + bcPayResult.getDetailInfo();
                        /**
                         * 你发布的项目中不应该出现如下错误，此处由于支付宝政策原因，
                         * 不再提供支付宝支付的测试功能，所以给出提示说明
                         */
                        if (bcPayResult.getErrMsg().equals("PAY_FACTOR_NOT_SET") &&
                                bcPayResult.getDetailInfo().startsWith("支付宝参数")) {
                            toastMsg = "支付失败：由于支付宝政策原因，故不再提供支付宝支付的测试功能，给您带来的不便，敬请谅解";
                        }
                        /**
                         * 以下是正常流程，请按需处理失败信息
                         */
                        Toast.makeText(ChargeSubmit_Activity.this, toastMsg, Toast.LENGTH_LONG).show();
                        Log.e(TAG, toastMsg);
//                        if (bcPayResult.getErrMsg().equals(BCPayResult.FAIL_PLUGIN_NOT_INSTALLED) ||
//                                bcPayResult.getErrMsg().equals(BCPayResult.FAIL_PLUGIN_NEED_UPGRADE)) {
//                            //银联需要重新安装控件
//                            Message msg = mHandler.obtainMessage();
//                            msg.what = 1;
//                            mHandler.sendMessage(msg);
//                        }
                    } else if (result.equals(BCPayResult.RESULT_UNKNOWN)) {
                        //可能出现在支付宝8000返回状态
                        Toast.makeText(ChargeSubmit_Activity.this, "订单状态未知", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(ChargeSubmit_Activity.this, "invalid return", Toast.LENGTH_LONG).show();
                    }
                    if (bcPayResult.getId() != null) {
                        //你可以把这个id存到你的订单中，下次直接通过这个id查询订单
                        Log.w(TAG, "bill id retrieved : " + bcPayResult.getId());

                        //根据ID查询
                        getBillInfoByID(bcPayResult.getId());

                    }
                }
            });
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.changeend);
        // 如果调起支付太慢, 可以在这里开启动画, 以progressdialog为例
        loadingDialog = new ProgressDialog(ChargeSubmit_Activity.this);
        loadingDialog.setMessage("启动第三方支付，请稍候...");
        loadingDialog.setIndeterminate(true);
        loadingDialog.setCancelable(true);
        Intent intent=getIntent();
        money=intent.getStringExtra("amount");

        initData();
        initBeeCloud();

    }

    private void initBeeCloud(){

        BeeCloud.setSandbox(true);
        BeeCloud.setAppIdAndSecret("c92a2bb3-5f3b-4c21-9340-28eff0aca577"
                , "d3aa3a10-50a8-4233-b813-4c9dce224541");
        Log.e("签名=================>", "执行");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater=getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId()  ==  android.R.id.home){
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
    private void initData(){
        button_end = (ImageButton) findViewById(R.id.button_end);
        text_money = (TextView) findViewById(R.id.text_money);
        ck_cash= (CheckBox) findViewById(R.id.check_cash);
        ck_pay= (CheckBox) findViewById(R.id.check_pay);
        ck_wechat= (CheckBox) findViewById(R.id.check_wechat);
        text_money.setText(money);

        ck_cash.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    ck_wechat.setClickable(false);
                    ck_pay.setClickable(false);
                } else {
                    ck_wechat.setClickable(true);
                    ck_pay.setClickable(true);
                }
            }
        });

        //支付宝
        ck_pay.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    loadingDialog.show();
                    Map<String, String> mapOptional = new HashMap<String, String>();
                    mapOptional.put("客户端", "安卓");
                    mapOptional.put("consumptioncode", "consumptionCode");
                    mapOptional.put("money", "2");
                    //发起支付
                    Log.e("发起支付" + "==============>", "BCPay");
                    BCPay.getInstance(ChargeSubmit_Activity.this).reqAliPaymentAsync(
                            "安卓支付宝支付测试",
                            1,
                            BillUtils.genBillNum(),
                            mapOptional,
                            bcCallback);

                    ck_cash.setClickable(false);
                    ck_wechat.setClickable(false);
                }else {
                    ck_cash.setClickable(true);
                    ck_wechat.setClickable(true);
                }
            }
        });
        ck_wechat.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    loadingDialog.show();
                    //对于微信支付, 手机内存太小会有OutOfResourcesException造成的卡顿, 以致无法完成支付
                    //这个是微信自身存在的问题
                    Map<String, String> mapOptional = new HashMap<String, String>();

                    mapOptional.put("testkey1", "测试value值1");

                    if (BCPay.isWXAppInstalledAndSupported() &&
                            BCPay.isWXPaySupported()) {

                        BCPay.getInstance(ChargeSubmit_Activity.this).reqWXPaymentAsync(
                                "安卓微信支付测试",               //订单标题
                                1,                           //订单金额(分)
                                BillUtils.genBillNum(),  //订单流水号
                                mapOptional,            //扩展参数(可以null)
                                bcCallback);            //支付完成后回调入口

                    } else {
                        Toast.makeText(ChargeSubmit_Activity.this,
                                "您尚未安装微信或者安装的微信版本不支持", Toast.LENGTH_LONG).show();
                        loadingDialog.dismiss();
                    }
                    ck_pay.setClickable(false);
                    ck_cash.setClickable(false);
                }else {
                    ck_pay.setClickable(true);
                    ck_cash.setClickable(true);
                }
            }
        });

    }

    void getBillInfoByID(String id) {

        BCQuery.getInstance().queryBillByIDAsync(id,
                new BCCallback() {
                    @Override
                    public void done(BCResult result) {
                        BCQueryBillResult billResult = (BCQueryBillResult) result;

                        Log.d(TAG, "------ response info ------");
                        Log.d(TAG, "------getResultCode------" + billResult.getResultCode());
                        Log.d(TAG, "------getResultMsg------" + billResult.getResultMsg());
                        Log.d(TAG, "------getErrDetail------" + billResult.getErrDetail());

                        if (billResult.getResultCode() != 0)
                            return;

                        Log.d(TAG, "------- bill info ------");
                        BCBillOrder billOrder = billResult.getBill();
                        Log.d(TAG, "订单号:" + billOrder.getBillNum());
                        Log.d(TAG, "订单金额, 单位为分:" + billOrder.getTotalFee());
                        Log.d(TAG, "渠道类型:" + BCReqParams.BCChannelTypes.getTranslatedChannelName(billOrder.getChannel()));
                        Log.d(TAG, "子渠道类型:" + BCReqParams.BCChannelTypes.getTranslatedChannelName(billOrder.getSubChannel()));
                        Log.d(TAG, "订单是否成功:" + billOrder.getPayResult());

                        if (billOrder.getPayResult())
                            Log.d(TAG, "渠道返回的交易号，未支付成功时，是不含该参数的:" + billOrder.getTradeNum());
                        else
                            Log.d(TAG, "订单是否被撤销，该参数仅在线下产品（例如二维码和扫码支付）有效:"
                                    + billOrder.getRevertResult());

                        Log.d(TAG, "订单创建时间:" + new Date(billOrder.getCreatedTime()));
                        Log.d(TAG, "扩展参数:" + billOrder.getOptional());
                        Log.w(TAG, "订单是否已经退款成功(用于后期查询): " + billOrder.getRefundResult());
                        Log.w(TAG, "渠道返回的详细信息，按需处理: " + billOrder.getMessageDetail());

                    }
                });
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        //清理当前的activity引用
        BCPay.clear();

        //使用微信的，在initWechatPay的activity结束时detach
        BCPay.detachWechat();

        //使用百度支付的，在activity结束时detach
        BCPay.detachBaiduPay();
    }

}
