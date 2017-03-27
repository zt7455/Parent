package com.shengliedu.parent.chat.xmpp;

import org.jivesoftware.smack.ConnectionListener;

import android.content.Intent;
import android.util.Log;

import com.shengliedu.parent.activity.LoginActivity;
import com.shengliedu.parent.app.App;
import com.shengliedu.parent.chat.constant.Constants;
import com.shengliedu.parent.chat.util.MyAndroidUtil;


public class XmppConnecionListener implements ConnectionListener {

	@Override
	public void connectionClosed() {
		Log.e("smack xmpp", "close");
		XmppConnection.getInstance().setNull();
	}

	@Override
	public void connectionClosedOnError(Exception e) {
//		Log.e("smack xmpp", e.getMessage());
		if(e.getMessage().contains("conflict")){
			MyAndroidUtil.removeXml(Constants.LOGIN_PWD);
			if (!App.sharedPreferences.getBoolean(Constants.LOGIN_CHECK, false)) {
				MyAndroidUtil.removeXml(Constants.LOGIN_ACCOUNT);
			}
			Constants.USER_NAME = "";
			Constants.loginUser = null;
			XmppConnection.getInstance().closeConnection();
			App.getInstance().sendBroadcast(new Intent("conflict"));
			Intent intent = new Intent();
			intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			intent.putExtra("isRelogin", true);
			intent.setClass(App.getInstance(), LoginActivity.class);
			App.getInstance().startActivity(intent);
		}
	}

	@Override
	public void reconnectingIn(int seconds) {
//		Log.e("smack xmpp", "reconing:"+seconds);
	}

	@Override
	public void reconnectionSuccessful() {
		XmppConnection.getInstance().loadFriendAndJoinRoom();
//		Log.e("smack xmpp", "suc");
	}

	@Override
	public void reconnectionFailed(Exception e) {
//		Log.e("smack xmpp", "re fail");
	}

	

}
