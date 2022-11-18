package widget;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkInfo;
import android.widget.Toast;

import androidx.lifecycle.MutableLiveData;

import com.wdcs.callback.VideoInteractiveParam;
import com.wdcs.constants.Constants;
import com.wdcs.utils.NetworkUtil;
import com.wdcs.utils.SPUtils;

public class NetBroadcastReceiver extends BroadcastReceiver {
    public static NetBroadcastReceiver netBroadcastReceiver;

    public NetBroadcastReceiver() {
    }

    public static NetBroadcastReceiver getInstance() {
        if (netBroadcastReceiver == null) {
            synchronized (VideoInteractiveParam.class) {
                if (netBroadcastReceiver == null) {
                    netBroadcastReceiver = new NetBroadcastReceiver();
                }
            }
        }
        return netBroadcastReceiver;
    }

    @Override
    public void onReceive(Context context, Intent intent) {

        try {
            System.out.println("网络状态发生变化");
            //检测API是不是小于23，因为到了API23之后getNetworkInfo(int networkType)方法被弃用
            if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.LOLLIPOP) {

                //获得ConnectivityManager对象
                ConnectivityManager connMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

                //获取ConnectivityManager对象对应的NetworkInfo对象
                //获取WIFI连接的信息
                NetworkInfo wifiNetworkInfo = connMgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
                //获取移动数据连接的信息
                NetworkInfo dataNetworkInfo = connMgr.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
                if (!wifiNetworkInfo.isConnected() && dataNetworkInfo.isConnected()) {
                    Toast.makeText(context, "当前处于非WIFI状态，请注意流量消耗", Toast.LENGTH_SHORT).show();
                } else if (!wifiNetworkInfo.isConnected() && !dataNetworkInfo.isConnected()) {
                    //                Toast.makeText(context, "WIFI已断开,移动数据已断开", Toast.LENGTH_SHORT).show();
                }
                //API大于23时使用下面的方式进行网络监听
            } else {

                System.out.println("API level 大于23");
                //获得ConnectivityManager对象
                ConnectivityManager connMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

                //获取所有网络连接的信息
                Network[] networks = connMgr.getAllNetworks();
                //用于存放网络连接信息
                StringBuilder sb = new StringBuilder();
                //通过循环将网络信息逐个取出来
                for (int i = 0; i < networks.length; i++) {
                    //获取ConnectivityManager对象对应的NetworkInfo对象
                    try {
                        NetworkInfo networkInfo = connMgr.getNetworkInfo(networks[i]);
                        if (null != networkInfo && null != networkInfo.getTypeName()) {
                            sb.append(networkInfo.getTypeName() + " connect is " + networkInfo.isConnected());
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                if (networks.length == 1 && !sb.toString().contains("WIFI")) {
                    return;
                }
                String netState;
                String oleNetState = SPUtils.getInstance().getString("net_state");
                if (NetworkUtil.isWifi(context)) {
                    netState = "0";
                } else {
                    netState = "1";
                }
                SPUtils.getInstance().put("net_state", netState);

                if (oleNetState.equals("1") && netState.equals(oleNetState)) {
                    Toast.makeText(context, "当前为非WIFI状态，请注意流量消耗", Toast.LENGTH_SHORT).show();
                } else {
                    LiveDataParam.getInstance().wifiState.setValue(true);
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
