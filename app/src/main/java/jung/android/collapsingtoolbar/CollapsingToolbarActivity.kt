package jung.android.collapsingtoolbar

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.View.INVISIBLE
import android.view.View.VISIBLE
import android.view.animation.AlphaAnimation
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.google.android.material.appbar.AppBarLayout
import jung.android.collapsingtoolbar.databinding.ActivityCollapsingToolbarBinding
import java.lang.Math.abs


class CollapsingToolbarActivity : AppCompatActivity(), AppBarLayout.OnOffsetChangedListener {

    private lateinit var binding: ActivityCollapsingToolbarBinding

    private val PERCENTAGE_TO_SHOW_TITLE_AT_TOOLBAR = 0.9f
    private val PERCENTAGE_TO_HIDE_TITLE_DETAILS = 0.3f
    private val ALPHA_ANIMATIONS_DURATION = 200L

    private var mIsTheTitleVisible = false
    private var mIsTheTitleContainerVisible = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_collapsing_toolbar)
        binding.run {
            lifecycleOwner = this@CollapsingToolbarActivity
            mainAppbar.addOnOffsetChangedListener(this@CollapsingToolbarActivity)
            mainToolbar.inflateMenu(R.menu.menu_main)
            setSupportActionBar(mainToolbar)
            supportActionBar!!.setDisplayHomeAsUpEnabled(true)
            startAlphaAnimation(mainTextviewTitle, 0, INVISIBLE)
        }

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                Toast.makeText(this, "뒤로가기 버튼 클릭", Toast.LENGTH_SHORT).show()
            }
            R.id.action_settings -> {
                Toast.makeText(this, "클릭", Toast.LENGTH_SHORT).show()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onOffsetChanged(appBarLayout: AppBarLayout?, offset: Int) {
        val maxScroll = appBarLayout!!.totalScrollRange
        val percentage = abs(offset).toFloat() / maxScroll.toFloat()

        handleAlphaOnTitle(percentage)
        handleToolbarTitleVisibility(percentage)
    }

    private fun handleToolbarTitleVisibility(percentage: Float) {
        if (percentage >= PERCENTAGE_TO_SHOW_TITLE_AT_TOOLBAR) {
            if (!mIsTheTitleVisible) {
                startAlphaAnimation(binding.mainTextviewTitle, ALPHA_ANIMATIONS_DURATION, VISIBLE)
                startAlphaAnimation(binding.mainTextviewSubTitle, ALPHA_ANIMATIONS_DURATION, VISIBLE)
                binding.mainTextviewTitle.text = "Title"
                binding.mainTextviewSubTitle.text = "subTitle"
                mIsTheTitleVisible = true
            }
        } else {
            if (mIsTheTitleVisible) {
                startAlphaAnimation(binding.mainTextviewTitle, ALPHA_ANIMATIONS_DURATION, INVISIBLE)
                startAlphaAnimation(binding.mainTextviewSubTitle, ALPHA_ANIMATIONS_DURATION, INVISIBLE)
                mIsTheTitleVisible = false
            }
        }
    }

    private fun handleAlphaOnTitle(percentage: Float) {
        if (percentage >= PERCENTAGE_TO_HIDE_TITLE_DETAILS) {
            if (mIsTheTitleContainerVisible) {
                startAlphaAnimation(binding.mainLinearlayoutTitle, ALPHA_ANIMATIONS_DURATION, INVISIBLE)
                mIsTheTitleContainerVisible = false
            }
        } else {
            if (!mIsTheTitleContainerVisible) {
                startAlphaAnimation(binding.mainLinearlayoutTitle, ALPHA_ANIMATIONS_DURATION, VISIBLE)
                mIsTheTitleContainerVisible = true
            }
        }
    }

    private fun startAlphaAnimation(view: View, duration: Long, visibility: Int) {
        val alphaAnimation =
            if (visibility == VISIBLE) AlphaAnimation(0f, 1f)
            else AlphaAnimation(1f, 0f)
        alphaAnimation.apply {
            this.duration = duration
            this.fillAfter = true
            view.startAnimation(this)
        }
    }

}