package wangzq.cn;

import android.hardware.fingerprint.FingerprintManager;
import android.os.Bundle;
import android.support.v4.hardware.fingerprint.FingerprintManagerCompat;
import android.support.v4.os.CancellationSignal;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.math.BigInteger;
import java.security.PrivateKey;
import java.security.Signature;
import java.security.interfaces.RSAPrivateKey;

public class FingerprintActivity extends AppCompatActivity {


    String TAG = getClass().getSimpleName();
    Button mBtnShowFinger;

    FingerprintManagerCompat mFingerManager;
    FingerprintManagerCompat.CryptoObject mCryptoObject;

    PrivateKey mPrivateKey ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fingerprint);

        mFingerManager = FingerprintManagerCompat.from(this);

        try {


            mCryptoObject =new CryptoObjectHelper().buildCryptoObject();


//            mPrivateKey = new RSAPrivateKey() {
//                @Override
//                public BigInteger getPrivateExponent() {
//                    return null;
//                }
//
//                @Override
//                public String getAlgorithm() {
//                    return null;
//                }
//
//                @Override
//                public String getFormat() {
//                    return null;
//                }
//
//                @Override
//                public byte[] getEncoded() {
//                    return new byte[0];
//                }
//
//                @Override
//                public BigInteger getModulus() {
//                    return null;
//                }
//            };

//            Signature signature = Signature.getInstance("RSA");
//            signature.initSign(mPrivateKey);


//            mCryptoObject = new FingerprintManagerCompat.CryptoObject(signature);



        } catch (Exception e) {
            Log.w(TAG, e.getMessage(), e);
        }

        mBtnShowFinger = (Button) findViewById(R.id.btn_show_finger);
        mBtnShowFinger.setOnClickListener(new FingerPrintClickListener());
    }


    class FingerPrintClickListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {


            Toast.makeText(getBaseContext(), "指纹识别开始", Toast.LENGTH_SHORT).show();

            boolean isHardwareDetected = mFingerManager.isHardwareDetected();
            boolean hasEnrolledFingerprints = mFingerManager.hasEnrolledFingerprints();

            Log.i(TAG, "isHardwareDetected : " + isHardwareDetected + "   hasEnrolledFingerprints : " + hasEnrolledFingerprints);

            mFingerManager.authenticate(mCryptoObject, 0, null, new FingerprintsAuthenticationCallback(), null);
        }
    }


    class FingerprintsAuthenticationCallback extends FingerprintManagerCompat.AuthenticationCallback {
        @Override
        public void onAuthenticationError(int errMsgId, CharSequence errString) {
            Toast.makeText(getBaseContext(), "指纹识别失败:" + errString, Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onAuthenticationHelp(int helpMsgId, CharSequence helpString) {
        }

        @Override
        public void onAuthenticationSucceeded(FingerprintManagerCompat.AuthenticationResult result) {
            //TODO 校验

            Toast.makeText(getBaseContext(), "指纹识别成功", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onAuthenticationFailed() {


            Toast.makeText(getBaseContext(), "指纹识别失败", Toast.LENGTH_SHORT).show();
        }
    }
}
