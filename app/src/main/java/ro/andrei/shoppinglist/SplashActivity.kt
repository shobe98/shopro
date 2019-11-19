package ro.andrei.shoppinglist

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.animation.AnimationUtils
import kotlinx.android.synthetic.main.activity_splash.*

class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        ivCart.startAnimation(
            AnimationUtils.loadAnimation(
                this@SplashActivity,
                R.anim.splash_animation
            )
        )

        Handler().postDelayed({
            var intentMain = Intent()
            intentMain.setClass(this@SplashActivity, MainActivity::class.java)
            startActivity(intentMain)
            finish()
        }, 3000)
    }
}
