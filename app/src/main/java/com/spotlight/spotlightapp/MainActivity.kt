package com.spotlight.spotlightapp

import android.os.Bundle
import android.transition.ChangeBounds
import android.transition.ChangeTransform
import android.transition.Fade
import android.transition.TransitionSet
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentFactory
import androidx.fragment.app.commit
import com.spotlight.spotlightapp.data.task.Task
import com.spotlight.spotlightapp.splash.SplashScreenFragment
import com.spotlight.spotlightapp.task.CurrentTaskFragment
import com.spotlight.spotlightapp.task.DailyIntentListFragment
import com.spotlight.spotlightapp.task.TasksFragment

class MainActivity : AppCompatActivity(R.layout.activity_main),
    DailyIntentListFragment.Callback {
    private val fragmentFactory = object : FragmentFactory() {
        override fun instantiate(classLoader: ClassLoader, className: String): Fragment {
            return when (loadFragmentClass(classLoader, className)) {
                DailyIntentListFragment::class.java -> DailyIntentListFragment(this@MainActivity)
                CurrentTaskFragment::class.java -> CurrentTaskFragment().apply {
                    val sharedElementTransition = TransitionSet()
                        .addTransition(ChangeTransform())
                        .addTransition(ChangeBounds())

                    sharedElementEnterTransition = sharedElementTransition
                    returnTransition = Fade()
                    postponeEnterTransition()
                }
                else -> super.instantiate(classLoader, className)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        supportFragmentManager.fragmentFactory = fragmentFactory
        super.onCreate(savedInstanceState)

        configureSplashScreenFragment()
    }

    override fun openTaskPage(view: View, task: Task) {
        val bundle = Bundle().apply {
            putParcelable(CurrentTaskFragment.TASK, task)
        }

        supportFragmentManager.commit {
            setReorderingAllowed(true)
            addSharedElement(view, view.transitionName)
            replace(
                R.id.fragmentContainer, CurrentTaskFragment::class.java, bundle,
                CurrentTaskFragment.TAG)
            addToBackStack(CurrentTaskFragment.TAG)
        }
    }

    override fun openTaskList() {
        supportFragmentManager.commit {
            setReorderingAllowed(true)
            replace(
                R.id.fragmentContainer, TasksFragment::class.java, null, TasksFragment.TAG)
            addToBackStack(TasksFragment.TAG)
        }
    }

    private fun configureSplashScreenFragment() {
        supportFragmentManager.apply {
            setFragmentResultListener(
                SplashScreenFragment.REQUEST_KEY,
                this@MainActivity) { requestKey, result ->
                if (requestKey == SplashScreenFragment.REQUEST_KEY &&
                    result.getBoolean(SplashScreenFragment.IS_FINISHED_SPLASH_ANIMATION, false)) {
                    configureDailyIntentListFragment()
                }
            }
            commit {
                setReorderingAllowed(true)
                replace(R.id.fragmentContainer, SplashScreenFragment::class.java, null)
            }
        }
    }

    private fun configureDailyIntentListFragment() {
        supportFragmentManager.commit {
            setReorderingAllowed(true)
            replace(R.id.fragmentContainer, DailyIntentListFragment::class.java, null)
        }
    }
}
