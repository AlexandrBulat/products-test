package com.test.view

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.NavController
import androidx.navigation.NavOptions
import androidx.navigation.Navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import com.test.R
import com.test.databinding.ActivityMainBinding
import com.test.di.DaggerViewModelFactory
import com.test.viewModels.MainActivityViewModelImpl
import dagger.android.AndroidInjection
import dagger.android.DispatchingAndroidInjector
import dagger.android.support.HasSupportFragmentInjector
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject


class MainActivity : AppCompatActivity(), HasSupportFragmentInjector {

    @Inject
    lateinit var fragmentInjector: DispatchingAndroidInjector<Fragment>

    @Inject
    lateinit var viewmodelFactory: DaggerViewModelFactory
    lateinit var viewModel: MainActivityViewModelImpl
    @Inject
    lateinit var bundle: Bundle

    lateinit var viewBinding: ActivityMainBinding
    private var disposables = CompositeDisposable()

    override fun supportFragmentInjector() = fragmentInjector

    override fun onSupportNavigateUp(): Boolean {
        return findNavController(this, R.id.container_fragment).navigateUp()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        viewModel =
            ViewModelProviders.of(this, viewmodelFactory).get(MainActivityViewModelImpl::class.java)
        setTheme(R.style.AppTheme)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        val navHostFragment = supportFragmentManager.fragments.first() as? NavHostFragment
        if (navHostFragment != null) {
            val childFragments = navHostFragment.childFragmentManager.fragments
            childFragments.forEach { fragment ->
                fragment.onActivityResult(requestCode, resultCode, data)
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        disposables.dispose()
    }

    override fun onSaveInstanceState(oldInstanceState: Bundle) {
        super.onSaveInstanceState(oldInstanceState)
        oldInstanceState.clear()
    }
}
