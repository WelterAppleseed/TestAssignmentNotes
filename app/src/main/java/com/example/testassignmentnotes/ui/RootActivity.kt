package com.example.testassignmentnotes.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.testassignmentnotes.R
import com.example.testassignmentnotes.databinding.ActivityRootBinding
import com.example.testassignmentnotes.ui._base.FragmentNavigator
import com.example.testassignmentnotes.ui.list.NoteListFragment

class RootActivity : AppCompatActivity(), FragmentNavigator {
    private var viewBinding: ActivityRootBinding? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val viewBinding =  ActivityRootBinding.inflate(layoutInflater)
        this.viewBinding = viewBinding
        setContentView(R.layout.activity_root)
        supportFragmentManager
            .beginTransaction()
            .replace(
                viewBinding.container.id,
                NoteListFragment()
            )
            .commit()
    }
    override fun navigateTo(
        fragment: Fragment
    ) {
        val viewBinding = this.viewBinding ?: return
        supportFragmentManager
            .beginTransaction()
            .replace(
                viewBinding.container.id,
                fragment
            )
            .addToBackStack(null)
            .commit()
    }

    override fun onBackPressed() {
        if (supportFragmentManager.backStackEntryCount > 0) {
            supportFragmentManager.popBackStack()
        } else {
            super.onBackPressed()
        }
    }

}