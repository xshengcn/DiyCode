package com.xshengcn.diycode.util;

import io.reactivex.Observable;
import io.reactivex.subjects.PublishSubject;

public class RxBus {

    private final PublishSubject<Object> mBus = PublishSubject.create();

    public RxBus() {
    }

    public void send(final Object event) {
        mBus.onNext(event);
    }

    public Observable<?> toObservable() {
        return mBus;
    }

    public boolean hasObservers() {
        return mBus.hasObservers();
    }
}
