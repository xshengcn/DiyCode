package com.xshengcn.diycode.ui.presenter;

import java.lang.ref.Reference;
import java.lang.ref.WeakReference;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

public abstract class BasePresenter<T> {

    private final CompositeDisposable mDisposable = new CompositeDisposable();
    private Reference<T> mReference;

    public void onAttach(T view) {
        mReference = new WeakReference<>(view);
    }

    protected void addDisposable(Disposable s) {
        mDisposable.add(s);
    }

    public CompositeDisposable getDisposable() {
        return mDisposable;
    }

    public void onDetach() {

        mDisposable.clear();

        if (mReference != null) {
            mReference.clear();
            mReference = null;
        }
    }

    protected T getView() {
        T t = mReference.get();
        if (t == null) {
            throw new NullPointerException("must attach to presenter");
        }
        return mReference.get();
    }
}
