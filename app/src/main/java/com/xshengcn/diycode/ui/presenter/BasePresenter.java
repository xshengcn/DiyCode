package com.xshengcn.diycode.ui.presenter;

import com.orhanobut.logger.Logger;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

public abstract class BasePresenter<T> {

    private final CompositeDisposable mDisposable = new CompositeDisposable();
    private T mView;

    public void onAttach(T view) {
        Logger.d("onAttach: " + view.getClass().getSimpleName());
        mView = view;
    }

    protected void addDisposable(Disposable s) {
        mDisposable.add(s);
    }

    public CompositeDisposable getDisposable() {
        return mDisposable;
    }

    public void onDetach() {
        Logger.d("onDetach");
        mDisposable.clear();
        mView = null;
    }

    protected T getView() {
        if (mView == null) {
            throw new NullPointerException("must attach to presenter");
        }
        return mView;
    }
}
