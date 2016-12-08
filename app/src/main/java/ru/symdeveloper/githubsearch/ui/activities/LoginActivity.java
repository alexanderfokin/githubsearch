package ru.symdeveloper.githubsearch.ui.activities;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import icepick.Icepick;
import ru.symdeveloper.githubsearch.Constants;
import ru.symdeveloper.githubsearch.R;
import ru.symdeveloper.githubsearch.ui.presenters.LoginPresenter;
import ru.symdeveloper.githubsearch.ui.presenters.LoginPresenterFactory;
import ru.symdeveloper.githubsearch.ui.presenters.LoginView;
import ru.symdeveloper.githubsearch.ui.presenters.base.BasePresenterLoader;
import ru.symdeveloper.githubsearch.utils.CropCircleTransformation;
import ru.symdeveloper.githubsearch.utils.Settings;

public class LoginActivity
        extends AppCompatActivity
        implements LoginView,
        LoaderManager.LoaderCallbacks<LoginPresenter> {

    @BindView(R.id.coverImageView)
    ImageView coverImageView;

    @BindView(R.id.loginEditText)
    EditText loginEditText;

    @BindView(R.id.passwordEditText)
    EditText passwordEditText;

    @BindView(R.id.progress)
    View progress;

    @BindView(R.id.loginView)
    View loginView;

    private LoginPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Icepick.restoreInstanceState(this, savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        getSupportLoaderManager().initLoader(Constants.LOADER_ID_LOGIN, null, this);

        Glide.with(this)
                .load(R.mipmap.cat)
                .asBitmap()
                .transform(new CropCircleTransformation(this))
                .into(coverImageView);

        passwordEditText.setOnEditorActionListener((textView, id, keyEvent) -> {
            if (id == R.id.login || id == EditorInfo.IME_NULL) {
                attemptLogin();
                return true;
            }
            return false;
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        presenter.onViewAttached(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        presenter.onViewDetached(false);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Icepick.saveInstanceState(this, outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        Icepick.restoreInstanceState(this, savedInstanceState);
    }

    @OnClick(R.id.signInButton)
    void onSignInButtonClicked() {
        attemptLogin();
    }

    private void attemptLogin() {
        loginEditText.setError(null);
        passwordEditText.setError(null);

        String login = loginEditText.getText().toString();
        String password = passwordEditText.getText().toString();

        boolean cancel = false;
        View focusView = null;

        if (TextUtils.isEmpty(password)) {
            passwordEditText.setError(getString(R.string.error_field_required));
            focusView = passwordEditText;
            cancel = true;
        } else if (!isPasswordValid(password)) {
            passwordEditText.setError(getString(R.string.error_invalid_password));
            focusView = passwordEditText;
            cancel = true;

        }
        if (TextUtils.isEmpty(login)) {
            loginEditText.setError(getString(R.string.error_field_required));
            focusView = loginEditText;
            cancel = true;
        } else if (!isLoginValid(login)) {
            loginEditText.setError(getString(R.string.error_invalid_email));
            focusView = loginEditText;
            cancel = true;
        }
        if (cancel) {
            focusView.requestFocus();
        } else {
            Settings.setAuthInformation(login, password);
            showProgress(true);
            presenter.login();
        }
    }

    private boolean isLoginValid(String email) {
        return email.length() > 4;
    }

    private boolean isPasswordValid(String password) {
        return password.length() >= 7;
    }

    private void showProgress(final boolean show) {
        int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);
        loginView.setVisibility(show ? View.GONE : View.VISIBLE);
        loginView.animate().setDuration(shortAnimTime).alpha(show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                loginView.setVisibility(show ? View.GONE : View.VISIBLE);
            }
        });

        progress.setVisibility(show ? View.VISIBLE : View.GONE);
        progress.animate().setDuration(shortAnimTime).alpha(show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                progress.setVisibility(show ? View.VISIBLE : View.GONE);
            }
        });
    }

    @Override
    public void loginCompleted() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }

    @Override
    public void loginError() {
        Settings.clearAuthInformation();
        showProgress(false);
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle(R.string.error_title)
                .setMessage(R.string.error_wrong_credentials)
                .setCancelable(false)
                .setPositiveButton(R.string.button_ok, null)
                .create();
        dialog.show();
    }

    @Override
    public Loader<LoginPresenter> onCreateLoader(int id, Bundle args) {
        return new BasePresenterLoader<>(this, new LoginPresenterFactory());
    }

    @Override
    public void onLoadFinished(Loader<LoginPresenter> loader, LoginPresenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void onLoaderReset(Loader<LoginPresenter> loader) {
        this.presenter = null;
    }
}

