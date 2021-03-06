package com.huangyu.mdfolder.ui.activity;


import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.huangyu.library.mvp.IBaseView;
import com.huangyu.mdfolder.R;
import com.huangyu.mdfolder.ui.fragment.SettingsFragment;

import butterknife.BindView;

public class SettingsActivity extends ThematicActivity {

    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_file_list;
    }

    @Override
    protected IBaseView initAttachView() {
        return null;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        mToolbar.setTitle(getString(R.string.menu_settings));
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        replaceFragment();
    }

    private void replaceFragment() {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.rl_file, new SettingsFragment())
                .commitAllowingStateLoss();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
//        outState.putString("WORKAROUND_FOR_BUG_19917_KEY", "WORKAROUND_FOR_BUG_19917_VALUE");
//        super.onSaveInstanceState(outState);
    }

}
