package com.xshengcn.diycode.util;

import io.reactivex.Observable;
import io.reactivex.subjects.PublishSubject;

public final class RxBus {

  public RxBus() {
  }

  private final PublishSubject<Object> bus = PublishSubject.create();

  public void send(final Object event) {
    bus.onNext(event);
  }

  public Observable<?> toObservable() {
    return bus;
  }

  public boolean hasObservers() {
    return bus.hasObservers();
  }
}
