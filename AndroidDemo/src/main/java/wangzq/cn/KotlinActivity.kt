package wangzq.cn

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.widget.Toast

class KotlinActivity : AppCompatActivity() {

    var   TAG:String=javaClass.name

    var str:String?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_kotlin)
        findViewById(R.id.center_text).setOnClickListener {
            Toast.makeText(baseContext, "点击了--", Toast.LENGTH_LONG).show()
            Log.w(TAG, aaa()+"-"+str?.length)
        }
    }

    fun aaa(): String {
        return "ss"
    }


}
